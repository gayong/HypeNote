package com.surf.quiz.service;


import com.surf.quiz.common.BaseResponse;
import com.surf.quiz.dto.ExampleDto;
import com.surf.quiz.dto.MemberDto;
import com.surf.quiz.dto.QuestionDto;
import com.surf.quiz.dto.UserDto;
import com.surf.quiz.dto.editor.EditorShareListRequestDto;
import com.surf.quiz.dto.editor.EditorShareMemberRequestDto;
import com.surf.quiz.dto.member.FindUserPkListDto;
import com.surf.quiz.dto.member.UserInfoResponseDto;
import com.surf.quiz.dto.request.CreateRoomRequestDto;
import com.surf.quiz.dto.request.SearchMemberRequestDto;
import com.surf.quiz.dto.response.DetailResponseDto;
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
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class QuizRoomService {
    private final QuizRoomRepository quizRepo;
    private final QuizRepository quizRepository;
    private final FeignService feignService;
    private final SimpMessagingTemplate messageTemplate; // 메시지 브로커를 통해 클라이언트와 서버 간의 실시간 메시지 교환을 처리

    // 일정한 시간 간격으로 작업(태스크)를 실행하거나 지연 실행할 수 있는 스레드 풀 기반의 스케줄링 서비스
    //단일 스레드로 동작하는 스케줄링 서비스(ScheduledExecutorService) 객체를 생성하고, 이 객체(scheduler)에 대한 참조를 유지
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public QuizRoomService(QuizRoomRepository quizRepo, FeignService feignService, SimpMessagingTemplate messageTemplate, QuizRepository quizRepository) {
        this.quizRepo = quizRepo;
        this.messageTemplate = messageTemplate;
        this.quizRepository = quizRepository;
        this.feignService = feignService;
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

    public List<QuizRoom> findByInviteUser(Long userPk) {
        {
            return quizRepo.findByInviteUsers_UserPk(userPk);
        }
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

    public void findAllSend() {
        List<QuizRoom> roomList = this.findAll().stream()
                .filter(room -> !room.isSingle() && !room.isRoomStatus())
                .collect(Collectors.toList());
        scheduler.schedule(() -> messageTemplate.convertAndSend("/sub/quizroom/roomList", roomList), 1, TimeUnit.SECONDS);
    }

    public void findAllMySend(Long userPk) {
        List<QuizRoom> roomList = this.findByInviteUser(userPk).stream()
                .filter(room -> !room.isSingle() && !room.isRoomStatus())
                .collect(Collectors.toList());
        scheduler.schedule(() -> messageTemplate.convertAndSend("/sub/quizroom/roomList/"+userPk, roomList), 1, TimeUnit.SECONDS);
    }


    public void findAllAndSend(QuizRoom quizRoom) {
        for (UserDto user : quizRoom.getInviteUsers()) {
            List<QuizRoom> roomList = this.findByInviteUser(user.getUserPk()).stream()
                    .filter(room -> !room.isSingle() && !room.isRoomStatus())
                    .toList();
            scheduler.schedule(() -> messageTemplate.convertAndSend("/sub/quizroom/roomList/" + user.getUserPk(), roomList), 1, TimeUnit.SECONDS);
        }
        }
/// 구독을 userpk로 구독하게하기
    /// roomlist를 찾을 때 구독한 userPk로 보내게 하기


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
        List<UserDto> inviteUsers = createInviteUsers(SearchMemberRequestDto.getPages());

        roomDto.setInviteUsers(inviteUsers);

        return roomDto;
    }

    private List<UserDto> createInviteUsers(List<String> pages) {
        List<Integer> res = feignService.getEditorShare(new EditorShareMemberRequestDto(pages));
        List<Integer> res1 = feignService.editorShareList(new EditorShareListRequestDto(pages));
        System.out.println("res = " + res);
        System.out.println("res1 = " + res1);

        List<UserDto> inviteUsers = new ArrayList<>();
        if(res1 == null || res1.isEmpty()) {
            return inviteUsers;
        }

        FindUserPkListDto dto = new FindUserPkListDto();
        dto.setUserPkList(res1);  // res를 userPkList로 설정
        List<UserInfoResponseDto> lst = feignService.userInfoByUserPkList(dto);
        System.out.println("lst = " + lst);

        if(lst == null || lst.isEmpty()) {
            return inviteUsers;
        }

        System.out.println("lst = " + lst);

        for(UserInfoResponseDto userInfo : lst) {
            UserDto user = createUser((long)userInfo.getUserPk(), userInfo.getNickName(), userInfo.getProfileImage());
            inviteUsers.add(user);
        }

        return inviteUsers;
    }

    private UserDto createUser(Long userPk, String userName, String userImg) {
        UserDto user = new UserDto();
        user.setUserPk(userPk);
        user.setUserName(userName);
        user.setUserImg(userImg);
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
        member.setReady(true);
        member.setUserPk(createRoomRequestDto.getUsers().get(0).getUserPk());
        member.setUserName(createRoomRequestDto.getUsers().get(0).getUserName());
        member.setUserImg(createRoomRequestDto.getUsers().get(0).getUserImg());
        if (createRoomRequestDto.getUsers().get(0).getUserImg().equals("성공")) {
            member.setUserImg("/assets/유령.png");
        }
        List<MemberDto> members = new ArrayList<>();
        members.add(member);

        createQuizRoom.setUsers(members);
        createQuizRoom.setRoomCnt(1);
        createQuizRoom.setReadyCnt(1);

        QuizRoom createdQuizroom = this.save(createQuizRoom);



        // 퀴즈 생성 작업을 비동기로 수행
        CompletableFuture.runAsync(() -> {
            Quiz quiz = this.createQuiz(createdQuizroom);
            SendQuizReady(createdQuizroom.getId());
            quizRepository.save(quiz);
        }).exceptionally(ex -> {
            // 오류 처리 코드
            System.out.println("퀴즈 생성 중 오류 발생: " + ex.getMessage());
            return null;
        });

        // 스레드 스케줄러
        this.findAllAndSend(createdQuizroom);
        this.SendCreateQuizRoom(createdQuizroom);

        return createdQuizroom;
    }




    // 레디, 언레디
    public void changeReady(Long roomId, Map<String, Object> payload) {
        QuizRoom quizRoom = this.findById(roomId).orElseThrow();

        long id;
        Object userPk = payload.get("userPk");
        if (userPk instanceof Number) {
            id = ((Number) userPk).longValue();
        } else {
            id = Long.parseLong((String) userPk);
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

        this.findAllAndSend(quizRoom);
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
        if (memberDto.getUserImg().equals("성공")) {
            memberDto.setUserImg("/assets/유령.png");
        }
        //입장
        quizRoom.memberIn(memberDto);
        quizRoom.setRoomCnt(quizRoom.getRoomCnt() + 1);
        // 저장
        saveAndSendQuizRoom(roomId, quizRoom);
        this.findAllAndSend(quizRoom);
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
        System.out.println("member = " + member.isHost() + member.getUserPk());
        quizRoom.memberOut(memberDto);
        quizRoom.setRoomCnt(quizRoom.getRoomCnt() - 1);

        // 빈 방이면 삭제, 빈 방 아니면 다른 녀석에 방장 주기
        handleEmptyQuizRoom(roomId, quizRoom, member);
    }

    private Optional<MemberDto> findMemberInQuizRoom(QuizRoom quizRoom, MemberDto memberDto) {
        return quizRoom.getUsers().stream()
                .filter(member -> member.getUserPk().equals(memberDto.getUserPk()))
                .findFirst();
    }

    private void handleEmptyQuizRoom(Long roomId, QuizRoom quizRoom, MemberDto memberDto) {
        System.out.println("memberDto = " + memberDto);
        System.out.println("quizRoom = " + quizRoom.getUsers());
        if (quizRoom.getRoomCnt()==0) {
            delete(quizRoom);
            deleteQuiz(roomId.intValue());
        } else {
            if (memberDto.isHost()) {
                quizRoom.getUsers().get(0).setHost(true);
                quizRoom.getUsers().get(0).setReady(true);
                System.out.println("quizRoom = " + quizRoom.getUsers());
            }
            quizRoom.setReadyCnt(quizRoom.getReadyCnt()+1);
            System.out.println("quizRoom = " + quizRoom.getUsers());
            saveAndSendQuizRoom(roomId, quizRoom);
            this.save(quizRoom);
            this.findAllAndSend(quizRoom);
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
        DetailResponseDto response = detailConvert(quizRoom);

        boolean isQuizExist = quizRepository.findByRoomId(quizRoom.getId().intValue()).isPresent();

        payload.put("type", "detail");
        payload.put("quizReady", isQuizExist);
        payload.put("result", response);
        messageTemplate.convertAndSend("/sub/quiz/" + roomId, payload);
    }

    private void SendCreateQuizRoom(QuizRoom quizRoom) {
        Map<String, Object> payload = new HashMap<>();
        DetailResponseDto response = detailConvert(quizRoom);

        boolean isQuizExist = quizRepository.findByRoomId(quizRoom.getId().intValue()).isPresent();

        payload.put("type", "detail");
        payload.put("quizReady", isQuizExist);
        payload.put("result", response);
        scheduler.schedule(() -> messageTemplate.convertAndSend("/sub/quiz/" + quizRoom.getId(), payload), 2, TimeUnit.SECONDS);
    }

    private void SendQuizReady(Long roomId) {
        QuizRoom quizRoom = quizRepo.findById(roomId).orElseThrow();
        Map<String, Object> payload = new HashMap<>();
        DetailResponseDto response = detailConvert(quizRoom);

        payload.put("type", "detail");
        payload.put("quizReady", true);
        payload.put("result", response);
        messageTemplate.convertAndSend("/sub/quiz/" + quizRoom.getId(), payload);
    }


    public DetailResponseDto detailConvert(QuizRoom quizRoom) {
        MemberDto hostUser = quizRoom.getUsers().stream()
                .filter(MemberDto::isHost)
                .findFirst()
                .orElse(null);

        return DetailResponseDto.builder()
                .id(quizRoom.getId())
                .roomName(quizRoom.getRoomName())
                .roomCnt(quizRoom.getRoomCnt())
                .roomMax(quizRoom.getRoomMax())
                .roomStatus(quizRoom.isRoomStatus())
                .quizCnt(quizRoom.getQuizCnt())
                .users(quizRoom.getUsers())
                .readyCnt(quizRoom.getReadyCnt())
                .content(quizRoom.getContent())
                .createdDate(quizRoom.getCreatedDate())
                .inviteUsers(quizRoom.getInviteUsers())
                .single(quizRoom.isSingle())
                .pages(quizRoom.getPages())
                .sharePages(quizRoom.getSharePages())
                .host(hostUser != null ? hostUser.getUserPk() : null)
                .build();
    }


    public CompletableFuture<BaseResponse<List<QuestionDto>>> getGptWithRetry(int retry, int index, String res) {
        return CompletableFuture.supplyAsync(() -> {
            for (int i = 0; i < retry; i++) {
                try {
                    return feignService.getGpt(1, res, index + 1);
                } catch (Exception e) {
                    // 예외 발생 시 로그 출력 및 재시도
                    System.out.println("재시도: " + (i + 1));
                    if (i == retry - 1) {
                        // 최대 재시도 횟수를 넘었을 때 null 반환
                        return null;
                    }
                }
            }
            return null;
        });
    }


    public Quiz createQuiz(QuizRoom createdQuizroom) {
//        String res = feignService.getEditorInfoQuiz("6549f6984cce962b0ff0a058");
        List<String> res = createdQuizroom.getPages().stream()
                .map(feignService::getEditorInfoQuiz)
                .toList();
        Quiz quiz = new Quiz();
        quiz.setRoomId(createdQuizroom.getId().intValue());
        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        quiz.setCreatedDate(formattedDateTime);
        quiz.setQuizCnt(createdQuizroom.getQuizCnt() * createdQuizroom.getPages().size());
        quiz.setRoomName(createdQuizroom.getRoomName());
        quiz.setUsers(createdQuizroom.getUsers());

        // GPT 문제 생성 보내기
        try {
            List<CompletableFuture<BaseResponse<List<QuestionDto>>>> futures = res.stream()
                    .flatMap(r -> IntStream.range(0, createdQuizroom.getQuizCnt())
                            .mapToObj(i -> getGptWithRetry( 3, i, r)))
                    .toList();

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            List<QuestionDto> question = futures.stream()
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)  // null 제외
                    .flatMap(response -> response.getResult().stream())  // List<QuestionDto>를 QuestionDto 스트림으로 변환
                    .collect(Collectors.toList());
            // 각 QuestionDto의 id 설정
            for (int i = 0; i < question.size(); i++) {
                question.get(i).setId(i + 1);
            }

            quiz.setQuestion(question);
            quiz.setComplete(false);

            return quiz;

        } catch (Exception e) {
            // 예외 처리 코드를 작성합니다.
        }

        List<QuestionDto> questions = new ArrayList<>();
        for (int i = 0; i < createdQuizroom.getQuizCnt(); i++) {
            QuestionDto question = new QuestionDto();
            question.setQuestion("IP(Internet Protocol) 주소는 어떻게 구성되며, 어떤 역할을 담당하고 있나요?");
            question.setId(i + 1);

            List<ExampleDto> examples = new ArrayList<>();
            examples.add(new ExampleDto("1", "IP 주소는 64비트로 구성되어 있으며, 데이터의 무결성을 검증한다."));
            examples.add(new ExampleDto("2", "IP 주소는 4바이트로 이루어져 있으며, 컴퓨터의 주소 역할을 한다."));
            examples.add(new ExampleDto("3", "IP 주소는 데이터의 재조합을 처리하며, 데이터의 순서를 조정한다."));
            examples.add(new ExampleDto("4", "IP 주소는 하드웨어 고유의 식별번호인 MAC 주소와 동일하다."));

            question.setExample(examples);

            question.setAnswer("2");
            question.setCommentary("IP 주소는 4바이트로 이루어져 있으며, 컴퓨터의 주소 역할을 한다.");

            questions.add(question);
        }

        quiz.setQuestion(questions);
        quiz.setComplete(false);
        System.out.println("quiz = " + quiz);
        return quiz;
    }
}
