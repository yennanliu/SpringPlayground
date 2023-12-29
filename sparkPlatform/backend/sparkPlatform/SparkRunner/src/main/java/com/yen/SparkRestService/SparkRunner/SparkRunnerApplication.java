package com.yen.SparkRestService.SparkRunner;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SparkRunnerApplication {
    public static void main(String[] args) throws IOException, InterruptedException {

        final boolean waitForCompletion = true;

        // https://spark.apache.org/docs/1.6.1/api/java/org/apache/spark/launcher/SparkLauncher.html
        // https://stackoverflow.com/questions/31754328/spark-launcher-waiting-for-job-completion-infinitely
        // src code ref : https://github.com/apache/spark/blob/v1.6.0/launcher/src/main/java/org/apache/spark/launcher/LauncherServer.java
        // launch local standalone spark master via SparkLauncher
        System.out.println("SparkRunnerApplication start...");

        Map<String, Object> envParam = new HashMap<>();

        // manually install spark 3.3.4 via https://spark.apache.org/downloads.html
        String SPARK_HOME = "/Users/yennanliu/spark-3.3.4-bin-hadoop3";

        SparkLauncher sparkLauncher = new SparkLauncher()
                .setSparkHome(SPARK_HOME)
                .setAppResource(SPARK_HOME + "/examples/jars" + "spark-examples_2.12-3.3.4.jar")
                .setDeployMode("cluster")
                .setMainClass("org.apache.spark.examples.SparkPi")
                .setMaster("local[*]")
                //.setMaster("spark://master:7077")
                .setConf("spark.app.id", "AppID if you have one")
                .setConf("spark.driver.memory", "8g")
                .setConf("spark.akka.frameSize", "200")
                .setConf("spark.executor.memory", "2g")
                .setConf("spark.executor.instances", "32")
                .setConf("spark.executor.cores", "32")
                .setConf("spark.default.parallelism", "100")
                .setConf("spark.driver.allowMultipleContexts", "true")
                .setVerbose(true);

        SparkAppHandle handle = sparkLauncher.startApplication();

        log.info("Started application; handle=%s", handle);

        // Poll until application gets submitted

        while (handle.getAppId() == null) {
            log.info("Waiting for application to be submitted: status = ", handle.getState());
            Thread.sleep(1500L);
        }
        log.info("Submitted as {}", handle.getAppId());

        if (waitForCompletion) {
            while (!handle.getState().isFinal()) {
                log.info("%s: status=%s", handle.getAppId(), handle.getState());
                Thread.sleep(1500L);
            }
            log.info("Finished as %s", handle.getState());
        } else {
            handle.disconnect();
        }

//        System.out.println("sparkLauncher start...");
//        //handler.wait();
//        //sparkLauncher.launch();
//        //Thread.sleep(1000000);
//        System.out.println("sparkLauncher end...");
    }

}