package lab.easyPoll.controller;

import static lab.easyPoll.TestUtility.asJsonString;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lab.easyPoll.domain.Poll;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class PollControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@Order(1)
	public void Should_get_201_When_create_poll_successfully() throws Exception {
		Poll p1 = Poll.of("您最喜歡什麼顏色?", "紅色", "黑色", "藍色", "白色");
		mockMvc
			.perform(post("/v1/polls")
					.content(asJsonString(p1))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(r -> System.out.println("@Order(1): " + r.getResponse().getHeaderNames()))
			.andExpect(status().isCreated())
			.andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/v1/polls/1"));
	}
	
	@Test
	@Order(2)
	public void Should_get_200_When_query_poll_successfully() throws Exception {
		mockMvc
			.perform(get("/v1/polls/1")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(r -> System.out.println("@Order(2): " + r.getResponse().getContentAsString(StandardCharsets.UTF_8)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value("1"))
			.andExpect(jsonPath("$.question").value("您最喜歡什麼顏色?"))
			.andExpect(jsonPath("$.options[0].value").value("紅色"))
			.andExpect(jsonPath("$.options[1].value").value("黑色"))
			.andExpect(jsonPath("$.options[2].value").value("藍色"))
			.andExpect(jsonPath("$.options[3].value").value("白色"))
			.andExpect(jsonPath("$._links").exists())
			.andExpect(jsonPath("$._links.self.href").value("http://localhost/v1/polls/1"))
			.andExpect(jsonPath("$._links.votes.href").value("http://localhost/v1/polls/1/votes"))
			.andExpect(jsonPath("$._links.compute-result").exists());
	}
	
	@Test
	@Order(3)
	public void Should_get_200_When_query_all_polls_successfully() throws Exception {
		Poll p2 = Poll.of("您最喜歡的運動是什麼?", "足球", "籃球", "板球", "棒球");
		mockMvc
			.perform(post("/v1/polls")
					.content(asJsonString(p2))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated());
		
		mockMvc
			.perform(get("/v1/polls")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(r -> System.out.println("@Order(3): " + r.getResponse().getContentAsString(StandardCharsets.UTF_8)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._links.self.href").value("http://localhost/v1/polls"))
			.andExpect(jsonPath("$._embedded.pollList").exists())
			.andExpect(jsonPath("$._embedded.pollList[0].question").value("您最喜歡什麼顏色?"))
			.andExpect(jsonPath("$._embedded.pollList[0].options[0].value").value("紅色"))
			.andExpect(jsonPath("$._embedded.pollList[1].question").value("您最喜歡的運動是什麼?"))
			.andExpect(jsonPath("$._embedded.pollList[1].options[1].value").value("棒球"));
	}
	
	@Test
	@Order(4)
	public void Should_get_200_When_query_all_polls_with_paging_successfully() throws Exception {
		Poll p3 = Poll.of("您使用Spring框架多久?", "1年", "2年", "3年", "4年");
		mockMvc
			.perform(post("/v1/polls")
					.content(asJsonString(p3))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated());
		
		mockMvc
			.perform(get("/v2/polls?page=1&size=2")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(r -> System.out.println("@Order(4): " + r.getResponse().getContentAsString(StandardCharsets.UTF_8)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded.pollList[0].question").value("您使用Spring框架多久?"))
			.andExpect(jsonPath("$._embedded.pollList[0].options[0].value").value("3年"))
			.andExpect(jsonPath("$._links.first.href").value("http://localhost/v2/polls?page=0&size=2"))
			.andExpect(jsonPath("$._links.prev.href").value("http://localhost/v2/polls?page=0&size=2"))
			.andExpect(jsonPath("$._links.self.href").value("http://localhost/v2/polls?page=1&size=2"))
			.andExpect(jsonPath("$._links.last.href").value("http://localhost/v2/polls?page=1&size=2"))
			.andExpect(jsonPath("$.page.size").value("2"))
			.andExpect(jsonPath("$.page.totalElements").value("3"))
			.andExpect(jsonPath("$.page.totalPages").value("2"))
			.andExpect(jsonPath("$.page.number").value("1"));
	}


	@Test
	@Order(5)
	public void Should_get_200_When_replace_poll_successfully() throws Exception {		
		mockMvc
			.perform(put("/v1/polls/1")
					.content(asJsonString(Poll.of("tobe question", "opt1", "opt2")))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		
		mockMvc
			.perform(get("/v1/polls/1")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(r -> System.out.println("@Order(5): " + r.getResponse().getContentAsString(StandardCharsets.UTF_8)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.question").value("tobe question"))
			.andExpect(jsonPath("$.options[0].value").value("opt1"))
			.andExpect(jsonPath("$.options[1].value").value("opt2"))
			.andExpect(jsonPath("$.options[2]").doesNotExist());
	}

	
	@Test
	@Order(6)
	public void Should_get_200_When_update_poll_successfully() throws Exception {
		mockMvc
			.perform(patch("/v1/polls/1")
					.content(asJsonString(Poll.of("new tobe question", "opt3")))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		
		mockMvc
			.perform(get("/v1/polls/1")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(r -> System.out.println("@Order(6): " + r.getResponse().getContentAsString(StandardCharsets.UTF_8)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.question").value("new tobe question"))
			.andExpect(jsonPath("$.options[0].value").value("opt1"))
			.andExpect(jsonPath("$.options[1].value").value("opt2"))
			.andExpect(jsonPath("$.options[2].value").value("opt3"));		
	}
	
	@Test
	@Order(7)
	public void Should_get_200_When_delete_poll_successfully() throws Exception {
		mockMvc
			.perform(delete("/v1/polls/1"))
			.andExpect(status().isOk());
		
		mockMvc
			.perform(get("/v2/polls?page=1&size=2")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(r -> System.out.println("@Order(7): " + r.getResponse().getContentAsString(StandardCharsets.UTF_8)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._embedded").doesNotExist())
			.andExpect(jsonPath("$._links").exists())
			.andExpect(jsonPath("$.page.size").value("2"))
			.andExpect(jsonPath("$.page.totalElements").value("2"))
			.andExpect(jsonPath("$.page.totalPages").value("1"))
			.andExpect(jsonPath("$.page.number").value("1"));		
	}

	
	
	@Test
	@Order(8)
	public void Should_get_404_When_query_poll_not_found() throws Exception {
		mockMvc
			.perform(get("/v1/polls/1")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(r -> System.out.println("@Order(8): " + r.getResponse().getContentAsString(StandardCharsets.UTF_8)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.title").value("Resource Not Found"))
			.andExpect(jsonPath("$.detail").value("Poll with id 1 not found"))
			.andExpect(jsonPath("$.developerMessage").value("lab.easyPoll.exception.ResourceNotFoundException"));
	}
	
	
	@Test
	@Order(9)
	public void Should_get_400_When_create_poll_with_empty_input() throws Exception {
		Poll unsaved = new Poll();
		mockMvc
			.perform(post("/v1/polls")
					.content(asJsonString(unsaved))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(r -> System.out.println("@Order(9): " + r.getResponse().getContentAsString(StandardCharsets.UTF_8)))			
			.andExpect(status().isBadRequest())
			.andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException))
			;
	}


	@Test
	@Order(10)
	public void Should_get_404_When_replace_poll_not_found() throws Exception {
		Poll tobe = Poll.of("tobe question", "tobe option");
		mockMvc
			.perform(put("/v1/polls/1")
					.content(asJsonString(tobe))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(r -> System.out.println("@Order(10): " + r.getResponse().getContentAsString(StandardCharsets.UTF_8)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.title").value("Resource Not Found"))
			.andExpect(jsonPath("$.detail").value("Poll with id 1 not found"))
			.andExpect(jsonPath("$.developerMessage").value("lab.easyPoll.exception.ResourceNotFoundException"));
	}
	@Test
	@Order(11)
	public void Should_get_500_When_replace_poll_validation_failed() throws Exception {
		Poll tobe = Poll.of("tobe question", "tobe option");
		mockMvc
		.perform(put("/v1/polls/6")
				.content(asJsonString(tobe))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(r -> System.out.println("@Order(11): " + r.getResponse().getContentAsString(StandardCharsets.UTF_8)))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$.title").value("Request Validation Error"))
		.andExpect(jsonPath("$.developerMessage").value("lab.easyPoll.exception.RestControllerException"));	
	}
	
	
	@Test
	@Order(12)
	public void Should_get_404_When_update_poll_not_found() throws Exception {
		Poll tobe = Poll.of("tobe question", "tobe option");
		mockMvc
			.perform(patch("/v1/polls/1")
					.content(asJsonString(tobe))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(r -> System.out.println("@Order(12): " + r.getResponse().getContentAsString(StandardCharsets.UTF_8)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.title").value("Resource Not Found"))
			.andExpect(jsonPath("$.detail").value("Poll with id 1 not found"))
			.andExpect(jsonPath("$.developerMessage").value("lab.easyPoll.exception.ResourceNotFoundException"));
	}
	@Test
	@Order(13)
	public void Should_get_500_When_update_poll_failed() throws Exception {		
		Poll tobe = Poll.of("", "tobe option");
		mockMvc
		.perform(patch("/v1/polls/6")
				.content(asJsonString(tobe))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(r -> System.out.println("@Order(13): " + r.getResponse().getContentAsString(StandardCharsets.UTF_8)))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$.title").value("Request Validation Error"))
		.andExpect(jsonPath("$.developerMessage").value("lab.easyPoll.exception.RestControllerException"));	
	}
	
	
	@Test
	@Order(14)
	public void Should_get_404_When_delete_poll_not_found() throws Exception {
		mockMvc
			.perform(delete("/v1/polls/1")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(r -> System.out.println("@Order(8): " + r.getResponse().getContentAsString(StandardCharsets.UTF_8)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.title").value("Resource Not Found"))
			.andExpect(jsonPath("$.detail").value("Poll with id 1 not found"))
			.andExpect(jsonPath("$.developerMessage").value("lab.easyPoll.exception.ResourceNotFoundException"));
	}
	
	
}
