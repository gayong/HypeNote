package com.surf.quiz.service;


import com.surf.quiz.dto.MemberDto;
import com.surf.quiz.entity.Quiz;
import com.surf.quiz.repository.QuizRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizRoomService quizRoomService;

    @Autowired
    public QuizService(QuizRepository quizRepository, QuizRoomService quizRoomService) {
        this.quizRepository = quizRepository;
        this.quizRoomService = quizRoomService;
    }



    // 정답 저장
    @Transactional
    public Quiz processAnswer(String roomId, String userId, Map<Long, String> userAnswerDto) {
        Quiz quiz = findQuizByRoomId(roomId);
        Map<String, Map<Integer, Pair<String, LocalDateTime>>> userAnswers = quiz.getUserAnswers();

        // 유저 정답이 있으면
        if(userAnswerDto != null) {
            processUserAnswers(userId, userAnswerDto, userAnswers);
        } else {
            // null 예외처리
            throw new IllegalArgumentException("User answer cannot be null");
        }

        // 답변 설정
        quiz.setUserAnswers(userAnswers);
        // 퀴즈 저장
        quizRepository.save(quiz);

        return quiz;
    }

    private Quiz findQuizByRoomId(String roomId) {
        return quizRepository.findByRoomId(Integer.parseInt(roomId))
                .orElseThrow(() -> new NoSuchElementException("Quiz not found for roomId: " + roomId));
    }

    private void processUserAnswers(String userId, Map<Long, String> userAnswerDto, Map<String, Map<Integer, Pair<String, LocalDateTime>>> userAnswers) {
        Map<Integer, Pair<String, LocalDateTime>> convertedAnswers = convertUserAnswer(userAnswerDto);

        // 기존 유저의 답변이 있는 경우, 병합
        if(userAnswers.containsKey(userId)) {
            Map<Integer, Pair<String, LocalDateTime>> existingAnswers = userAnswers.get(userId);
            userAnswers.remove(userId);
            existingAnswers.putAll(convertedAnswers); // 기존 답변에 새 답변 추가
            userAnswers.put(userId, existingAnswers); // 기존 답변을 다시 추가
        } else { // 새로운 유저의 답변인 경우, 추가
            userAnswers.put(userId, convertedAnswers);
        }
    }
    
    // 문제와 정답 추출
    private Map<Integer, Pair<String, LocalDateTime>> convertUserAnswer(Map<Long, String> userAnswerDto) {
        Map<Integer, Pair<String, LocalDateTime>> convertedAnswers = new HashMap<>();
        // 문제 / 정답 추출
        for (Map.Entry<Long, String> answerEntry : userAnswerDto.entrySet()) {
            Pair<String, LocalDateTime> answerWithTime = Pair.of(answerEntry.getValue(), LocalDateTime.now());
            convertedAnswers.put(Math.toIntExact(answerEntry.getKey()), answerWithTime);
        }
        return convertedAnswers;
    }



    // 답변 보낸 유저들이 퀴즈에 참여한 유저들과 일치
    // 전원이 제출했는지 확인
    public boolean isQuizFinished(String roomId, Map<String, Map<Integer, Pair<String, LocalDateTime>>> userAnswers) {
        List<MemberDto> members = quizRoomService.getUsersByRoomId(Long.parseLong(roomId));
        System.out.println("members = " + members);
        Set<String> userIds = members.stream()
                .map(member -> Long.toString(member.getUserPk()))
                .collect(Collectors.toSet());
        int submittedUserCount = userAnswers.keySet().size(); // 정답을 제출한 팀원 수
        int totalUserCount = userIds.size(); // 퀴즈룸의 전체 팀원 수
        System.out.println("totalUserCount = " + totalUserCount+submittedUserCount);
        return submittedUserCount == totalUserCount;
    }
}
