# SparkPlatform
> Data platform for Apache Flink job management


## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| http://localhost:8081 | GET| Flink UI  | |
| http://localhost:8082/#/ | GET| Zeppelin UI  | |
| http://localhost:8082/next/#/| GET| Zeppelin new UI  | |
| http://localhost:9999/swagger-ui.html | GET| BE api swagger (Flink REST) | |
| http://localhost:8081/jobs/overview | GET| Get all jobs  | |
| http://localhost:8081/jobs/<job_id> | GET| Get a job detail | |
| http://localhost:8081/v1/jars/upload | POST | upload job jar| |
| http://localhost:8081/v1/jars | GET | get submitted job id |
| http://localhost:8081/v1/jars/<id>/run?entry-class=xxx&program-args=xxx| POST | trigger submitted job |
| http://localhost:8081/v1/<jar_id> | DELETE | delete submitted jar |
| http://localhost:8081/v1/<jar_id> | GET | cancel a submitted jar |



## Build


## Run

<details>
<summary>Run</summary>

```bash
#---------------------------
# Run app
#---------------------------

# build
mvn package

# run
java -jar <built_jar>
```

```bash

#---------------------------
# Spark Ref cmd
#---------------------------

# launch spark local master
cd /Users/yennanliu/spark-3.3.4-bin-hadoop3/sbin
bash start-master.sh
#bash start-all.sh
bash start-slave.sh

# stop spark local master
bash stop-master.sh
# bash stop-all.sh
bash stop-slave.sh


# UI : http://localhost:8080/


# run example job
cd /Users/yennanliu/spark-3.3.4-bin-hadoop3

bash bin/spark-submit \
  --class org.apache.spark.examples.SparkPi \
  --master spark://yennanliudeMacBook-Air.local:7077 \
  --deploy-mode cluster \
  --supervise \
  --executor-memory 20G \
  --total-executor-cores 100 \
  examples/jars/spark-examples_2.12-3.3.4.jar \
  1000
```


```bash

#---------------------------
# Flink Ref cmd
#---------------------------


# download flink
# https://nightlies.apache.org/flink/flink-docs-release-1.17/zh/docs/try-flink/local_installation/

cd flink-1.17.2


# start cluster
bash bin/start-cluster.sh

# Flink UI : http://localhost:8081/

# submit example job
bash bin/flink run examples/streaming/WordCount.jar

bash bin/flink run examples/streaming/TopSpeedWindowing.jar

# stop cluster
bash bin/stop-cluster.sh
```

```bash

#---------------------------
# Flink op cmd
#---------------------------

# curl upload a jar
# curl -X POST -H "Expect:" -F "@jarfile=/Users/yennanliu/flink-1.17.2/examples/streaming/StateMachineExample.jar" http://localhost:8081/jars/upload

# https://juejin.cn/s/flink%20rest%20api%20upload%20jar


#---------------------------------
# Flink REST API
#---------------------------------

# https://nightlies.apache.org/flink/flink-docs-release-1.18/zh/docs/ops/rest_api/
# https://nightlies.apache.org/flink/flink-docs-release-1.18/generated/rest_v1_dispatcher.yml

#---------------------------------
# Flink upload jar via API call
#---------------------------------

# How to upload multiple files at once using Curl?
# https://reqbin.com/req/c-dot4w5a2/curl-post-file
# curl [URL] -F file1=@filename1 -F file2=@filename2 -F file3=@filename3

curl -X POST -H "Expect:" -F "jarfile=@streaming/StateMachineExample.jar" http://localhost:8081/jars/upload

curl -X POST -H "Expect:" -F "jarfile=@table/StreamSQLExample.jar" http://localhost:8081/jars/upload

curl -X POST -F "jarfile=@table/StreamSQLExample.jar" http://localhost:8081/jars/upload


#---------------------------------
# Flink submit jar via API call
#---------------------------------

# submit job
# https://stackoverflow.com/questions/54348050/flink-rest-api-error-request-did-not-match-expected-format-jarrunrequestbody
curl -X POST -H 'Content-Type: application/json' --data '
{
  "programArgsList" : [
    "--input-job-name",
    "StreamSQLExample"
  ],
"parallelism": 30
}
' http://localhost:8081/jars/927a9fac-c7bf-48cd-b1b8-b4e536449eb0_StreamSQLExample.jar/run


curl -X POST -H 'Content-Type: application/json' http://localhost:8081/jars/927a9fac-c7bf-48cd-b1b8-b4e536449eb0_StreamSQLExample.jar/run


#---------------------------------
# Flink op via Swagger (FlinkRestService)
#---------------------------------

# upload jar param (POST)
{
  "jarFile": "/Users/yennanliu/flink-1.17.2/examples/table/StreamSQLExample.jar"
}

{
  "jarFile": "/Users/yennanliu/flink-1.17.2/examples/streaming/TopSpeedWindowing.jar"
}


# Submit flink job with jar id
{
  "allowNonRestoredState": true,
  "entryClass": "string",
  "jarId": "6219018d-42ed-4d68-bff7-bfc60b7d20c3_StreamSQLExample.jar",
  "parallelism": 1,
  "programArgs": "string",
  "savePointPath": "string"
}

{
  "allowNonRestoredState": true,
  "entryClass": "string",
  "jarId": "bead32dc-a0cd-49e0-b525-0d927375c4c7_StreamSQLExample.jar",
  "parallelism": 1,
  "programArgs": "string",
  "savePointPath": "string"
}

# stop job
# /jobs/:jobid/stop.

curl http://localhost:8081/jobs/6e80fe182c310a484bf7e9d4f25ac18d/cancel

```

```bash
#---------------------------------
# Flink SQL gateway
#---------------------------------


# https://nightlies.apache.org/flink/flink-docs-master/zh/docs/dev/table/sql-gateway/overview/

# start SQL gateway
bash bin/sql-gateway.sh start -Dsql-gateway.endpoint.rest.address=localhost

# stop SQL gateway
bash bin/sql-gateway.sh stop

#bash bin/sql-gateway.sh

curl http://localhost:8083/v1/info

curl --request POST http://localhost:8083/v1/sessions

# ➜  flink-1.17.2 curl --request POST http://localhost:8083/v1/sessions
# {"sessionHandle":"01def222-f1bd-427b-be19-56bc21a5728f"}%

sessionHandle="25c1a1b1-2c2e-4c70-be27-a60c152881d6"

url="http://localhost:8083/v1/sessions/${sessionHandle}/statements/"

curl --request POST $url --data '{"statement": "SELECT 1, 2, 3"}'

# curl --request POST $url --data '{"statement": "SELECT 1"}'
# {"operationHandle":"d256d8b1-f93e-4ee3-bb75-447be071cb5d"}%

operationHandle="d2ea0f50-54fe-4c22-b529-91b389c44304"

result_url="http://localhost:8083/v1/sessions/${sessionHandle}/operations/${operationHandle}/result/0"

curl --request GET $result_url

# curl --request GET $result_url
# {"resultType":"PAYLOAD","isQueryResult":true,"jobID":"d9a289ba044b6f0d07284cbdc6f7e63c","resultKind":"SUCCESS_WITH_CONTENT","results":{"columns":[{"name":"EXPR$0","logicalType":{"type":"INTEGER","nullable":false},"comment":null}],"rowFormat":"JSON","data":[{"kind":"INSERT","fields":[1]}]},"nextResultUri":"/v1/sessions/25c1a1b1-2c2e-4c70-be27-a60c152881d6/operations/7fa9b9c1-0a19-46c5-ae97-776a3e944bac/result/1"}%
```

```bash
#---------------------------------
# Apache Zeeplin
#---------------------------------

# https://medium.com/luckspark/setting-up-spark-2-0-1-and-zeppelin-0-6-2-on-macos-sierra-b163db9848f3#:~:text=to%20stop%20the%20zeppelin%2C%20simply,%2Ddaemon.sh%20stop%20command.

# start Zeeplin server
bash bin/zeppelin-daemon.sh start

# stop Zeeplin server
bash bin/zeppelin-daemon.sh stop
```

```bash
#---------------------------------
# Apache Zeeplin (Docker)
#---------------------------------

# https://zeppelin.apache.org/docs/0.10.1/quickstart/install.html

# install dokcer image

# docker run -p 8080:8080 --rm -v /Users/yennanliu/docker_file \
# -v /Users/yennanliu/docker_file/flink -e FLINK_HOME=/Users/yennanliu/flink-1.17.2 -e ZEPPELIN_NOTEBOOK_DIR='/notebook' --name zeppelin apache/zeppelin:0.10.0

# map 8080 port (in docker) to 8082 (external)
# docker run  -p 8082:8080 --rm -v /Users/yennanliu/flink-1.17.2:/opt/flink -e FLINK_HOME=/opt/flink  --name zeppelin apache/zeppelin:0.10.0

# pass zeppelin conf to docker env
cd SpringPlayground/SpringDataPlatform/backend

docker run  -p 8082:8080 --rm -v /Users/yennanliu/flink-1.17.2:/opt/flink --rm -v $(pwd)/zeppelin:/opt/zeppelin/conf:ro -e FLINK_HOME=/opt  --name zeppelin apache/zeppelin:0.10.0

docker run  -p 8082:8080 --rm -v /Users/yennanliu/flink-1.17.2:/opt/flink -e FLINK_HOME=/opt  --name zeppelin apache/zeppelin:0.10.0
```
</details>


## Important Concepts

## Ref

- Zeppelin client API
  - https://blog.csdn.net/weixin_44870914/article/details/124375498
  - https://www.laitimes.com/article/1pl86_1vmmq.html
  - https://zeppelin.apache.org/docs/latest/usage/zeppelin_sdk/client_api.htmls
  - https://developer.aliyun.com/search?spm=a2c6h.12873639.J_XmGx2FZCDAeIy2ZCWL7sW.i0.71aa3f894flC57&k=Apache%20Zeppelin%E7%B3%BB%E5%88%97%E6%95%99%E7%A8%8B&scene=community

- Zeppelin client session
  - https://zeppelin.apache.org/docs/latest/usage/zeppelin_sdk/session_api.html
