package com.surf.quiz.controller;

import com.surf.quiz.entity.Quiz;
import com.surf.quiz.entity.QuizResult;
import com.surf.quiz.repository.QuizRepository;
import com.surf.quiz.repository.QuizResultRepository;
import com.surf.quiz.service.QuizResultService;
import com.surf.quiz.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


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

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();



    @PreDestroy
    public void destroy() {
        scheduler.shutdown();
    }


    @MessageMapping("/quiz/{roomId}")
    public void StartQuiz(@DestinationVariable int roomId) {
        // 퀴즈 생성
        Quiz quiz = quizService.createQuiz(roomId);
        quizRepository.save(quiz);

        messageTemplate.convertAndSend("/sub/quiz/" + roomId, quiz);

        // 퀴즈 10분 후 종료를 위한 스케줄러
        scheduler.schedule(this::completeQuizScheduled, 10, TimeUnit.MINUTES);

    }


    @MessageMapping("/answer/{roomId}/{userId}")
    public void receiveAnswer(@DestinationVariable String roomId, @DestinationVariable String userId, @Payload List<String> answers) {

        // 답변 전송
        Quiz quiz = quizService.processAnswer(roomId, userId, answers);

        // 답변을 보낸 유저들이 전부 일치하는지 확인
        if (quizService.isQuizFinished(roomId, quiz.getUserAnswers())) {
            // 퀴즈 완료 처리
            quizResultService.completeQuiz(roomId, quiz);
        }
    }


    // 나의 퀴즈 기록 보기
    @GetMapping("/quiz/history/{userPk}")
    @Operation(summary = "나의 퀴즈 기록")
    public List<QuizResult> getMyQuizHistory(@PathVariable Long userPk) {
        return quizResultRepository.findByUserPk(userPk);
    }


    // 스케줄링 작업에 의해 10분 후에 실행되는 메서드
    @Scheduled(fixedDelay = 10 * 60 * 1000) // 10분 (단위: 밀리초)
    public void completeQuizScheduled() {
        // 퀴즈를 종료하는 방식
        // 제출하지 않았으면 답변 다 0으로 처리
        // 빵점 처리
    }
}
