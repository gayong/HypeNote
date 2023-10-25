package com.surf.editor.repository;

import com.surf.editor.domain.Editor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorRepository extends MongoRepository<Editor,String> {
}
