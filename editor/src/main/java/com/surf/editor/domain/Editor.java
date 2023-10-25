package com.surf.editor.domain;

import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(collection = "editor")
public class Editor {
    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String id;

    private String title;
    private String content;


    public static Editor toEntity(String title, String content){
        return Editor.builder()
                .id(null)
                .content(content)
                .title(title)
                .build();
    }
}
