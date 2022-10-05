package com.yen.dev;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

// https://www.baeldung.com/java-http-request

public class httpRequestTest1 {

    public class ParameterStringBuilder {
        public String getParamsString(Map<String, String> params)
                throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                result.append("&");
            }

            String resultString = result.toString();
            return resultString.length() > 0
                    ? resultString.substring(0, resultString.length() - 1)
                    : resultString;
        }
    }

    @Test
    public void test1() throws IOException {

        // Creating a Request
        URL url = new URL("http://example.com");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // Adding Request Parameters
        Map<String, String> parameters = new HashMap<>();
        parameters.put("param1", "val");

        // Setting Request Headers
        con.setRequestProperty("Content-Type", "application/json");
        String contentType = con.getHeaderField("Content-Type");

        // Configuring Timeouts (optional)
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        // Handling Cookies
        // dev

        // Handling Redirects
        // dev

        // run
        ParameterStringBuilder parameterStringBuilder = new ParameterStringBuilder();

        //con.setDoOutput(true);
//        DataOutputStream out = new DataOutputStream(con.getOutputStream());
//        out.writeBytes(parameterStringBuilder.getParamsString(parameters));
//        out.flush();
//        out.close();

        // Reading the Response
        int status = con.getResponseCode();

        System.out.println( ">>> ResponseCode : " + con.getResponseCode());
        System.out.println( ">>> ResponseMessage : " + con.getResponseMessage());

        // read the response of the request and place it in a content String
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }

        in.close();
        con.disconnect();

        // Reading the Response on Failed Requests

        Reader streamReader = null;

        if (status > 299) {
            streamReader = new InputStreamReader(con.getErrorStream());
        } else {
            streamReader = new InputStreamReader(con.getInputStream());
        }

    }

}
