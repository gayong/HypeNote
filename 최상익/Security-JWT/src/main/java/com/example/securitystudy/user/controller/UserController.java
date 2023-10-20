package com.example.securitystudy.user.controller;

import com.example.securitystudy.user.UserProvider;
import com.example.securitystudy.user.dto.GetUserRes;
import com.example.securitystudy.user.entity.User;
import com.example.securitystudy.util.BaseException;
import com.example.securitystudy.util.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserProvider userProvider;

    @Autowired
    public UserController(UserProvider userProvider) {
        this.userProvider = userProvider;
    }


    @Tag(name = "멤버", description = "멤버")
    @GetMapping("")
    @Operation(summary = "전체 멤버 조회")
    public BaseResponse<List<GetUserRes>> getAllUsers() {
        try {
            List<User> users = userProvider.retrieveAll(); // retrieveAll() 메소드는 모든 유저를 반환해야 합니다.
            List<GetUserRes> userResponses = users.stream()
                    .map(user -> new GetUserRes(user.getId(), user.getName(), user.getNickname(), user.getIntroduce(), user.getEmail()))
                    .collect(Collectors.toList());
            return new BaseResponse<>(userResponses);
        } catch (DataAccessException e) {
            return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Data access error");
        }
    }

    @Tag(name = "멤버", description = "멤버")
    @GetMapping("/{user_id}")
    @Operation(summary = "단일 멤버 조회")
    public BaseResponse<GetUserRes> getUserById(@PathVariable("user_id") Long userId) {
        try {
            User user = userProvider.retrieveById(userId);
            if (user == null) {
                return new BaseResponse<>(HttpStatus.NOT_FOUND.value(), "User not found");
            }
            GetUserRes userResponse = new GetUserRes(user.getId(), user.getName(), user.getNickname(), user.getIntroduce(), user.getEmail());
            return new BaseResponse<>(userResponse);
        } catch (DataAccessException e) {
            return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Data access error");
        } catch (BaseException e) { // BaseException 처리
            return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
