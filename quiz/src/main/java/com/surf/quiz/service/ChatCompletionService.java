package com.surf.quiz.service;


import com.surf.quiz.dto.gpt.ChatResponse;
import com.surf.quiz.dto.gpt.Usage;
import com.surf.quiz.fegin.ChatCompletionClient;
import com.surf.quiz.dto.gpt.ChatRequest;
import com.surf.quiz.dto.gpt.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ChatCompletionService {
    private final ChatCompletionClient chatCompletionClient;
    private final static String ROLE_USER = "user";
    private final static String MODEL = "gpt-3.5-turbo";

    @Value("${apikey}")
    private String apikey;

    public String chatCompletions(final String question) {
        Message systemMessage = Message.builder()
                .role("system")
                .content("나는 Computer Science에 대해서 공부하고 있는 학생이야.\n" +
                        "내가 공부하고 정리한 내용을 문제로 만들고 싶어.\n" +
                        "문제의 형식은\n" +
                        "{ question:\"문제\"" +
                        "\texample:[{\"ex\": \"1\", \"content\":\"보기\"},{\"ex\": \"2\", \"content\": \"\"},{\"ex\": \"3\", \"content\": \"\"}.{\"ex\": \"4\", \"content\": \"\"}]\n" +
                        "\tanswer:\"정답\"" +
                        "\tcommentary:\"해설\"" +
                        "}\n" +
                        "이런 문제, 보기 4개, 정답, 해설의 형식이야\n" +
                        "이 형식으로 내가 아래에 제시할 정리 내용을 문제로 만들어줘\n" +
                        "문제의 갯수는 2개야\n" +
                        "답변을 할 때는 다른 어떤 내용도 없이 만들어진 문제만 List 형식으로 코드로 답변해줘")  // prompt 내용을 여기에 작성하세요.
                .build();

        Message message = Message.builder()
                .role(ROLE_USER)
                .content(question)
                .build();
        ChatRequest chatRequest = ChatRequest.builder()
                .model(MODEL)
                .messages(Arrays.asList(systemMessage, message))
                .build();

        ChatResponse chatResponse = chatCompletionClient
                .chatCompletions(apikey, chatRequest);

        Usage usage = chatResponse.getUsage();
        System.out.println("Usage: " + usage);

        return chatResponse
                .getChoices()
                .stream()
                .findFirst()
                .orElseThrow()
                .getMessage()
                .getContent();

    }
}
