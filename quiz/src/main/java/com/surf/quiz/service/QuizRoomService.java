package com.surf.quiz.service;


import com.surf.quiz.entity.Member;
import com.surf.quiz.entity.QuizRoom;
import com.surf.quiz.repository.QuizRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class QuizRoomService {
    private final QuizRoomRepository quizRepo;

    @Autowired
    public QuizRoomService(QuizRoomRepository quizRepo) {
        this.quizRepo = quizRepo;
    }

    public void saveScore(Long roomId, long userId) {
        Optional<QuizRoom> optional = quizRepo.findById(roomId);
        if (optional.isPresent()) {
            for (Member member : optional.get().getUsers()) {
                if (userId == member.getUserId()) {
//                    member.setGameScore(member.getGameScore() + 100);
                    member.setCorrect(member.getCorrect() + 1);
                }
            }
            quizRepo.save(optional.get());
        }
    }

    public QuizRoom save(QuizRoom room) {
        return quizRepo.save(room);
    }

    public void delete(QuizRoom room) {
        quizRepo.delete(room);
    }

    public Optional<QuizRoom> findById(Long Id) {
        return quizRepo.findById(Id);
    }

    public List<QuizRoom> findAll() {

        return quizRepo.findAll();
    }

    public void terminate(Long roomId) {
        // MongoDB에서 특정 ID를 가진 데이터만 삭제합니다.
        quizRepo.deleteById(roomId);
    }

    public List<Member> getUsersByRoomId(Long roomId) {
        Optional<QuizRoom> optional = quizRepo.findById(roomId);
        if (optional.isPresent()) {
            return optional.get().getUsers();
        } else {
            throw new NoSuchElementException("No QuizRoom for ID: " + roomId);
        }
    }


}
