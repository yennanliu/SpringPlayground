package lab.easyPoll.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lab.easyPoll.domain.Option;
import lab.easyPoll.domain.Poll;

@TestMethodOrder(OrderAnnotation.class)
public class TestRestTemplateV2Client {

	private static final String EASY_POLL_URI = "http://localhost:8080/v2/polls";

	@Test
	@Order(1)
	public void testPostForXXX() {
		Poll p1 = buildPoll("您最喜歡什麼顏色?", "紅色", "黑色", "藍色", "白色");
		Poll p2 = buildPoll("您最喜歡的運動是什麼?", "足球", "籃球", "板球", "棒球");
		Poll p3 = buildPoll("您使用Spring框架多久?", "1年", "2年", "3年", "4年");

		RestTemplate restTemplate = new RestTemplate();
		// postForLocation
		URI location = restTemplate.postForLocation(EASY_POLL_URI, p1);
		assertTrue(location.toString().startsWith(EASY_POLL_URI));
		// postForObject
		Poll object = restTemplate.postForObject(EASY_POLL_URI, p2, Poll.class);
		assertNull(object);
		// postForEntity
		ResponseEntity<String> entity = restTemplate.postForEntity(EASY_POLL_URI, p3, String.class);
		assertNull(entity.getBody());
		assertEquals(HttpStatus.CREATED, entity.getStatusCode());
		assertTrue(entity.getHeaders().getLocation().toString().startsWith(EASY_POLL_URI));
	}

	@Test
	@Order(2)
	public void testExchange4Post() {
		Poll p = buildPoll("奧林匹克旗幟上有多少個環?", "6", "8", "5", "4");
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Poll> request = new HttpEntity<>(p);
		ResponseEntity<Poll> entity = restTemplate.exchange(EASY_POLL_URI, HttpMethod.POST, request, Poll.class);
		assertNull(entity.getBody());
		assertEquals(HttpStatus.CREATED, entity.getStatusCode());
		assertTrue(entity.getHeaders().getLocation().toString().startsWith(EASY_POLL_URI));
	}
	private static Poll buildPoll(String question, String... options) {
		Poll newPoll = new Poll();
		newPoll.setQuestion(question);
		Set<Option> optionSet = new HashSet<>();
		newPoll.setOptions(optionSet);
		for (String s : options) {
			Option o = new Option();
			o.setValue(s);
			optionSet.add(o);
		}
		return newPoll;
	}

	
	@Test
	@Order(3)
	public void testExchange4GetPaging() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> params = new HashMap<>();
		params.put("page", 0);
		params.put("size", 2);
		String url = EASY_POLL_URI + "?page={page}&size={size}";
		ResponseEntity<String> response = restTemplate.exchange(
				url, HttpMethod.GET, null, String.class, params);
		String body = response.getBody();
		List<Poll> polls = extractPolls(body);
		System.out.println(polls);
		verifyPagingPolls(polls);
	}
	private static List<Poll> extractPolls(String body) throws JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		JsonNode jsNode = om.readTree(body);
		String pollList = jsNode.at("/_embedded/pollList").toString();
		List<Poll> polls = om.readValue(pollList, new TypeReference<List<Poll>>() {
		});
		return polls;
	}
	private static void verifyPagingPolls(List<Poll> polls) {
		assertEquals(2, polls.size());
		List<Poll> expected = Arrays.asList(
				Poll.of("您最喜歡什麼顏色?", "紅色", "黑色", "藍色", "白色"), 
				Poll.of("您最喜歡的運動是什麼?", "籃球", "棒球", "足球", "板球"));
		assertEquals(expected.size(), polls.size());
		assertTrue(polls.containsAll(expected));
		assertTrue(expected.containsAll(polls));
	}
	
}
