package com.surf.quiz.service;


import com.surf.quiz.dto.ExampleDto;
import com.surf.quiz.dto.MemberDto;
import com.surf.quiz.dto.QuestionDto;
import com.surf.quiz.dto.UserDto;
import com.surf.quiz.dto.request.CreateRoomRequestDto;
import com.surf.quiz.dto.request.SearchMemberRequestDto;
import com.surf.quiz.dto.response.SearchMemberResponseDto;
import com.surf.quiz.entity.Quiz;
import com.surf.quiz.entity.QuizRoom;
import com.surf.quiz.repository.QuizRepository;
import com.surf.quiz.repository.QuizRoomRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class QuizRoomService {
    private final QuizRoomRepository quizRepo;
    private final QuizRepository quizRepository;
    private final SimpMessagingTemplate messageTemplate; // 메시지 브로커를 통해 클라이언트와 서버 간의 실시간 메시지 교환을 처리

    // 일정한 시간 간격으로 작업(태스크)를 실행하거나 지연 실행할 수 있는 스레드 풀 기반의 스케줄링 서비스
    //단일 스레드로 동작하는 스케줄링 서비스(ScheduledExecutorService) 객체를 생성하고, 이 객체(scheduler)에 대한 참조를 유지
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public QuizRoomService(QuizRoomRepository quizRepo, SimpMessagingTemplate messageTemplate, QuizRepository quizRepository) {
        this.quizRepo = quizRepo;
        this.messageTemplate = messageTemplate;
        this.quizRepository = quizRepository;
    }


    public QuizRoom save(QuizRoom room) {
        return quizRepo.save(room);
    }

    public void delete(QuizRoom room) {
        quizRepo.delete(room);
    }

    public Optional<QuizRoom> findById(Long Id) {
        return quizRepo.findById(Id);
    }

    public List<QuizRoom> findAll() {

        return quizRepo.findAll();
    }

    public List<MemberDto> getUsersByRoomId(Long roomId) {
        Optional<QuizRoom> optional = quizRepo.findById(roomId);
        if (optional.isPresent()) {
            return optional.get().getUsers();
        } else {
            throw new NoSuchElementException("No QuizRoom for ID: " + roomId);
        }
    }


    public void findAllAndSend() {
        List<QuizRoom> roomList = this.findAll().stream()
                .filter(room -> !room.isSingle())
                .collect(Collectors.toList());
        scheduler.schedule(() -> messageTemplate.convertAndSend("/sub/quizroom/roomList", roomList), 1, TimeUnit.SECONDS);
    }


    public SearchMemberResponseDto createSearchMemberResponseDto(SearchMemberRequestDto SearchMemberRequestDto) {
        SearchMemberResponseDto roomDto = new SearchMemberResponseDto();
        roomDto.setRoomName(SearchMemberRequestDto.getRoomName());
        roomDto.setContent(SearchMemberRequestDto.getContent());
        roomDto.setQuizCnt(SearchMemberRequestDto.getQuizCnt());
        roomDto.setPages(SearchMemberRequestDto.getPages());
        roomDto.setSharePages(SearchMemberRequestDto.getSharePages());
        roomDto.setSingle(SearchMemberRequestDto.isSingle());

        // invite users = 초대 받은 유저 + 방 만든 유저
        // 방을 만든 사람이면 리스트의 0번으로 넣기 > 방장으로 만들기 위해서
        List<UserDto> inviteUsers = createInviteUsers();

        roomDto.setInviteUsers(inviteUsers);

        return roomDto;
    }

    private List<UserDto> createInviteUsers() {
        List<UserDto> inviteUsers = new ArrayList<>();
        inviteUsers.add(createUser(1L, "csi"));
        inviteUsers.add(createUser(2L, "isc"));

        return inviteUsers;
    }

    private UserDto createUser(Long userPk, String userName) {
        UserDto user = new UserDto();
        user.setUserPk(userPk);
        user.setUserName(userName);

        return user;
    }



    public QuizRoom createAndSaveQuizRoom(CreateRoomRequestDto createRoomRequestDto) {
        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        QuizRoom createQuizRoom = QuizRoom.builder()
                .roomName(createRoomRequestDto.getRoomName())
                .content(createRoomRequestDto.getContent())
                .quizCnt(createRoomRequestDto.getQuizCnt())
                .createdDate(formattedDateTime)
                .sharePages(createRoomRequestDto.getSharePages())
                .pages(createRoomRequestDto.getPages())
                .single(createRoomRequestDto.isSingle())
                .inviteUsers(createRoomRequestDto.getUsers())
                .build();

        // 싱글 모드면 roomMax 1 / 그룹 모드면 초대 인원 수
        if (createQuizRoom.isSingle()) {
            createQuizRoom.setRoomMax(1);
        } else {
            createQuizRoom.setRoomMax(createRoomRequestDto.getUsers().size());
        }

        MemberDto member = new MemberDto();
        member.setHost(true);
        member.setReady(createQuizRoom.isSingle());
        member.setUserPk(1L);
        member.setUserName("csi");
        member.setUserImg("default");
        List<MemberDto> members = new ArrayList<>();
        members.add(member);
        createQuizRoom.setUsers(members);
        createQuizRoom.setRoomCnt(1);

        QuizRoom createdQuizroom = this.save(createQuizRoom);




        // 퀴즈 생성
        Quiz quiz = this.createQuiz(createdQuizroom);
        quizRepository.save(quiz);

        // 스레드 스케줄러
        this.findAllAndSend();

        return createdQuizroom;
    }




    // 레디, 언레디
    public void changeReady(Long roomId, Map<String, Object> payload) {
        QuizRoom quizRoom = this.findById(roomId).orElseThrow();

        long id;
        Object userPk = payload.get("userPk");
        if (userPk instanceof Number) {
            id = ((Number) userPk).longValue();
            System.out.println("id = " + id);
        } else {
            id = Long.parseLong((String) userPk);
            System.out.println("id = " + id);
        }

        String action = (String) payload.get("action");

        // 레디 상태 변경, 언레디 상태 변경
        boolean isProcessed = quizRoom.memberReady(id, action);

        if (isProcessed) {
            // 레디 카운트 변경
            updateReadyCount(quizRoom, action);
            // 퀴즈룸 저장
            saveAndSendQuizRoom(roomId, quizRoom);
        }

        this.findAllAndSend();
    }

    private void updateReadyCount(QuizRoom quizRoom, String action) {
        int increment = action.equals("ready") ? 1 : -1;
        quizRoom.setReadyCnt(quizRoom.getReadyCnt() + increment);
    }

    // 방입장
    public void enterQuizRoom(Long roomId, MemberDto memberDto ) {
        QuizRoom quizRoom = findById(roomId).orElseThrow();
        if (quizRoom.getRoomCnt()>=quizRoom.getRoomMax()) {
            return ;
        }
        // 이미 방에 있으면 리턴
        if (!canEnterQuizRoom(quizRoom, memberDto)) {
            return;
        }
        //입장
        quizRoom.memberIn(memberDto);
        quizRoom.setRoomCnt(quizRoom.getRoomCnt() + 1);
        // 저장
        saveAndSendQuizRoom(roomId, quizRoom);
        this.findAllAndSend();
    }

    private boolean canEnterQuizRoom(QuizRoom quizRoom, MemberDto memberDto) {
        return quizRoom.getInviteUsers().stream().anyMatch(user -> Objects.equals(user.getUserPk(), memberDto.getUserPk()))
                && quizRoom.getUsers().stream().noneMatch(member -> memberDto.getUserPk().equals(member.getUserPk()));
    }

    public void leaveQuizRoom(Long roomId, MemberDto memberDto) {
        QuizRoom quizRoom = findById(roomId).orElseThrow();
        // 방에 있는 멤버인지 찾기
        Optional<MemberDto> optionalMember = findMemberInQuizRoom(quizRoom, memberDto);

        // 멤버 못 찾으면 리턴
        if (optionalMember.isEmpty()) {
            return;
        }

        MemberDto member = optionalMember.get();
        if (member.isReady()) {
            quizRoom.setReadyCnt(quizRoom.getReadyCnt() - 1);
        }

        quizRoom.memberOut(memberDto);
        quizRoom.setRoomCnt(quizRoom.getRoomCnt() - 1);

        // 빈 방이면 삭제, 빈 방 아니면 다른 녀석에 방장 주기
        handleEmptyQuizRoom(roomId, quizRoom, memberDto);
        this.save(quizRoom);
        this.findAllAndSend();
    }

    private Optional<MemberDto> findMemberInQuizRoom(QuizRoom quizRoom, MemberDto memberDto) {
        return quizRoom.getUsers().stream()
                .filter(member -> member.getUserPk().equals(memberDto.getUserPk()))
                .findFirst();
    }

    private void handleEmptyQuizRoom(Long roomId, QuizRoom quizRoom, MemberDto memberDto) {
        if (quizRoom.getUsers().isEmpty()) {
            delete(quizRoom);
            deleteQuiz(roomId.intValue());
        } else {
            if (memberDto.isHost()) {
                quizRoom.getUsers().get(0).setHost(true);
            }
            saveAndSendQuizRoom(roomId, quizRoom);
        }
    }

    private void deleteQuiz(int roomId) {
        Optional<Quiz> optionalQuiz = quizRepository.findByRoomId(roomId);
        Quiz quiz = optionalQuiz.orElseThrow();
        quizRepository.delete(quiz);
    }

    private void saveAndSendQuizRoom(Long roomId, QuizRoom quizRoom) {
        this.save(quizRoom);
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "detail");
        payload.put("result", quizRoom);
        messageTemplate.convertAndSend("/sub/quiz/" + roomId, payload);
    }



    public Quiz createQuiz(QuizRoom createdQuizroom) {
        Quiz quiz = new Quiz();

        quiz.setRoomId(createdQuizroom.getId().intValue());
        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        quiz.setCreatedDate(formattedDateTime);
        quiz.setQuizCnt(createdQuizroom.getQuizCnt());
        quiz.setRoomName(createdQuizroom.getRoomName());



        QuestionDto question1 = new QuestionDto();
        question1.setQuestion("IP(Internet Protocol) 주소는 어떻게 구성되며, 어떤 역할을 담당하고 있나요?");
        question1.setId(1);

        List<ExampleDto> examples = new ArrayList<>();
        examples.add(new ExampleDto("1", "IP 주소는 64비트로 구성되어 있으며, 데이터의 무결성을 검증한다."));
        examples.add(new ExampleDto("2", "IP 주소는 4바이트로 이루어져 있으며, 컴퓨터의 주소 역할을 한다."));
        examples.add(new ExampleDto("3", "IP 주소는 데이터의 재조합을 처리하며, 데이터의 순서를 조정한다."));
        examples.add(new ExampleDto("4", "IP 주소는 하드웨어 고유의 식별번호인 MAC 주소와 동일하다."));

        question1.setExample(examples);

        question1.setAnswer("2");
        question1.setCommentary("해설1");

        QuestionDto question2 = new QuestionDto();
        question2.setQuestion("TCP/IP는 어떤 두 가지 프로토콜로 구성되어 있으며, 어떤 역할을 각각 수행하고 있는가?");
        question2.setId(2);

        List<ExampleDto> examples2 = new ArrayList<>();
        examples2.add(new ExampleDto("1", "TCP가 데이터의 추적을 처리하고, IP가 데이터의 배달을 담당한다."));
        examples2.add(new ExampleDto("2", "IP가 데이터의 추적을 처리하고, TCP가 데이터의 배달을 담당한다."));
        examples2.add(new ExampleDto("3", "TCP와 IP가 모두 데이터의 재조합을 처리한다."));
        examples2.add(new ExampleDto("4", "TCP와 IP가 모두 데이터의 손실 여부를 확인한다."));

        question2.setExample(examples2);

        question2.setAnswer("1");
        question2.setCommentary("해설2");

        List<QuestionDto> questions = new ArrayList<>();
        questions.add(question1);
        questions.add(question2);

        quiz.setQuestion(questions);

        quiz.setComplete(false);

        return quiz;
    }
}
