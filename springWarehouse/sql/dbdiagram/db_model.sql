// Use DBML to define your database structure
// Docs: https://dbml.dbdiagram.io/docs

// https://dbdiagram.io/d

Table product {
    id integer [primary key]
    name varchar
    type_id integer
    price integer
    merchant_id varchar
    product_status varchar
    amount integer
}

Table product_type {
    id integer [primary key]
    name varchar
}


Table merchant {
    id integer [primary key]
    name varchar
    city varchar
    type varchar
    status varchar
}



Ref: product.id > product_type.id // many-to-one
Ref: product.merchant_id > merchant.id // many-to-one