-- create table pms_attr
-- (
--    attr_id              bigint not null auto_increment comment '属性id',
--    attr_name            char(30) comment '属性名',
--    search_type          tinyint comment '是否需要检索[0-不需要，1-需要]',
--    icon                 varchar(255) comment '属性图标',
--    value_select         char(255) comment '可选值列表[用逗号分隔]',
--    attr_type            tinyint comment '属性类型[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]',
--    enable               bigint comment '启用状态[0 - 禁用，1 - 启用]',
--    catelog_id           bigint comment '所属分类',
--    show_desc            tinyint comment '快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整',
--    primary key (attr_id)
-- );


INSERT INTO pms_attr (attr_id, attr_name, search_type, enable, catelog_id)
VALUES
(1, 'macbook', 1, 1, 123),
(2, 'iphone', 1, 1, 123),
(3, 'mac', 1, 1, 123);



-- create table pms_sku_info
-- (
--    sku_id               bigint not null auto_increment comment 'skuId',
--    spu_id               bigint comment 'spuId',
--    sku_name             varchar(255) comment 'sku名称',
--    sku_desc             varchar(2000) comment 'sku介绍描述',
--    catalog_id           bigint comment '所属分类id',
--    brand_id             bigint comment '品牌id',
--    sku_default_img      varchar(255) comment '默认图片',
--    sku_title            varchar(255) comment '标题',
--    sku_subtitle         varchar(2000) comment '副标题',
--    price                decimal(18,4) comment '价格',
--    sale_count           bigint comment '销量',
--    primary key (sku_id)
-- );


INSERT INTO pms_sku_info (sku_id, spu_id, sku_name, brand_id, sku_title, price, sale_count)
VALUES
(1, 1, 'macbook', 1, 'macbook', 500, 100),
(2, 1, 'iphone', 2, 'macbook', 1000, 100),
(3, 1, 'mac', 3, 'macbook', 9000, 100);



-- create table pms_product_attr_value
-- (
--    id                   bigint not null auto_increment comment 'id',
--    spu_id               bigint comment '商品id',
--    attr_id              bigint comment '属性id',
--    attr_name            varchar(200) comment '属性名',
--    attr_value           varchar(200) comment '属性值',
--    attr_sort            int comment '顺序',
--    quick_show           tinyint comment '快速展示【是否展示在介绍上；0-否 1-是】',
--    primary key (id)
-- );


INSERT INTO pms_product_attr_value (id, spu_id, attr_id, attr_name)
VALUES
(1, 1, 1, 'size'),
(2, 1, 2, 'length'),
(3, 1, 3, 'price');


-- create table pms_category
-- (
--    cat_id               bigint not null auto_increment comment '分类id',
--    name                 char(50) comment '分类名称',
--    parent_cid           bigint comment '父分类id',
--    cat_level            int comment '层级',
--    show_status          tinyint comment '是否显示[0-不显示，1显示]',
--    sort                 int comment '排序',
--    icon                 char(255) comment '图标地址',
--    product_unit         char(50) comment '计量单位',
--    product_count        int comment '商品数量',
--    primary key (cat_id)
-- );


-- INSERT INTO pms_category (cat_id, name, show_status, product_unit, product_count)
-- VALUES
-- (1, 'category_1', 1, 'count', 100),
-- (2, 'category_2', 1, 'count', 100),
-- (3, 'category_3', 1, 'count', 100);
