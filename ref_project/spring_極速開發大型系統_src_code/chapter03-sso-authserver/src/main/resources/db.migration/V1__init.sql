#授权客户端基本信息表
create table oauth_client_details (
  client_id varchar(256) primary key comment '用于标识客户端，类似于appKey',
  resource_ids varchar(256) comment '客户端能访问的资源ID集合，以逗号分割，例如order-resource,pay-resource',
  client_secret varchar(256) comment '客户端访问密钥类似于appSecret,必须要有前缀代表加密方式，例如：{bcrypt}10gY/Hauph1tqvVWiH4atxteSH8sRX03IDXRIQi03DVTFGzKfz8ZtGi',
  scope varchar(256) comment '用于指定客户端的权限范围，如读写权限、移动端或web端等,例如：read,write/web,mobile',
  authorized_grant_types varchar(256) comment '可选值，如授权码模式:authorization_code；密码模式:password；刷新token:refresh_token；隐式模式:implicit；客户端模式:client_credentials，支持多种方式以逗号分隔，例如：password,refresh_token',
  web_server_redirect_uri varchar(256) comment '客户端重定向URL，authorization_code和implicit模式时需要该值进行校验，例如:http://baidu.com',
  authorities varchar(256) comment '可为空，指定用户的权限范围，如果授权过程需要用户登录，该字段不生效，implicit和client_credentials模式时需要；例如：ROLE_ADMIN,ROLE_USER',
  access_token_validity integer comment '可为空，设置access_token的有效时间(秒)，默认为12小时，例如：3600',
  refresh_token_validity integer comment '可为空，设置refresh_token有效期(秒），默认30天，例如7200',
  additional_information varchar(4096) comment '可为空，值必须是JSON格式，例如{"key", "value"}',
  autoapprove varchar(256) comment '默认false,适用于authorization_code模式,设置用户是否自动approval操作,设置true跳过用户确认授权操作页面，直接跳到redirect_uri'
);

#客户端Token信息表
create table oauth_client_token (
  token_id varchar(256) comment '从服务器端获取到的access_token的值',
  token blob comment '这是一个二进制的字段, 存储的数据是OAuth2AccessToken.java对象序列化后的二进制数据',
  authentication_id varchar(256) primary key comment '该字段具有唯一性, 是根据当前的username(如果有),client_id与scope通过MD5加密生成的(参考->DefaultClientKeyGenerator.java类)',
  user_name varchar(256) comment '登录时的用户名',
  client_id varchar(256) comment '客户端ID'
);

#授权访问Token信息表
create table oauth_access_token (
  token_id varchar(256) comment '该字段的值是将access_token的值通过MD5加密后存储的',
  token blob comment '存储将OAuth2AccessToken.java对象序列化后的二进制数据, 是真实的AccessToken的数据值',
  authentication_id varchar(256) primary key comment '该字段具有唯一性, 是根据当前的username(如果有),client_id与scope通过MD5加密生成的(参考->DefaultClientKeyGenerator.java类)',
  user_name varchar(256) comment '登录时的用户名,若客户端没有用户名(如grant_type="client_credentials"),则该值等于client_id',
  client_id varchar(256) comment '客户端ID',
  authentication blob comment '存储将OAuth2Authentication.java对象序列化后的二进制数据',
  refresh_token varchar(256) comment '该字段的值是将refresh_token的值通过MD5加密后存储的'
);

#授权刷新Token信息表
create table oauth_refresh_token (
  token_id varchar(256) comment '该字段的值是将refresh_token的值通过MD5加密后存储的',
  token blob comment '存储将OAuth2RefreshToken.java对象序列化后的二进制数据',
  authentication binary comment '存储将OAuth2Authentication.java对象序列化后的二进制数据'
);

#授权码信息表
create table oauth_code (
  code varchar(256) comment '存储服务端系统生成的code的值(未加密)',
  authentication blob comment '存储将AuthorizationRequestHolder.java对象序列化后的二进制数据'
);

#用户授权历史表
create table oauth_approvals (
  userId varchar(256) comment '授权用户ID',
  clientId varchar(256) comment '客户端ID',
  scope varchar(256) comment '授权访问',
  status varchar(10) comment '授权状态，例如：APPROVED',
  expiresAt timestamp comment '授权失效时间',
  lastModifiedAt timestamp default current_timestamp comment '最后修改时间'
);

#生成供资源服务器使用的受信任Client配置
insert into `auth`.`oauth_client_details`(`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) values ('resourceclient', null, '{noop}123456', 'all,read,write', 'authorization_code,refresh_token,password', 'http://www.baidu.com', 'role_trusted_client', 7200, 7200, null, 'true');

#生成接入方测试Client配置信息
insert intO `auth`.`oauth_client_details`(`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) VALUES ('accessDemo', NULL, '{noop}123456', 'all,read,write', 'authorization_code,refresh_token,password', 'http://www.baidu.com', 'ROLE_TRUSTED_CLIENT', 7200, 7200, NULL, 'true');