package com.udacity.jwdnd.course1.cloudstorage.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import java.util.List;
import com.udacity.jwdnd.course1.cloudstorage.model.User;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM USERS")
    List<User> getAll();
    @Select("SELECT * FROM USERS WHERE userid = #{userId}")
    User getUser(int userId);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES(#{userName}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int add(User user);

    @Update("UPDATE USERS SET username = #{userName},salt = #{salt}, password = #{password}, lastname = #{lastName} WHERE userid = #{userId}")
    void update(User user);

    @Delete("DELETE FROM USERS WHERE userid = #{userId}")
    void deleteByID(int userId);

    @Select("SELECT * FROM USERS WHERE username = #{userName}")
    User getByUserName(String userName);
}
