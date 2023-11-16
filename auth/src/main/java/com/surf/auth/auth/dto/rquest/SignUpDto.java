package com.surf.auth.auth.dto.rquest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class SignUpDto {

    private String email;
    private String password;
    private String nickName;
    private MultipartFile profileImage;
}
