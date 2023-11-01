package com.surf.diagram.diagram.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v2.*;
import com.surf.diagram.diagram.dto.request.CreateDiagramDto;
import com.surf.diagram.diagram.dto.request.CreateDiagramWithParentDto;
import com.surf.diagram.diagram.dto.request.UpdateDiagramDto;
import com.surf.diagram.diagram.dto.request.UpdatePositionDto;
import com.surf.diagram.diagram.entity.Diagram;
import com.surf.diagram.diagram.repository.DiagramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.List;
import java.util.Optional;

@Service
public class DiagramServiceImpl implements DiagramService {

    @Autowired
    private DiagramRepository diagramRepository;

    @Override
    public String createDiagram(CreateDiagramDto dto) {
        Diagram diagram = Diagram.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
        diagramRepository.save(diagram);
        return "다이어그램 생성 완료";
    }

    @Override
    public List<Diagram> getAllDiagrams() {
        return diagramRepository.findAll();
    }

    @Override
    public Optional<Diagram> getDiagramById(Long id) {
        return diagramRepository.findById(id);
    }

    @Override
    public String updateDiagram(Long id, UpdateDiagramDto dto) {
        Optional<Diagram> optionalDiagaram = diagramRepository.findById(id);
        if(optionalDiagaram.isPresent()) {
            Diagram existingDiagaram = optionalDiagaram.get();
            existingDiagaram.setTitle(dto.getTitle());
            diagramRepository.save(existingDiagaram);
            return "다이어그램 수정 완료";
        } else {
            return null;
        }
    }

    @Override
    public String updatePosition(Long id, UpdatePositionDto dto) {
        Optional<Diagram> optionalDiagram = diagramRepository.findById(id);
        if(optionalDiagram.isPresent()) {
            Diagram existingDiagram = optionalDiagram.get();
            existingDiagram.setX(dto.getX());
            existingDiagram.setY(dto.getY());
            existingDiagram.setZ(dto.getZ());
            diagramRepository.save(existingDiagram);
            return "위치 수정 완료";
        } else {
            return null;
        }
    }


    @Override
    public String deleteById(Long id) {
        Optional<Diagram> optionalDiagram = diagramRepository.findById(id);
        if(optionalDiagram.isPresent()) {
            diagramRepository.deleteById(id);
            return "다이어그램 삭제 완료";
        } else {
            return null;
        }
    }


    public Diagram createDiagramWithParent(Long parentid, CreateDiagramWithParentDto dto) {
        return diagramRepository.findById(parentid)
                .map(parent -> {
                    Diagram child = Diagram.builder()
                            .title(dto.getTitle())
                            .content(dto.getContent())
                            .parentId(parentid)
                            .build();
                    return diagramRepository.save(child);
                })
                .orElse(null);
    }

    public void classifyAndSaveEmptyCategoryDiagrams() throws Exception {
        // category가 빈 칸인 Diagram들을 불러옵니다.
        List<Diagram> diagrams = diagramRepository.findAll()
                .stream()
                .filter(d -> d.getCategory() == null || d.getCategory().isEmpty())
                .toList();

        // 인증 키 파일 경로 설정
        String keyPath = "src/main/resources/static/natural-402603-1827cceef8e7.json";

        // 인증 키 파일을 사용하여 Credentials 객체 생성
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(keyPath));

        for (Diagram diagram : diagrams) {
            String myString = diagram.getContent();

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

                if (response != null && !response.getCategoriesList().isEmpty()) {
                    diagram.setCategory(response.getCategoriesList().get(0).getName());
                    diagramRepository.save(diagram);  // 결과를 Diagram의 category에 저장하고 데이터베이스에 저장합니다.
                    System.out.println("분석이 성공적으로 완료되었습니다.");
                } else {
                    System.out.println("분석에 실패했습니다.");
                }
            }
        }
    }


}
