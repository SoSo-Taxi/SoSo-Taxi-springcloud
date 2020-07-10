package com.apicaller.sosotaxi.utils;


import com.apicaller.sosotaxi.project.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author shuang.kou
 * @description 获取当前请求的用户
 */
@Component
public class CurrentUserUtils {

//    private final UserService userService;

    /**
     * 之后根据自己写的实现
     * @return User
     */
    public User getCurrentUser() {
        return null;
    }

    private static String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }
}
