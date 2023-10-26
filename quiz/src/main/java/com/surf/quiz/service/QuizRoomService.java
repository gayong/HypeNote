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

    public List<Member> getUsersByRoomId(Long roomId) {
        Optional<QuizRoom> optional = quizRepo.findById(roomId);
        if (optional.isPresent()) {
            return optional.get().getUsers();
        } else {
            throw new NoSuchElementException("No QuizRoom for ID: " + roomId);
        }
    }


}
