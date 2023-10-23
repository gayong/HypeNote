package com.surf.diagram.diagram.repository;

import com.surf.diagram.diagram.entity.Diagram;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiagramRepository extends MongoRepository<Diagram, Long> {

}
