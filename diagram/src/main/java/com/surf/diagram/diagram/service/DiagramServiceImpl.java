package com.surf.diagram.diagram.service;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v2.*;
import com.surf.diagram.diagram.dto.request.CreateDiagramDto;
import com.surf.diagram.diagram.dto.request.CreateDiagramWithParentDto;
import com.surf.diagram.diagram.dto.request.UpdateDiagramDto;
import com.surf.diagram.diagram.dto.request.UpdatePositionDto;
import com.surf.diagram.diagram.entity.Diagram;
import com.surf.diagram.diagram.entity.Link;
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
                    boolean categoryFound = false;
                    for (ClassificationCategory category : response.getCategoriesList()) {
                        if (category.getName().contains("Computer")) {
                            node.setCategory(category.getName());
                            node.setConfidence(category.getConfidence());
                            System.out.println("분석이 성공적으로 완료되었습니다.");
                            categoryFound = true;
                            break;
                        }
                    }
                    if (!categoryFound) {
                        System.out.println("'Computer'를 포함하는 카테고리가 없습니다.");
                        node.setCategory("other");
                        node.setConfidence(100F);
                    }
                } else {
                    System.out.println("분석에 실패했습니다.");
                    node.setCategory("other");
                    node.setConfidence(0F);
                }
                nodeRepository.save(node);
            }
        }
    }

    public void linkNodesByCategoryAndConfidence() {
        // userId가 1인 Node들을 불러옵니다.
        List<Node> nodes = nodeRepository.findByUserId(1);

        // 카테고리별 Node를 저장할 Map 생성
        Map<String, List<Node>> categoryNodeMap = new HashMap<>();

        // 카테고리별 Node 분류
        for (Node node : nodes) {
            String category = node.getCategory();
            if (category != null && !category.isEmpty()) {
                if (!categoryNodeMap.containsKey(category)) {
                    categoryNodeMap.put(category, new ArrayList<>());
                }
                categoryNodeMap.get(category).add(node);
            }
        }

        // 같은 카테고리 내에서 confidence가 가장 근사치인 Node를 찾아 Link 생성
        for (Map.Entry<String, List<Node>> entry : categoryNodeMap.entrySet()) {
            List<Node> categoryNodes = entry.getValue();

            for (int i = 0; i < categoryNodes.size() - 1; i++) {
                Node sourceNode = categoryNodes.get(i);
                Node closestNode = null;
                float minDiff = Float.MAX_VALUE;

                for (int j = i + 1; j < categoryNodes.size(); j++) {
                    Node targetNode = categoryNodes.get(j);
                    float diff = Math.abs(sourceNode.getConfidence() - targetNode.getConfidence());

                    if (diff < minDiff) {
                        closestNode = targetNode;
                        minDiff = diff;
                    }
                }

                if (closestNode != null) {
                    int source = sourceNode.getId().intValue();
                    int target = closestNode.getId().intValue();
                    int userId = 1;

                    // 이미 같은 소스와 타겟, userId를 가진 Link가 있는지 확인
                    if (!linkRepository.existsBySourceAndTargetAndUserId(source, target, userId)) {
                        Link link = new Link();
                        link.setSource(source);
                        link.setTarget(target);
                        link.setUserId(userId);
                        linkRepository.save(link);
                    }
                }
            }
        }
    }

}
