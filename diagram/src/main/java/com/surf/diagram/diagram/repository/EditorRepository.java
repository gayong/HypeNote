package com.surf.diagram.diagram.repository;


import com.surf.diagram.diagram.entity.Editor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EditorRepository extends MongoRepository<Editor, String> {
}
