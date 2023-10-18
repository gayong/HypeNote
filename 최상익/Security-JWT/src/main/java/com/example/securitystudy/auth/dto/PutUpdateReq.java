package com.example.securitystudy.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutUpdateReq {
    private String username;
    private String nickname;
}
