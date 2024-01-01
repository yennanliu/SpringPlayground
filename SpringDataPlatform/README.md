# SparkPlatform
> Data platform for Apache Spark job management


## Steps


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


# curl upload a jar
# curl -X POST -H "Expect:" -F "@jarfile=/Users/yennanliu/flink-1.17.2/examples/streaming/StateMachineExample.jar" http://localhost:8081/jars/upload

# https://juejin.cn/s/flink%20rest%20api%20upload%20jar

cd examples

# How to upload multiple files at once using Curl?
# https://reqbin.com/req/c-dot4w5a2/curl-post-file
# curl [URL] -F file1=@filename1 -F file2=@filename2 -F file3=@filename3

curl -X POST -H "Expect:" -F "jarfile=@streaming/StateMachineExample.jar" http://localhost:8081/jars/upload

curl -X POST -H "Expect:" -F "jarfile=@table/StreamSQLExample.jar" http://localhost:8081/jars/upload

curl -X POST -F "jarfile=@table/StreamSQLExample.jar" http://localhost:8081/jars/upload
```
</details>

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| http://localhost:8081 | GET| Flink UI  | |
| http://localhost:8081/jobs/overview | GET| Get all jobs  | |
| http://localhost:8081/jobs/<job_id> | GET| Get a job detail | |
| http://localhost:8081/v1/jars/upload | POST | upload job jar| |
| http://localhost:8081/v1/jars | GET | get submitted job id |
| http://localhost:8081/v1/jars/<id>/run?entry-class=xxx&program-args=xxx| POST | trigger submitted job |
| http://localhost:8081/v1/<jar_id> | DELETE | delete submitted jar |
| http://localhost:8081/v1/<jar_id> | GET | cancel a submitted jar |

## Important Concepts

## Ref

- Course
    - Video
        - xxx
