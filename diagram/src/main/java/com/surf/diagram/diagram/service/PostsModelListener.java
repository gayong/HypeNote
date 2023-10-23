package com.surf.diagram.diagram.service;

import com.surf.diagram.diagram.entity.Diagram;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostsModelListener extends AbstractMongoEventListener<Diagram> {
    private final SequenceGeneratorService sequenceGenerator;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Diagram> event) {
        event.getSource().setId(sequenceGenerator.generateSequence(Diagram.SEQUENCE_NAME));
    }

}
