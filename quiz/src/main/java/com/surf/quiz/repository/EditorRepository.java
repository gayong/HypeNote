package com.surf.quiz.repository;

import com.surf.quiz.entity.Editor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EditorRepository extends MongoRepository<Editor, String> {
}
