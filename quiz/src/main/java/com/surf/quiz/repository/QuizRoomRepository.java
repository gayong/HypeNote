package com.surf.quiz.repository;


import com.surf.quiz.entity.QuizRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuizRoomRepository extends MongoRepository<QuizRoom, Long> {
}
