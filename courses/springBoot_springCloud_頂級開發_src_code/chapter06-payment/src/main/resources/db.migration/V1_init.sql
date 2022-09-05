#支付订单流水表
create table pay_order (
 id bigint not null primary key auto_increment,
 order_id varchar (50) comment '业务方订单号（业务方系统唯一）',
 trade_type varchar (30) comment '业务交易类型，例如topup-表示钱包充值',
 amount bigint comment '交易金额，以分为单位',
 currency varchar (10) comment '币种，cny-人民币',
 status varchar (2) comment '支付状态，0-待支付；1-支付中；2-支付成功；3-支付失败',
 channel varchar (10) comment '支付渠道编码，0-微信支付，1-支付宝支付',
 pay_type varchar (30) comment '渠道支付方式，ali_pay_pc-支付宝电脑网页支付；ali_pay_app-支付宝移动应用支付..',
 pay_id varchar (50) comment '支付平台自己生成的唯一订单流水号，用于与第三方渠道交互',
 trade_no varchar (32) comment '支付渠道流水号',
 user_id varchar (60) comment '业务方用户id',
 update_time timestamp null default current_timestamp on update current_timestamp comment '最后一次更新时间',
 create_time timestamp null default current_timestamp comment '交易创建时间',
 remark varchar(128)  comment '订单备注信息'
);
alter table pay_order comment '支付订单表';
#添加索引信息
alter table pay_order add index unique_idx_pay_id ( pay_id );
alter table pay_order add index idx_order_id ( order_id );
alter table pay_order add index idx_create_time ( create_time );

#支付通知报文信息表
create table pay_notify (
 id bigint not null primary key auto_increment,
 pay_id varchar (50) comment '支付平台订单流水号',
 channel varchar (10) comment '支付渠道编码，0-微信支付，1-支付宝支付',
 status varchar (2) comment '支付通知状态，1-支付中；2-支付成功；3-支付失败',
 fullinfo text comment '渠道通知原始报文信息',
 order_id varchar (50) comment '业务方订单流水号',
 verify varchar (2) comment '报文验签结果，0-验证成功；1-验签失败',
 merchant_id varchar (30) comment '支付渠道商户号，用于精准识别渠道参数',
 receive_status varchar (2) comment '接收处理状态，1-已接收；2-已处理；3-已同步至业务方',
 notify_count int comment '业务方通知次数',
 notify_time timestamp comment '业务方最近通知时间',
 update_time timestamp null default current_timestamp on update current_timestamp comment '最后一次更新时间',
 create_time timestamp null default current_timestamp comment '交易创建时间'
);
alter table pay_order comment '支付通知表，记录渠道通知报文及业务方通知状态信息';
#添加索引信息
alter table pay_notify add index unique_idx_pay_id ( pay_id );
alter table pay_notify add index idx_order_id ( order_id );

#支付渠道路由配置表
create table pay_channel_route_config (
 id bigint not null primary key auto_increment,
 app_id varchar (50) comment '接入方应用标识',
 trade_type varchar (10) comment '接入方业务类型',
 channel varchar (10) comment '支付渠道编码，0-微信支付，1-支付宝支付',
 pay_type varchar (30) comment '渠道具体支付方式',
 partner varchar (50) comment '具体支付渠道接入唯一账号标识',
 status varchar(2) NOT NULL DEFAULT '0' COMMENT '状态 0-可用，1-不可用',
 update_time timestamp null default current_timestamp on update current_timestamp comment '最后一次更新时间',
 create_time timestamp null default current_timestamp comment '创建时间'
);
alter table pay_channel_route_config comment '支付渠道路由配置信息表';
#添加索引信息
alter table pay_channel_route_config add index idx_app_id (app_id);

#支付渠道接口参数信息表
create table pay_channel_param (
 id bigint not null primary key auto_increment,
 partner varchar (50) comment '具体支付渠道接入唯一账号标识',
 sign_type varchar(10) NOT NULL COMMENT '签名加密方式，如：RSA、MD5、3DES',
 key_type varchar(30) COMMENT '证书类型,如：publickey-公钥；privatekey-私钥，若签名加密方式为对称加密则约定为私钥类型',
 key_context text COMMENT '证书文本内容',
 expire_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '证书到期时间',
 status varchar(2) NOT NULL DEFAULT '0' COMMENT '状态 0可用，1不可用',
 update_time timestamp null default current_timestamp on update current_timestamp comment ''最后一次更新时间'',
 create_time timestamp null default current_timestamp comment '创建时间',
 remark varchar(128) COMMENT '证书使用用途描述'
);
alter table pay_channel_param comment '支付渠道参数配置表，例如密钥、加密方式等信息';
#添加索引信息
alter table pay_channel_param add index idx_partner(partner);

#初始化渠道参数配置
INSERT INTO `pay_channel_param`(`id`, `partner`, `sign_type`, `key_type`, `key_context`, `expire_time`, `status`, `update_time`, `create_time`, `remark`) VALUES (1, '2016101800715197', 'RSA2', 'publickey', 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4Z0RuCT/DAxYzK4A1qU7yPmhEcO5vFoos/r9AI2J94BuvE16gR4rH0Xv6j1i7h/KcSnehdIwh2YNBzKbP+I+KCqyaK4fbbJKND5FOj+nWgvug8MII+mjHoTtCbt2h95odeTp+e9nU3zRFZw42018d1hwoGJpZwu8a8C8Dsn9tHMSTGhg1UrjJn3sP69q8eVTRcIQP+EPCsKohYaolXXmqoeevudSrVg5GIcXyXuuJPFGcKkOQo+Fujxj2JZxQcPYXRxcqPGVT2Q+bvTRA3BKKtALChWU5JbQTM3zMBdGQSDVfd1ipVnLAubzXB/Np6I23fAWywNKWRCWvLQFql46wwIDAQAB', '2021-03-02 10:07:26', '0', '2020-03-02 10:08:51', '2020-03-02 10:07:26', '');
INSERT INTO `pay_channel_param`(`id`, `partner`, `sign_type`, `key_type`, `key_context`, `expire_time`, `status`, `update_time`, `create_time`, `remark`) VALUES (2, '2016101800715197', 'RSA2', 'privatekey', 'MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDLBQXRgdVI0QfuTRqa5A2P9/LdSJ7F6KkOfYsz2VOzJ+QyA6C+lFyubv8FxakZhDfYEI6FT4YzC8xctoa/iGdEhLlhI20FFHbZ+lxVYIrjjTQBrqmztnZCcjVShK8q4zD6ErhxtEqdDaCqjkms36Veb/Py/0LcXBJoxv4hHI2uzzgQ9jX4XSHoLiHIWsJoyOnf9/BwQqfuxKV4CbcSdf0uWxNi/licF2mDx+2XlFlEx53r7cdQLczrFUC/grrlqF3e5Gky0a2byck9+Iz0U1ZAKTQjL7mrdtCDHteKk3WG9bZMphhYE4sF0JhSTPhFBVrdG2fmylSJbT0SOezx7qhxAgMBAAECggEAcAmij18OHDQ3IzxrzUTDc0sLPTEhZeiiuztvQbrSpREDrtIvuFzZ1O2uUTomus0roFJqxIkBb1Q4gIW+UR7ulnKEd2X8eQw1FtBCel9f5nn7VF5WirXMB5lNce+FV66vTaJdJWqwroFhxB3n7L4zXa18vAJAnKk3A5mljxJZ4ji/WPvlN5lPl1rPLwngtDy8vn9rm/eg1vUm8OHRcW3XepwlejUOgD1bXpm5SWVmjhYNYXmGSCwHGj878XNaT9ZHvee3lrxCErWckmZdJ9HEg/yOQwjEqaYgTyLEK9AYs8cGBhjk7J0NOlOthpfw3iKA0TUKY7qkoqm74aKMHPkoAQKBgQDsND7Ni/BNcoylcLYc8/k2oT99JyHgqFnonlu+x5EuJVgNy8wHHMQY6obt/i3+jB84ynr4aWa+r8HB2GgxI5RMU4OrQkwonq3CR5TQDvd3by4XsK7i3rGpj8t8VeOGngS84B6DsxJG2EaD9O+3ehuX2UrtKnXWScbOrMBLLoWGgQKBgQDcCM4n83thqpg+qACb7ZlW0oG0yQfir2F5E1kVvIbCpSA2R5G6fpGvLcp1l921Z51DwosqkToyKoxaqgmLe98yYRbKrV78mP8f5i5JL6G7B8JZQnhKaApp0v3TyYb+9Q+uaFNg0iZ9+fXs2hK3ML7jlYddG43KCmebkTywkUCJ8QKBgQCLYuFUxqnN6jUZRAQT3d1I4arnnfod6vrzjM+zK21+/8aQjZ0e7VjDI1Lwirh1qBacmtAqW3ioOmtqitwhc0+GIxpmK79pkQoUxc0JYmuibVeT3020z5mj6Bk6jCyyOfmtw8v+B+RXLjWelSII8ePqne3bOt1C0VKij8sCErf1AQKBgGuom0YM6feVgrxVc0bX8Ej84p3UJtmROj4Fa3cpMT6XkLU+O11mhF2UKBV1YvNZaSc4TuNa/4CANQA/ZkArMb/ypTYxseIh8eJnu4nvllBusXb/AOBXG5E/vRYc6sJgoXWScglTiaXl4aAr6zBa12t/RCYYTz3l7V++plU2HL/hAoGAF/x70gOE/GHIZEepzrhtuPGEyN570P+UOmXd6/soHa81MoeXJjiWr1OVEtRYbbRaSsMNQR8vcF1uTyyVNqf4UvqEuz3ejS/bqlllIKid3yM/AoNRS9zxCx3EGktfkZrsRwiw8D04hR14CL0tW2nerd6q0JSR0wtHcXXv4LXz/Xg=', '2021-03-02 10:09:10', '0', '2020-03-02 10:09:49', '2020-03-02 10:09:10', NULL);