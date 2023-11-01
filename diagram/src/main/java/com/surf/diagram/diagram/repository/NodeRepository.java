package com.surf.diagram.diagram.repository;

import com.surf.diagram.diagram.entity.Diagram;
import com.surf.diagram.diagram.entity.Node;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface NodeRepository extends MongoRepository<Node, Long> {
    List<Node> findByUserId(int userId);
}
