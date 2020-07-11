package com.apicaller.sosotaxi.service;

import com.apicaller.sosotaxi.entity.Result;
import com.apicaller.sosotaxi.mapper.UserMapper;
import com.apicaller.sosotaxi.project.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 此处的注册、登录和查找非常不完善，记得修改
     * @param user
     * @return
     */
    public Result regist(User user){
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        try{
            User existUser = userMapper.findUserByName(user.getUserName());
            if(existUser != null){
                result.setMsg("用户名已存在");
            }else{
                userMapper.regist(user);
                result.setMsg("注册成功");
                result.setSuccess(true);
                result.setDetail(user);
            }
        }catch(Exception e){
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public User findUserByName(String name){
        try{
            User user = userMapper.findUserByName(name);
            if(user != null){
                return user;
            }else{
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
