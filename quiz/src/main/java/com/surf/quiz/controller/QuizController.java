package com.surf.quiz.controller;

import com.surf.quiz.common.BaseResponse;
import com.surf.quiz.common.BaseResponseStatus;
import com.surf.quiz.dto.ResultDto;
import com.surf.quiz.dto.request.AnswerDto;
import com.surf.quiz.dto.response.QuizResultResponseDto;
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


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.jsoup.Jsoup;

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
        QuizRoom quizroom = quizRoomRepository.findById((long) roomId).orElseThrow(() -> new IllegalArgumentException("Invalid roomId: " + roomId));

        // 퀴즈룸 레디 안 했으면 리턴
        if (quizroom.getReadyCnt() != quizroom.getRoomCnt()) {
            return;
        }

        Quiz quiz = quizRepository.findByRoomId(roomId).orElseThrow(() -> new IllegalArgumentException("Invalid roomId: " + roomId));
        quiz.setUserCnt(quizroom.getUsers().toArray().length);
        quiz.setUsers(quizroom.getUsers());
        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        quiz.setCreatedDate(formattedDateTime);
        quizroom.setRoomStatus(true);
        quizRoomRepository.save(quizroom);


        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "quiz");
        payload.put("result", quiz);
        quizRepository.save(quiz);
        messageTemplate.convertAndSend("/sub/quiz/" + roomId, payload);

        // 시간 제한 퀴즈 종료 스케줄러 작동
        int delay = (quiz.getQuizCnt() * 30) + 5;
        scheduler.schedule(() -> completeQuizScheduled(quiz), delay, TimeUnit.SECONDS);
    }


    @PostMapping("/{roomId}/{userId}")
    @Operation(summary = "정답 제출하기")
    public BaseResponse<Void> receiveAnswer(@PathVariable String roomId, @PathVariable String userId, @RequestBody AnswerDto answerDto) {

        Map<Long, String> userAnswers = answerDto.getAnswers();

        // 답변 전송
        Quiz quiz = quizService.processAnswer(roomId, userId, userAnswers);

        // 답변을 보낸 유저들이 전부 일치하는지 확인
        if (quizService.isQuizFinished(roomId, quiz.getUserAnswers())) {
            // 퀴즈 완료 처리
            quizResultService.completeQuiz(roomId);
        }

        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }


    // 나의 퀴즈 기록 보기
    @GetMapping("/{userId}")
    @Operation(summary = "나의 퀴즈 기록")
    public BaseResponse<QuizResultResponseDto> getMyQuizHistory(@PathVariable Long userId) {
        List<QuizResult> quizResults = quizResultRepository.findByUser_UserPk(userId);
        QuizResultResponseDto quizResultResponseDto = new QuizResultResponseDto();

        quizResultResponseDto.setQuizCnt((long) quizResults.size());
        quizResultResponseDto.setTotalCnt(quizResults.stream().mapToLong(QuizResult::getTotals).sum());
        quizResultResponseDto.setCorrectCnt(quizResults.stream().mapToLong(QuizResult::getCorrect).sum());

        List<ResultDto> quiz = new ArrayList<>();
        for (QuizResult quizResult : quizResults) {
            ResultDto resultDto = new ResultDto();
            resultDto.setQuizId(quizResult.getQuizId().intValue());
            resultDto.setRoomName(quizResult.getRoomName());
            resultDto.setTotals(quizResult.getTotals());
            resultDto.setCorrect(quizResult.getCorrect());
            resultDto.setExamDone(quizResult.getExamDone());
            quiz.add(resultDto);
        }
        quizResultResponseDto.setQuiz(quiz);

        return new BaseResponse<>(quizResultResponseDto);
    }

    // 나의 퀴즈 단일 기록 보기
    @GetMapping("/{userId}/{quizId}")
    @Operation(summary = "나의 퀴즈 기록 단일")
    public BaseResponse<QuizResult> getMyQuizResultDetail(@PathVariable Long userId, @PathVariable int quizId) {
        QuizResult result = quizResultRepository.findByUser_UserPkAndRoomId(userId, quizId);
        return new BaseResponse<>(result);
    }


    public void completeQuizScheduled(Quiz quiz) {
        System.out.println(" = " + "스케줄러 작동");
        quizResultService.completeQuiz(String.valueOf(quiz.getRoomId()));
    }
}
