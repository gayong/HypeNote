package com.surf.quiz.service;


import com.surf.quiz.dto.QuestionDto;
import com.surf.quiz.dto.QuestionResultDto;
import com.surf.quiz.entity.Quiz;
import com.surf.quiz.entity.QuizResult;
import com.surf.quiz.repository.QuizRepository;
import com.surf.quiz.repository.QuizResultRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QuizResultService {

    private final QuizRepository quizRepository;
    private final SimpMessagingTemplate messageTemplate;
    private final QuizResultRepository quizResultRepository;


    public QuizResultService(QuizRepository quizRepository, SimpMessagingTemplate messageTemplate, QuizResultRepository quizResultRepository) {
        this.quizRepository = quizRepository;
        this.messageTemplate = messageTemplate;
        this.quizResultRepository = quizResultRepository;
    }


    @Transactional
    public void completeQuiz(String roomId, Quiz quiz) {
        quiz.setComplete(true);
        quizRepository.save(quiz);

        int userAnswersSize = quiz.getUserAnswers().size();
        // 각 유저의 퀴즈 결과 생성 및 저장
        for (Map.Entry<String, List<String>> entry : quiz.getUserAnswers().entrySet()) {
            String userId = entry.getKey();
            List<String> userAnswerList = entry.getValue();

            // 퀴즈 결과 생성
            QuizResult quizResult = new QuizResult();
            quizResult.setQuizId(quiz.getId());
            quizResult.setRoomId(String.valueOf(quiz.getRoomId()));
            quizResult.setUserPk(Long.parseLong(userId));
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
            quizResult.setExamStart(quiz.getCreatedDate());
            quizResultRepository.save(quizResult);

            // 각 유저의 답안이 저장 > 결과 전송
            if (quizResultRepository.countByRoomId(roomId) == userAnswersSize) {
                List<QuizResult> results = quizResultRepository.findByRoomId(roomId);
                messageTemplate.convertAndSend("/sub/quiz/" + roomId, results);
            }
        }
    }

}
