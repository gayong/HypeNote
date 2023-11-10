package com.surf.editor.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.ArrayList;
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

    private List<Integer> writePermission;

    public static Editor editorCreate(int userId){
        Editor editor = Editor.builder()
                .id(null)
                .userId(userId)
                .content("<h1 class=\"_inlineContent_nstdf_297\">제목 없음</h1><p class=\"_inlineContent_nstdf_297\"></p>")
                .title("제목 없음")
                .parentId("root")
                .childId(new ArrayList<>())
                .hyperLink(new ArrayList<>())
                .writePermission(new ArrayList<>())
                .build();

        editor.getWritePermission().add(userId);

        return editor;
    }

    public void write(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void childRelation(String child){
        childId.add(child);
    }
    public void parentRelation(String parentId){
        this.parentId = parentId;
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

    public void writerPermissionAdd(int userId){
        this.writePermission.add(userId);
    }

    public void writerPermissionSub(int userId){
        this.writePermission.remove(userId);
    }
}
