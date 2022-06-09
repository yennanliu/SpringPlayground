package lab.easyPoll.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.jupiter.api.Test;

public class TestJdkV1Client {
	@Test
	public void Should_get_response_When_use_jdk() throws IOException {
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		try {
			URL restAPIUrl = new URL("http://localhost:8080/v1/polls");
			connection = (HttpURLConnection) restAPIUrl.openConnection();
			connection.setRequestMethod("GET");
			// Read the response
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuilder jsonData = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				jsonData.append(line);
			}
			assertEquals("{\"_links\":{\"self\":{\"href\":\"http://localhost:8080/v1/polls\"}}}", jsonData.toString());
		} finally {
			if (reader != null)
				reader.close();
			if (connection != null)
				connection.disconnect();
		}
	}
}
