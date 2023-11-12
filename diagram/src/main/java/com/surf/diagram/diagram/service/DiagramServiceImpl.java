package com.surf.diagram.diagram.service;


import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v2.*;
import com.surf.diagram.diagram.dto.editor.EditorListRequestDto;
import com.surf.diagram.diagram.dto.editor.EditorListResponseDto;
import com.surf.diagram.diagram.dto.member.UserInfoResponseDto;
import com.surf.diagram.diagram.dto.response.DiagramResponseDto;
import com.surf.diagram.diagram.dto.response.LinkResponseDto;
import com.surf.diagram.diagram.dto.response.NodeResponseDto;
import com.surf.diagram.diagram.entity.Link;
import com.surf.diagram.diagram.entity.Node;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.Token;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import java.text.DecimalFormat;

import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiagramServiceImpl implements DiagramService {


    private final FeginEditorService feginEditorService;
    private final FeginUserService feginUserService;
    private static final Set<String> STOPWORDS = new HashSet<>();
    private final DecimalFormat df = new DecimalFormat("#.###");

    private final Map<Integer, DiagramResponseDto> diagramCache = new HashMap<>();

    public DiagramServiceImpl( FeginEditorService feginEditorService, FeginUserService feginUserService) {
        this.feginEditorService = feginEditorService;
        this.feginUserService = feginUserService;
    }
    static {
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/static/stopwords.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                STOPWORDS.add(line.trim());
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static final Set<String> PUNCTUATIONS = new HashSet<>();

    static {
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/static/punctuations.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                PUNCTUATIONS.add(line.trim());
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public DiagramResponseDto getDiagram(int userId) {
        UserInfoResponseDto res = feginUserService.userInfoByUserPk(userId);

        List<EditorListResponseDto> editorList = feginEditorService.editorList(new EditorListRequestDto(res.getDocumentsRoots()));
        System.out.println(" = " + editorList.get(0).getContent());
        List<NodeResponseDto> nodeResponseDtos = createNodeResponseDtos(editorList);
        List<LinkResponseDto> linkResponseDtos = createLinkResponseDtos(editorList);
        List<Node> nodes = classifyAndSaveEmptyCategoryNodes(editorList);
        List<Link> links = linkNodesByCategoryAndConfidence(nodes);
        List<LinkResponseDto> linkResponseDtos1 = convertLinkResponseDtos(links);
        linkResponseDtos.addAll(linkResponseDtos1);
        DiagramResponseDto response = createDiagramResponseDto(nodeResponseDtos, linkResponseDtos);
        diagramCache.put(userId, response);
        return response;
    }


    public List<NodeResponseDto> createNodeResponseDtos(List<EditorListResponseDto> editorList) {
        List<NodeResponseDto> nodeResponseDtos = new ArrayList<>();

        for (EditorListResponseDto editor : editorList) {
            nodeResponseDtos.add(new NodeResponseDto(editor.getId(), editor.getTitle(), editor.getOwner(), editor.getId()));

            // 자식 노드들에 대해서도 같은 작업 수행
            if (editor.getChildren() != null) {
                nodeResponseDtos.addAll(createNodeResponseDtos(editor.getChildren()));
            }
        }

        return nodeResponseDtos;
    }

    public List<LinkResponseDto> createLinkResponseDtos(List<EditorListResponseDto> editorList) {
        return editorList.stream()
                .flatMap(editor -> createLinks(editor).stream())
                .map(link -> new LinkResponseDto(link.getSource(), link.getTarget(), Math.exp(link.getSimilarity()) * 1, link.getUserId()))
                .collect(Collectors.toList());
    }

    public List<LinkResponseDto> convertLinkResponseDtos(List<Link> links) {
        return links.stream()
                .map(link -> new LinkResponseDto(link.getSource(), link.getTarget(), Math.exp(link.getSimilarity()) * 1, link.getUserId()))
                .collect(Collectors.toList());
    }


    public List<LinkResponseDto> createLinks(EditorListResponseDto editorDto) {
        List<LinkResponseDto> links = new ArrayList<>();

        if(editorDto.getChildren() != null) {
            for(EditorListResponseDto child : editorDto.getChildren()) {
                links.add(new LinkResponseDto(editorDto.getId(), child.getId(), 0.5, editorDto.getOwner()));
                links.addAll(createLinks(child));
            }
        }

        return links;
    }

    public DiagramResponseDto createDiagramResponseDto(List<NodeResponseDto> nodeResponseDtos, List<LinkResponseDto> linkResponseDtos) {
        return new DiagramResponseDto(nodeResponseDtos, linkResponseDtos);
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


    // 노드 불러오기
    public List<Node> classifyAndSaveEmptyCategoryNodes(List<EditorListResponseDto> editorList){
        List<Node> nodes = new ArrayList<>();
        try {
            // 인증 키 파일을 사용하여 Credentials 객체 생성
            GoogleCredentials credentials = getCredentials();
            editorList.parallelStream().forEach(node -> {
                try {
                    Node res = analyzeAndSetNodeCategory(node, credentials);
                    nodes.add(res);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            // 파일 읽기 에러, API 호출 에러
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nodes;
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
            e.printStackTrace();
            return null;
        }
    }


    // 노드 카테고리 분석
    private Node analyzeAndSetNodeCategory(EditorListResponseDto node, GoogleCredentials credentials) throws IOException {
        // 텍스트 전처리
        String content = preprocessContent(node.getContent());
        System.out.println("content = " + content);
        // 키워드 추출
        List<String> keywords = extractKeywords(content);
        System.out.println("keywords = " + keywords);

        // 키워드가 포함된 문장 추출
        String analysisInput = extractKeywordSentences(content, keywords);
        System.out.println("analysisInput = " + analysisInput);
        // 텍스트 분류
        ClassifyTextResponse response = classifyText(analysisInput, credentials);
        System.out.println("response = " + response);
        // 카테고리 설정
        return setNodeCategory(node, response);

    }

    private String preprocessContent(String content) {
        String text = extractTextFromHtml(content);
        text = text.toLowerCase(); // 대소문자 통일
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

    private Node setNodeCategory(EditorListResponseDto res, ClassifyTextResponse response) {
        Node node = new Node();
        node.setId(res.getId());
        node.setTitle(res.getTitle());
        node.setEditorId(res.getId());
        node.setContent(res.getContent());
        node.setUserId(res.getOwner());

        if (response != null && !response.getCategoriesList().isEmpty()) {
            boolean categoryFound = false;
            for (ClassificationCategory category : response.getCategoriesList()) {
                if (category.getName().contains("Computer")) {
                    node.setCategory(category.getName());
                    categoryFound = true;
                    break;
                }
            }
            if (!categoryFound) {
                node.setCategory("other");
            }
        } else {
            node.setCategory("other");
        }
        return node;
    }


    // 키워드 추출
    private List<String> extractKeywordsWithKomoran(String content, int topN) {
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
//        komoran.setUserDic("path/to/userdic.txt");

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
    public List<Link> linkNodesByCategoryAndConfidence(List<Node> nodes) {
        // 노드 카테고리 분류
        Map<String, List<Node>> categoryNodeMap = categorizeNodes(nodes);

        return createLinksFromNodes(categoryNodeMap);
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
    private List<Link> createLinksFromNodes(Map<String, List<Node>> categoryNodeMap) {
        List<Link> links = new ArrayList<>();
        for (Map.Entry<String, List<Node>> entry : categoryNodeMap.entrySet()) {
            List<Node> categoryNodes = entry.getValue();

            for (int i = 0; i < categoryNodes.size() - 1; i++) {
                Node sourceNode = categoryNodes.get(i);
                // 가장 유사도 높은 노드 찾기
                Pair<Node, Double> pair = findMostSimilarNode(categoryNodes, sourceNode, i + 1);
                Node targetNode = pair.getLeft();
                double maxSimilarity = pair.getRight();
                // 이미 있는 게 아니라면 저장하기
                Link link = saveLinkIfNotExists(sourceNode, targetNode, maxSimilarity, sourceNode.getUserId());
                System.out.println("link = " + link.getSource());
                links.add(link);
            }
        }
        return links;
    }

    // 가장 유사도 높은 노드 찾기
    private Pair<Node, Double> findMostSimilarNode(List<Node> nodes, Node sourceNode, int startIndex) {
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
        return Pair.of(mostSimilarNode, maxSimilarity);
    }

    // 이미 있는 게 아니라면 저장하기
    private Link saveLinkIfNotExists(Node sourceNode, Node targetNode, double maxSimilarity, int userId) {
        Link link = new Link();
        if (targetNode != null) {
            String source = sourceNode.getId();
            String target = targetNode.getId();
            link.setSource(source);
            link.setTarget(target);
            link.setSimilarity(Double.parseDouble(df.format(maxSimilarity)));
            link.setUserId(userId);
            }
        return link;
    }


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

    public DiagramResponseDto linkNodesByShares(int userId,  List<Integer> targetUserIds) {
        UserInfoResponseDto res = feginUserService.userInfoByUserPk(userId);

        List<EditorListResponseDto> editorList = feginEditorService.editorList(new EditorListRequestDto(res.getDocumentsRoots()));
        List<EditorListResponseDto> shareeditorList = feginEditorService.editorList(new EditorListRequestDto(res.getSharedDocumentsRoots()));
        shareeditorList = shareeditorList.stream()
                .filter(editor -> targetUserIds.contains(editor.getOwner()))
                .collect(Collectors.toList());
        editorList.addAll(shareeditorList);
        List<NodeResponseDto> nodeResponseDtos = createNodeResponseDtos(editorList);
        List<LinkResponseDto> linkResponseDtos = createLinkResponseDtos(editorList);
        List<Node> nodes = classifyAndSaveEmptyCategoryNodes(editorList);
        List<Link> links = linkNodesByCategoryAndConfidence(nodes);
        List<LinkResponseDto> linkResponseDtos1 = convertLinkResponseDtos(links);
        linkResponseDtos.addAll(linkResponseDtos1);
        DiagramResponseDto response = createDiagramResponseDto(nodeResponseDtos, linkResponseDtos);
        diagramCache.put(userId, response);
        return response;
    }


    // 공유 링크
//    public DiagramResponseDto linkNodesByShare(int userId, int targetUserId) {
//
//        List<Node> nodes1 = nodeRepository.findByUserId(userId);
//        List<Node> nodes2 = nodeRepository.findByUserId(targetUserId);
//        List<Link> links1 = linkRepository.findByUserId(userId);
//        List<Link> links2 = linkRepository.findByUserId(targetUserId);
//
//        List<NodeResponseDto> nodeDtoList = new ArrayList<>();
//        List<LinkResponseDto> linkDtoList = new ArrayList<>();

        // 링크들을 LinkResponseDto로 변환
//        linkDtoList.addAll(convertLinksToDtos(links1));
//        linkDtoList.addAll(convertLinksToDtos(links2));

        // 노드들을 NodeResponseDto로 변환
//        nodeDtoList.addAll(convertNodesToDtos(nodes1, userId));
//        nodeDtoList.addAll(convertNodesToDtos(nodes2, targetUserId));

        // 유사도가 가장 높은 노드끼리 링크를 생성
//        linkDtoList.addAll(getBestMatchLinks(nodes1, nodes2, userId));
//
//        return new DiagramResponseDto(nodeDtoList, linkDtoList);
//    }

    // 유사도 높은 녀석들 링크
//    private List<LinkResponseDto> getBestMatchLinks(List<Node> nodes1, List<Node> nodes2, int userId) {
//        List<LinkResponseDto> linkDtoList = new ArrayList<>();
//
//        for (Node node1 : nodes1) {
//            Node bestMatch = null;
//            double maxSimilarity = 0.0;
//
//            for (Node node2 : nodes2) {
//                double similarity = calculateSimilarity(node1.getTitle(), node2.getTitle());
//                if (similarity > maxSimilarity) {
//                    bestMatch = node2;
//                    maxSimilarity = similarity;
//                }
//            }
//
//            if (bestMatch != null) {
//                LinkResponseDto linkDto = new LinkResponseDto(node1.getId(), bestMatch.getId(), Math.exp(Double.parseDouble(df.format(maxSimilarity)))*1, userId);
//                linkDtoList.add(linkDto);
//            }
//        }
//
//        return linkDtoList;
//    }

//    private NodeResponseDto convertNodeToDto(Node node, int userId) {
//        return new NodeResponseDto(node.getId(), node.getTitle(), userId, node.getEditorId());
//    }

//    private LinkResponseDto convertLinkToDto(Link link) {
//        return new LinkResponseDto(link.getSource(), link.getTarget(), Math.exp(link.getSimilarity()) * 1, link.getUserId());
//    }


//    private List<NodeResponseDto> convertNodesToDtos(List<Node> nodes, int userId) {
//        return nodes.stream()
//                .map(node -> convertNodeToDto(node, userId))
//                .collect(Collectors.toList());
//    }

//    private List<LinkResponseDto> convertLinksToDtos(List<Link> links) {
//        return links.stream()
//                .map(this::convertLinkToDto)
//                .collect(Collectors.toList());
//    }


//    public DiagramResponseDto linkNodesByShares(int userId, List<Integer> targetUserIds) {
//        List<Node> nodes1 = nodeRepository.findByUserId(userId);
//        List<Link> links1 = linkRepository.findByUserId(userId);
//
//        List<LinkResponseDto> linkDtoList = new ArrayList<>(convertLinksToDtos(links1));
////        List<NodeResponseDto> nodeDtoList = new ArrayList<>(convertNodesToDtos(nodes1, userId));
//
//
//        for (Integer targetUserId : targetUserIds) {
//            List<Node> nodes2 = nodeRepository.findByUserId(targetUserId);
//            List<Link> links2 = linkRepository.findByUserId(targetUserId);
//
//            // 링크들을 LinkResponseDto로 변환
//            linkDtoList.addAll(convertLinksToDtos(links2));
//
//            // 노드들을 NodeResponseDto로 변환
////            nodeDtoList.addAll(convertNodesToDtos(nodes2, targetUserId));
//
//            // 유사도가 가장 높은 노드끼리 링크를 생성
//            linkDtoList.addAll(getBestMatchLinks(nodes1, nodes2, userId));
//        }
//        return null;
////        return new DiagramResponseDto(nodeDtoList, linkDtoList);
//    }


// // 2-gram
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


}
