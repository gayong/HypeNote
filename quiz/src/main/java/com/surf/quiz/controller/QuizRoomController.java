package com.surf.quiz.controller;


import com.surf.quiz.dto.CreateRoomDto;
import com.surf.quiz.dto.SearchMemberDto;
import com.surf.quiz.dto.UserDto;
import com.surf.quiz.entity.QuizRoom;
import com.surf.quiz.service.QuizRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "퀴즈룸", description = "퀴즈룸")
public class QuizRoomController {
    private final QuizRoomService quizroomService;

    private final SimpMessagingTemplate messageTemplate; // 메시지 브로커를 통해 클라이언트와 서버 간의 실시간 메시지 교환을 처리


    // 일정한 시간 간격으로 작업(태스크)를 실행하거나 지연 실행할 수 있는 스레드 풀 기반의 스케줄링 서비스
    //단일 스레드로 동작하는 스케줄링 서비스(ScheduledExecutorService) 객체를 생성하고, 이 객체(scheduler)에 대한 참조를 유지
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // 앱 종료 시 스레드 종료
    @PreDestroy
    public void destroy() {
        scheduler.shutdown();
    }

    @PostMapping("/quizroom")
    @Operation(summary = "멤버 탐색")
    public CreateRoomDto searchMember(@RequestBody SearchMemberDto SearchMemberDto) {
        CreateRoomDto roomDto = new CreateRoomDto();
        roomDto.setRoomName(SearchMemberDto.getRoomName());
        roomDto.setQuizCnt(SearchMemberDto.getQuizCnt());
        roomDto.setPages(SearchMemberDto.getPages());
        roomDto.setSharePages(SearchMemberDto.getSharePages());
        roomDto.setSingle(SearchMemberDto.isSingle());

        // invite users = 초대 받은 유저 + 방 만든 유저
        // 방을 만든 사람이면 리스트의 0번으로 넣기 > 방장으로 만들기 위해서
        List<UserDto> inviteUsers = new ArrayList<>();
        UserDto user1 = new UserDto();
        user1.setUserPk(1L);
        user1.setUserName("csi");
        inviteUsers.add(user1);

        UserDto user2 = new UserDto();
        user2.setUserPk(2L);
        user2.setUserName("isc");
        inviteUsers.add(user2);

        roomDto.setInviteUsers(inviteUsers);

        return roomDto;
    }


    @PostMapping("/quizroom/invite")
    @Operation(summary = "방 생성")
    public QuizRoom createQuizRoom(@RequestBody CreateRoomDto createRoomDto) {
        QuizRoom createQuizRoom = QuizRoom.builder()
                .roomName(createRoomDto.getRoomName())
                .quizCnt(createRoomDto.getQuizCnt())
                .createdDate(LocalDateTime.now())
                .sharePages(createRoomDto.getSharePages())
                .pages(createRoomDto.getPages())
                .single(createRoomDto.isSingle())
                .inviteUsers(createRoomDto.getInviteUsers())
                .build();

        // 싱글 모드면 roomMax 1 / 그룹 모드면 초대 인원 수
        if (createQuizRoom.isSingle()) {
            createQuizRoom.setRoomMax(1);
        } else {
            createQuizRoom.setRoomMax(createRoomDto.getInviteUsers().size());
        }

        QuizRoom createdQuizroom = quizroomService.save(createQuizRoom);
        List<QuizRoom> roomList = quizroomService.findAll();

        // 스케줄러로 룸리스트 1초 후 전송
        scheduler.schedule(() -> messageTemplate.convertAndSend("/sub/quizroom/roomList", roomList), 1, TimeUnit.SECONDS);

        // 방을 만들자마자 방장이 들어가게 하거나
        // 혹은 0명인 상태로 아무도 안 들어가고 5분 지나면 폭파 이런 방식 ?

//        // 1초마다 전송
//        scheduler.scheduleAtFixedRate(() -> messageTemplate.convertAndSend("/sub/quizroom/roomList", roomList), 1000, 1000, TimeUnit.MILLISECONDS);
        return createdQuizroom;
    }

    @GetMapping("/quizroom")
    @Operation(summary = "전체 방 탐색")
    public List<QuizRoom> findQuizRooms() {
        return quizroomService.findAll();
    }


    @MessageMapping("/quizroom/in/{roomId}")
    @Operation(summary = "방 입장")
    public void inQuizRoom(@DestinationVariable Long roomId, @Payload SearchMemberDto.Member body) {

        Optional<QuizRoom> optionalQuizRoom = quizroomService.findById(roomId);
        QuizRoom quizRoom = optionalQuizRoom.orElseThrow();

        List<SearchMemberDto.Member> members = quizRoom.getUsers();

        // 초대 유저에 없으면 입장 x
        if (quizRoom.getInviteUsers().stream().noneMatch(user -> Objects.equals(user.getUserPk(), body.getUserPk()))) {
            return;
        }

        // 멤버가 있는데 들어가려는 유저가 이미 방에 있으면 리턴
        if (members != null) {
            for (SearchMemberDto.Member member : members) {
                if (body.getUserPk().equals(member.getUserPk())) {
//                    messageTemplate.convertAndSend("/sub/quizroom/detail/" + roomId, quizRoom);
                    return;
                }
            }
        }

        // 방에 입장
        quizRoom.memberIn(body);

        quizRoom.setRoomCnt(quizRoom.getRoomCnt() + 1);
        quizroomService.save(quizRoom);
        List<QuizRoom> roomList = quizroomService.findAll();

        messageTemplate.convertAndSend("/sub/quizroom/detail/" + roomId, quizRoom);
        scheduler.schedule(() -> messageTemplate.convertAndSend("/sub/quizroom/roomList", roomList), 1, TimeUnit.SECONDS);
        // 1초 후에 지정된 메시지(roomList)를 "/sub/quizroom/roomList" 주제로 구독 중인 클라이언트들에게 전송하는 작업을 예약
    }

    @MessageMapping("/quizroom/out/{roomId}")
    public void outQuizRoom(@DestinationVariable Long roomId, @Payload SearchMemberDto.Member body) {

        Optional<QuizRoom> optionalQuizRoom = quizroomService.findById(roomId);
        QuizRoom quizRoom = optionalQuizRoom.orElseThrow();

        // 나가려는 유저가 방에 있는 지 확인
        Optional<SearchMemberDto.Member> optionalMember = quizRoom.getUsers().stream()
                .filter(member -> member.getUserPk().equals(body.getUserPk()))
                .findFirst();

        // 방에 없으면 리턴
        if (optionalMember.isEmpty()) {
            return;
        }

        SearchMemberDto.Member member = optionalMember.get();

        // 나가는 멤버가 레디 상태면 레디 -1
        if (member.isReady()) {
            quizRoom.setReadyCnt(quizRoom.getReadyCnt() - 1);
        }

        // 방에서 퇴장
        quizRoom.memberOut(body);
        quizRoom.setRoomCnt(quizRoom.getRoomCnt() - 1);


        // 멤버가 나가서 방이 비면 삭제
        if (quizRoom.getUsers().isEmpty()) {
            quizroomService.delete(quizRoom);
        } else {
            // 나가는 멤버가 방장인 경우 다른 멤버 중 첫 번째 멤버를 방장 설정
            if (body.isHost()) {
                quizRoom.getUsers().get(0).setHost(true);
                messageTemplate.convertAndSend("/sub/quizroom/detail/" + roomId, quizRoom);
            } else {
                messageTemplate.convertAndSend("/sub/quizroom/detail/" + roomId, quizRoom);
            }
        }

        quizroomService.save(quizRoom);
        List<QuizRoom> roomList = quizroomService.findAll();
        scheduler.schedule(() -> messageTemplate.convertAndSend("/sub/quizroom/roomList", roomList), 1, TimeUnit.SECONDS);
    }


    @MessageMapping("/quizroom/ready/{roomId}")
    public void ready(@DestinationVariable Long roomId, @Payload Map<String, Object> payload) {

        Optional<QuizRoom> optionalQuizRoom = quizroomService.findById(roomId);
        QuizRoom quizRoom = optionalQuizRoom.orElseThrow();

        // userpk , 레디 받기
        Long id = ((Number) payload.get("userPk")).longValue();
        String action = (String) payload.get("action");

        if (action.equals("ready")) {
            // 레디 처리
            quizRoom.memberReady(id, action);
            quizRoom.setReadyCnt(quizRoom.getReadyCnt() + 1);
            quizroomService.save(quizRoom);
            messageTemplate.convertAndSend("/sub/quizroom/detail/" + roomId, quizRoom);
        } else if (action.equals("unready")) {
            // 언레디 처리
            quizRoom.memberReady(id, action);
            quizRoom.setReadyCnt(quizRoom.getReadyCnt() - 1);
            quizroomService.save(quizRoom);
            messageTemplate.convertAndSend("/sub/quizroom/detail/" + roomId, quizRoom);
        }
        List<QuizRoom> roomList = quizroomService.findAll();
        scheduler.schedule(() -> messageTemplate.convertAndSend("/sub/quizroom/roomList", roomList), 1, TimeUnit.SECONDS);
    }


    @MessageMapping("/quizroom/roomList")
    public void getQuizRooms() {

        List<QuizRoom> rooms = quizroomService.findAll();

        messageTemplate.convertAndSend("/sub/quizroom/roomList", rooms);
    }

    @MessageMapping("/quizroom/{roomId}")
    public void getQuizRoom(@DestinationVariable Long roomId) {

        Optional<QuizRoom> optionalQuizRoom = quizroomService.findById(roomId);
        QuizRoom quizRoom = optionalQuizRoom.orElseThrow();

        messageTemplate.convertAndSend("/sub/quizroom/detail/" + roomId, quizRoom);
    }



}
