-- DDL for oauth_client_details table
-- DB : sso (mysql)

-- https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter03-sso-authserver/src/main/resources/db.migration/V1__init.sql

DROP table oauth_client_details;

CREATE TABLE IF NOT EXISTS oauth_client_details (
    client_id VARCHAR(256) PRIMARY KEY,
    resource_ids VARCHAR(256),
    client_secret VARCHAR(256),
    scope VARCHAR(256),
    authorized_grant_types VARCHAR(256),
    web_server_redirect_uri VARCHAR(256),
    authorities VARCHAR(256),
    access_token_validity integer,
    refresh_token_validity integer,
    additional_information VARCHAR(4096),
    autoapprove VARCHAR(256)
);

-- insert data
-- book p.3-44
#生成供资源服务器使用的受信任Client配置
insert into oauth_client_details(`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) 
    values ('resourceclient', null, '{noop}123456', 'all,read,write', 'authorization_code,refresh_token,password', 'http://www.baidu.com', 'role_trusted_client', 7200, 7200, null, 'true');

#生成接入方测试Client配置信息
insert intO oauth_client_details(`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) 
    VALUES ('accessDemo', NULL, '{noop}123456', 'all,read,write', 'authorization_code,refresh_token,password', 'http://www.baidu.com', 'ROLE_TRUSTED_CLIENT', 7200, 7200, NULL, 'true');
