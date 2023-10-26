package com.surf.quiz.controller;

import com.surf.quiz.dto.QuestionDto;
import com.surf.quiz.dto.QuestionResultDto;
import com.surf.quiz.entity.Member;
import com.surf.quiz.entity.Quiz;
import com.surf.quiz.entity.QuizResult;
import com.surf.quiz.repository.QuizRepository;
import com.surf.quiz.repository.QuizResultRepository;
import com.surf.quiz.service.QuizRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class QuizController {
    private final QuizRoomService quizroomService;
    private final SimpMessagingTemplate messageTemplate;
    private final QuizRepository quizRepository;
    private final QuizResultRepository quizResultRepository;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @MessageMapping("/quiz/{roomId}")
    public void StartQuiz(@DestinationVariable int roomId) {
        Optional<Quiz> optionalQuiz = quizRepository.findByRoomId(roomId);
        if(optionalQuiz.isPresent()) {
            return;
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
            if (isQuizFinished(roomId, quiz.getUserAnswers())) {
                quiz.setComplete(true);
                messageTemplate.convertAndSend("/sub/quiz/" + roomId, quiz);

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
                        messageTemplate.convertAndSend("/sub/quiz/" + roomId, result);


                }
            }
        } else {
            throw new NoSuchElementException("Quiz not found for roomId: " + roomId);
        }
    }

    public boolean isQuizFinished(String roomId, Map<String, List<String>> userAnswers) {
        List<Member> members = quizroomService.getUsersByRoomId(Long.parseLong(roomId));
        Set<String> userIds = members.stream()
                .map(member -> Long.toString(member.getUserPk()))
                .collect(Collectors.toSet());
        return userIds.containsAll(userAnswers.keySet()) && userIds.size() == userAnswers.keySet().size();
    }
}
