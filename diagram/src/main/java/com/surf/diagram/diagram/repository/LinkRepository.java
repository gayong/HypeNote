package com.surf.diagram.diagram.repository;

import com.surf.diagram.diagram.entity.Link;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LinkRepository extends MongoRepository<Link, Long> {
    List<Link> findByUserId(int userId);
    boolean existsBySourceAndTargetAndUserId(int source, int target, int userId);
}
