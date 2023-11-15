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
import com.surf.diagram.diagram.dto.LinkDto;
import com.surf.diagram.diagram.dto.NodeDto;
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

    private final DecimalFormat df = new DecimalFormat("#.###");

    private final Map<Integer, DiagramResponseDto> diagramCache = new HashMap<>();

    public DiagramServiceImpl( FeginEditorService feginEditorService, FeginUserService feginUserService) {
        this.feginEditorService = feginEditorService;
        this.feginUserService = feginUserService;
    }


    // 불용어 사전
    private static final Set<String> STOPWORDS = new HashSet<>();
    static {
        try {
            InputStream in = DiagramServiceImpl.class.getResourceAsStream("/static/stopwords.txt");
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
            InputStream in = DiagramServiceImpl.class.getResourceAsStream("/static/punctuations.txt");
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

    // 나의 뇌 그리기
    @Override
    public DiagramResponseDto getDiagram(String token) {

//        UserInfoResponseDto res = feginUserService.userInfoByUserPk(userId);
        UserInfoResponseDto res = feginUserService.userInfoByToken(token);
        System.out.println("res1 = " + token);
        System.out.println("res1 = " + res.getNickName());

        List<EditorListResponseDto> editorList = feginEditorService.editorList(new EditorListRequestDto(res.getDocumentsRoots()));
        System.out.println(" = " + editorList.get(0).getContent());
        List<NodeResponseDto> nodeResponseDtos = createNodeResponseDtos(editorList);
        List<LinkResponseDto> linkResponseDtos = createLinkResponseDtos(editorList);
        List<NodeDto> nodeDtos = classifyAndSaveEmptyCategoryNodes(editorList);
        List<LinkDto> linkDtos = linkNodesByCategoryAndConfidence(nodeDtos);
        List<LinkResponseDto> linkResponseDtos1 = convertLinkResponseDtos(linkDtos);
        linkResponseDtos.addAll(linkResponseDtos1);
        DiagramResponseDto response = createDiagramResponseDto(nodeResponseDtos, linkResponseDtos);
        diagramCache.put(res.getUserPk(), response);
        return response;
    }


    // 에디터 > 노드로 만들기
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

    // 에디터 트리 구조 링크로 변환
    public List<LinkResponseDto> createLinkResponseDtos(List<EditorListResponseDto> editorList) {
        return editorList.stream()
                .flatMap(editor -> createLinks(editor).stream())
                .map(link -> new LinkResponseDto(link.getSource(), link.getTarget(), Math.exp(link.getSimilarity()) * 1, link.getUserId()))
                .collect(Collectors.toList());
    }

    // 에디터 유사도 링크로 변환
    public List<LinkResponseDto> convertLinkResponseDtos(List<LinkDto> linkDtos) {
        return linkDtos.stream()
                .map(linkDto -> new LinkResponseDto(linkDto.getSource(), linkDto.getTarget(), Math.exp(linkDto.getSimilarity()) * 1, linkDto.getUserId()))
                .collect(Collectors.toList());
    }


    // 링크 연결
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

    // 응답 형태로 변환
    public DiagramResponseDto createDiagramResponseDto(List<NodeResponseDto> nodeResponseDtos, List<LinkResponseDto> linkResponseDtos) {
        return new DiagramResponseDto(nodeResponseDtos, linkResponseDtos);
    }


    // content html 변환
    public String extractTextFromHtml(String html) {
        org.jsoup.nodes.Document document = Jsoup.parse(html);
        Elements elements = document.body().children();
        List<String> texts = new ArrayList<>();

        for (Element element : elements) {
            texts.add(element.text());
        }


        return String.join("|||", texts); // '|'를 구분자로 사용
    }


    // 키워드 분석 병렬처리
    public List<NodeDto> classifyAndSaveEmptyCategoryNodes(List<EditorListResponseDto> editorList){
        List<NodeDto> nodeDtos = new ArrayList<>();
        try {
            // 인증 키 파일을 사용하여 Credentials 객체 생성
            GoogleCredentials credentials = getCredentials();
            processNodes(editorList, nodeDtos, credentials);
        } catch (IOException e) {
            // 파일 읽기 에러, API 호출 에러
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nodeDtos;
    }

    // 키워드분석
    private void processNodes(List<EditorListResponseDto> nodeList, List<NodeDto> nodeDtos, GoogleCredentials credentials) {
        nodeList.parallelStream().forEach(node -> {
            try {
                Optional<NodeDto> resOpt = analyzeAndSetNodeCategory(node, credentials);

                // Optional.isPresent()를 사용하여 NodeDto 객체가 존재하는지 확인
                if(resOpt.isPresent()) {
                    NodeDto res = resOpt.get();
                    nodeDtos.add(res);
                }

                // 자식 노드가 있다면 재귀적으로 처리
                if(node.getChildren() != null && !node.getChildren().isEmpty()) {
                    processNodes(node.getChildren(), nodeDtos, credentials);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    // 구글 인증키 생성
    private GoogleCredentials getCredentials() throws IOException {
        // 인증 키 파일 경로 설정
        String keyPath = "/static/natural-402603-1827cceef8e7.json";

        // 인증 키 파일을 사용하여 Credentials 객체 생성
        try {
            InputStream in = DiagramServiceImpl.class.getResourceAsStream(keyPath);
            if (in == null) {
                throw new FileNotFoundException("파일 경로 오류 'static/natural-402603-1827cceef8e7.json'");
            }
            return GoogleCredentials.fromStream(in);
        } catch (Exception e) {
            // 파일을 찾지 못했을 때
            e.printStackTrace();
            return null;
        }
    }


    // 노드 카테고리 분석
    private Optional<NodeDto> analyzeAndSetNodeCategory(EditorListResponseDto node, GoogleCredentials credentials) throws IOException {
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

    // content 텍스트 전처리
    private String preprocessContent(String content) {
        String text = extractTextFromHtml(content);
        text = text.toLowerCase(); // 대소문자 통일
        return text.replaceAll("[^a-zA-Z0-9가-힣|]", "");
    }

    // content 키워드 추출
    private List<String> extractKeywords(String content) {
        return extractKeywordsWithKomoran(content, 5); // 상위 5개 키워드 추출
    }

    // content 문장 추출
    private String extractKeywordSentences(String content, List<String> keywords) {
        List<String> sentences = Arrays.stream(content.split("\\|\\|\\|"))
                .filter(sentence -> keywords.stream().anyMatch(sentence::contains))
                .collect(Collectors.toList());

        return String.join(". ", sentences);
    }


    // 구글 자연어 처리 카테고리 분류 요청
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

    // 노드에 카테고리 지정
    private Optional<NodeDto> setNodeCategory(EditorListResponseDto res, ClassifyTextResponse response) {
        NodeDto nodeDto = new NodeDto();
        nodeDto.setId(res.getId());
        nodeDto.setTitle(res.getTitle());
        nodeDto.setEditorId(res.getId());
        nodeDto.setContent(res.getContent());
        nodeDto.setUserId(res.getOwner());

        if (response != null && !response.getCategoriesList().isEmpty()) {
            boolean categoryFound = false;
            for (ClassificationCategory category : response.getCategoriesList()) {
                if (category.getName().contains("Computer")) {
                    nodeDto.setCategory(category.getName());
                    categoryFound = true;
                    break;
                }
            }
            if (!categoryFound) {
                nodeDto.setCategory("other");
                return Optional.empty();
            }
        } else {
            nodeDto.setCategory("other");
            return Optional.empty();
        }
        return Optional.of(nodeDto);
    }


    // 키워드 추출 with Komoran
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
    public List<LinkDto> linkNodesByCategoryAndConfidence(List<NodeDto> nodeDtos) {
        System.out.println("nodeDtos = " + nodeDtos);
        // 노드 카테고리 분류
        Map<String, List<NodeDto>> categoryNodeMap = categorizeNodes(nodeDtos);

        return createLinksFromNodes(categoryNodeMap);
    }


    // 노드 카테고리 분류
    private Map<String, List<NodeDto>> categorizeNodes(List<NodeDto> nodeDtos) {
        Map<String, List<NodeDto>> categoryNodeMap = new HashMap<>();
        for (NodeDto nodeDto : nodeDtos) {
            String category = nodeDto.getCategory();
            if (category != null && !category.isEmpty()) {
                categoryNodeMap.computeIfAbsent(category, k -> new ArrayList<>()).add(nodeDto);
            }
        }
        return categoryNodeMap;
    }


    // 링크 생성
    private List<LinkDto> createLinksFromNodes(Map<String, List<NodeDto>> categoryNodeMap) {
        List<LinkDto> linkDtos = new ArrayList<>();
        for (Map.Entry<String, List<NodeDto>> entry : categoryNodeMap.entrySet()) {
            List<NodeDto> categoryNodeDtos = entry.getValue();

            for (int i = 0; i < categoryNodeDtos.size() - 1; i++) {
                NodeDto sourceNodeDto = categoryNodeDtos.get(i);
                // 가장 유사도 높은 노드 찾기
                Pair<NodeDto, Double> pair = findMostSimilarNode(categoryNodeDtos, sourceNodeDto, i + 1);
                NodeDto targetNodeDto = pair.getLeft();
                double maxSimilarity = pair.getRight();
                if (maxSimilarity >= 0.6) {
                    // 이미 있는 게 아니라면 저장하기
                    LinkDto linkDto = saveLinkIfNotExists(sourceNodeDto, targetNodeDto, maxSimilarity, sourceNodeDto.getUserId());
                    System.out.println("link = " + linkDto.getSource());
                    linkDtos.add(linkDto);
                }
            }

        }
        // 중복 링크 제거
        return linkDtos.stream().distinct().collect(Collectors.toList());
    }

    // 가장 유사도 높은 노드 찾기
    private Pair<NodeDto, Double> findMostSimilarNode(List<NodeDto> nodeDtos, NodeDto sourceNodeDto, int startIndex) {
        double maxSimilarity = Double.MIN_VALUE;
        NodeDto mostSimilarNodeDto = null;
        for (int j = startIndex; j < nodeDtos.size(); j++) {
            NodeDto targetNodeDto = nodeDtos.get(j);
            double similarity = calculateSimilarity(sourceNodeDto.getContent(), targetNodeDto.getContent());
            System.out.println("similarity = " + similarity);
            if (similarity > maxSimilarity) {
                mostSimilarNodeDto = targetNodeDto;
                maxSimilarity = similarity;
            }
        }
        return Pair.of(mostSimilarNodeDto, maxSimilarity);
    }

    // 링크 저장하기
    private LinkDto saveLinkIfNotExists(NodeDto sourceNodeDto, NodeDto targetNodeDto, double maxSimilarity, int userId) {
        LinkDto linkDto = new LinkDto();
        if (targetNodeDto != null) {
            String source = sourceNodeDto.getId();
            String target = targetNodeDto.getId();
            linkDto.setSource(source);
            linkDto.setTarget(target);
            linkDto.setSimilarity(Double.parseDouble(df.format(maxSimilarity)));
            linkDto.setUserId(userId);
            }
        return linkDto;
    }

    // 유사도 분석 with 코사인 유사도
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

    @Override
    public DiagramResponseDto getLinkDiagram(String token) {
        UserInfoResponseDto res = feginUserService.userInfoByToken(token);
//        UserInfoResponseDto res = feginUserService.userInfoByUserPk(userId);

        List<EditorListResponseDto> editorList = feginEditorService.editorList(new EditorListRequestDto(res.getDocumentsRoots()));
        System.out.println(" = " + editorList.get(0).getContent());
        List<NodeResponseDto> nodeResponseDtos = createNodeResponseDtos(editorList);
        List<LinkResponseDto> linkResponseDtos = createLinkResponseDtos(editorList);

        return createDiagramResponseDto(nodeResponseDtos, linkResponseDtos);
    }

    // 친구 뇌 받기
    public DiagramResponseDto linkNodesByShares(String token,  List<Integer> targetUserIds) {
        UserInfoResponseDto res = feginUserService.userInfoByToken(token);

        List<EditorListResponseDto> editorList = feginEditorService.editorList(new EditorListRequestDto(res.getDocumentsRoots()));
        List<EditorListResponseDto> shareeditorList = feginEditorService.editorList(new EditorListRequestDto(res.getSharedDocumentsRoots()));
        shareeditorList = shareeditorList.stream()
                .filter(editor -> targetUserIds.contains(editor.getOwner()))
                .collect(Collectors.toList());
        editorList.addAll(shareeditorList);
        List<NodeResponseDto> nodeResponseDtos = createNodeResponseDtos(editorList);
        List<LinkResponseDto> linkResponseDtos = createLinkResponseDtos(editorList);
        List<NodeDto> nodeDtos = classifyAndSaveEmptyCategoryNodes(editorList);
        List<LinkDto> linkDtos = linkNodesByCategoryAndConfidence(nodeDtos);
        List<LinkResponseDto> linkResponseDtos1 = convertLinkResponseDtos(linkDtos);
        linkResponseDtos.addAll(linkResponseDtos1);
        DiagramResponseDto response = createDiagramResponseDto(nodeResponseDtos, linkResponseDtos);
        diagramCache.put(res.getUserPk(), response);
        return response;
    }


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
