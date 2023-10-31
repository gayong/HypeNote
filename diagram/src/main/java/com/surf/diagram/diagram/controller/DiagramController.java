package com.surf.diagram.diagram.controller;

import com.surf.diagram.diagram.common.BaseResponse;
import com.surf.diagram.diagram.dto.request.CreateDiagramDto;
import com.surf.diagram.diagram.dto.request.CreateDiagramWithParentDto;
import com.surf.diagram.diagram.dto.request.UpdateDiagramDto;
import com.surf.diagram.diagram.dto.request.UpdatePositionDto;
import com.surf.diagram.diagram.entity.Diagram;
import com.surf.diagram.diagram.repository.DiagramRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/diagram")
@Tag(name = "다이어그램", description = "다이어그램")
public class DiagramController {


    @Autowired
    private DiagramRepository diagramRepository;

    @PostMapping
    @Operation(summary = "다이어그램 생성")
    public BaseResponse<String> createDiagram(@RequestBody CreateDiagramDto dto) {
        // 요청으로부터 필요한 정보를 추출하여 Diagram 객체를 생성합니다.
        Diagram diagram = Diagram.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        // MongoDB에 Diagram 객체를 저장합니다.
        diagramRepository.save(diagram);
        return new BaseResponse<>("다이어그램 생성 완료");
    }
    @GetMapping
    @Operation(summary = "모든 다이어그램 조회")
    public BaseResponse<List<Diagram>> getAllDiagrams() {
        List<Diagram> diagrams = diagramRepository.findAll();
        return new BaseResponse<>(diagrams);
    }


    @GetMapping("/{id}")
    @Operation(summary = "단일 다이어그램 조회")
    public BaseResponse<Diagram> getDiagramById(@PathVariable("id") Long id) {
        Optional<Diagram> optionalDiagram = diagramRepository.findById(id);

        if (optionalDiagram.isPresent()) {
            Diagram diagram = optionalDiagram.get();
            return new BaseResponse<>(diagram);
        } else {
            return new BaseResponse<>(null);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "다이어그램 수정")
    public BaseResponse<String> updateDiagram(@PathVariable("id") Long id, @RequestBody UpdateDiagramDto dto) {
        Optional<Diagram> optionalDiagaram = diagramRepository.findById(id);

        if(optionalDiagaram.isPresent()) {
            Diagram existingDiagaram = optionalDiagaram.get();

            existingDiagaram.setTitle(dto.getTitle());

            diagramRepository.save(existingDiagaram);

            return new BaseResponse<>("다이어그램 수정 완료");
        } else {
            return new BaseResponse<>(null);
        }
    }

    @PutMapping("/position/{id}")
    @Operation(summary = "다이어그램 위치 수정")
    public BaseResponse<String> updatePosition(@PathVariable("id") Long id, @RequestBody UpdatePositionDto dto) {
        Optional<Diagram> optionalDiagaram = diagramRepository.findById(id);

        if(optionalDiagaram.isPresent()) {
            Diagram existingDiagaram = optionalDiagaram.get();

            existingDiagaram.setX(dto.getX());
            existingDiagaram.setY(dto.getY());
            existingDiagaram.setZ(dto.getZ());

            diagramRepository.save(existingDiagaram);

            return new BaseResponse<>("위치 수정 완료");
        } else {
            return new BaseResponse<>(null);
        }
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "다이어그램 삭제")
    public BaseResponse<String> deleteById(@PathVariable("id") Long id){
        Optional<Diagram> optionalDiaogram=diagramRepository.findById(id);
        if(optionalDiaogram.isPresent()){
            diagramRepository.deleteById(id);
            return new BaseResponse<>("다이어그램 삭제 완료");
        }else{
            return new BaseResponse<>(null);
        }
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
    @PostMapping("/parent/{parentid}")
    @Operation(summary = "부모 노드 참조")
    public BaseResponse<String> createDiagramWithParent(@PathVariable("parentid") Long parentid, @RequestBody CreateDiagramWithParentDto dto) {
        Optional<Diagram> optionalParent = diagramRepository.findById(parentid);

        if (optionalParent.isPresent()) {
            Diagram parent = optionalParent.get();

            // 요청으로부터 필요한 정보를 추출하여 Diagram 객체(자식)을 생성합니다.
            Diagram child = Diagram.builder()
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .parentId((parentid))
                    .build();

            // MongoDB에 변경된 Diagram 객체(자식)을 저장합니다.
            diagramRepository.save(child);

            return new BaseResponse<>("부모 노드 참조");
        } else {
            return new BaseResponse<>(null);
        }
    }

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
