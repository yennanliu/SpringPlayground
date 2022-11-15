# ddl sql for init table
# db : wallet
# 
# https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter05-wallet/src/main/resources/db.migration/V1_init.sql

create database wallet;
	
#余额订单信息表，用于余额充值、退款等涉及交易支付逻辑的处理
create table user_balance_order
(
  id          bigint not null primary key auto_increment,
  order_id    varchar(50) comment '订单号id',
  user_id     varchar(60) comment '用户id',
  amount      bigint comment '交易金额',
  trade_type  varchar(20) comment '交易类型，charge-余额充值；refund-余额退款',
  currency    varchar(10) comment '币种',
  trade_no    varchar(32) comment '支付渠道流水号',
  status      varchar(2) comment '支付状态，0-待支付；1-支付中；2-支付成功；3-支付失败',
  is_renew    int default 0 comment '是否自动续费充值，0-不自动续费；1-自动续费',
  trade_time  timestamp default current_timestamp comment '交易时间',
  update_time timestamp null default current_timestamp on update current_timestamp comment '最后一次更新时间',
  create_time timestamp null default current_timestamp comment '创建时间'
);

alter table user_balance_order comment '电子钱包余额订单表';
#添加索引信息
alter table user_balance_order add index unique_idx_order_id(order_id);
alter table user_balance_order add index idx_user_id(user_id);
alter table user_balance_order add index idx_trade_time(trade_time);

#用户余额信息表
create table user_balance
(
  id          bigint not null primary key auto_increment,
  user_id     varchar(60) comment '用户id',
  acc_no      varchar(60) comment '余额系统生成的账户唯一标示',
  acc_type    varchar(2) comment '余额账户类型，0-现金；1-赠送金',
  currency    varchar(10) comment '账户币种，如usd美元账户，与用户所属国家绑定',
  balance     bigint comment '账户余额，以分为单位',
  update_time timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次更新时间',
  create_time timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

alter table user_balance comment '用户个人余额账户信息表';
#添加相应索引
alter table user_balance add index idx_ub_user_id(user_id);
alter table user_balance add index idx_ub_acc_no(acc_no);

#用户余额账户流水信息表，用于记录用户余额变动流水
create table user_balance_flow
(
  id            bigint not null primary key auto_increment,
  user_id       varchar(60) comment '业务方用户id',
  flow_no       varchar(64) comment '账户流水号,与业务方发起的流水号映射',
  acc_no        varchar(60) comment '账户唯一标示',
  busi_type     varchar(10) comment '余额流水业务类型,0-订单结费；1-购买月卡',
  amount        bigint comment '变动金额，以分为单位，区分正负；如：+10，-10',
  currency      varchar(10) comment '币种',
  begin_balance bigint comment '变动前余额',
  end_balance   bigint comment '变动后余额',
  fund_direct   varchar(2) comment '借贷方向，00-借方；01-贷方',
  update_time timestamp null default current_timestamp on update current_timestamp comment '最后一次更新时间',
  create_time timestamp null default current_timestamp comment '创建时间'
);

alter table user_balance_flow comment '余额变动流水信息表';

#创建相关索引信息
alter table user_balance_flow add index idx_ubf_user_id(user_id);
alter table user_balance_flow add index idx_ubf_acc_no(acc_no);
alter table user_balance_flow add index idx_ubf_flow_no(flow_no);
alter table user_balance_flow add index idx_ubf_create_time(create_time);