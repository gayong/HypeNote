package com.surf.quiz.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.surf.quiz.common.BaseResponse;
import com.surf.quiz.dto.QuestionDto;
import com.surf.quiz.dto.editor.*;
import com.surf.quiz.dto.member.FindUserPkListDto;
import com.surf.quiz.dto.member.UserInfoResponseDto;
import com.surf.quiz.fegin.ChatCompletionClient;
import com.surf.quiz.fegin.EditorServiceFeignClient;
import com.surf.quiz.fegin.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeignService {
    @Autowired
    private EditorServiceFeignClient editorServiceFeignClient;
    @Autowired
    private UserServiceFeignClient userServiceFeignClient;
    @Autowired
    private ChatCompletionClient chatCompletionClient;

    private final ChatCompletionService chatCompletionService;

    public BaseResponse<List<QuestionDto>> getGpt(int cnt, String content, int id) {
        List<String> abc = chatCompletionService.chatCompletions(cnt, content, id);

        // ObjectMapper 객체 생성
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // JSON 문자열을 객체로 담고 있는 리스트로 변환
            List<QuestionDto> questionList = new ArrayList<>();
            for (String json : abc) {
                if (json.startsWith("[")) { // JSON 문자열이 배열인 경우
                    List<QuestionDto> tempList = objectMapper.readValue(json, new TypeReference<>() {});
                    if (!tempList.isEmpty()) {
                        questionList.add(tempList.get(0));
                    }
                } else { // JSON 문자열이 객체인 경우
                    QuestionDto questionDto = objectMapper.readValue(json, QuestionDto.class);
                    questionList.add(questionDto);
                }
            }
            // 변환된 리스트 사용
            for (QuestionDto question : questionList) {
                System.out.println(question.getQuestion());
                System.out.println(question.getAnswer());
            }
            return new BaseResponse<>(questionList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new BaseResponse<>(null);
        }
    }



    public String getEditorInfoQuiz(String editorId) {
        ApiResponse<EditorCheckResponse> response = editorServiceFeignClient.getEditor(editorId);

        String res = response.getData().getContent();
        if (response.getData().getContent() == null) {
            res = "<p>TCP/IP (흐름제어/혼잡제어) #들어가기 전 #TCP 통신이란? 네트워크 통신에서 신뢰적인 연결방식 <p>TCP는 기본적으로 unreliable network에서, reliable network를 보장할 수 있도록 하는 프로토콜</p> TCP는 network congestion avoidance algorithm을 사용#reliable network를 보장한다는 것은 4가지 문제점 존재 <p>손실 : packet이 손실될 수 있는 문제순서 바뀜 : packet의 순서가 바뀌는 문제Congestion : 네트워크가 혼잡한 문제Overload : receiver가 overload 되는 문제</p> <p>#흐름제어/혼잡제어란?흐름제어 (endsystem 대 endsystem)송신측과 수신측의 데이터 처리 속도 차이를 해결하기 위한 기법Flow Control은 receiver가 packet을 지나치게 많이 받지 않도록 조절하는 것기본 개념은 receiver가 sender에게 현재 자신의 상태를 feedback 한다는 점혼잡제어 : 송신측의 데이터 전달과 네트워크의 데이터 처리 속도 차이를 해결하기 위한 기법</p>#전송의 전체 과정Application layer : sender application layer가 socket에 data를 씀.<p>Transport layer : data를 segment에 감싼다. 그리고 network layer에 넘겨줌.그러면 아랫단에서 어쨋든 receiving node로 전송이 됨.</p> 이 때, sender의 send buffer에 data를 저장하고, receiver는 receive buffer에 data를 저장함.application에서 준비가 되면 이 buffer에 있는 것을 읽기 시작함.따라서 flow control의 핵심은 이 receiver buffer가 넘치지 않게 하는 것임.따라서 receiver는 RWND(Receive WiNDow) : <p>receive buffer의 남은 공간을 홍보함#1. 흐름제어 (Flow Control)수신측이 송신측보다 데이터 처리 속도가 빠르면 문제없지만, 송신측의 속도가 빠를 경우 문제가 생긴다.수신측에서 제한된 저장 용량을 초과한 이후에 도착하는 데이터는 손실 될 수 있으며, 만약 손실 된다면 불필요하게 응답과 데이터 전송이 송/수신 측 간에 빈번이 발생한다.</p>이러한 위험을 줄이기 위해 송신 측의 데이터 전송량을 수신측에 따라 조절해야한다.</p>";
        }

        return res;
    }


    public List<Integer> getEditorShare(EditorShareMemberRequestDto editorShareMemberRequestDto) {
        ApiResponse<EditorShareMemberResponseDto> response = editorServiceFeignClient.getShares(editorShareMemberRequestDto);
        return response.getData().getUserList();
    }


    public List<UserInfoResponseDto> userInfoByUserPkList(FindUserPkListDto findUserPkListDto) {
        ResponseEntity<List<UserInfoResponseDto>> response = userServiceFeignClient.userInfoByUserPkList(findUserPkListDto);
        return response.getBody();
    }

    public List<Integer> editorShareList(EditorShareListRequestDto editorShareListRequestDto) {
        ApiResponse<EditorShareListResponseDto> response = editorServiceFeignClient.editorShareList(editorShareListRequestDto);
        return response.getData().getUserList();
    }


}
