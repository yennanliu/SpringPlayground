--地理围栏图层信息表，用于约定围栏的用途、类型等管理逻辑
create sequence fence_geo_layer_id_seq;
create table fence_geo_layer
(
    id integer not null default nextval('fence_geo_layer_id_seq'::regclass),
    code character varying(16) collate pg_catalog."default" not null default ''::character varying,
    name character varying(32) collate pg_catalog."default" not null default ''::character varying,
    explain character varying(100) collate pg_catalog."default" not null default ''::character varying,
    check_city boolean not null default false,
    city_code character varying(16) collate pg_catalog."default",
    state smallint not null default 0,
    type smallint  not null default 0,
    create_time timestamp with time zone not null default now(),
    update_time timestamp with time zone not null default now(),
    create_user character varying(32) collate pg_catalog."default" not null default ''::character varying,
    update_user character varying(32) collate pg_catalog."default" not null default ''::character varying,
    constraint fence_geo_layer_pkey primary key (id)
)
with (
    oids = false
);
comment on table fence_geo_layer is '地理图围栏层定义表';
comment on column fence_geo_layer.code is '地理图层code码';
comment on column fence_geo_layer.name is '地理图层名称';
comment on column fence_geo_layer.explain is '地理图层说明';
comment on column fence_geo_layer.check_city is '是否检查城市，配合city_code';
comment on column fence_geo_layer.city_code is '开通城市列表';
comment on column fence_geo_layer.state is '0：默认有效，1：删除';
comment on column fence_geo_layer.type is '0:未知分类，1:干预、2:调度、3:停车围栏、4:运营范围、5-技术';
comment on column fence_geo_layer.create_time is '创建时间';
comment on column fence_geo_layer.update_time is '修改时间';
comment on column fence_geo_layer.create_user is '创建用户';
comment on column fence_geo_layer.update_user is '修改用户';


--创建电子地理围栏核心信息表相关
create sequence fence_geo_id_seq;
 
create table fence_geo_info
(
    id bigint not null default nextval('fence_geo_id_seq'::regclass),
    name character varying(254) collate pg_catalog."default" not null,
    explain character varying(200) collate pg_catalog."default" default ''::character varying,
    city_code character varying(16) collate pg_catalog."default" not null default ''::character varying,
    ad_code character varying(16) collate pg_catalog."default",
    layer_code character varying(16) collate pg_catalog."default" not null,
    region geometry(geometry,4326) not null,
    centre geometry(point,4326),
    area numeric(16,2),
    custom_info jsonb,
    batch_id bigint,
    from_id bigint,
    geo_json text collate pg_catalog."default",
    geo_hash character varying(16)[] collate pg_catalog."default",
    date_range tstzrange,
    time_bucket int4range[],
    state smallint,
    update_time timestamp with time zone,
    create_time timestamp with time zone,
    update_user character varying(32) collate pg_catalog."default" default ''::character varying,
    create_user character varying(32) collate pg_catalog."default" default ''::character varying,
    constraint fence_geo_pkey primary key (id)
)
with (
    oids = false
);
--添加字段备注
comment on table fence_geo_info is '电子地理围栏信息表';

comment on column fence_geo_info.id is '围栏id';

comment on column fence_geo_info.name is '围栏名称';

comment on column fence_geo_info.explain is '围栏描述';

comment on column fence_geo_info.city_code is '管理归属city_code';

comment on column fence_geo_info.ad_code is '管理归属分区编码';

comment on column fence_geo_info.layer_code is '地理图层code';

comment on column fence_geo_info.region is '围栏描述geometry';

comment on column fence_geo_info.centre is '围栏中心点';

comment on column fence_geo_info.area is '围栏面积（单位：平方米）';

comment on column fence_geo_info.custom_info is '自定义字段数据';

comment on column fence_geo_info.batch_id is '批量导入，同一批次标记';

comment on column fence_geo_info.from_id is '来源围栏id';

comment on column fence_geo_info.geo_json is '冗余围栏的geojson';

comment on column fence_geo_info.geo_hash is '围栏覆盖的geohash列表';

comment on column fence_geo_info.date_range is '有效期（开始时间、结束时间）';

comment on column fence_geo_info.time_bucket is '一天内的有效时间段(分钟表示){[360,480),[600,840]}';

comment on column fence_geo_info.state is '0：生效／1：已删除 ／2：失效';

--添加索引
create index idx_fence_geo_centre on fence_geo_info using gist(centre);

comment on index idx_fence_geo_centre is '地理围栏中心点索引';

create index idx_fence_geo_city_code on fence_geo_info using btree(city_code collate pg_catalog."default");

comment on index idx_fence_geo_city_code is '地理围栏表citycode索引';

create index idx_fence_geo_region on fence_geo_info using gist(region);

comment on index idx_fence_geo_region is '地理围栏地理范围索引';