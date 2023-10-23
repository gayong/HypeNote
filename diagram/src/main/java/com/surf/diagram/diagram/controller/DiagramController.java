package com.surf.diagram.diagram.controller;

import com.surf.diagram.diagram.dto.CreateDiagramDto;
import com.surf.diagram.diagram.entity.Diagram;
import com.surf.diagram.diagram.repository.DiagramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/diagrams")
public class DiagramController {


    @Autowired
    private DiagramRepository diagramRepository;
    @PostMapping
    public ResponseEntity<String> createDiagram(@RequestBody CreateDiagramDto dto) {
        // 요청으로부터 필요한 정보를 추출하여 Diagram 객체를 생성합니다.
        Diagram diagram = Diagram.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        // MongoDB에 Diagram 객체를 저장합니다.
        diagramRepository.save(diagram);
        return ResponseEntity.ok("Diagram created successfully");
    }
    @GetMapping
    public ResponseEntity<List<Diagram>> getAllDiagrams() {
        List<Diagram> diagrams = diagramRepository.findAll();
        return ResponseEntity.ok(diagrams);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Diagram> getDiagramById(@PathVariable("id") Long id) {
        Optional<Diagram> optionalDiagram = diagramRepository.findById(id);

        if (optionalDiagram.isPresent()) {
            Diagram diagram = optionalDiagram.get();
            return ResponseEntity.ok(diagram);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
