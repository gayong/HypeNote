package com.example.securitystudy.user;

import com.example.securitystudy.user.entity.User;
import com.example.securitystudy.user.repository.UserRepository;
import com.example.securitystudy.util.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.securitystudy.util.BaseResponseStatus.*;

@Slf4j
@Service
@Component
public class UserProvider {

    private final UserDao userDao;
    private final UserRepository userRepository;

    @Autowired
    public UserProvider(UserDao userDao, UserRepository userRepository) {
        this.userDao = userDao;
        this.userRepository = userRepository;
    }

    public User retrieveByEmail(String email) throws BaseException {

        if (checkEmail(email) == 0)
            throw new BaseException(USERS_EMPTY_USER_EMAIL);
        try {
            System.out.println("userDao.selectByEmail(email) = " + userDao.selectByEmail(email));
            return userDao.selectByEmail(email);
        } catch (Exception e) {
            System.out.println("e = " + e);
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public User retrieveByEmailProvider(String email, String provider) throws BaseException {
        if (checkEmailProvider(email, provider) == 0)
            throw new BaseException(USERS_EMPTY_USER_EMAIL);
        try {
            return userDao.selectByEmailProvider(email, provider);
        } catch (Exception e) {
            System.out.println("e = " + e);
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public User retrieveById(Long user_id) throws BaseException {
        if (checkId(user_id) == 0)
            throw new BaseException(USERS_EMPTY_USER_ID);
        try {
            return userDao.selectById(user_id);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<User> retrieveAll() {
        return userRepository.findAll();
    }


    public int checkEmail(String email) throws BaseException {
        try {
            return userDao.checkEmail(email);
        } catch (Exception exception) {
            log.warn(exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public int checkId(Long id) throws BaseException {
        try {
            return userDao.checkId(id);
        } catch (Exception exception) {
            log.warn(exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkEmailProvider(String email, String provider) throws BaseException {
        try {
            return userDao.checkEmailProvider(email, provider);
        } catch (Exception exception) {
            log.warn(exception.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
