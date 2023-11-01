package com.surf.diagram.diagram.service;

import com.surf.diagram.diagram.dto.request.CreateDiagramDto;
import com.surf.diagram.diagram.dto.request.CreateDiagramWithParentDto;
import com.surf.diagram.diagram.dto.request.UpdateDiagramDto;
import com.surf.diagram.diagram.dto.request.UpdatePositionDto;
import com.surf.diagram.diagram.entity.Diagram;
import com.surf.diagram.diagram.repository.DiagramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
