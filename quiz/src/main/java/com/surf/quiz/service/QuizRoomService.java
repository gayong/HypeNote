package com.surf.quiz.service;


import com.surf.quiz.dto.MemberDto;
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
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class QuizRoomService {
    private final QuizRoomRepository quizRepo;
    private final SimpMessagingTemplate messageTemplate; // 메시지 브로커를 통해 클라이언트와 서버 간의 실시간 메시지 교환을 처리
//    private final QuizService quizService;
//    private final QuizRepository quizRepository;

    // 일정한 시간 간격으로 작업(태스크)를 실행하거나 지연 실행할 수 있는 스레드 풀 기반의 스케줄링 서비스
    //단일 스레드로 동작하는 스케줄링 서비스(ScheduledExecutorService) 객체를 생성하고, 이 객체(scheduler)에 대한 참조를 유지
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public QuizRoomService(QuizRoomRepository quizRepo, SimpMessagingTemplate messageTemplate) {
        this.quizRepo = quizRepo;
        this.messageTemplate = messageTemplate;
//        this.quizService = quizService;
//        this.quizRepository = quizRepository;
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
        List<QuizRoom> roomList = this.findAll();
        scheduler.schedule(() -> messageTemplate.convertAndSend("/sub/quizroom/roomList", roomList), 1, TimeUnit.SECONDS);
    }


    public SearchMemberResponseDto createSearchMemberResponseDto(SearchMemberRequestDto SearchMemberRequestDto) {
        SearchMemberResponseDto roomDto = new SearchMemberResponseDto();
        roomDto.setRoomName(SearchMemberRequestDto.getRoomName());
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
        QuizRoom createQuizRoom = QuizRoom.builder()
                .roomName(createRoomRequestDto.getRoomName())
                .quizCnt(createRoomRequestDto.getQuizCnt())
                .createdDate(LocalDateTime.now())
                .sharePages(createRoomRequestDto.getSharePages())
                .pages(createRoomRequestDto.getPages())
                .single(createRoomRequestDto.isSingle())
                .inviteUsers(createRoomRequestDto.getInviteUsers())
                .build();

        // 싱글 모드면 roomMax 1 / 그룹 모드면 초대 인원 수
        if (createQuizRoom.isSingle()) {
            createQuizRoom.setRoomMax(1);
        } else {
            createQuizRoom.setRoomMax(createRoomRequestDto.getInviteUsers().size());
        }

        QuizRoom createdQuizroom = this.save(createQuizRoom);

//        // 퀴즈 생성
//        Quiz quiz = quizService.createQuiz(createdQuizroom.getId().intValue());
//        quizRepository.save(quiz);

        // 스레드 스케줄러
        this.findAllAndSend();

        return createdQuizroom;
    }




    public void changeReady(Long roomId, Map<String, Object> payload) {
        Optional<QuizRoom> optionalQuizRoom = this.findById(roomId);
        QuizRoom quizRoom = optionalQuizRoom.orElseThrow();

        // userpk , 레디 받기
        Long id = ((Number) payload.get("userPk")).longValue();
        String action = (String) payload.get("action");

        if (action.equals("ready")) {
            // 레디 처리
            quizRoom.memberReady(id, action);
            quizRoom.setReadyCnt(quizRoom.getReadyCnt() + 1);
            this.save(quizRoom);
            messageTemplate.convertAndSend("/sub/quizroom/detail/" + roomId, quizRoom);

        } else if (action.equals("unready")) {
            // 언레디 처리
            quizRoom.memberReady(id, action);
            quizRoom.setReadyCnt(quizRoom.getReadyCnt() - 1);
            this.save(quizRoom);
            messageTemplate.convertAndSend("/sub/quizroom/detail/" + roomId, quizRoom);
        }

        // 스레드 스케줄러
        this.findAllAndSend();
    }



    public void enterQuizRoom(Long roomId, MemberDto memberDto ) {
        Optional<QuizRoom> optionalQuizRoom = findById(roomId);
        QuizRoom quizRoom = optionalQuizRoom.orElseThrow();

        List<MemberDto> members = quizRoom.getUsers();

        // 초대 유저에 없으면 입장 X
        if (quizRoom.getInviteUsers().stream().noneMatch(user -> Objects.equals(user.getUserPk(), memberDto.getUserPk()))) {
            return;
        }

        // 멤버가 있는데 들어가려는 유저가 이미 방에 있으면 리턴
        if (members != null) {
            for (MemberDto member : members) {
                if (memberDto.getUserPk().equals(member.getUserPk())) {
                    return;
                }
            }
        }

        // 방에 입장
        quizRoom.memberIn(memberDto);

        quizRoom.setRoomCnt(quizRoom.getRoomCnt() + 1);
        this.save(quizRoom);
        messageTemplate.convertAndSend("/sub/quizroom/detail/" + roomId, quizRoom);

        this.findAllAndSend();
    }


    public void leaveQuizRoom(Long roomId, MemberDto memberDto) {
        Optional<QuizRoom> optionalQuizRoom = findById(roomId);
        QuizRoom quizRoom = optionalQuizRoom.orElseThrow();

        Optional<MemberDto> optionalMember = quizRoom.getUsers().stream()
                .filter(member -> member.getUserPk().equals(memberDto.getUserPk()))
                .findFirst();

        if (optionalMember.isEmpty()) {
            return;
        }

        MemberDto member = optionalMember.get();

        if (member.isReady()) {
            quizRoom.setReadyCnt(quizRoom.getReadyCnt() - 1);
        }

        quizRoom.memberOut(memberDto);
        quizRoom.setRoomCnt(quizRoom.getRoomCnt() - 1);

        if (quizRoom.getUsers().isEmpty()) {
            delete(quizRoom);
        } else {
            if (memberDto.isHost()) {
                quizRoom.getUsers().get(0).setHost(true);
                messageTemplate.convertAndSend("/sub/quizroom/detail/" + roomId, quizRoom);
            } else {
                messageTemplate.convertAndSend("/sub/quizroom/detail/" + roomId, quizRoom);
            }
        }

        this.save(quizRoom);
        this.findAllAndSend();
    }

}
