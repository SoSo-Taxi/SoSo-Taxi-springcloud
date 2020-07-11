package com.apicaller.sosotaxi.mapper;

import com.apicaller.sosotaxi.project.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    @Select(value = "select u.userName,u.password from user u where u.userName=#{userName}")
    @Results
            ({@org.apache.ibatis.annotations.Result(property = "userName",column = "userName"),
                    @org.apache.ibatis.annotations.Result(property = "password",column = "password")})
    User findUserByName(@Param("userName") String userName);

    @Insert("insert into user values(#{id},#{userName},#{password})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void regist(User user);

    @Select("select u.id from user u where u.userName = #{userName} and password = #{password}")
    Long login(User user);
}
