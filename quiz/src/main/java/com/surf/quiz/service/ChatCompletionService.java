package com.surf.quiz.service;


import com.surf.quiz.dto.gpt.ChatResponse;
import com.surf.quiz.dto.gpt.Usage;
import com.surf.quiz.fegin.ChatCompletionClient;
import com.surf.quiz.dto.gpt.ChatRequest;
import com.surf.quiz.dto.gpt.Message;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.Token;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ChatCompletionService {
    private final ChatCompletionClient chatCompletionClient;
    private static final Logger logger = LoggerFactory.getLogger(ChatCompletionService.class);
    private final static String ROLE_USER = "user";
    private final static String MODEL = "gpt-3.5-turbo";
    // 불용어 사전
    private static final Set<String> STOPWORDS = new HashSet<>();
    static {
        try {
            InputStream in = ChatCompletionService.class.getResourceAsStream("/static/stopwords.txt");
            if (in == null) {
                throw new FileNotFoundException("파일 경로 오류 'static/stopwords.txt'");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                STOPWORDS.add(line.trim());
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 특수문자 사전
    private static final Set<String> PUNCTUATIONS = new HashSet<>();
    static {
        try {
            InputStream in = ChatCompletionService.class.getResourceAsStream("/static/punctuations.txt");
            if (in == null) {
                throw new FileNotFoundException("파일 경로 오류 'static/punctuations.txt'");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                PUNCTUATIONS.add(line.trim());
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Value("${apikey}")
    private String apikey;

    public List<String> chatCompletions(int cnt, final String question, int id) {
        if (cnt < 0 || question == null || question.trim().isEmpty()){
            return null;
        }
        System.out.println("id = " + id);

        // 텍스트 전처리
        String content = preprocessContent(question);

        // 키워드 추출
        List<String> keywords = extractKeywords(content);

        // 키워드가 포함된 문장 추출
        String analysisInput = extractKeywordSentences(content, keywords);
        System.out.println("analysisInput = " + analysisInput);

        Message systemMessage = Message.builder()
                .role("system")
                .content("I want to make a quiz about Computer Science\n" +
                        "The problem is a four-choice question.\n" +
                        "There is only one correct answer" +
                        "I want to make it in the format below\n" +
                        "    {\n" +
                        "      \"id\":" + "number" +
                        "      \"question\": \"question content\",\n" +
                        "      \"example\": [\n" +
                        "        {\n" +
                        "          \"ex\": \"1\",\n" +
                        "          \"content\": \"example content\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"ex\": \"2\",\n" +
                        "          \"content\": \"example content\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"ex\": \"3\",\n" +
                        "          \"content\": \"example content\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"ex\": \"4\",\n" +
                        "          \"content\": \"example content\"\n" +
                        "        }\n" +
                        "      ],\n" +
                        "      \"answer\": \"ex number\",\n" +
                        "      \"commentary\": \"commentary content\"\n" +
                        "    }\n" +
                        " Please respond with a List\n"+
                        "There is only 1 quiz" +"Please answer all in Korean.")
                .build();

//


        Message message = Message.builder()
                .role(ROLE_USER)
                .content(analysisInput)
                .build();

        ChatRequest chatRequest = ChatRequest.builder()
                .model(MODEL)
                .messages(Arrays.asList(systemMessage, message))
                .build();

        ChatResponse chatResponse = chatCompletionClient
                .chatCompletions(apikey, chatRequest);

        // 응답을 List에 넣기
        List<String> responses = chatResponse.getChoices().stream()
                .map(choice -> choice.getMessage().getContent())
                .collect(Collectors.toList());

        return responses;
    }

    // 텍스트 전처리
    private String preprocessContent(String content) {
        String text = extractTextFromHtml(content);
        return text.replaceAll("[^a-zA-Z0-9가-힣|]", "");
    }

    // html 변환
    public String extractTextFromHtml(String html) {
        org.jsoup.nodes.Document document = Jsoup.parse(html);
        Elements elements = document.body().children();
        List<String> texts = new ArrayList<>();

        for (Element element : elements) {
            texts.add(element.text());
        }

        return String.join("|||", texts); // '|'를 구분자로 사용
    }

    // 키워드 추출
    private List<String> extractKeywords(String content) {
        return extractKeywordsWithKomoran(content, 4); // 상위 4개 키워드 추출
    }

    // 코모란
    private List<String> extractKeywordsWithKomoran(String content, int topN) {
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
        Map<String, Integer> wordCount = new HashMap<>();

        List<Token> tokens = komoran.analyze(content).getTokenList();
        for (Token token : tokens) {
            String word = token.getMorph();
            String pos = token.getPos();
            if ((pos.startsWith("NN") || pos.startsWith("SL")) && (!STOPWORDS.contains(word) && !PUNCTUATIONS.contains(word))) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }
        }

        return wordCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // 키워드 포함한 문장 추출
    private String extractKeywordSentences(String content, List<String> keywords) {
        List<String> sentences = Arrays.stream(content.split("\\|\\|\\|"))
                .filter(sentence -> keywords.stream().anyMatch(sentence::contains))
                .collect(Collectors.toList());

        return String.join("|||", sentences);
    }
}
