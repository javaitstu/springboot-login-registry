Create table [IF NOT EXISTS]`exchange`.`user`
(
    `id`              varchar(255) NOT NULL COMMENT '主键',
    `email`           varchar(255) COMMENT '邮箱',
    `password`        varchar(255) COMMENT '密码',
    `salt`            varchar(255) COMMENT '盐',
    `activation_time` datetime COMMENT '激活失效时间',
    `is_valid`        tinyint(1) COMMENT '是否可用，0不可用，1可用',
    primary key (`id`)
) charset = utf8mb4
  collate = utf8mb4_0900_ai_ci