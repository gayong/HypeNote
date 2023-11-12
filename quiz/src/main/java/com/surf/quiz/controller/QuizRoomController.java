package com.surf.quiz.controller;


import com.surf.quiz.common.BaseResponse;
import com.surf.quiz.dto.request.CreateRoomRequestDto;
import com.surf.quiz.dto.MemberDto;
import com.surf.quiz.dto.request.SearchMemberRequestDto;
import com.surf.quiz.dto.response.SearchMemberResponseDto;
import com.surf.quiz.entity.QuizRoom;
import com.surf.quiz.service.QuizRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
@Tag(name = "퀴즈룸", description = "퀴즈룸")
public class QuizRoomController {
    private final QuizRoomService quizRoomService;

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
    public BaseResponse<SearchMemberResponseDto> searchMember(@RequestBody SearchMemberRequestDto SearchMemberRequestDto) {
//        System.out.println("token = " + token);
        SearchMemberResponseDto result = quizRoomService.createSearchMemberResponseDto(SearchMemberRequestDto);
        return new BaseResponse<>(result);
    }


    @PostMapping("/quizroom/invite")
    @Operation(summary = "방 생성")
    public BaseResponse<QuizRoom> createQuizRoom(@RequestBody CreateRoomRequestDto createRoomRequestDto) {
        QuizRoom result = quizRoomService.createAndSaveQuizRoom(createRoomRequestDto);
        return new BaseResponse<>(result);
    }


    @GetMapping("/quizroom")
    @Operation(summary = "전체 방 탐색")
    public BaseResponse<List<QuizRoom>> findQuizRooms() {
        List<QuizRoom> results = quizRoomService.findAll();
        return new BaseResponse<>(results);
    }

    @MessageMapping("/quizroom/in/{roomId}")
    @Operation(summary = "방 입장")
    public void inQuizRoom(@DestinationVariable Long roomId, @Payload MemberDto body) {
        quizRoomService.enterQuizRoom(roomId, body);
    }


    @MessageMapping("/quizroom/out/{roomId}")
    public void outQuizRoom(@DestinationVariable Long roomId, @Payload MemberDto body) {
        quizRoomService.leaveQuizRoom(roomId, body);
    }


    @MessageMapping("/quizroom/ready/{roomId}")
    public void ready(@DestinationVariable Long roomId, @Payload Map<String, Object> payload) {
        quizRoomService.changeReady(roomId, payload);
    }


    @MessageMapping("/quizroom/roomList")
    public void getQuizRooms() {
        // 스레드 스케줄러
        quizRoomService.findAllSend();
    }


    @MessageMapping("/quizroom/roomList/{userPk}")
    public void getMyQuizRooms(@DestinationVariable Long userPk) {
        // 스레드 스케줄러
        quizRoomService.findAllMySend(userPk);
    }


    @MessageMapping("/quizroom/{roomId}")
    public void getQuizRoom(@DestinationVariable Long roomId) {

        Optional<QuizRoom> optionalQuizRoom = quizRoomService.findById(roomId);
        QuizRoom quizRoom = optionalQuizRoom.orElseThrow();

        messageTemplate.convertAndSend("/sub/quiz/" + roomId, quizRoom);
    }
}
