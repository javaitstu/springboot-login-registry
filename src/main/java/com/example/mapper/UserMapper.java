package com.example.mapper;

import com.example.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface UserMapper {

    /**
     * 新增用户
     *
     * @param user 用户
     * @return int
     */
    @Insert("INSERT INTO user (email, password, salt, confirm_code, activation_time, is_valid) VALUES" +
            "(#{email}, #{password}, #{salt}, #{confirmCode}, #{activationTime}, #{isValid})")
    int insertUser(User user);

    /**
     * 查询激活时间确认
     *
     * @param confirmCode 确认代码
     * @return {@link User}
     */
    @Select("SELECT email, activation_time FROM user WHERE confirm_code = #{confirmCode}")
    User selectActivationTimeByConfirmCode(@Param("confirmCode") String confirmCode);

    /**
     * 更新确认代码是有效
     *
     * @param confirmCode 确认代码
     * @return int
     */
    @Update("UPDATE user SET is_valid = 1 WHERE confirm_code = #{confirmCode}")
    int updateIsValidByConfirmCode(@Param("confirmCode") String confirmCode);

    /**
     * 查询密码通过电子邮件
     *
     * @param email 电子邮件
     * @return {@link List}<{@link User}>
     */
    @Select("SELECT email, password, salt FROM user WHERE email = #{email} AND is_valid = 1")
    List<User> selectPasswordByEmail(@Param("email") String email);


}
