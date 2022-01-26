package com.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor  // 生成无参构造器
@AllArgsConstructor // 所有参数构造器
public class User implements Serializable {

    private Integer id; // 主键
    private String email; // 邮件
    private String password; // 密码 md5 + 盐
    private String salt; // 盐
    private String confirmCode; // 确认码
    private LocalDateTime activationTime; // 激活失效时间
    private Byte isValid; // 账号是否可用 0 不可用 1 可用

}
