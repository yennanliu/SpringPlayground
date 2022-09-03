create table test_info
(
   id                   bigint not null auto_increment,
   name            varchar(11) comment '测试名称',
   create_time          timestamp default current_timestamp comment '创建时间',
   update_time          timestamp default current_timestamp comment '更新时间',
   primary key (id)
);
alter table test_info comment '测试信息表';