package com.surf.diagram.diagram.service;


import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v2.*;
import com.surf.diagram.diagram.dto.response.DiagramResponseDto;
import com.surf.diagram.diagram.dto.response.LinkResponseDto;
import com.surf.diagram.diagram.dto.response.NodeResponseDto;
import com.surf.diagram.diagram.entity.Link;
import com.surf.diagram.diagram.entity.Node;
import com.surf.diagram.diagram.repository.LinkRepository;
import com.surf.diagram.diagram.repository.NodeRepository;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.Token;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiagramServiceImpl implements DiagramService {


    private final NodeRepository nodeRepository;
    private final LinkRepository linkRepository;
    private static final Set<String> STOPWORDS = new HashSet<>(Arrays.asList(
            "그리고", "그런데", "그러나", "그래서", "또한", "하지만", "따라서", "은", "는",
            "이", "그", "저", "것", "들", "인", "있", "하", "겠", "들", "같", "되", "수", "이", "보", "않", "없", "나", "사람", "주", "아니", "등", "같", "우리", "때", "년", "가", "한", "지", "대하", "오", "말", "일", "그렇", "위하"
    )); // 불용어 리스트
    private static final Set<String> PUNCTUATIONS = new HashSet<>(Arrays.asList(".", ",", "!", "?", ";", ":", "/", "(", ")", "[", "]", "{", "}", "", "<", ">", "-", "=", "+", ",", "_", "#", "/", "\\", "?", ":", "^", "$", ".", "@", "*", "\"", "※", "~", "&", "%", "ㆍ", "!", "』", "\\", "‘", "|", "(", ")", "[", "]", "<", ">", "`", "'", "…", "》"));

    public DiagramServiceImpl(NodeRepository nodeRepository, LinkRepository linkRepository) {
        this.nodeRepository = nodeRepository;
        this.linkRepository = linkRepository;
    }

    // html 변환
    public String extractTextFromHtml(String html) {
        org.jsoup.nodes.Document document = Jsoup.parse(html);
        Elements elements = document.body().children();
        List<String> texts = new ArrayList<>();

        for (Element element : elements) {
            texts.add(element.text());
        }

        return String.join(". ", texts); // '|'를 구분자로 사용
    }


    // 노드 불러오기
    public void classifyAndSaveEmptyCategoryNodes(int userId){
        try {
            // category가 빈 칸인 Node들을 불러옵니다.
            List<Node> nodes = getEmptyCategoryNodesByUserId(userId);

            // 인증 키 파일을 사용하여 Credentials 객체 생성
            GoogleCredentials credentials = getCredentials();

            for (Node node : nodes) {
                analyzeAndSetNodeCategory(node, credentials);
                nodeRepository.save(node);
            }
        } catch (IOException e) {
            // 파일 읽기 에러, API 호출 에러
            e.printStackTrace();
        } catch (Exception e) {
            // 그 외 에러
            e.printStackTrace();
        }
    }

    // 카테고리가 널인 노드 제외
    private List<Node> getEmptyCategoryNodesByUserId(int userId) {
        return nodeRepository.findByUserId(userId)
                .stream()
                .filter(n -> n.getCategory() == null || n.getCategory().isEmpty())
                .toList();
    }


    // 구글 인증키 생성
    private GoogleCredentials getCredentials() throws IOException {
        // 인증 키 파일 경로 설정
        String keyPath = "src/main/resources/static/natural-402603-1827cceef8e7.json";

        // 인증 키 파일을 사용하여 Credentials 객체 생성
        try {
            return GoogleCredentials.fromStream(new FileInputStream(keyPath));
        } catch (FileNotFoundException e) {
            // 파일을 찾지 못했을 때의 에러 처리
            // 이 부분은 상황에 맞게 적절히 처리해 주세요.
            e.printStackTrace();
            return null;
        }
    }


    // 노드 카테고리 분석
    private void analyzeAndSetNodeCategory(Node node, GoogleCredentials credentials) throws IOException {
        // 텍스트 전처리
        String content = preprocessContent(node.getContent());

        // 키워드 추출
        List<String> keywords = extractKeywords(content);

        // 키워드가 포함된 문장 추출
        String analysisInput = extractKeywordSentences(content, keywords);

        // 텍스트 분류
        ClassifyTextResponse response = classifyText(analysisInput, credentials);
        System.out.println("response = " + response);
        // 카테고리 설정
        setNodeCategory(node, response);
    }

    private String preprocessContent(String content) {
        String text = extractTextFromHtml(content);
        return text.replaceAll("[^a-zA-Z0-9가-힣|]", "");
    }

    private List<String> extractKeywords(String content) {
        return extractKeywordsWithKomoran(content, 5); // 상위 5개 키워드 추출
    }

    private String extractKeywordSentences(String content, List<String> keywords) {
        List<String> sentences = Arrays.stream(content.split("\\|\\|\\|"))
                .filter(sentence -> keywords.stream().anyMatch(sentence::contains))
                .collect(Collectors.toList());

        return String.join("|||", sentences);
    }

    private ClassifyTextResponse classifyText(String analysisInput, GoogleCredentials credentials) throws IOException {
        try (LanguageServiceClient language = LanguageServiceClient.create(LanguageServiceSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build())) {
            Document doc = Document.newBuilder()
                    .setContent(analysisInput)
                    .setType(Document.Type.PLAIN_TEXT)
                    .build();
            ClassifyTextRequest request = ClassifyTextRequest.newBuilder()
                    .setDocument(doc)
                    .build();
            return language.classifyText(request);
        }
    }

    private void setNodeCategory(Node node, ClassifyTextResponse response) {
        if (response != null && !response.getCategoriesList().isEmpty()) {
            boolean categoryFound = false;
            for (ClassificationCategory category : response.getCategoriesList()) {
                if (category.getName().contains("Computer")) {
                    node.setCategory(category.getName());
                    node.setConfidence(category.getConfidence());
                    categoryFound = true;
                    break;
                }
            }
            if (!categoryFound) {
                node.setCategory("other");
                node.setConfidence(100F);
            }
        } else {
            node.setCategory("other");
            node.setConfidence(0F);
        }
    }


    // 키워드 추출
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



    // 노드와 링크 연결
    public void linkNodesByCategoryAndConfidence(int userId) {
        // 노드 가져오기
        List<Node> nodes = fetchNodesByUserId(userId);

        // 노드 카테고리 분류
        Map<String, List<Node>> categoryNodeMap = categorizeNodes(nodes);

        // 링크 생성
        createLinksFromNodes(categoryNodeMap, userId);
    }

    // 노드 가져오기
    private List<Node> fetchNodesByUserId(int userId) {
        return nodeRepository.findByUserId(userId);
    }

    // 노드 카테고리 분류
    private Map<String, List<Node>> categorizeNodes(List<Node> nodes) {
        Map<String, List<Node>> categoryNodeMap = new HashMap<>();
        for (Node node : nodes) {
            String category = node.getCategory();
            if (category != null && !category.isEmpty()) {
                categoryNodeMap.computeIfAbsent(category, k -> new ArrayList<>()).add(node);
            }
        }
        return categoryNodeMap;
    }

    // 링크 생성
    private void createLinksFromNodes(Map<String, List<Node>> categoryNodeMap, int userId) {
        for (Map.Entry<String, List<Node>> entry : categoryNodeMap.entrySet()) {
            List<Node> categoryNodes = entry.getValue();

            for (int i = 0; i < categoryNodes.size() - 1; i++) {
                Node sourceNode = categoryNodes.get(i);
                // 가장 유사도 높은 노드 찾기
                Node targetNode = findMostSimilarNode(categoryNodes, sourceNode, i + 1);

                // 이미 있는 게 아니라면 저장하기
                saveLinkIfNotExists(sourceNode, targetNode, userId);
            }
        }
    }

    // 가장 유사도 높은 노드 찾기
    private Node findMostSimilarNode(List<Node> nodes, Node sourceNode, int startIndex) {
        double maxSimilarity = Double.MIN_VALUE;
        Node mostSimilarNode = null;
        for (int j = startIndex; j < nodes.size(); j++) {
            Node targetNode = nodes.get(j);
            double similarity = calculateSimilarity(sourceNode.getContent(), targetNode.getContent());
            System.out.println("similarity = " + similarity);
            if (similarity > maxSimilarity) {
                mostSimilarNode = targetNode;
                maxSimilarity = similarity;
            }
        }
        return mostSimilarNode;
    }

    // 이미 있는 게 아니라면 저장하기
    private void saveLinkIfNotExists(Node sourceNode, Node targetNode, int userId) {
        if (targetNode != null) {
            int source = sourceNode.getId().intValue();
            int target = targetNode.getId().intValue();

            if (!linkRepository.existsBySourceAndTargetAndUserId(source, target, userId)) {
                Link link = new Link();
                link.setSource(source);
                link.setTarget(target);
                link.setUserId(userId);
                linkRepository.save(link);
            }
        }
    }


//    private double calculateSimilarity(String text1, String text2) {
//        Map<String, Integer> wordCount1 = getWordCount(text1, 2);
//        Map<String, Integer> wordCount2 = getWordCount(text2, 2);
//
//        Set<String> allWords = new HashSet<>();
//        allWords.addAll(wordCount1.keySet());
//        allWords.addAll(wordCount2.keySet());
//
//        double dotProduct = 0.0;
//        double norm1 = 0.0;
//        double norm2 = 0.0;
//
//        for (String word : allWords) {
//            int count1 = wordCount1.getOrDefault(word, 0);
//            int count2 = wordCount2.getOrDefault(word, 0);
//
//            dotProduct += count1 * count2;
//            norm1 += count1 * count1;
//            norm2 += count2 * count2;
//        }
//
//        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
//    }
//
//    private Map<String, Integer> getWordCount(String text, int n) {
//        Map<String, Integer> wordCount = new HashMap<>();
//        String[] words = text.split("\\s+");
//
//        for (int i = 0; i < words.length - n + 1; i++) {
//            StringBuilder sb = new StringBuilder();
//            for (int j = 0; j < n; j++) {
//                sb.append((j > 0 ? " " : "") + words[i + j]);
//            }
//            String ngram = sb.toString();
//            wordCount.put(ngram, wordCount.getOrDefault(ngram, 0) + 1);
//        }
//
//        return wordCount;
//    }

    // 유사도 분석 코사인 유사도
    private double calculateSimilarity(String text1, String text2) {
        Map<String, Integer> wordCount1 = getWordCount(text1);
        Map<String, Integer> wordCount2 = getWordCount(text2);

        Set<String> allWords = new HashSet<>();
        allWords.addAll(wordCount1.keySet());
        allWords.addAll(wordCount2.keySet());

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (String word : allWords) {
            int count1 = wordCount1.getOrDefault(word, 0);
            int count2 = wordCount2.getOrDefault(word, 0);

            dotProduct += count1 * count2;
            norm1 += count1 * count1;
            norm2 += count2 * count2;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    // 단어 세기
    private Map<String, Integer> getWordCount(String text) {
        Map<String, Integer> wordCount = new HashMap<>();

        for (String word : text.split("\\s+")) {
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        }

        return wordCount;
    }



    // 공유 링크
    public DiagramResponseDto linkNodesByShare(int userId, int targetUserId) {
        List<Node> nodes1 = nodeRepository.findByUserId(userId);
        List<Node> nodes2 = nodeRepository.findByUserId(targetUserId);
        List<Link> links1 = linkRepository.findByUserId(userId);
        List<Link> links2 = linkRepository.findByUserId(targetUserId);

        List<NodeResponseDto> nodeDtoList = new ArrayList<>();
        List<LinkResponseDto> linkDtoList = new ArrayList<>();

        // 링크들을 LinkResponseDto로 변환
        linkDtoList.addAll(convertLinksToDtos(links1));
        linkDtoList.addAll(convertLinksToDtos(links2));

        // 노드들을 NodeResponseDto로 변환
        nodeDtoList.addAll(convertNodesToDtos(nodes1, userId));
        nodeDtoList.addAll(convertNodesToDtos(nodes2, targetUserId));

        // 유사도가 가장 높은 노드끼리 링크를 생성
        linkDtoList.addAll(getBestMatchLinks(nodes1, nodes2, userId));

        return new DiagramResponseDto(nodeDtoList, linkDtoList);
    }

    // 유사도 높은 녀석들 링크
    private List<LinkResponseDto> getBestMatchLinks(List<Node> nodes1, List<Node> nodes2, int userId) {
        List<LinkResponseDto> linkDtoList = new ArrayList<>();

        for (Node node1 : nodes1) {
            Node bestMatch = null;
            double maxSimilarity = 0.0;

            for (Node node2 : nodes2) {
                double similarity = calculateSimilarity(node1.getTitle(), node2.getTitle());
                if (similarity > maxSimilarity) {
                    bestMatch = node2;
                    maxSimilarity = similarity;
                }
            }

            if (bestMatch != null) {
                LinkResponseDto linkDto = new LinkResponseDto(node1.getId().intValue(), bestMatch.getId().intValue(), userId);
                linkDtoList.add(linkDto);
            }
        }

        return linkDtoList;
    }

    private NodeResponseDto convertNodeToDto(Node node, int userId) {
        return new NodeResponseDto(node.getId(), node.getTitle(), userId, node.getEditorId());
    }

    private LinkResponseDto convertLinkToDto(Link link) {
        return new LinkResponseDto(link.getSource(), link.getTarget(), link.getUserId());
    }


    private List<NodeResponseDto> convertNodesToDtos(List<Node> nodes, int userId) {
        return nodes.stream()
                .map(node -> convertNodeToDto(node, userId))
                .collect(Collectors.toList());
    }

    private List<LinkResponseDto> convertLinksToDtos(List<Link> links) {
        return links.stream()
                .map(this::convertLinkToDto)
                .collect(Collectors.toList());
    }


    public DiagramResponseDto linkNodesByShares(int userId, List<Integer> targetUserIds) {
        List<Node> nodes1 = nodeRepository.findByUserId(userId);
        List<Link> links1 = linkRepository.findByUserId(userId);

        List<NodeResponseDto> nodeDtoList = new ArrayList<>();
        List<LinkResponseDto> linkDtoList = new ArrayList<>();

        // 링크들을 LinkResponseDto로 변환
        linkDtoList.addAll(convertLinksToDtos(links1));

        // 노드들을 NodeResponseDto로 변환
        nodeDtoList.addAll(convertNodesToDtos(nodes1, userId));

        for (Integer targetUserId : targetUserIds) {
            List<Node> nodes2 = nodeRepository.findByUserId(targetUserId);
            List<Link> links2 = linkRepository.findByUserId(targetUserId);

            // 링크들을 LinkResponseDto로 변환
            linkDtoList.addAll(convertLinksToDtos(links2));

            // 노드들을 NodeResponseDto로 변환
            nodeDtoList.addAll(convertNodesToDtos(nodes2, targetUserId));

            // 유사도가 가장 높은 노드끼리 링크를 생성
            linkDtoList.addAll(getBestMatchLinks(nodes1, nodes2, userId));
        }

        return new DiagramResponseDto(nodeDtoList, linkDtoList);
    }

}
