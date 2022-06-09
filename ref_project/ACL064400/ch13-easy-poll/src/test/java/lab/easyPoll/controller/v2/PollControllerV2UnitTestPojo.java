package lab.easyPoll.controller.v2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import lab.easyPoll.TestUtility;
import lab.easyPoll.domain.Poll;
import lab.easyPoll.repository.PollRepository;

@ExtendWith(MockitoExtension.class)
public class PollControllerV2UnitTestPojo {

	@Mock
	private PollRepository pollRepository;
	@Mock
	private PollModelAssembler pollModelAssembler;
	@Mock
	PagedResourcesAssembler<Poll> pagedResourcesAssembler;
	// SUT
	private PollController pollController;

	@BeforeEach
	public void setup() {
		pollController = new PollController();
		ReflectionTestUtils.setField(pollController, "pollRepository", pollRepository);
		ReflectionTestUtils.setField(pollController, "pollModelAssembler", pollModelAssembler);
		ReflectionTestUtils.setField(pollController, "pagedResourcesAssembler", pagedResourcesAssembler);
	}

	@Test
	public void Should_get_200_When_query_all_polls_with_paging_successfully() {		
		// Given
		long size = 2l;
		long number = 0;
		long totalElements = 3l;
		long totalPages = 2l;
		PageMetadata pm = new PageMetadata(size, number, totalElements, totalPages);

		List<Poll> polls = Arrays.asList(
				Poll.of("question-1", "option-1"), 
				Poll.of("question-2", "option-2"));
		Page<Poll> pagedPolls = new PageImpl<>(polls);

		when(pollRepository.findAll(isA(Pageable.class))).thenReturn(pagedPolls);
		when(pagedResourcesAssembler.toModel(pagedPolls, pollModelAssembler))
				.thenReturn(TestUtility.createPagedModel(pagedPolls, pm));

		// When
		Pageable page1stWith2Elements = PageRequest.of(0, 2);
		ResponseEntity<PagedModel<EntityModel<Poll>>> allPollsEntity = pollController.getAllPolls(page1stWith2Elements);

		// Then
		verify(pollRepository, times(1)).findAll(isA(Pageable.class));
		verify(pagedResourcesAssembler, times(1)).toModel(pagedPolls, pollModelAssembler);
		assertEquals(HttpStatus.OK, allPollsEntity.getStatusCode());
		assertEquals(2, allPollsEntity.getBody().getContent().size());
		assertEquals(0, allPollsEntity.getBody().getMetadata().getNumber());
		assertEquals(2l, allPollsEntity.getBody().getMetadata().getSize());
		assertEquals(3l, allPollsEntity.getBody().getMetadata().getTotalElements());
		assertEquals(2l, allPollsEntity.getBody().getMetadata().getTotalPages());
	}

}
