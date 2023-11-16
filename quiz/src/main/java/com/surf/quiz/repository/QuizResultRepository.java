package com.surf.quiz.repository;

import com.surf.quiz.entity.QuizResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizResultRepository extends MongoRepository<QuizResult, String> {
    List<QuizResult> findByRoomId(int roomId);
    QuizResult findByUser_UserPkAndRoomId(Long userId, int roomId);
    List<QuizResult> findByUser_UserPk(Long userPk);
    long countByRoomId(int roomId);

}
