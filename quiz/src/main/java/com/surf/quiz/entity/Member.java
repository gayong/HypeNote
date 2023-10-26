package com.surf.quiz.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "user")
public class Member {

    private Long userId;
    private String userName;
    
    // True == 방장, False == 일반
    private boolean host;

    // True == 레디, False == 대기
    private boolean ready;
}
