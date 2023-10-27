package com.surf.quiz.service.autoIncrement;


import com.surf.quiz.entity.QuizRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostsModelListener extends AbstractMongoEventListener<QuizRoom> {
    private final SequenceGeneratorService sequenceGenerator;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<QuizRoom> event) {
        if (event.getSource().getId() == null) {
            event.getSource().setId(sequenceGenerator.generateSequence(QuizRoom.SEQUENCE_NAME));
        }
    }

}
