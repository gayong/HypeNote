package com.surf.editor.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(collection = "editor")
public class Editor {
    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String id;
    private int userId; //첫 생성자

    private String title;
    private String content;

    private String parentId;
    private List<String> childId;
    private List<String> hyperLink;

    public static Editor toEntity(String title, String content){
        return Editor.builder()
                .id(null)
                .content(content)
                .title(title)
                .build();
    }

    public void write(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void childRelation(String child){
        childId.add(child);
    }
    public void childHyperLink(String child){
        hyperLink.add(child);
    }

    public void parentDelete(){
        parentId = null;
    }
    public void childDelete(String child){
        if (childId != null && childId.contains(child)) {
            childId.remove(child);
        }
    }
}
