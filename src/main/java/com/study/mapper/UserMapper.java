package com.study.mapper;

import com.study.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    // 1. 添加用户
    @Insert("insert into t_user (email, password, salt, confirm_code, activation_time, is_valid) " +
            "value (#{email}, #{password}, #{salt}, #{confirmCode}, #{activationTime}, #{isValid})")
    int insertAdminUser(User user);

    // 2. 通过确认码查询用户
    @Select("select email, activation_time from t_user where confirm_code = #{confirmCode}")
    User selectActivationTimeByConfirmCode(@Param("confirmCode") String confirmCode);

    // 3. 通过确认码更新用户账号可用情况
    @Update("update t_user set is_valid = 1 where confirm_code = #{confirmCode}")
    int updateIsValidByConfirmCode(@Param("confirmCode") String confirmCode);

    // 4. 通过邮箱查询用户
    @Select("select email, password, salt from t_user where email = #{email} and is_valid = 1")
    List<User> selectPasswordByEmail(String email);

    // 5.
    @Select("select email, password, salt from t_user where email = #{email}")
    List<User> selectByEmail(String email);
}