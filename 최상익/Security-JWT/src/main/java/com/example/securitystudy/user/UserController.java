package com.example.securitystudy.user;

import com.example.securitystudy.user.dto.GetUserRes;
import com.example.securitystudy.user.entity.User;
import com.example.securitystudy.util.BaseException;
import com.example.securitystudy.util.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

//    @GetMapping("")
//    public BaseResponse<GetUserRes> getProfile(HttpServletRequest request) {
//        try {
//            Long user_id = Long.parseLong(request.getAttribute("user_id").toString());
//            User user = userProvider.retrieveById(user_id);
//            GetUserRes getUserRes = new GetUserRes(user.getNickname(), user.getIntroduce(), user.getEmail());
//            return new BaseResponse<>(getUserRes);
//        } catch (BaseException e) {
//            return new BaseResponse<>(e.getStatus());
//        }
//    }
    @GetMapping("")
    public BaseResponse<List<GetUserRes>> getAllUsers() {
        try {
            List<User> users = userProvider.retrieveAll(); // retrieveAll() 메소드는 모든 유저를 반환해야 합니다.
            List<GetUserRes> userResponses = users.stream()
                    .map(user -> new GetUserRes(user.getNickname(), user.getIntroduce(), user.getEmail()))
                    .collect(Collectors.toList());
            return new BaseResponse<>(userResponses);
        } catch (DataAccessException e) {
            return new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Data access error");
        }
    }
}
