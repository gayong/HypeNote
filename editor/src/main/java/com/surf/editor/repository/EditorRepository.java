package com.surf.editor.repository;

import com.surf.editor.domain.Editor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EditorRepository extends MongoRepository<Editor,String> {
    Optional<List<Editor>> findByTitleContainingOrContentContaining(String searchTextInTitle, String searchTextInContent);
    Optional<List<String>> findByParentIdAndUserId(String parentId, int userId);

    Optional<Editor> findByParentId(String parentId);

    Optional<List<Editor>> findAllByUserId(int userId);
}
