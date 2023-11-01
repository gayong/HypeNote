package com.surf.diagram.diagram.controller;

import com.surf.diagram.diagram.common.BaseResponse;
import com.surf.diagram.diagram.dto.request.CreateDiagramDto;
import com.surf.diagram.diagram.dto.request.CreateDiagramWithParentDto;
import com.surf.diagram.diagram.dto.request.UpdateDiagramDto;
import com.surf.diagram.diagram.dto.request.UpdatePositionDto;
import com.surf.diagram.diagram.dto.response.NodeResponseDto;
import com.surf.diagram.diagram.entity.Diagram;
import com.surf.diagram.diagram.entity.Link;
import com.surf.diagram.diagram.entity.Node;
import com.surf.diagram.diagram.repository.LinkRepository;
import com.surf.diagram.diagram.repository.NodeRepository;
import com.surf.diagram.diagram.service.DiagramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diagram")
@Tag(name = "다이어그램", description = "다이어그램")
public class DiagramController {

    private final DiagramService diagramService;
    private final NodeRepository nodeRepository;
    private final LinkRepository linkRepository;

    public DiagramController(DiagramService diagramService, NodeRepository nodeRepository ,LinkRepository linkRepository) {
        this.diagramService = diagramService;
        this.nodeRepository = nodeRepository;
        this.linkRepository = linkRepository;
    }

    @PostMapping
    @Operation(summary = "다이어그램 생성")
    public BaseResponse<String> createDiagram(@RequestBody CreateDiagramDto dto) {
        String message = diagramService.createDiagram(dto);
        return new BaseResponse<>(message);
    }
    @GetMapping
    @Operation(summary = "모든 다이어그램 조회")
    public BaseResponse<List<Diagram>> getAllDiagrams() {
        List<Diagram> diagrams = diagramService.getAllDiagrams();
        return new BaseResponse<>(diagrams);
    }


    @GetMapping("/{id}")
    @Operation(summary = "단일 다이어그램 조회")
    public BaseResponse<Diagram> getDiagramById(@PathVariable("id") Long id) {
        Diagram diagram = diagramService.getDiagramById(id).orElse(null);
        return new BaseResponse<>(diagram);
    }

    @PutMapping("/{id}")
    @Operation(summary = "다이어그램 수정")
    public BaseResponse<String> updateDiagram(@PathVariable("id") Long id, @RequestBody UpdateDiagramDto dto) {
        String message = diagramService.updateDiagram(id, dto);
        return new BaseResponse<>(message);
    }

    @PutMapping("/position/{id}")
    @Operation(summary = "다이어그램 위치 수정")
    public BaseResponse<String> updatePosition(@PathVariable("id") Long id, @RequestBody UpdatePositionDto dto) {
        String message = diagramService.updatePosition(id, dto);
        return new BaseResponse<>(message);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "다이어그램 삭제")
    public BaseResponse<String> deleteById(@PathVariable("id") Long id){
        String message = diagramService.deleteById(id);
        return new BaseResponse<>(message);
    }


    @PostMapping("/parent/{parentid}")
    @Operation(summary = "부모 노드 참조")
    public BaseResponse<Diagram> createDiagramWithParent(@PathVariable("parentid") Long parentid, @RequestBody CreateDiagramWithParentDto dto) {
        Diagram diagram = diagramService.createDiagramWithParent(parentid, dto);
        return new BaseResponse<>(diagram);
    }

    @PostMapping("/share/{shareId}")
    @Operation(summary = "공유 노드")
    public BaseResponse<List<Diagram>> getShareNode(@PathVariable("shareId") Long shareId) {
        List<Diagram> diagrams = diagramService.getShareNode(shareId);
        return new BaseResponse<>(diagrams);
    }

    @GetMapping("/node")
    @Operation(summary = "내 노드와 링크 조회")
    public BaseResponse<NodeResponseDto> getNodes() {
        NodeResponseDto response = new NodeResponseDto();
        List<Node> nodes = nodeRepository.findByUserId(1);
        List<Link> links = linkRepository.findByUserId(1);

        response.setNodes(nodes);
        response.setLinks(links);
        return new BaseResponse<>(response);
    }


    @PostMapping("/node")
    @Operation(summary = "노드와 링크 생성")
    public void createNodeAndLink() {

        Node node = new Node();
        Link link = new Link();

        node.setUserId(1);
        node.setTitle("1");
        node.setContent("1");
        node.setCategory("12");
        node.setEditorId(1);

        link.setTarget("123");
        link.setSource("123");
        link.setUserId(1);

        nodeRepository.save(node);
        linkRepository.save(link);
    }





//    @PostMapping("/parent/{parentid}")
//    @Operation(summary = "자식 다이어그램 삽입")
//    public ResponseEntity<String> createDiagramWithParent(@PathVariable("parentid") Long parentid, @RequestBody CreateDiagramWithParentDto dto) {
//        Optional<Diagram> optionalParent = diagramRepository.findById(parentid);
//
//        if (optionalParent.isPresent()) {
//            Diagram parent = optionalParent.get();
//
//            // 요청으로부터 필요한 정보를 추출하여 Diagram 객체(자식)을 생성합니다.
//            Diagram child = Diagram.builder()
//                    .title(dto.getTitle())
//                    .content(dto.getContent())
//                    .build();
//
//            // 자식 다이어그램을 부모의 children 리스트에 추가합니다.
//            List<Diagram> children = parent.getChildren();
//            if (children == null) {
//                children = new ArrayList<>();
//                parent.setChildren(children);
//            }
//            children.add(child);
//
//            // MongoDB에 변경된 Diagram 객체(부모)을 저장합니다.
//            diagramRepository.save(parent);
//
//            return ResponseEntity.ok("자식 다이어그램 삽입");
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

//    @PostMapping("/parent/{parentid}/{childid}")
//    @Operation(summary = "부모 노드 참조 추가")
//    public ResponseEntity<String> addPArent(@PathVariable("parentid") Long parentid, @PathVariable("childid") Long childid,) {
//        Optional<Diagram> optionalParent = diagramRepository.findById(parentid);
//
//        if (optionalParent.isPresent()) {
//            Diagram parent = optionalParent.get();
//
//            Optional<Diagram> optionalChild = diagramRepository.findById(childid);
//            Diagram child = optionalChild.get();
//            child.setParentId();
//
//            // MongoDB에 변경된 Diagram 객체(자식)을 저장합니다.
//            diagramRepository.save(child);
//
//            return ResponseEntity.ok("부모 노드 참조");
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

}
