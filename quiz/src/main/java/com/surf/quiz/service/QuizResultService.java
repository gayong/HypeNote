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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
            createQuizResultForNoAnswer(roomId, quiz, quizroom);
        } else if (userAnswersSize == quizroom.getUsers().size()) {
            createQuizResultForAnswer(roomId, userAnswersSize, quiz);
        } else {
            createQuizResultForPartialAnswers(roomId, quiz, quizroom);
        }

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

    private void createQuizResultForNoAnswer(String roomId, Quiz quiz, QuizRoom quizroom) {
        for (MemberDto user : quizroom.getUsers()) {
            // 퀴즈 결과 생성
            QuizResult quizResult = new QuizResult();
            quizResult.setQuizId(quiz.getId());
            quizResult.setRoomId(String.valueOf(quiz.getRoomId()));
            quizResult.setUserPk(user.getUserPk());
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
                questionResult.setMyAnswer("0");
                questionResults.add(questionResult);
            }
            quizResult.setCorrect(correctCount);
            quizResult.setQuestionResult(questionResults);
            quizResult.setExamStart(quiz.getCreatedDate());
            System.out.println("questionResults = " + questionResults);
            quizResultRepository.save(quizResult);
        }
        // 각 유저의 답안이 저장 > 결과 전송
        this.saveAndSendResults(roomId, quiz);
    }

    private void createQuizResultForAnswer(String roomId, int userAnswersSize, Quiz quiz) {
        for (Map.Entry<String, Map<Integer, String>> entry : quiz.getUserAnswers().entrySet()) {
            System.out.println("entry = " + entry);
            String userId = entry.getKey();
            Map<Integer, String> userAnswerList = entry.getValue();

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
                questionResult.setMyAnswer(userAnswerList.get(i+1));
                questionResults.add(questionResult);

                System.out.println("questionDto.getAnswer() = " + questionDto.getAnswer());
                System.out.println("userAnswerList.get(i) = " + userAnswerList);

                // 답변 비교하여 맞은 경우 correctCount 증가
                if (questionDto.getAnswer().equals(userAnswerList.get(i+1))) {
                    correctCount++;
                }
            }

            quizResult.setCorrect(correctCount);
            quizResult.setQuestionResult(questionResults);
            quizResult.setExamStart(quiz.getCreatedDate());
            System.out.println("questionResults = " + questionResults);
            quizResultRepository.save(quizResult);

        }
        // 각 유저의 답안이 저장 > 결과 전송
        this.saveAndSendResults(roomId, quiz);

    }

    private void createQuizResultForPartialAnswers(String roomId, Quiz quiz, QuizRoom quizroom) {

        for (MemberDto user : quizroom.getUsers()) {
            if (quiz.getUserAnswers().containsKey(String.valueOf(user.getUserPk()))) {
                for (Map.Entry<String, Map<Integer, String>> entry : quiz.getUserAnswers().entrySet()) {
                    System.out.println("entry = " + entry);
                    String userId = entry.getKey();
                    Map<Integer, String> userAnswerList = entry.getValue();

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
                        questionResult.setMyAnswer(userAnswerList.get(i+1));
                        questionResults.add(questionResult);

                        // 답변 비교하여 맞은 경우 correctCount 증가
                        if (questionDto.getAnswer().equals(userAnswerList.get(i+1))) {
                            correctCount++;
                        }
                    }

                    quizResult.setCorrect(correctCount);
                    quizResult.setQuestionResult(questionResults);
                    quizResult.setExamStart(quiz.getCreatedDate());
                    System.out.println("questionResults = " + questionResults);
                    quizResultRepository.save(quizResult);

                }
            } else {
                for (MemberDto mem : quizroom.getUsers()) {
                    Map<String, Map<Integer, String>> userAnswers = quiz.getUserAnswers();

                    // Skip if the user has already answered.
                    if (userAnswers.containsKey(String.valueOf(mem.getUserPk()))) {
                        continue;
                    }
                    // 퀴즈 결과 생성
                    QuizResult quizResult = new QuizResult();
                    quizResult.setQuizId(quiz.getId());
                    quizResult.setRoomId(String.valueOf(quiz.getRoomId()));
                    quizResult.setUserPk(mem.getUserPk());
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
                        questionResult.setMyAnswer("0");
                        questionResults.add(questionResult);
                    }
                    quizResult.setCorrect(correctCount);
                    quizResult.setQuestionResult(questionResults);
                    quizResult.setExamStart(quiz.getCreatedDate());
                    System.out.println("questionResults = " + questionResults);
                    quizResultRepository.save(quizResult);
                }
            }
            // 각 유저의 답안이 저장 > 결과 전송
            this.saveAndSendResults(roomId, quiz);
        }
    }



    // 각 유저의 답안이 저장 > 결과 전송
    private void saveAndSendResults(String roomId, Quiz quiz) {
        if (quizResultRepository.countByRoomId(roomId) == quiz.getUserCnt()) {
            quiz.setComplete(true);
            quizRepository.save(quiz);

            List<QuizResult> results = quizResultRepository.findByRoomId(roomId);
            System.out.println("results = " + results);
            messageTemplate.convertAndSend("/sub/quiz/" + roomId, results);
        }
    }
}