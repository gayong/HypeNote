package com.surf.quiz.entity;

import com.surf.quiz.dto.QuestionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "quiz")
public class Quiz {
    @Transient
    public static final String SEQUENCE_NAME = "quiz_sequence";


    @Id
    private Long id;
    int roomId;
    @CreatedDate
    private LocalDateTime createdDate;

    List<QuestionDto> question;

    Map<String, List<String>> userAnswers = new HashMap<>();

    private boolean complete;
}
