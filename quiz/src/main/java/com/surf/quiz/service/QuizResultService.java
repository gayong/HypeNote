package com.surf.quiz.service;


import com.surf.quiz.dto.MemberDto;
import com.surf.quiz.dto.QuestionDto;
import com.surf.quiz.dto.QuestionResultDto;
import com.surf.quiz.dto.response.QuizResultDto;
import com.surf.quiz.entity.Quiz;
import com.surf.quiz.entity.QuizResult;
import com.surf.quiz.entity.QuizRoom;
import com.surf.quiz.repository.QuizRepository;
import com.surf.quiz.repository.QuizResultRepository;
import com.surf.quiz.repository.QuizRoomRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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



    // 퀴즈 완료
    @Transactional
    public void completeQuiz(String roomId) {
        Quiz quiz = findQuizByRoomId(roomId);
        if (quiz.isComplete()) {
            return;
        }
        QuizRoom quizroom = getQuizRoomById(roomId);

        // 정답 대조
        processUserAnswers(quiz, quizroom);
        // 결과 저장
        saveAndSendResults(roomId, quiz);
    }

    private Quiz findQuizByRoomId(String roomId) {
        return quizRepository.findByRoomId(Integer.parseInt(roomId))
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
    }

    private QuizRoom getQuizRoomById(String roomId) {
        return quizRoomRepository.findById(Long.valueOf(roomId))
                .orElseThrow(() -> new IllegalArgumentException("Invalid roomId: " + roomId));
    }

    // 정답 처리
    private void processUserAnswers(Quiz quiz, QuizRoom quizroom) {
        int userAnswersSize = getUserAnswersSize(quiz);
        System.out.println("userAnswersSize = " + userAnswersSize);
        for (MemberDto user : quizroom.getUsers()) {
            // 유저 정답 리스트 가져오기
            Map<Integer, Pair<String, LocalDateTime>> userAnswerList = getUserAnswerList(quiz, user, userAnswersSize);
            createQuizResultAndSave(user, quiz, userAnswerList);
        }
    }


    private Map<Integer, Pair<String, LocalDateTime>> getUserAnswerList(Quiz quiz, MemberDto user, int userAnswersSize) {
        if (userAnswersSize == 0 || userAnswersSize != quiz.getUserCnt()) {
            return quiz.getUserAnswers().get(String.valueOf(user.getUserPk()));
        } else {
            return quiz.getUserAnswers().entrySet().stream()
                    .filter(entry -> user.getUserPk() == Long.parseLong(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
    }

    private int getUserAnswersSize(Quiz quiz) {
        return Optional.ofNullable(quiz.getUserAnswers())
                .map(Map::size)
                .orElse(0);
    }

    // 문제 결과 만들고 저장
    private void createQuizResultAndSave(MemberDto user, Quiz quiz, Map<Integer, Pair<String, LocalDateTime>> userAnswerList) {
        QuizResult quizResult = createQuizResult(user, quiz);
        List<QuestionResultDto> questionResults = createQuestionResults(quiz, userAnswerList, quizResult);
        quizResult.setQuestionResult(questionResults);
        quizResultRepository.save(quizResult);
    }

    // 개인 결과
    private QuizResult createQuizResult(MemberDto user, Quiz quiz) {
        QuizResult quizResult = new QuizResult();
        quizResult.setQuizId(quiz.getId());
        quizResult.setRoomId(quiz.getRoomId());
        quizResult.setRoomName(quiz.getRoomName());
        quizResult.setUser(user);
        quizResult.setTotals(quiz.getQuestion().size());
        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        quizResult.setExamDone(formattedDateTime);
        quizResult.setExamStart(quiz.getCreatedDate());

        return quizResult;
    }

    // 퀴즈 결과 만들기
    private List<QuestionResultDto> createQuestionResults(Quiz quiz, Map<Integer, Pair<String, LocalDateTime>> userAnswerList, QuizResult quizResult) {
        int correctCount = 0;
        List<QuestionResultDto> questionResults = new ArrayList<>();
        for (int i = 0; i < quiz.getQuestion().size(); i++) {
            QuestionDto questionDto = quiz.getQuestion().get(i);
            QuestionResultDto questionResult = createQuestionResult(questionDto, userAnswerList, i, quizResult);
            questionResults.add(questionResult);
            if (isCorrectAnswer(questionDto, questionResult)) {
                correctCount++;
            }
        }
        quizResult.setCorrect(correctCount);
        return questionResults;
    }

    // 문제 결과
    private QuestionResultDto createQuestionResult(QuestionDto questionDto, Map<Integer, Pair<String, LocalDateTime>> userAnswerList, int index, QuizResult quizResult) {
        QuestionResultDto questionResult = new QuestionResultDto();
        questionResult.setId(questionDto.getId());
        questionResult.setQuestion(questionDto.getQuestion());
        questionResult.setExample(questionDto.getExample());
        questionResult.setAnswer(questionDto.getAnswer());
        String myAnswer = (userAnswerList != null && userAnswerList.get(index+1) != null && userAnswerList.get(index+1).getLeft() != null) ? userAnswerList.get(index+1).getLeft() : "0";
        questionResult.setMyAnswer(myAnswer);
        questionResult.setCommentary(questionDto.getCommentary());

        LocalDateTime latestAnswerTime = null;
        if (userAnswerList != null) {
            latestAnswerTime = userAnswerList.values().stream()
                    .map(Pair::getRight)
                    .filter(Objects::nonNull)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
        }

        quizResult.setLatestAnswerTime(latestAnswerTime);
        return questionResult;
    }

    // 정답이 맞는지 확인
    private boolean isCorrectAnswer(QuestionDto questionDto, QuestionResultDto questionResult) {
        return questionResult.getMyAnswer().equals(questionDto.getAnswer());
    }

    // 각 유저의 답안이 저장 > 결과 전송
    private void saveAndSendResults(String roomId, Quiz quiz) {
        if (quizResultRepository.countByRoomId(Integer.parseInt(roomId)) == quiz.getUserCnt()) {
            quiz.setComplete(true);
            quizRepository.save(quiz);


            List<QuizResult> results = quizResultRepository.findByRoomId(Integer.parseInt(roomId));
            results.sort((o1, o2) -> {
                int compare = Integer.compare(o2.getCorrect(), o1.getCorrect());
                if (compare == 0) {
                    // correct가 같으면 latestAnswerTime를 기준으로 오름차순 정렬
                    return o1.getLatestAnswerTime().compareTo(o2.getLatestAnswerTime());
                }
                return compare;
            });

            Map<Long, QuizResultDto> resultsDtoMap = results.stream()
                    .collect(Collectors.toMap(result -> result.getUser().getUserPk(), this::toQuizResultDto));

            List<Map<String, Object>> ranking = createRanking(results);

            Map<String, Object> payload = createPayload(ranking, resultsDtoMap);


            messageTemplate.convertAndSend("/sub/quiz/" + roomId, payload);
        }
    }


    // 랭킹 생성
    private List<Map<String, Object>> createRanking(List<QuizResult> results) {
        List<Map<String, Object>> ranking = new ArrayList<>();
        int rank = 1;
        for (QuizResult result : results) {
            Map<String, Object> userRanking = new HashMap<>();
            userRanking.put("userPk", result.getUser().getUserPk());
            userRanking.put("userName", result.getUser().getUserName());
            userRanking.put("userImg", result.getUser().getUserImg());

            userRanking.put("ranking", rank++);
            userRanking.put("correct", result.getCorrect());
            userRanking.put("total", result.getTotals());
            ranking.add(userRanking);
        }
        return ranking;
    }

    // 전달할 페이로드 생성
    private Map<String, Object> createPayload(List<Map<String, Object>> ranking, Map<Long, QuizResultDto> resultsDtoMap) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "result");
        payload.put("ranking", ranking);
        payload.put("result", resultsDtoMap);
        return payload;
    }

    // 퀴즈 결과 dto로 변환
    public QuizResultDto toQuizResultDto(QuizResult quizResult) {
        QuizResultDto quizResultDto = new QuizResultDto();
        quizResultDto.setQuizId(quizResult.getQuizId());
        quizResultDto.setRoomId(quizResult.getRoomId());
        quizResultDto.setRoomName(quizResult.getRoomName());
        quizResultDto.setTotals(quizResult.getTotals());
        quizResultDto.setCorrect(quizResult.getCorrect());
        quizResultDto.setQuestionResult(quizResult.getQuestionResult());
        quizResultDto.setExamStart(quizResult.getExamStart());
        quizResultDto.setExamDone(quizResult.getExamDone());

        return quizResultDto;
    }
}