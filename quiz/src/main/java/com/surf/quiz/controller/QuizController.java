package com.surf.quiz.controller;

import com.surf.quiz.common.BaseResponse;
import com.surf.quiz.entity.Quiz;
import com.surf.quiz.entity.QuizResult;
import com.surf.quiz.entity.QuizRoom;
import com.surf.quiz.repository.QuizRepository;
import com.surf.quiz.repository.QuizResultRepository;
import com.surf.quiz.repository.QuizRoomRepository;
import com.surf.quiz.service.QuizResultService;

import com.surf.quiz.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;


import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Slf4j // 로깅
@EnableScheduling  // 스케줄러 허용
@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor // notnull 생성자 사용
@Tag(name = "퀴즈", description = "퀴즈")
public class QuizController {
    private final SimpMessagingTemplate messageTemplate;
    private final QuizRepository quizRepository;
    private final QuizResultRepository quizResultRepository;
    private final QuizService quizService;
    private final QuizResultService quizResultService;
    private final QuizRoomRepository quizRoomRepository;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();



    @PreDestroy
    public void destroy() {
        scheduler.shutdown();
    }


    @MessageMapping("/quiz/{roomId}")
    public void StartQuiz(@DestinationVariable int roomId) {
        Quiz quiz = quizRepository.findByRoomId(roomId).orElseThrow(() -> new IllegalArgumentException("Invalid roomId: " + roomId));
        QuizRoom quizroom = quizRoomRepository.findById((long) roomId).orElseThrow(() -> new IllegalArgumentException("Invalid roomId: " + roomId));
        quiz.setUserCnt(quizroom.getUsers().toArray().length);
        quizRepository.save(quiz);

        messageTemplate.convertAndSend("/sub/quiz/" + roomId, quiz);

        int delay = quiz.getQuizCnt() * 30;
        scheduler.schedule(() -> completeQuizScheduled(quiz), delay, TimeUnit.SECONDS);
    }


    @PostMapping("/{roomId}/{userId}")
    @Operation(summary = "정답 제출하기")
    public BaseResponse<Void> receiveAnswer(@PathVariable String roomId, @PathVariable String userId, @RequestBody Map<String, Map<String, String>> answers) {

        // 답변 전송
        Quiz quiz = quizService.processAnswer(roomId, userId, answers);

        // 답변을 보낸 유저들이 전부 일치하는지 확인
        if (quizService.isQuizFinished(roomId, quiz.getUserAnswers())) {
            // 퀴즈 완료 처리
            quizResultService.completeQuiz(roomId);
        }
        return new BaseResponse<>(null);
    }


    // 나의 퀴즈 기록 보기
    @GetMapping("/{userId}")
    @Operation(summary = "나의 퀴즈 기록")
    public BaseResponse<List<QuizResult>> getMyQuizHistory(@PathVariable Long userId) {
        List<QuizResult> result = quizResultRepository.findByUserPk(userId);
        return new BaseResponse<>(result);
    }

    public void completeQuizScheduled(Quiz quiz) {
        System.out.println(" = " + "스케줄러 작동");
        quizResultService.completeQuiz(String.valueOf(quiz.getRoomId()));
    }
}
