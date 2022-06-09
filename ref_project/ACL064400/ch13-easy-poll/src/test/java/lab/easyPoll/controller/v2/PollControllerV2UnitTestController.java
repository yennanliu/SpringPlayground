package lab.easyPoll.controller.v2;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import lab.easyPoll.TestUtility;
import lab.easyPoll.domain.Poll;
import lab.easyPoll.repository.PollRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class PollControllerV2UnitTestController {
	@Mock
	private PollRepository pollRepository;
	@Mock
	private PollModelAssembler pollModelAssembler;
	@Mock
	private PagedResourcesAssembler<Poll> pagedResourcesAssembler;

	@InjectMocks
	PollController pollController;

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp() throws Exception {
		mockMvc = standaloneSetup(pollController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
				.build();
	}

	@Test
	public void Should_get_200_When_query_all_polls_with_paging_successfully() throws Exception {
		long size = 2l;
		long number = 1;
		long totalElements = 3l;
		long totalPages = 2l;

		PageMetadata pm = new PageMetadata(size, number, totalElements, totalPages);

		List<Poll> polls = Arrays.asList(Poll.of("question-3", "option-3"));
		Page<Poll> pagedPolls = new PageImpl<>(polls);

		when(pollRepository.findAll(isA(Pageable.class))).thenReturn(pagedPolls);
		when(pagedResourcesAssembler.toModel(pagedPolls, pollModelAssembler))
				.thenReturn(TestUtility.createPagedModel(pagedPolls, pm));

		// When & Then
		mockMvc.perform(get("/v2/polls?page=1&size=2")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(r -> System.out.println(r.getResponse().getContentAsString(StandardCharsets.UTF_8)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.content[1]").doesNotExist())
				.andExpect(jsonPath("$.content[0].question").value("question-3"))
				.andExpect(jsonPath("$.content[0].options[0].value").value("option-3"))
				.andExpect(jsonPath("$.page.size").value("2"))
				.andExpect(jsonPath("$.page.totalElements").value("3"))
				.andExpect(jsonPath("$.page.totalPages").value("2"))
				.andExpect(jsonPath("$.page.number").value("1"));
	}

}
