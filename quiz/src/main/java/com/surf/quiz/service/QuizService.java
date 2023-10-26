package com.surf.quiz.service;


import com.surf.quiz.dto.QuestionDto;
import com.surf.quiz.dto.SearchMemberDto;
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
    public Quiz createQuiz(int roomId) {
        Optional<Quiz> optionalQuiz = quizRepository.findByRoomId(roomId);
        if(optionalQuiz.isPresent()) {
            return null;
//            throw new AlreadyExistsException("Quiz already exists for room: " + roomId);
        }
        // 퀴즈 생성
        Quiz quiz = new Quiz();

        quiz.setId(1L);
        quiz.setRoomId(roomId);
        quiz.setCreatedDate(LocalDateTime.now());
        // 문제 생성
        QuestionDto question1 = new QuestionDto();

        question1.setQuestion("IP(Internet Protocol) 주소는 어떻게 구성되며, 어떤 역할을 담당하고 있나요?");
        question1.setId(1);
        // 보기 생성
        List<String> examples = new ArrayList<>();
        examples.add("1) IP 주소는 64비트로 구성되어 있으며, 데이터의 무결성을 검증한다.");
        examples.add("2) IP 주소는 4바이트로 이루어져 있으며, 컴퓨터의 주소 역할을 한다.");
        examples.add("3) IP 주소는 데이터의 재조합을 처리하며, 데이터의 순서를 조정한다.");
        examples.add("4) IP 주소는 하드웨어 고유의 식별번호인 MAC 주소와 동일하다.");

        question1.setExample(examples); // 변경된 부분


        // 문제에 답안 설정
        question1.setAnswer("2) IP 주소는 4바이트로 이루어져 있으며, 컴퓨터의 주소 역할을 한다.");


        QuestionDto question2 = new QuestionDto();
        question2.setQuestion("TCP/IP는 어떤 두 가지 프로토콜로 구성되어 있으며, 어떤 역할을 각각 수행하고 있는가?");
        question2.setId(2);
        List<String> examples2 = new ArrayList<>();
        examples2.add("1) TCP가 데이터의 추적을 처리하고, IP가 데이터의 배달을 담당한다.");
        examples2.add("2) IP가 데이터의 추적을 처리하고, TCP가 데이터의 배달을 담당한다.");
        examples2.add("3) TCP와 IP가 모두 데이터의 재조합을 처리한다.");
        examples2.add("4) TCP와 IP가 모두 데이터의 손실 여부를 확인한다.");
        question2.setExample(examples2);
        question2.setAnswer("1) TCP가 데이터의 추적을 처리하고, IP가 데이터의 배달을 담당한다.");

        // 퀴즈에 문제 추가
        List<QuestionDto> questions = new ArrayList<>();
        questions.add(question1);
        quiz.setQuestion(questions);
        quiz.getQuestion().add(question2);

        // 퀴즈 완료 상태 설정
        quiz.setComplete(false);
        return quiz;
    }




    public boolean isQuizFinished(String roomId, Map<String, List<String>> userAnswers) {
        List<SearchMemberDto.Member> members = quizRoomService.getUsersByRoomId(Long.parseLong(roomId));
        Set<String> userIds = members.stream()
                .map(member -> Long.toString(member.getUserPk()))
                .collect(Collectors.toSet());
        return userIds.containsAll(userAnswers.keySet()) && userIds.size() == userAnswers.keySet().size();
    }
}
