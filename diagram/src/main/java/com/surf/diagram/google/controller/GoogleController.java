package com.surf.diagram.google.controller;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v2.*;
import com.surf.diagram.google.dto.KeywordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;

@RestController
@RequestMapping("/api/diagram/google")
@Tag(name = "구글", description = "구글")
public class GoogleController {

    @PostMapping("/keyword")
    @Operation(summary = "키워드 분석")
    public void classifyContent(@RequestBody KeywordDto keywordDto) throws Exception {
        String myString = keywordDto.getContent();
        // 인증 키 파일 경로 설정
        String keyPath = "src/main/resources/static/natural-402603-1827cceef8e7.json";

        // 인증 키 파일을 사용하여 Credentials 객체 생성
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(keyPath));

        try (LanguageServiceClient language = LanguageServiceClient.create(LanguageServiceSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build())) {
            Document doc = Document.newBuilder()
                    .setContent(myString)
                    .setType(Document.Type.PLAIN_TEXT)
                    .build();
            ClassifyTextRequest request = ClassifyTextRequest.newBuilder()
                    .setDocument(doc)
                    .build();
            ClassifyTextResponse response = language.classifyText(request);
            System.out.println("response = " + response);

            // 성공 여부에 따라 적절한 응답 처리를 수행합니다.
            if (response != null) {
                // 성공 시 처리할 로직 작성
                System.out.println("분석이 성공적으로 완료되었습니다.");
            } else {
                // 실패 시 처리할 로직 작성
                System.out.println("분석에 실패했습니다.");
            }
        }
    }
}
