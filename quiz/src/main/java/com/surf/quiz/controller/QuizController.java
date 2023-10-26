package com.surf.quiz.controller;

import com.surf.quiz.dto.QuestionDto;
import com.surf.quiz.dto.QuestionResultDto;

import com.surf.quiz.entity.Quiz;
import com.surf.quiz.entity.QuizResult;
import com.surf.quiz.repository.QuizRepository;
import com.surf.quiz.repository.QuizResultRepository;
import com.surf.quiz.service.QuizResultService;
import com.surf.quiz.service.QuizRoomService;
import com.surf.quiz.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "퀴즈", description = "퀴즈")
public class QuizController {
    private final QuizRoomService quizroomService;
    private final SimpMessagingTemplate messageTemplate;
    private final QuizRepository quizRepository;
    private final QuizResultRepository quizResultRepository;
    private final QuizService quizService;
    private final QuizResultService quizResultService;


    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @MessageMapping("/quiz/{roomId}")
    public void StartQuiz(@DestinationVariable int roomId) {
        Quiz quiz = quizService.createQuiz(roomId);
        quizRepository.save(quiz);
        messageTemplate.convertAndSend("/sub/quiz/" + roomId, quiz);
    }




    @MessageMapping("/answer/{roomId}/{userId}")
    public void receiveAnswer(@DestinationVariable String roomId, @DestinationVariable String userId, @Payload List<String> answers) {
        Optional<Quiz> optionalQuiz = quizRepository.findByRoomId(Integer.parseInt(roomId));
        if (optionalQuiz.isPresent()) {
            Quiz quiz = optionalQuiz.get();
            Map<String, List<String>> userAnswers = quiz.getUserAnswers();
            userAnswers.put(userId, answers);
            quiz.setUserAnswers(userAnswers);
            quizRepository.save(quiz);


            // 답변을 보낸 유저들이 전부 일치하면 게임 완료 처리
            if (quizService.isQuizFinished(roomId, quiz.getUserAnswers())) {
                quiz.setComplete(true);
                messageTemplate.convertAndSend("/sub/quiz/" + roomId, quiz);

                int userAnswersSize = quiz.getUserAnswers().size();
                // 각 유저의 퀴즈 결과 생성 및 저장
                for (Map.Entry<String, List<String>> entry  : quiz.getUserAnswers().entrySet()) {
                    String userPk = entry.getKey();
                    List<String> userAnswerList = entry .getValue();

                    // 퀴즈 결과 생성
                    QuizResult quizResult = new QuizResult();
                    quizResult.setQuizId(quiz.getId());
                    quizResult.setRoomId(String.valueOf(quiz.getRoomId()));
                    quizResult.setUserPk(Long.parseLong(userPk));
                    quizResult.setTotals(quiz.getQuestion().size());
                    quizResult.setExamDone(LocalDateTime.now());

                    int correctCount = 0;
                    // 각 문제의 결과 생성
                    List<QuestionResultDto> questionResults = new ArrayList<>();
                    for (int i = 0; i < quiz.getQuestion().size(); i++) {
                        QuestionDto questionDto = quiz.getQuestion().get(i);
                        QuestionResultDto questionResult = new QuestionResultDto();
                        questionResult.setId(questionDto.getId());
                        questionResult.setQuestion(questionDto.getQuestion());
                        questionResult.setExample(questionDto.getExample());
                        questionResult.setAnswer(questionDto.getAnswer());
                        questionResult.setMyAnswer(userAnswerList.get(i));
                        questionResults.add(questionResult);

                        // 답변 비교하여 맞은 경우 correctCount 증가
                        if (questionDto.getAnswer().equals(userAnswerList.get(i))) {
                            correctCount++;
                        }
                    }

                    quizResult.setCorrect(correctCount);
                    quizResult.setQuestionResult(questionResults);
                    quizResultRepository.save(quizResult);

                // 여기서 길이 측정하셈
                    List<QuizResult> result = quizResultRepository.findByRoomId(roomId);
                    if (result.size() == userAnswersSize) {
                        messageTemplate.convertAndSend("/sub/quiz/" + roomId, result);
                    }

                }
            }
        } else {
            throw new NoSuchElementException("Quiz not found for roomId: " + roomId);
        }
    }



    // 나의 퀴즈 기록 보기
    @GetMapping("/quiz/history/{userPk}")
    @Operation(summary = "퀴즈 기록")
    public List<QuizResult> getMyQuizHistory(@PathVariable Long userPk) {
        return quizResultRepository.findByUserPk(userPk);
    }

}
