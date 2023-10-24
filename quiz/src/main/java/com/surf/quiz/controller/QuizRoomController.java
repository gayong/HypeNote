package com.surf.quiz.controller;


import com.surf.quiz.dto.CreateRoomDto;
import com.surf.quiz.entity.Member;
import com.surf.quiz.entity.QuizRoom;
import com.surf.quiz.service.QuizRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "퀴즈", description = "퀴즈")
public class QuizRoomController {
    private final QuizRoomService quizroomService;
    private final SimpMessagingTemplate messageTemplate; // 메시지 브로커를 통해 클라이언트와 서버 간의 실시간 메시지 교환을 처리

    // 일정한 시간 간격으로 작업(태스크)를 실행하거나 지연 실행할 수 있는 스레드 풀 기반의 스케줄링 서비스
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    //단일 스레드로 동작하는 스케줄링 서비스(ScheduledExecutorService) 객체를 생성하고, 이 객체(scheduler)에 대한 참조를 유지

    @PostMapping("/quizroom")
    @Operation(summary = "방 생성")
    public QuizRoom createRoom(@RequestBody CreateRoomDto createRoomDto) {
        QuizRoom quizroomToBeCreated = QuizRoom.builder()
                .roomName(createRoomDto.getRoomName())
                .quizCnt(createRoomDto.getQuizCount())
                .createdDate(LocalDateTime.now())
                .build();

        QuizRoom createdQuizroom = quizroomService.save(quizroomToBeCreated);
        List<QuizRoom> listRoom = quizroomService.findAll();
        scheduler.schedule(() -> messageTemplate.convertAndSend("/sub/quizroom/roomList", listRoom), 100, TimeUnit.MILLISECONDS);
        return createdQuizroom;
    }

    @GetMapping("/quizroom/{id}")
    @Operation(summary = "단일 방 탐색")
    public Optional<QuizRoom> searchRoom(@PathVariable Long id) {
        return quizroomService.findById(id);
    }

    @GetMapping("/quizroom")
    @Operation(summary = "전체 방 탐색")
    public List<QuizRoom> searchAllRoom() {
        return quizroomService.findAll();
    }


    @MessageMapping("/quizroom/entrance/{roomId}")
    @Operation(summary = "방 입장")
    public void entranceRoom(
            @DestinationVariable Long roomId,
            @Payload Member body) {

        QuizRoom targetRoom = quizroomService.findById(roomId).orElseThrow();

        List<Member> members = targetRoom.getUsers();

        for (Member member : members) {
            if (body.getUserId().equals(member.getUserId())) {
                messageTemplate.convertAndSend("/sub/quizroom/info/" + roomId, targetRoom);
                return;
            }
        }

        targetRoom.memberEntrance(body);


        targetRoom.setRoomCnt(targetRoom.getRoomCnt() + 1);
        quizroomService.save(targetRoom);
        List<QuizRoom> listRoom = quizroomService.findAll();

        messageTemplate.convertAndSend("/sub/quizroom/info/" + roomId, targetRoom);
        scheduler.schedule(() -> messageTemplate.convertAndSend("/sub/quizroom/roomList", listRoom), 100, TimeUnit.MILLISECONDS);
        // 100 밀리초 후에 지정된 메시지(listRoom)를 "/sub/quizroom/roomList" 주제로 구독 중인 클라이언트들에게 전송하는 작업을 예약
    }

    @MessageMapping("/quizroom/exit/{roomId}")
    public void exitRoom(
            @DestinationVariable Long roomId,
            @Payload Member body) {

        QuizRoom targetRoom = quizroomService.findById(roomId).orElseThrow();

        boolean check = false;

        for (Member member : targetRoom.getUsers()) {
            if (member.getUserId().equals(body.getUserId())) {
                targetRoom.memberExit(body);
                targetRoom.setRoomCnt(targetRoom.getRoomCnt() - 1);
                quizroomService.save(targetRoom);
                check = true;
                break;
            }
        }

        if (!check) {
            return;
        }

        if (targetRoom.getUsers().isEmpty()) {
            quizroomService.delete(targetRoom);
        } else {
            if (body.isHost()) {
                targetRoom.getUsers().get(0).setHost(true);
                messageTemplate.convertAndSend("/sub/quizroom/info/" + roomId, targetRoom);
            } else {
                messageTemplate.convertAndSend("/sub/quizroom/info/" + roomId, targetRoom);
            }
        }

        List<QuizRoom> listRoom = quizroomService.findAll();

        scheduler.schedule(() -> messageTemplate.convertAndSend("/sub/quizroom/roomList", listRoom), 100, TimeUnit.MILLISECONDS);

    }


    @MessageMapping("/quizroom/ready/{roomId}")
    public void ready(
            @DestinationVariable Long roomId,
            @Payload Member body) {

        QuizRoom targetRoom = quizroomService.findById(roomId).orElseThrow();

        targetRoom.readyMember(body);
        System.out.println("targetRoom = " + targetRoom.getUsers());
        System.out.println("body = " + body);
        targetRoom.setReady(targetRoom.getReady() + 1);
        quizroomService.save(targetRoom);
        messageTemplate.convertAndSend("/sub/quizroom/info/" + roomId, targetRoom);
    }

    @MessageMapping("/quizroom/unready/{roomId}")
    public void unready(
            @DestinationVariable Long roomId,
            @Payload Member body) {

        QuizRoom targetRoom = quizroomService.findById(roomId).orElseThrow();
        System.out.println("targetRoom = " + targetRoom);
        System.out.println("body = " + body);
        targetRoom.unreadyMember(body);

        targetRoom.setReady(targetRoom.getReady() - 1);
        quizroomService.save(targetRoom);
        messageTemplate.convertAndSend("/sub/quizroom/info/" + roomId, targetRoom);
    }

    @MessageMapping("/quizroom/roomList")
    public void roomList() {

        List<QuizRoom> rooms = quizroomService.findAll();

        messageTemplate.convertAndSend("/sub/quizroom/roomList", rooms);
    }

    @MessageMapping("/quizroom/check/{roomId}")
    public void checkRoomInfo(@DestinationVariable Long roomId) {

        QuizRoom targetRoom = quizroomService.findById(roomId).orElseThrow();

        messageTemplate.convertAndSend("/sub/quizroom/info/" + roomId, targetRoom);
    }

    @MessageMapping("/quizroom/terminate/{roomId}")
    public void terminate(@DestinationVariable Long roomId) {
        quizroomService.terminate(roomId);

        List<QuizRoom> rooms = quizroomService.findAll();
        messageTemplate.convertAndSend("/sub/quizroom/roomList", rooms);
    }

}
