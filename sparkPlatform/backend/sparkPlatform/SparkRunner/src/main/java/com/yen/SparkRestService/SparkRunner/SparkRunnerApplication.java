package com.yen.SparkRestService.SparkRunner;

import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SparkRunnerApplication {
    public static void main(String[] args) throws IOException, InterruptedException {


        // https://spark.apache.org/docs/1.6.1/api/java/org/apache/spark/launcher/SparkLauncher.html
        // https://stackoverflow.com/questions/31754328/spark-launcher-waiting-for-job-completion-infinitely
        // launch local standalone spark master via SparkLauncher
        System.out.println("SparkRunnerApplication start...");

        Map<String, Object> envParam = new HashMap<>();

        // manually install spark 3.3.4 via https://spark.apache.org/downloads.html
        String SPARK_HOME = "/Users/yennanliu/spark-3.3.4-bin-hadoop3";

        SparkAppHandle handler = new SparkLauncher()
                .setSparkHome(SPARK_HOME)
                .setAppResource(SPARK_HOME + "/jars")
                .setMainClass("com.yen.SparkRestService.SparkRunner")
                .setMaster("localhost")
                .setConf("spark.app.id", "AppID if you have one")
                .setConf("spark.driver.memory", "8g")
                .setConf("spark.akka.frameSize", "200")
                .setConf("spark.executor.memory", "2g")
                .setConf("spark.executor.instances", "32")
                .setConf("spark.executor.cores", "32")
                .setConf("spark.default.parallelism", "100")
                .setConf("spark.driver.allowMultipleContexts", "true")
                .setVerbose(true).startApplication();

        System.out.println("sparkLauncher start...");
        //handler.wait();
        //sparkLauncher.launch();
        Thread.sleep(1000000);
        System.out.println("sparkLauncher end...");
    }

}