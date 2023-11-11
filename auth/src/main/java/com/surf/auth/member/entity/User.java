package com.surf.auth.member.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

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
//    @Column(unique = true)
    private String profileImage;

    @ElementCollection
    private List<String> documentsRoots;

    @ElementCollection
    private List<String> sharedDocumentsRoots;

    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}