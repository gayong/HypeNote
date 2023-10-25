package com.surf.editor.repository;

import com.surf.editor.domain.Editor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EditorRepository extends MongoRepository<Editor,String> {
    List<Editor> findByTitleContainingOrContentContaining(String searchTextInTitle, String searchTextInContent);
}
