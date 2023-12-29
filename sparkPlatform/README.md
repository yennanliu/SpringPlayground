# SparkPlatform
> Data platform for Apache Spark job management


## Steps


## Run

<details>
<summary>App</summary>

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
# Ref cmd
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


</details>

## API

| API | Type | Purpose | Example cmd | Comment|
| ----- | -------- | ---- | ----- | ---- |
| Test |  | | |



## Important Concepts

## Ref

- Course
    - Video
        - xxx
