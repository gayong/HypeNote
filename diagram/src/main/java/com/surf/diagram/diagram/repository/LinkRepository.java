package com.surf.diagram.diagram.repository;

import com.surf.diagram.diagram.entity.Diagram;
import com.surf.diagram.diagram.entity.Link;
import com.surf.diagram.diagram.entity.Node;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LinkRepository extends MongoRepository<Link, Long> {
    List<Link> findByUserId(int userId);
}
