package com.surf.diagram.diagram.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v2.*;
import com.surf.diagram.diagram.dto.request.CreateDiagramDto;
import com.surf.diagram.diagram.dto.request.CreateDiagramWithParentDto;
import com.surf.diagram.diagram.dto.request.UpdateDiagramDto;
import com.surf.diagram.diagram.dto.request.UpdatePositionDto;
import com.surf.diagram.diagram.entity.Diagram;
import com.surf.diagram.diagram.entity.Node;
import com.surf.diagram.diagram.repository.DiagramRepository;
import com.surf.diagram.diagram.repository.LinkRepository;
import com.surf.diagram.diagram.repository.NodeRepository;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.*;
@Service
public class DiagramServiceImpl implements DiagramService {

    private final DiagramRepository diagramRepository;
    private final NodeRepository nodeRepository;
    private final LinkRepository linkRepository;

    public DiagramServiceImpl(DiagramRepository diagramRepository, NodeRepository nodeRepository, LinkRepository linkRepository) {
        this.diagramRepository = diagramRepository;
        this.nodeRepository = nodeRepository;
        this.linkRepository = linkRepository;
    }



    public void classifyAndSaveEmptyCategoryNodes() throws Exception {
        // userId가 1이고, category가 빈 칸인 Node들을 불러옵니다.
        List<Node> nodes = nodeRepository.findByUserId(1)
                .stream()
                .filter(n -> n.getCategory() == null || n.getCategory().isEmpty())
                .toList();

        // 인증 키 파일 경로 설정
        String keyPath = "src/main/resources/static/natural-402603-1827cceef8e7.json";

        // 인증 키 파일을 사용하여 Credentials 객체 생성
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(keyPath));

        for (Node node : nodes) {
            String myString = node.getContent();

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
                    for (ClassificationCategory category : response.getCategoriesList()) {
                        if (category.getName().contains("Computer")) {
                            node.setCategory(category.getName());
                            node.setConfidence(category.getConfidence());
                            nodeRepository.save(node);
                            System.out.println("분석이 성공적으로 완료되었습니다.");
                            break;
                        }
                    }
                    System.out.println("'Computer'를 포함하는 카테고리가 없습니다.");
                } else {
                    System.out.println("분석에 실패했습니다.");
                }
            }
        }
    }




//    @Override
//    public String createDiagram(CreateDiagramDto dto) {
//        Diagram diagram = Diagram.builder()
//                .title(dto.getTitle())
//                .content(dto.getContent())
//                .access("Private")
//                .childId(new ArrayList<>())
//                .build();
//        diagramRepository.save(diagram);
//        return "다이어그램 생성 완료";
//    }
//
//    @Override
//    public List<Diagram> getAllDiagrams() {
//        return diagramRepository.findAll();
//    }
//
//    @Override
//    public Optional<Diagram> getDiagramById(Long id) {
//        return diagramRepository.findById(id);
//    }
//
//    @Override
//    public String updateDiagram(Long id, UpdateDiagramDto dto) {
//        Optional<Diagram> optionalDiagaram = diagramRepository.findById(id);
//        if(optionalDiagaram.isPresent()) {
//            Diagram existingDiagaram = optionalDiagaram.get();
//            existingDiagaram.setTitle(dto.getTitle());
//            diagramRepository.save(existingDiagaram);
//            return "다이어그램 수정 완료";
//        } else {
//            return null;
//        }
//    }
//
//    @Override
//    public String updatePosition(Long id, UpdatePositionDto dto) {
//        Optional<Diagram> optionalDiagram = diagramRepository.findById(id);
//        if(optionalDiagram.isPresent()) {
//            Diagram existingDiagram = optionalDiagram.get();
//            existingDiagram.setX(dto.getX());
//            existingDiagram.setY(dto.getY());
//            existingDiagram.setZ(dto.getZ());
//            diagramRepository.save(existingDiagram);
//            return "위치 수정 완료";
//        } else {
//            return null;
//        }
//    }
//
//
//    @Override
//    public String deleteById(Long id) {
//        Optional<Diagram> optionalDiagram = diagramRepository.findById(id);
//        if(optionalDiagram.isPresent()) {
//            diagramRepository.deleteById(id);
//            return "다이어그램 삭제 완료";
//        } else {
//            return null;
//        }
//    }
//
//
//    public Diagram createDiagramWithParent(Long parentid, CreateDiagramWithParentDto dto) {
//        return diagramRepository.findById(parentid)
//                .map(parent -> {
//                    Diagram child = Diagram.builder()
//                            .title(dto.getTitle())
//                            .content(dto.getContent())
//                            .parentId(parentid)
//                            .build();
//                    return diagramRepository.save(child);
//                })
//                .orElse(null);
//    }
//
//    public void classifyAndSaveEmptyCategoryDiagrams() throws Exception {
//        // category가 빈 칸인 Diagram들을 불러옵니다.
//        List<Diagram> diagrams = diagramRepository.findAll()
//                .stream()
//                .filter(d -> d.getCategory() == null || d.getCategory().isEmpty())
//                .toList();
//
//        // 인증 키 파일 경로 설정
//        String keyPath = "src/main/resources/static/natural-402603-1827cceef8e7.json";
//
//        // 인증 키 파일을 사용하여 Credentials 객체 생성
//        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(keyPath));
//
//        for (Diagram diagram : diagrams) {
//            String myString = diagram.getContent();
//
//            try (LanguageServiceClient language = LanguageServiceClient.create(LanguageServiceSettings.newBuilder()
//                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
//                    .build())) {
//                Document doc = Document.newBuilder()
//                        .setContent(myString)
//                        .setType(Document.Type.PLAIN_TEXT)
//                        .build();
//                ClassifyTextRequest request = ClassifyTextRequest.newBuilder()
//                        .setDocument(doc)
//                        .build();
//                ClassifyTextResponse response = language.classifyText(request);
//                System.out.println("response = " + response);
//
//                if (response != null && !response.getCategoriesList().isEmpty()) {
//                    for (ClassificationCategory category : response.getCategoriesList()) {
//                        if (category.getName().contains("Computer")) {
//                            diagram.setCategory(category.getName());
//                            diagram.setConfidence(category.getConfidence());
//                            diagramRepository.save(diagram);
//                            System.out.println("분석이 성공적으로 완료되었습니다.");
//                            break;
//                        }
//                    }
//                    System.out.println("'Computer'를 포함하는 카테고리가 없습니다.");
//                } else {
//                    System.out.println("분석에 실패했습니다.");
//                }
//            }
//        }
//    }
//
//
//    @Override
//    public List<Diagram> getShareNode(Long shareId) {
//        int id = 1;
//
//        // userID가 id인 필드들과 id2인 필드들
//        List<Diagram> diagrams1 = diagramRepository.findByUserId(id);
//        List<Diagram> diagrams2 = diagramRepository.findByUserId(shareId.intValue());
//
//        for (Diagram diagram1 : diagrams1) {
//            // 카테고리가 같은 Diagram
//            List<Diagram> sameCategoryDiagrams = diagrams2.stream()
//                    .filter(d -> d.getCategory().equals(diagram1.getCategory()))
//                    .toList();
//
//            if (!sameCategoryDiagrams.isEmpty()) {
//                // 같은 카테고리의 Diagram 중에서 confidence가 가장 높은 Diagram
//                Diagram maxConfidenceDiagram = sameCategoryDiagrams.stream()
//                        .max(Comparator.comparing(Diagram::getConfidence))
//                        .orElseThrow(NoSuchElementException::new);
//
//                // userID가 1인 필드의 자식 리스트에 userID가 2인 필드 추가
//                diagram1.getChildId().add(maxConfidenceDiagram.getId());
//            }
//        }
//
//        return diagrams1;
//    }


}
