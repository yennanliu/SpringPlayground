package lab.easyPoll.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lab.easyPoll.domain.Poll;

@TestMethodOrder(OrderAnnotation.class)
public class TestRestTemplateV1Client {

	private static final String EASY_POLL_URI = "http://localhost:8080/v1/polls";

	@Test
	@Order(1)
	public void testPostForXXX() {
		Poll p1 = Poll.of("您最喜歡什麼顏色?", "紅色", "黑色", "藍色", "白色");
		Poll p2 = Poll.of("您最喜歡的運動是什麼?", "足球", "籃球", "板球", "棒球");
		Poll p3 = Poll.of("您使用Spring框架多久?", "1年", "2年", "3年", "4年");

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
		Poll p = Poll.of("奧林匹克旗幟上有多少個環?", "6", "8", "5", "4");
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Poll> request = new HttpEntity<>(p);
		ResponseEntity<Poll> entity = restTemplate.exchange(EASY_POLL_URI, HttpMethod.POST, request, Poll.class);
		assertNull(entity.getBody());
		assertEquals(HttpStatus.CREATED, entity.getStatusCode());
		assertTrue(entity.getHeaders().getLocation().toString().startsWith(EASY_POLL_URI));
	}


	@Test
	@Order(3)
	public void testGetForXXX() {
		RestTemplate restTemplate = new RestTemplate();
		// getForObject()
		Poll p1 = restTemplate.getForObject(EASY_POLL_URI + "/{pollId}", Poll.class, 1l);
		assertEquals(Poll.of("您最喜歡什麼顏色?", "紅色", "黑色", "藍色", "白色"), p1);

		// getForEntity()
		ResponseEntity<Poll> resp = restTemplate.getForEntity(EASY_POLL_URI + "/{pollId}", Poll.class, 6l);
		Poll p6 = resp.getBody();
		assertEquals(HttpStatus.OK, resp.getStatusCode());
		assertEquals("6, 您最喜歡的運動是什麼?, [7,籃球, 8,棒球, 9,足球, 10,板球]", p6.toString());
	}

	
	@Test
	@Order(4)
	public void testExchange4Get() throws JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();
		// exchange()
		ResponseEntity<String> response = restTemplate.exchange(EASY_POLL_URI + "/{pollId}", HttpMethod.GET, null,
				String.class, 11l);
		String body = response.getBody();
		Map<String, String> links = extractLinks(body);
		Poll poll = extractPoll(body); 
		// verify
		verifyLinks(links);
		assertEquals(Poll.of("您使用Spring框架多久?", "3年", "2年", "1年", "4年"), poll);
	}
	private static Map<String, String> extractLinks(String body) throws JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		JsonNode jnBody = om.readTree(body);
		Map<String, String> linkMaps = new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("self", jnBody.at("/_links/self/href").textValue());
				put("votes", jnBody.at("/_links/votes/href").textValue());
				put("compute-result", jnBody.at("/_links/compute-result/href").textValue());
			}
		};
		return linkMaps;
	}
	private static Poll extractPoll(String body) throws JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Poll poll = om.readValue(body, new TypeReference<Poll>() { });
		return poll;
	}
	private void verifyLinks(Map<String, String> links) {
		assertEquals(new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("self", "http://localhost:8080/v1/polls/11");
				put("votes", "http://localhost:8080/v1/polls/11/votes");
				put("compute-result", "http://localhost:8080/v1/computeresult?pollId=11");
			}
		}, links);
	}
	
	
	@Test
	@Order(5)
	public void testExchange4GetAll() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(EASY_POLL_URI, HttpMethod.GET, null, String.class);
		String body = response.getBody();
		List<Poll> polls = extractPolls(body);
		System.out.println(polls);
		verifyPolls(polls);
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
	private static void verifyPolls(List<Poll> polls) {
		assertEquals(4, polls.size());
		List<Poll> expected = Arrays.asList(
				Poll.of("奧林匹克旗幟上有多少個環?", "6", "8", "5", "4"),
				Poll.of("您最喜歡什麼顏色?", "紅色", "黑色", "藍色", "白色"), 
				Poll.of("您最喜歡的運動是什麼?", "籃球", "棒球", "足球", "板球"),
				Poll.of("您使用Spring框架多久?", "3年", "2年", "1年", "4年"));
		assertTrue(expected.size() == polls.size());
		assertTrue(polls.containsAll(expected));
		assertTrue(expected.containsAll(polls));
	}
	
	
	@Test
	@Order(6)
	public void testPut() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		// replace
		Poll newPoll = Poll.of("您最喜歡的線上購物網站?", "Yahoo", "momo", "Pchome");
		restTemplate.put(EASY_POLL_URI + "/{pollId}", newPoll, 1l);
		// verify
		Poll p1 = restTemplate.getForObject(EASY_POLL_URI + "/{pollId}", Poll.class, 1l);
		assertEquals(newPoll, p1);
	}
	
	
	@Test
	@Order(7)
	public void testPatch() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		// add new option
		Poll newPoll = Poll.of("(v2)您最喜歡的線上購物網站?", "Yahoo", "momo", "Pchome", "東森");
		restTemplate.patchForObject(EASY_POLL_URI + "/{pollId}", newPoll, Poll.class, 1l);
		// verify
		Poll p1 = restTemplate.getForObject(EASY_POLL_URI + "/{pollId}", Poll.class, 1l);
		assertEquals(newPoll, p1);
	}
	
	
	@Test
	@Order(8)
	public void testDelete() throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		long id = 1;
		restTemplate.delete(EASY_POLL_URI + "/{pollId}",id);
		try {
			ResponseEntity<String> response = restTemplate.exchange(EASY_POLL_URI + "/{pollId}", HttpMethod.GET, null, String.class, id);
			fail();
		} catch (HttpClientErrorException e) {
			assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
		}
	}

}
