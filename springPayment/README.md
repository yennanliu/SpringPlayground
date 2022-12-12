# SpringPayment

> Build `payment` service via spring boot

## Steps

- Step 1) Please install, run Consul, Redis first (docker)
- Step 2) Create DB, run all DDL (springPayment/sql/ddl)
- Step 3) run app (backend)

## Run

<details>
<summary>App</summary>

```bash
#---------------------------
# Install : Consul
#---------------------------

# https://github.com/yennanliu/SpringPlayground/tree/main/springSSOAuth
# book p.2-31, p.6-13
# Consul
# V1 (docker)
cd springSSOAuth
mkdir -p /tmp/consul/{conf,data}

docker run --name consel -p 8500:8500 -v /tmp/consul/conf/:/consul/conf/ -v /tmp/consul/data:/tmp/consul/data -d consul

docker ps -a

# access consul UI :
# http://localhost:8500/ui/dc1/services
```


```bash
#---------------------------
# Install : Redis
#---------------------------

# book p.6-16
# Redis
# V1 (docker)
cd springPayment
docker run -p 6379:6379 -v $PWD/data:/data -d redis:3.2 redis-server --appendonly yes

# check redis status
docker ps

# access via CLI
redis-cli -h 127.0.0.1 -p 6379
```


```bash
#---------------------------
# Run app
#---------------------------

# build
mvn package

# run
java -jar <built_jar>
```

</details>

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Swagger |  |  |  ||
| GET | GET | API page | http://localhost:9092/swagger-ui/index.html |swagger


| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Payment |  |  |  ||
| POST | POST | unified pay | http://localhost:9092/pay/unifiedPay | plz check below example cmd
| POST | POST | alipay receive | http://localhost:9092/notify/aliPayReceive?notify_time=string&notify_type=string&notify_id=string&charset=string&version=string&sign_type=string&sign=string&auth_app_id=string&trade_no=string&app_id=string&out_trade_no=string&out_biz_no=string&buyer_id=string&seller_id=string&trade_status=string&total_amount=string&receipt_amount=string&invoice_amount=string&buyer_pay_amount=string&point_amount=string&refund_fee=string&subject=string&body=string&gmt_create=string&gmt_payment=string&gmt_refund=string&gmt_close=string&fund_bill_list=string&voucher_detail_list=string&passback_params=string | plz check below example cmd

```bash

# "unified pay" POST request
curl -X 'POST' \
  'http://localhost:9092/pay/unifiedPay' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "appId": "10001",
  "orderId": "2020021417160000001",
  "tradeType": "topup",
  "channel": 1,
  "payType": "ALI_PAY_H5",
  "amount": 10,
  "currency": "CNY",
  "userId": "1002",
  "subject": "xiaomi 10 pro",
  "body": "xiaomi 10 pro",
  "extraInfo": {},
  "notifyUrl": "http://www.baidu.com",
  "returnUrl": "http://www.baidu.com"
}'

# "alipay receive" POST request
#  TODO : fix param (book 6-62)
curl -X 'POST' \
  'http://localhost:9092/notify/aliPayReceive?notify_time=string&notify_type=string&notify_id=string&charset=string&version=string&sign_type=string&sign=string&auth_app_id=string&trade_no=string&app_id=string&out_trade_no=string&out_biz_no=string&buyer_id=string&seller_id=string&trade_status=string&total_amount=string&receipt_amount=string&invoice_amount=string&buyer_pay_amount=string&point_amount=string&refund_fee=string&subject=string&body=string&gmt_create=string&gmt_payment=string&gmt_refund=string&gmt_close=string&fund_bill_list=string&voucher_detail_list=string&passback_params=string' \
  -H 'accept: */*' \
  -d ''
```

## Important Concepts

## Ref

- Course
    - code
        - [chapter06-payment](https://github.com/yennanliu/SpringPlayground/tree/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter06-payment)
    - Book
        - [最實用業界專案精選：用Spring Boot和Spring Cloud頂級開發](https://www.books.com.tw/products/0010923547)
            - ch.5
