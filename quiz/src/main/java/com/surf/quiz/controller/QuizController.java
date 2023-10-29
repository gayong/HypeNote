package com.surf.quiz.controller;

import com.surf.quiz.entity.Quiz;
import com.surf.quiz.entity.QuizResult;
import com.surf.quiz.repository.QuizRepository;
import com.surf.quiz.repository.QuizResultRepository;
import com.surf.quiz.service.QuizResultService;
import com.surf.quiz.service.QuizRoomService;
import com.surf.quiz.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
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
@RequiredArgsConstructor // notnull 생성자 사용
@Tag(name = "퀴즈", description = "퀴즈")
public class QuizController {
    private final SimpMessagingTemplate messageTemplate;
    private final QuizRepository quizRepository;
    private final QuizResultRepository quizResultRepository;
    private final QuizService quizService;
    private final QuizResultService quizResultService;
    private final QuizRoomService quizRoomService;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();



    @PreDestroy
    public void destroy() {
        scheduler.shutdown();
    }


    @MessageMapping("/quiz/{roomId}")
    public void StartQuiz(@DestinationVariable int roomId) {
        Quiz quiz = quizRepository.findByRoomId(roomId).orElseThrow(() -> new IllegalArgumentException("Invalid roomId: " + roomId));

        messageTemplate.convertAndSend("/sub/quiz/" + roomId, quiz);

        int delay = quiz.getQuizCnt() * 30;
        scheduler.schedule(() -> completeQuizScheduled(quiz), delay, TimeUnit.SECONDS);
    }


    @PostMapping("/answer/{roomId}/{userId}")
    public ResponseEntity<Void> receiveAnswer(@PathVariable String roomId, @PathVariable String userId, @RequestBody List<String> answers) {

        // 답변 전송
        Quiz quiz = quizService.processAnswer(roomId, userId, answers);

        // 답변을 보낸 유저들이 전부 일치하는지 확인
        if (quizService.isQuizFinished(roomId, quiz.getUserAnswers())) {
            // 퀴즈 완료 처리
            quizResultService.completeQuiz(roomId, quiz);
        }
        return ResponseEntity.ok().build();
    }


    // 나의 퀴즈 기록 보기
    @GetMapping("/quiz/history/{userPk}")
    @Operation(summary = "나의 퀴즈 기록")
    public List<QuizResult> getMyQuizHistory(@PathVariable Long userPk) {
        return quizResultRepository.findByUserPk(userPk);
    }

    public void completeQuizScheduled(Quiz quiz) {
        quizResultService.completeQuiz(String.valueOf(quiz.getRoomId()), quiz);
    }
}
