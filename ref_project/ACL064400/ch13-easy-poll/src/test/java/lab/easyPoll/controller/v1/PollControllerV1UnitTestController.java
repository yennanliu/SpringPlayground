package lab.easyPoll.controller.v1;

import static lab.easyPoll.TestUtility.asJsonString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import lab.easyPoll.TestUtility;
import lab.easyPoll.domain.Poll;
import lab.easyPoll.exception.ResourceNotFoundException;
import lab.easyPoll.exception.RestControllerException;
import lab.easyPoll.repository.PollRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class PollControllerV1UnitTestController {
	@Mock
	private PollRepository pollRepository;
	@Mock
	private PollModelAssembler pollModelAssembler;

	@InjectMocks
	PollController pollController;

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() throws Exception {
		mockMvc = standaloneSetup(pollController).build();
	}
	
	@Test
	public void Should_get_200_When_query_poll_successfully() throws Exception {
		// Given
		Long id = 9l;
		Poll poll = Poll.of("您最喜歡什麼顏色?", "紅色", "黑色", "藍色", "白色");
		poll.setId(id);
		when(pollRepository.findById(id)).thenReturn(Optional.of(poll));
		when(pollModelAssembler.toModel(poll)).thenReturn(EntityModel.of(poll));
		
		// When & Then
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/v1/polls/9")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);
	
		ResultHandler resultHandler = 
				r -> System.out.println(r.getResponse().getContentAsString(StandardCharsets.UTF_8));
		
		ResultMatcher resultMatcher = MockMvcResultMatchers.status().isOk();
		
		ResultActions resultActions = mockMvc.perform(requestBuilder);
		
		resultActions
			.andDo(resultHandler)
			.andExpect(resultMatcher)
			.andExpect(MockMvcResultMatchers.jsonPath("$.id").value("9"))
			.andExpect(jsonPath("$.question").value("您最喜歡什麼顏色?"))
			.andExpect(jsonPath("$.options[0].value").value("紅色"))
			.andExpect(jsonPath("$.options[1].value").value("黑色"))
			.andExpect(jsonPath("$.options[2].value").value("藍色"))
			.andExpect(jsonPath("$.options[3].value").value("白色"));
	}

	@Test
	public void Should_get_200_When_query_all_polls_successfully() throws Exception {
		// Given
		List<Poll> polls = Arrays.asList(
				Poll.of("您最喜歡什麼顏色?", "紅色", "黑色", "藍色", "白色"),
				Poll.of("您最喜歡的運動是什麼?", "足球", "籃球", "板球", "棒球"));
		when(pollRepository.findAll()).thenReturn(polls);
		when(pollModelAssembler.toCollectionModel(polls))
			.thenReturn(TestUtility.createCollectionModel(polls));
		// When & Then
		mockMvc
			.perform(get("/v1/polls")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(r -> System.out.println(r.getResponse().getContentAsString(StandardCharsets.UTF_8)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[2]").doesNotExist())
			.andExpect(jsonPath("$.content[0].question").value("您最喜歡什麼顏色?"))
			.andExpect(jsonPath("$.content[0].options[0].value").value("紅色"))
			.andExpect(jsonPath("$.content[0].options[4]").doesNotExist());
	}
	
	
	@Test
	public void Should_get_201_When_create_poll_successfully() throws Exception {
		// Given
		Poll unsaved = Poll.of("您最喜歡什麼顏色?", "紅色", "黑色", "藍色", "白色");
		Poll saved = Poll.of(9l, "您最喜歡什麼顏色?", "紅色", "黑色", "藍色", "白色");		
		when(pollRepository.save(unsaved)).thenReturn(saved);
		// When & Then
		mockMvc
			.perform(post("/v1/polls")
					.content(asJsonString(unsaved))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/v1/polls/9"));
	}
	
	
	@Test
	public void Should_get_200_When_replace_poll_successfully() throws Exception {
		// Given
		long id = 9l;
		Poll asis = Poll.of(id, "asis question", "asis option");
		when(pollRepository.findById(id)).thenReturn(Optional.of(asis));

		// When & Then
		Poll tobe = Poll.of("tobe question", "tobe option");
		mockMvc
			.perform(put("/v1/polls/9")
					.content(asJsonString(tobe))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	public void Should_get_200_When_update_poll_successfully() throws Exception {
		// Given
		long id = 9l;
		Poll asis = Poll.of(id, "asis question", "asis option");
		when(pollRepository.findById(id)).thenReturn(Optional.of(asis));
		
		// When & Then
		Poll tobe = Poll.of("tobe question", "tobe option");
		mockMvc
			.perform(patch("/v1/polls/9")
					.content(asJsonString(tobe))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
	
	@Test
	public void Should_get_200_When_delete_poll_successfully() throws Exception {
		// Given
		long id = 9l;
		Poll asis = new Poll();
		asis.setId(id);
		when(pollRepository.findById(id)).thenReturn(Optional.of(asis));
		
		// When & Then
		mockMvc
			.perform(delete("/v1/polls/9"))
			.andExpect(status().isOk());
	}

	
	
	@Test
	public void Should_get_404_When_query_poll_not_found() throws Exception {
		// Given
		long id = 9l;
		when(pollRepository.findById(id)).thenThrow(new ResourceNotFoundException("id not found~"));
		// When & Then
		mockMvc
			.perform(get("/v1/polls/9")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(r -> System.out.println(r.getResolvedException()))
			.andDo(r -> System.out.println(r.getResolvedException().getMessage()))
			.andExpect(status().isNotFound())
			.andExpect(r -> assertTrue(r.getResolvedException() instanceof ResourceNotFoundException))
			.andExpect(r -> assertEquals("id not found~", r.getResolvedException().getMessage()));	
	}
	
	
	@Test
	public void Should_get_400_When_create_poll_with_empty_input() throws Exception {
		// Given
		Poll unsaved = new Poll();
		// When & Then
		mockMvc
			.perform(post("/v1/polls")
					.content(asJsonString(unsaved))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(r -> System.out.println(r.getResolvedException().getMessage()))				
			.andExpect(status().isBadRequest())
			.andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException))
			;
	}
	@Test
	public void Should_get_500_When_create_poll_failed() throws Exception {
		// Given
		Poll unsaved = Poll.of("您最喜歡什麼顏色?", "紅色", "黑色", "藍色", "白色");
		when(pollRepository.save(unsaved)).thenThrow(new RuntimeException());	
		// When & Then
		mockMvc
			.perform(post("/v1/polls")
					.content(asJsonString(unsaved))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))	
			.andExpect(status().is5xxServerError())
			.andExpect(r -> assertTrue(r.getResolvedException() instanceof RestControllerException))
			.andExpect(r -> assertTrue(r.getResolvedException().getCause() instanceof RuntimeException));
	}

	
	@Test
	public void Should_get_404_When_replace_poll_not_found() throws Exception {
		// Given
		long id = 9l;
		Poll asis = new Poll();
		asis.setId(id);
		when(pollRepository.findById(id)).thenThrow(new ResourceNotFoundException("id not found~"));
		// When & Then
		mockMvc
			.perform(put("/v1/polls/9")
					.content(asJsonString(Poll.of("tobe question", "tobe option")))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(r -> System.out.println(r.getResolvedException().getMessage()))
			.andExpect(status().isNotFound())
			.andExpect(r -> assertTrue(r.getResolvedException() instanceof ResourceNotFoundException))
			.andExpect(r -> assertEquals("id not found~", r.getResolvedException().getMessage()));
	}
	@Test
	public void Should_get_500_When_replace_poll_validation_failed() throws Exception {
		// Given
		long id = 9l;
		Poll asis = new Poll();
		asis.setId(id);
		when(pollRepository.findById(id)).thenReturn(Optional.of(asis));
		Poll tobe = Poll.of(id, "tobe question", "tobe option");
		when(pollRepository.save(tobe)).thenThrow(new RuntimeException());
		// When & Then
		mockMvc
		.perform(put("/v1/polls/9")
				.content(asJsonString(tobe))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is5xxServerError())
		.andExpect(r -> assertTrue(r.getResolvedException() instanceof RestControllerException))
		.andExpect(r -> assertTrue(r.getResolvedException().getCause() instanceof RuntimeException));
	}
	
	
	@Test
	public void Should_get_404_When_update_poll_not_found() throws Exception {
		// Given
		long id = 9l;
		Poll tobe = Poll.of("tobe question", "tobe option");
		when(pollRepository.findById(id)).thenThrow(new ResourceNotFoundException("id not found~"));
		// When & Then
		mockMvc
			.perform(put("/v1/polls/9")
					.content(asJsonString(tobe))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(r -> System.out.println(r.getResolvedException().getMessage()))
			.andExpect(status().isNotFound())
			.andExpect(r -> assertTrue(r.getResolvedException() instanceof ResourceNotFoundException))
			.andExpect(r -> assertEquals("id not found~", r.getResolvedException().getMessage()));
	}
	@Test
	public void Should_get_500_When_update_poll_failed() throws Exception {
		// Given
		long id = 9l;
		Poll asis = Poll.of(id, "asis question", "asis option");
		when(pollRepository.findById(id)).thenReturn(Optional.of(asis));
		Poll tobe = Poll.of("tobe question", "tobe option");
		when(pollRepository.save(tobe)).thenThrow(new RuntimeException());		
		// When & Then
		mockMvc
			.perform(put("/v1/polls/9")
					.content(asJsonString(tobe))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
			.andDo(r -> System.out.println(r.getResolvedException().getMessage()))
			.andExpect(status().is5xxServerError())
			.andExpect(r -> assertTrue(r.getResolvedException() instanceof RestControllerException))
			.andExpect(r -> assertTrue(r.getResolvedException().getCause() instanceof RuntimeException));
	}

	@Test
	public void Should_get_404_When_delete_poll_not_found() throws Exception {
		// Given
		long id = 9l;
		Poll asis = new Poll();
		asis.setId(id);
		when(pollRepository.findById(id)).thenThrow(new ResourceNotFoundException("id not found~"));
		// When & Then
		mockMvc
			.perform(delete("/v1/polls/9"))
			.andExpect(status().isNotFound())
			.andExpect(r -> assertTrue(r.getResolvedException() instanceof ResourceNotFoundException))
			.andExpect(r -> assertEquals("id not found~", r.getResolvedException().getMessage()));
	}	
	
	
}
