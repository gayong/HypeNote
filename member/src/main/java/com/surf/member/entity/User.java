package com.surf.member.entity;


import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userPk;

    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    @NotBlank(message = "이메일 주소를 입력해주세요.")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Length(min = 8, max = 100, message = "비밀번호는 8자 이상 작성해주세요.")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요")
    @Length(min = 1, max = 10, message = "닉네임은 10자 이하로 작성해주세요.")
    @Column(unique = true)
    private String nickName;

    @Nullable
    private String profileImage;

    @ElementCollection
    private List<String> documentsRoots;

    private String role;
}
