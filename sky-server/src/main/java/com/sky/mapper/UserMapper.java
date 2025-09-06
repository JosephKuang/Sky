package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import static com.sky.enumeration.OperationType.INSERT;

@Mapper
public interface UserMapper {
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

//    @AutoFill(INSERT)
    void insertUser(User user);

    @Select("select * from user where id = #{userId}")
    User getById(Long userId);
}
