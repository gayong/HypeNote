package com.surf.quiz.service;


import com.surf.quiz.entity.Quiz;
import com.surf.quiz.entity.QuizRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostsModelListenerQuiz extends AbstractMongoEventListener<Quiz> {
    private final SequenceGeneratorService sequenceGenerator;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Quiz> event) {
        if (event.getSource().getId() == null) {
            event.getSource().setId(sequenceGenerator.generateSequence(Quiz.SEQUENCE_NAME));
        }
    }

}
