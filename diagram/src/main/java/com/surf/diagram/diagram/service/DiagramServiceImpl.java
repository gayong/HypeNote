package com.surf.diagram.diagram.service;



import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v2.*;
import com.surf.diagram.diagram.dto.response.DiagramResponseDto;
import com.surf.diagram.diagram.dto.response.LinkResponseDto;
import com.surf.diagram.diagram.dto.response.NodeResponseDto;
import com.surf.diagram.diagram.entity.Link;
import com.surf.diagram.diagram.entity.Node;
import com.surf.diagram.diagram.repository.DiagramRepository;
import com.surf.diagram.diagram.repository.LinkRepository;
import com.surf.diagram.diagram.repository.NodeRepository;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.Token;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Comparator;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiagramServiceImpl implements DiagramService {

    private final DiagramRepository diagramRepository;
    private final NodeRepository nodeRepository;
    private final LinkRepository linkRepository;
    private static final Set<String> STOPWORDS = new HashSet<>(Arrays.asList(
            "그리고", "그런데", "그러나", "그래서", "또한", "하지만", "따라서", "은", "는",
            "이", "그", "저", "것", "들", "인", "있", "하", "겠", "들", "같", "되", "수", "이", "보", "않", "없", "나", "사람", "주", "아니", "등", "같", "우리", "때", "년", "가", "한", "지", "대하", "오", "말", "일", "그렇", "위하"
    )); // 불용어 리스트
    private static final Set<String> PUNCTUATIONS = new HashSet<>(Arrays.asList(".", ",", "!", "?", ";", ":", "/", "(", ")", "[", "]", "{", "}", "", "<", ">", "-", "=", "+", ",", "_", "#", "/", "\\", "?", ":", "^", "$", ".", "@", "*", "\"", "※", "~", "&", "%", "ㆍ", "!", "』", "\\", "‘", "|", "(", ")", "[", "]", "<", ">", "`", "'", "…", "》"));

    public DiagramServiceImpl(DiagramRepository diagramRepository, NodeRepository nodeRepository, LinkRepository linkRepository) {
        this.diagramRepository = diagramRepository;
        this.nodeRepository = nodeRepository;
        this.linkRepository = linkRepository;
    }
    public String extractTextFromHtml(String html) {
        return Jsoup.parse(html).text();
    }


    public void classifyAndSaveEmptyCategoryNodes(int userId) throws Exception {
        // category가 빈 칸인 Node들을 불러옵니다.
        List<Node> nodes = nodeRepository.findByUserId(userId)
                .stream()
//                .filter(n -> n.getCategory() == null || n.getCategory().isEmpty())
                .toList();

        // 인증 키 파일 경로 설정
        String keyPath = "src/main/resources/static/natural-402603-1827cceef8e7.json";

        // 인증 키 파일을 사용하여 Credentials 객체 생성
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(keyPath));

        for (Node node : nodes) {
            String myyString = node.getContent();
            String myString = extractTextFromHtml(myyString);
            System.out.println("myString = " + myString);
            myString = myString.replaceAll("[^a-zA-Z0-9가-힣]", "");
            System.out.println("myString = " + myString);

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

//    public void classifyAndSaveEmptyCategoryNodes() throws Exception {
//        // userId가 1이고, category가 빈 칸인 Node들을 불러옵니다.
//        List<Node> nodes = nodeRepository.findByUserId(1)
//                .stream()
////                .filter(n -> n.getCategory() == null || n.getCategory().isEmpty())
//                .toList();
//
//        // 인증 키 파일 경로 설정
//        String keyPath = "src/main/resources/static/natural-402603-1827cceef8e7.json";
//
//        // 인증 키 파일을 사용하여 Credentials 객체 생성
//        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(keyPath));
//
//        for (Node node : nodes) {
//            String myString = node.getContent();
//
//            List<String> keywords = extractKeywordsWithKomoran(myString, 5); // 상위 5개 키워드 추출
//            System.out.println("keywords = " + keywords);
//
//            // 키워드가 포함된 문장 추출
//            List<String> sentences = Arrays.stream(myString.split(". "))
//                    .filter(sentence -> keywords.stream().anyMatch(sentence::contains))
//                    .collect(Collectors.toList());
//
//            // 키워드와 관련된 문장을 분석에 사용
//            String analysisInput = String.join(". ", sentences);
//            System.out.println("analysisInput = " + analysisInput);
//            try (LanguageServiceClient language = LanguageServiceClient.create(LanguageServiceSettings.newBuilder()
//                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
//                    .build())) {
//                Document doc = Document.newBuilder()
//                        .setContent(analysisInput)
//                        .setType(Document.Type.PLAIN_TEXT)
//                        .build();
//                ClassifyTextRequest request = ClassifyTextRequest.newBuilder()
//                        .setDocument(doc)
//                        .build();
//                ClassifyTextResponse response = language.classifyText(request);
//                System.out.println("response = " + response);
//
//                if (response != null && !response.getCategoriesList().isEmpty()) {
//                    boolean categoryFound = false;
//                    for (ClassificationCategory category : response.getCategoriesList()) {
//                        if (category.getName().contains("Computer")) {
//                            node.setCategory(category.getName());
//                            node.setConfidence(category.getConfidence());
//                            System.out.println("분석이 성공적으로 완료되었습니다.");
//                            categoryFound = true;
//                            break;
//                        }
//                    }
//                    if (!categoryFound) {
//                        System.out.println("'Computer'를 포함하는 카테고리가 없습니다.");
//                        node.setCategory("other");
//                        node.setConfidence(100F);
//                    }
//                } else {
//                    System.out.println("분석에 실패했습니다.");
//                    node.setCategory("other");
//                    node.setConfidence(0F);
//                }
//                nodeRepository.save(node);
//            }
//        }
//    }


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



    public void linkNodesByCategoryAndConfidence(int userId) {
        // Node들
        List<Node> nodes = nodeRepository.findByUserId(userId);

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


    public DiagramResponseDto linkNodesByShare(int userId, int targetUserId) {
        List<Node> nodes1 = nodeRepository.findByUserId(userId);
        List<Node> nodes2 = nodeRepository.findByUserId(targetUserId);
        List<Link> links1 = linkRepository.findByUserId(userId);
        List<Link> links2 = linkRepository.findByUserId(targetUserId);

        Map<String, Node> categoryNodeMap = new HashMap<>();

        List<NodeResponseDto> nodeDtoList = new ArrayList<>();
        List<LinkResponseDto> linkDtoList = new ArrayList<>();

        // 링크들을 LinkResponseDto로 변환
        for (Link link : links1) {
            linkDtoList.add(convertLinkToDto(link));
        }
        for (Link link : links2) {
            linkDtoList.add(convertLinkToDto(link));
        }

        for (Node node : nodes1) {
            NodeResponseDto nodeDto = new NodeResponseDto();
            nodeDto.setId(node.getId());
            nodeDto.setTitle(node.getTitle());
            nodeDto.setUserId(userId);
            nodeDto.setEditorId(node.getEditorId());

            nodeDtoList.add(nodeDto);

            String category = node.getCategory();
            if (category != null && !category.isEmpty()) {
                if (categoryNodeMap.containsKey(category)) {
                    Node existingNode = categoryNodeMap.get(category);
                    if (node.getConfidence() > existingNode.getConfidence()) {
                        categoryNodeMap.put(category, node);
                    }
                } else {
                    categoryNodeMap.put(category, node);
                }
            }
        }

        for (Node node : nodes2) {
            NodeResponseDto nodeDto = new NodeResponseDto();
            nodeDto.setId(node.getId());
            nodeDto.setTitle(node.getTitle());
            nodeDto.setUserId(targetUserId);
            nodeDto.setEditorId(node.getEditorId());

            nodeDtoList.add(nodeDto);

            String category = node.getCategory();
            if (category != null && !category.isEmpty() && categoryNodeMap.containsKey(category)) {
                Node sourceNode = categoryNodeMap.get(category);

                LinkResponseDto linkDto = new LinkResponseDto();
                linkDto.setSource(sourceNode.getId().intValue());
                linkDto.setTarget(node.getId().intValue());
                linkDto.setUserId(userId);

                linkDtoList.add(linkDto);
            }
        }

        DiagramResponseDto responseDto = new DiagramResponseDto();
        responseDto.setNodes(nodeDtoList);
        responseDto.setLinks(linkDtoList);

        return responseDto;
    }


    private LinkResponseDto convertLinkToDto(Link link) {
        LinkResponseDto linkDto = new LinkResponseDto();
        linkDto.setSource(link.getSource());
        linkDto.setTarget(link.getTarget());
        linkDto.setUserId(link.getUserId());
        return linkDto;
    }
}
