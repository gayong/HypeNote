package com.surf.quiz.service;


import com.surf.quiz.dto.MemberDto;
import com.surf.quiz.dto.QuestionDto;
import com.surf.quiz.dto.request.AnswerDto;
import com.surf.quiz.dto.request.SearchMemberRequestDto;
import com.surf.quiz.entity.Quiz;
import com.surf.quiz.repository.QuizRepository;
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



    @Transactional
    public Quiz processAnswer(String roomId, String userId, Map<String, Map<Long, String>> userAnswerDto) {
        Optional<Quiz> optionalQuiz = quizRepository.findByRoomId(Integer.parseInt(roomId));

        // 퀴즈가 있으면
        if (optionalQuiz.isPresent()) {
            Quiz quiz = optionalQuiz.get();
            Map<String, Map<Integer, String>> userAnswers = quiz.getUserAnswers();

            if(userAnswerDto != null) {
                for (Map.Entry<String, Map<Long, String>> entry : userAnswerDto.entrySet()) {
                    Map<Integer, String> convertedAnswers = new HashMap<>();
                    for (Map.Entry<Long, String> answerEntry : entry.getValue().entrySet()) {
                        convertedAnswers.put(Math.toIntExact(answerEntry.getKey()), answerEntry.getValue());
                    }

                    // 기존 유저의 답변이 있는 경우, 병합
                    if(userAnswers.containsKey(entry.getKey())) {
                        Map<Integer, String> existingAnswers = userAnswers.get(entry.getKey());
                        existingAnswers.putAll(convertedAnswers); // 기존 답변에 새 답변 추가
                        userAnswers.put(entry.getKey(), existingAnswers);
                    } else { // 새로운 유저의 답변인 경우, 추가
                        userAnswers.put(entry.getKey(), convertedAnswers);
                    }
                }
            }

            // 답변 설정
            quiz.setUserAnswers(userAnswers);
            // 퀴즈 저장
            quizRepository.save(quiz);

            return quiz;
        } else {
            throw new NoSuchElementException("Quiz not found for roomId: " + roomId);
        }
    }



    // 답변 보낸 유저들이 퀴즈에 참여한 유저들과 일치
    // 전원이 제출했는지 확인
    public boolean isQuizFinished(String roomId, Map<String, Map<Integer, String>> userAnswers) {
        List<MemberDto> members = quizRoomService.getUsersByRoomId(Long.parseLong(roomId));
        Set<String> userIds = members.stream()
                .map(member -> Long.toString(member.getUserPk()))
                .collect(Collectors.toSet());
        return userIds.containsAll(userAnswers.keySet()) && userIds.size() == userAnswers.keySet().size();
    }
}
