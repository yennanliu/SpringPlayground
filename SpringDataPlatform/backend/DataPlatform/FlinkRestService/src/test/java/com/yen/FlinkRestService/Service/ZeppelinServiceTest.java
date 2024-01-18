package com.yen.FlinkRestService.Service;

import org.apache.commons.lang3.StringUtils;
import org.apache.zeppelin.client.ClientConfig;
import org.apache.zeppelin.client.ExecuteResult;
import org.apache.zeppelin.client.ZSession;
import org.apache.zeppelin.client.ZeppelinClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ZeppelinServiceTest {

//    @Autowired
//    private ZeppelinClient zeppelinClient;

    private ZSession session = null;

    private final String ZEPPELIN_URL = "http://localhost:8082";

    /**
     * Zeppelin flink test
     * <p>
     * https://github.com/apache/zeppelin/blob/master/zeppelin-client-examples/src/main/java/org/apache/zeppelin/client/examples/FlinkExample.java
     */
    @Test
    public void zSessionFlinkTest() {
        try {

            ClientConfig clientConfig = new ClientConfig(ZEPPELIN_URL);
            Map<String, String> intpProperties = new HashMap<>();

            session = ZSession.builder()
                    .setClientConfig(clientConfig)
                    .setInterpreter("flink")
                    .setIntpProperties(intpProperties)
                    .build();

            session.start();
            System.out.println(">>>>>>>> Flink Web UI: " + session.getWeburl());

            // scala (single result)
            ExecuteResult result = session.execute("benv.fromElements(1,2,3).print()");
            System.out.println("Result: " + result.getResults().get(0).getData());

            // scala (multiple result)
            result = session.execute("val data = benv.fromElements(1,2,3).map(e=>(e, e * 2))\n" +
                    "data.print()\n" +
                    "z.show(data)");

            // The first result is text output
            System.out.println("Result 1: type: " + result.getResults().get(0).getType() +
                    ", data: " + result.getResults().get(0).getData() );
            // The second result is table output
            System.out.println("Result 2: type: " + result.getResults().get(1).getType() +
                    ", data: " + result.getResults().get(1).getData() );
            System.out.println("Flink Job Urls:\n" + StringUtils.join(result.getJobUrls(), "\n"));


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                try {
                    session.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Zeppelin spark test
     * <p>
     * https://zeppelin.apache.org/docs/latest/usage/zeppelin_sdk/session_api.html
     */
    @Test
    public void zSessionSparkTest() {

        try {
            ClientConfig clientConfig = new ClientConfig(ZEPPELIN_URL);
            Map<String, String> intpProperties = new HashMap<>();
            intpProperties.put("spark.master", "local[*]");

            session = ZSession.builder()
                    .setClientConfig(clientConfig)
                    .setInterpreter("spark")
                    .setIntpProperties(intpProperties)
                    .build();

            session.start();
            System.out.println("Spark Web UI: " + session.getWeburl());

            // scala (single result)
            ExecuteResult result = session.execute("println(sc.version)");
            System.out.println("Spark Version: " + result.getResults().get(0).getData());

            // scala (multiple result)
            result = session.execute("println(sc.version)\n" +
                    "val df = spark.createDataFrame(Seq((1,\"a\"), (2,\"b\")))\n" +
                    "z.show(df)");

            // The first result is text output
            System.out.println("Result 1: type: " + result.getResults().get(0).getType() +
                    ", data: " + result.getResults().get(0).getData());
            // The second result is table output
            System.out.println("Result 2: type: " + result.getResults().get(1).getType() +
                    ", data: " + result.getResults().get(1).getData());
            System.out.println("Spark Job Urls:\n" + StringUtils.join(result.getJobUrls(), "\n"));

            // error output
            result = session.execute("1/0");
            System.out.println("Result status: " + result.getStatus() +
                    ", data: " + result.getResults().get(0).getData());

            // pyspark
            result = session.execute("pyspark", "df = spark.createDataFrame([(1,'a'),(2,'b')])\n" +
                    "df.registerTempTable('df')\n" +
                    "df.show()");
            System.out.println("PySpark dataframe: " + result.getResults().get(0).getData());

            // matplotlib
            result = session.execute("ipyspark", "%matplotlib inline\n" +
                    "import matplotlib.pyplot as plt\n" +
                    "plt.plot([1,2,3,4])\n" +
                    "plt.ylabel('some numbers')\n" +
                    "plt.show()");
            System.out.println("Matplotlib result, type: " + result.getResults().get(0).getType() +
                    ", data: " + result.getResults().get(0).getData());

            // sparkr
            result = session.execute("r", "df <- as.DataFrame(faithful)\nhead(df)");
            System.out.println("Sparkr dataframe: " + result.getResults().get(0).getData());

            // spark sql
            result = session.execute("sql", "select * from df");
            System.out.println("Spark Sql dataframe: " + result.getResults().get(0).getData());

            // spark invalid sql
            result = session.execute("sql", "select * from unknown_table");
            System.out.println("Result status: " + result.getStatus() +
                    ", data: " + result.getResults().get(0).getData());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                try {
                    session.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void myTest() {
        System.out.println(123);
    }

}