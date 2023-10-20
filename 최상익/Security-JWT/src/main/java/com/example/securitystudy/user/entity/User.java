package com.example.securitystudy.user.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String nickname;
    private String email;
    private String password;
    private String introduce;
    private String role;
    private String provider;
    private String provider_id;
    private int status=1;

    public User(String name, String nickname, String email, String password, String role, String provider, String provider_id, int status){
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
//        this.introduce = introduce;
        this.role = role;
        this.provider = provider;
        this.provider_id = provider_id;
        this.status = status;
    }

    public User(Long id ,String name, String nickname, String email, String password, String role, String provider, String provider_id){
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.provider = provider;
        this.provider_id = provider_id;
//        this.status = status;
    }
}
