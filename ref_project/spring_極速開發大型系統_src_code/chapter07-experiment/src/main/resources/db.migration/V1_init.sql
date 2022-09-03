#Ab实验信息表
create table abtest_exp_info (
  id int(11) not null auto_increment comment '实验id',
  name varchar(255) collate utf8_bin not null comment '实验名称',
  factor_tag varchar(50) collate utf8_bin not null comment '系统对应的业务标签',
  layer_id int(11) not null comment '分层id',
  group_field_id int(11) not null comment '分组字段(参数)类型，1 用户，2 地理位置',
  exp_hyp varchar(255) collate utf8_bin default null comment '实验假设',
  result_expect varchar(255) collate utf8_bin default null comment '实验预期',
  metric_ids varchar(255) collate utf8_bin not null comment '指标ids',
  start_time datetime default null comment '开始时间',
  end_time datetime default null comment '结束时间',
  status int(4) not null default '0' comment '实验状态, 0 新建, 1 已发布, 2 生效中, 3 已暂停, 4 已终止, 5 已结束',
  online tinyint(2) not null default '0' comment '是否已上线，0 未上线，1 已上线',
  partition_type tinyint(4) not null default '0' comment '分区类型',
  is_sampling tinyint(2) default '0' comment '是否抽样打点 0:否 1:是',
  sampling_ratio int(4) default '5' comment '抽样率 n%',
  config varchar(1024) collate utf8_bin default null comment '实验配置',
  service_name varchar(50) collate utf8_bin default 'all' comment '系统名称，逗号分隔，区分使用端',
  owner varchar(50) collate utf8_bin not null comment '负责人',
  is_delete tinyint(2) not null comment '是否已删除，0 未删除 1 已删除',
  ext varchar(1000) collate utf8_bin not null default '{}' comment '扩展字段json',
  create_time timestamp not null default current_timestamp comment '记录创建时间，默认当前时间',
  update_time timestamp not null default current_timestamp on update current_timestamp comment '记录更新时间，默认当前时间',
  primary key(id)
);

alter table abtest_exp_info comment '实验信息表';
#索引信息
alter table abtest_exp_info add index idx_factor_tag(factor_tag);
alter table abtest_exp_info add index idx_layer(layer_id);
alter table abtest_exp_info add index idx_partition_type_deleted_status(partition_type,is_delete,status);

#实验分层表
create table abtest_layer (
  id int(11) unsigned not null auto_increment comment '分层id',
  `name` varchar(255) collate utf8_bin not null comment '名称',
  `desc` varchar(255) collate utf8_bin not null default '' comment '分层描述',
  group_field_id int(11) not null comment '分组字段(参数)类型，1 用户，2 地理位置',
  bucket_total_num int(11) not null comment '当前层的分桶总数',
  unused_bucket_nos text collate utf8_bin not null comment '未使用的分桶编号列表',
  partition_type tinyint(4) not null default '0' comment '分区类型',
  update_time datetime not null default current_timestamp on update current_timestamp comment '更新时间',
  create_time datetime not null default current_timestamp comment '创建时间',
  is_delete tinyint(2) not null comment '是否已删除，0 未删除 1 已删除',
  primary key (id)
);
alter table abtest_layer comment '实验分层表';

#索引信息
alter table abtest_layer add index idx_partition_type_group_field(`partition_type`,`group_field_id`);

#分组表
create table abtest_group (
  id int(11) not null auto_increment comment '分组id',
  group_type tinyint(4) default null comment '分组类别；0-实验组，1-对照组',
  flow_ratio int(11) not null default '0' comment '分流后, 分组流量占比',
  exp_id int(11) default null comment '实验id；默认对照组=-group_field_id',
  name varchar(50) collate utf8_bin default '' comment '分组名称',
  group_partition_type int(11) default null comment '分组类型，0-区间分组，1-单双号分组',
  group_partition_details text collate utf8_bin comment '分流内包含的编号列表，用逗号分隔，如：00,09',
  strategy_detail varchar(1000) collate utf8_bin default null comment '策略对应json',
  online tinyint(2) not null default '0' comment '是否已上线，0 未上线，1 已上线',
  create_time timestamp not null default current_timestamp comment '记录创建时间，默认当前时间',
  update_time timestamp not null default current_timestamp on update current_timestamp comment '记录更新时间，默认当前时间',
  dilution_ratio int(11) not null default '1' comment '稀释倍率',
  white_list text collate utf8_bin comment '白名单',
  primary key (id)
);
alter table abtest_group comment '分组表';

#索引信息
alter table abtest_group add index idx_expid_online(exp_id,online);

#实验指标表
create table abtest_metric (
   id int(11) not null auto_increment comment '指标id',
   `name` varchar(100) collate utf8_bin default null comment '中文名',
   name_en varchar(100) collate utf8_bin default null comment '英文名',
   formula varchar(255) collate utf8_bin default null comment '指标计算公式',
   group_field_id int(11) not null comment '分组因子id; 0-所有分组因子都具有的指标',
   `desc` varchar(255) collate utf8_bin not null comment '指标描述',
   `status` tinyint(2) not null default '1' comment '是否可用；0-否，1-是',
   create_time datetime not null default current_timestamp comment '创建时间',
   update_time datetime not null default current_timestamp on update current_timestamp comment '更新时间',
   primary key (id)
);
alter table abtest_metric comment '实验指标表';

#索引信息
alter table abtest_metric add index idx_groupfieldid(group_field_id);
