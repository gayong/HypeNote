package com.surf.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class DocumentSharedUser {

    @Id
    private int userPk;

    private String editorId;

    private String sharedNickName;

    private boolean writePermission;
}
