create table user_info
(
   id                   bigint not null auto_increment,
   user_id              varchar(10) comment '用户id',
   nick_name            varchar(30) comment '用户昵称',
   mobile_no            varchar(11) comment '用户注册手机号',
   password             varchar(64) comment '登录密码',
   is_login             int comment '是否登录，0：未登录；1：已登录',
   login_time           timestamp default current_timestamp comment '最近登录时间',
   is_del               int comment '是否注销，0-未注销；1-已注销',
   create_time          timestamp default current_timestamp comment '创建时间',
   primary key (id)
);
alter table user_info comment '用户信息表';
create index idx_ui_user_id on user_info(user_id);
create index idx_ui_mobile_no on user_info(mobile_no);

create table user_sms_code
(
   id                   bigint not null auto_increment comment 'id',
   mobile_no            varchar(11) comment '用户注册手机号',
   sms_code             varchar(10) comment '短信验证码',
   send_time            timestamp default current_timestamp comment '短信发送信息',
   create_time          timestamp default current_timestamp comment '创建时间',
   primary key (id)
);
alter table user_sms_code comment '短信验证码表';
create index idx_usc_mobile_no on user_sms_code(mobile_no);
