//package com.surf.diagram.diagram.service.autoIncrement;
//
//import com.surf.diagram.diagram.entity.Node;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
//import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
//import org.springframework.stereotype.Component;
//
//@RequiredArgsConstructor
//@Component
//public class PostsModelListenerNode extends AbstractMongoEventListener<Node> {
//    private final SequenceGeneratorService sequenceGenerator;
//
//    @Override
//    public void onBeforeConvert(BeforeConvertEvent<Node> event) {
//        if (event.getSource().getId() == null) {
//            event.getSource().setId(sequenceGenerator.generateSequence(Node.SEQUENCE_NAME));
//        }
//    }
//
//}
