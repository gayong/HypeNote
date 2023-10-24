package com.surf.quiz.repository;


import com.surf.quiz.entity.QuizRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRoomRepository extends MongoRepository<QuizRoom, Long> {
    QuizRoom save(QuizRoom quizroom);
    void delete(QuizRoom quizroom);
    Optional<QuizRoom> findById(Long Id);
    List<QuizRoom> findAll();
}
