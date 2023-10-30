package com.surf.quiz.service;


import com.surf.quiz.dto.MemberDto;
import com.surf.quiz.dto.QuestionDto;
import com.surf.quiz.dto.QuestionResultDto;
import com.surf.quiz.entity.Quiz;
import com.surf.quiz.entity.QuizResult;
import com.surf.quiz.entity.QuizRoom;
import com.surf.quiz.repository.QuizRepository;
import com.surf.quiz.repository.QuizResultRepository;
import com.surf.quiz.repository.QuizRoomRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizResultService {

    private final QuizRepository quizRepository;
    private final SimpMessagingTemplate messageTemplate;
    private final QuizResultRepository quizResultRepository;
    private final QuizRoomRepository quizRoomRepository;


    public QuizResultService(QuizRepository quizRepository, SimpMessagingTemplate messageTemplate, QuizResultRepository quizResultRepository, QuizRoomRepository quizRoomRepository) {
        this.quizRepository = quizRepository;
        this.messageTemplate = messageTemplate;
        this.quizResultRepository = quizResultRepository;
        this.quizRoomRepository = quizRoomRepository;
    }


    private QuizRoom getQuizRoomById(String roomId) {
        return quizRoomRepository.findById(Long.valueOf(roomId))
                .orElseThrow(() -> new IllegalArgumentException("Invalid roomId: " + roomId));
    }

    private int getUserAnswersSize(Quiz quiz) {
        return Optional.ofNullable(quiz.getUserAnswers())
                .map(Map::size)
                .orElse(0);
    }


    private void createQuizResultAndSave(MemberDto user, Quiz quiz, Map<Integer, String> userAnswerList) {
        QuizResult quizResult = new QuizResult();
        quizResult.setQuizId(quiz.getId());
        quizResult.setRoomId(String.valueOf(quiz.getRoomId()));
        quizResult.setUserPk(user.getUserPk());
        System.out.println("quizResult.setUserPk = " + user.getUserPk());
        quizResult.setTotals(quiz.getQuestion().size());
        quizResult.setExamDone(LocalDateTime.now());
        System.out.println("userAnswerList = " + userAnswerList);

        int correctCount = 0;
        List<QuestionResultDto> questionResults = new ArrayList<>();
        for (int i = 0; i < quiz.getQuestion().size(); i++) {
            QuestionDto questionDto = quiz.getQuestion().get(i);
            QuestionResultDto questionResult = new QuestionResultDto();
            questionResult.setId(questionDto.getId());
            questionResult.setQuestion(questionDto.getQuestion());
            questionResult.setExample(questionDto.getExample());
            questionResult.setAnswer(questionDto.getAnswer());
            String myAnswer = userAnswerList.get(i+1) != null ? userAnswerList.get(i+1) : "0";
            System.out.println("myAnswer = " + myAnswer);
            questionResult.setMyAnswer(myAnswer);
            questionResults.add(questionResult);

            if (myAnswer.equals(questionDto.getAnswer())) {
                correctCount++;
            }
        }

        quizResult.setCorrect(correctCount);
        quizResult.setQuestionResult(questionResults);
        quizResult.setExamStart(quiz.getCreatedDate());
        System.out.println("questionResults = " + questionResults);
        quizResultRepository.save(quizResult);
    }

    @Transactional
    public void completeQuiz(String roomId) {
        Quiz quiz = quizRepository.findByRoomId(Integer.parseInt(roomId)).orElseThrow(() -> new RuntimeException("Quiz not found"));
        if (quiz.isComplete()) {
            return;
        }
        QuizRoom quizroom = getQuizRoomById(roomId);

        int userAnswersSize = getUserAnswersSize(quiz);
        System.out.println("userAnswersSize = " + userAnswersSize);
        if (userAnswersSize == 0) {
            for (MemberDto user : quizroom.getUsers()) {
                createQuizResultAndSave(user, quiz, null);
            }
        } else if (userAnswersSize == quizroom.getUsers().size()) {
            for (Map.Entry<String, Map<Integer, String>> entry : quiz.getUserAnswers().entrySet()) {
                MemberDto user = quizroom.getUsers().stream()
                        .filter(u -> u.getUserPk() == Long.parseLong(entry.getKey()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("User not found"));
                createQuizResultAndSave(user, quiz, entry.getValue());
            }
        } else {
            for (MemberDto user : quizroom.getUsers()) {
                Map<Integer, String> userAnswerList = quiz.getUserAnswers().get(String.valueOf(user.getUserPk()));
                createQuizResultAndSave(user, quiz, userAnswerList);
            }
        }

        this.saveAndSendResults(roomId, quiz);
    }


    // 각 유저의 답안이 저장 > 결과 전송
    private void saveAndSendResults(String roomId, Quiz quiz) {
        if (quizResultRepository.countByRoomId(roomId) == quiz.getUserCnt()) {
            quiz.setComplete(true);
            quizRepository.save(quiz);

            List<QuizResult> results = quizResultRepository.findByRoomId(roomId);
            results.sort((o1, o2) -> o2.getCorrect() - o1.getCorrect());

            // 각 QuizResult에서 userPk를 추출하여 ranking 리스트 생성
            List<Long> ranking = results.stream()
                    .map(QuizResult::getUserPk)
                    .collect(Collectors.toList());

            // results와 ranking을 하나의 객체에 담아 보냄
            Map<String, Object> payload = new HashMap<>();
            payload.put("results", results);
            payload.put("ranking", ranking);

            messageTemplate.convertAndSend("/sub/quiz/" + roomId, payload);
        }
    }
}