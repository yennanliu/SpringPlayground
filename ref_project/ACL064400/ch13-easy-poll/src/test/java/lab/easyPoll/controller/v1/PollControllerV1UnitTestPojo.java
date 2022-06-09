package lab.easyPoll.controller.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.ValidationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.collect.Lists;

import lab.easyPoll.TestUtility;
import lab.easyPoll.domain.Poll;
import lab.easyPoll.exception.ResourceNotFoundException;
import lab.easyPoll.exception.RestControllerException;
import lab.easyPoll.repository.PollRepository;

@ExtendWith(MockitoExtension.class)
public class PollControllerV1UnitTestPojo {

	@Mock
	private PollRepository pollRepository;
	@Mock
	private PollModelAssembler pollModelAssembler;
	
	private PollController pollController;
	
	@BeforeEach
	public void setup() {
		pollController = new PollController();
		ReflectionTestUtils.setField(pollController, "pollRepository", pollRepository);
		ReflectionTestUtils.setField(pollController, "pollModelAssembler", pollModelAssembler);
	}
	
	@Test
	public void Should_get_200_When_query_poll_successfully() {
		// Given
		long id = 9l;
		Poll poll = Poll.of("您最喜歡什麼顏色?", "紅色", "黑色", "藍色", "白色");
		poll.setId(id);
		when(pollRepository.findById(id)).thenReturn(Optional.of(poll));
		when(pollModelAssembler.toModel(poll)).thenReturn(EntityModel.of(poll));
		// When
		ResponseEntity<EntityModel<Poll>> resp = pollController.getPoll(id);
		// Then
		verify(pollRepository, times(2)).findById(id);
		verify(pollModelAssembler, times(1)).toModel(poll);
		assertEquals(HttpStatus.OK, resp.getStatusCode());
		assertEquals(poll, resp.getBody().getContent());
	}

	@Test
	public void Should_get_200_When_query_all_polls_successfully() {
		// Given
		List<Poll> polls = Arrays.asList(
				Poll.of("您最喜歡什麼顏色?", "紅色", "黑色", "藍色", "白色"),
				Poll.of("您最喜歡的運動是什麼?", "足球", "籃球", "板球", "棒球"));
		when(pollRepository.findAll()).thenReturn(polls);
		when(pollModelAssembler.toCollectionModel(polls))
			.thenReturn(TestUtility.createCollectionModel(polls));
		// When
		ResponseEntity<CollectionModel<EntityModel<Poll>>> allPollsEntity = pollController.getAllPolls();
		// Then
		verify(pollRepository, times(1)).findAll();
		verify(pollModelAssembler, times(1)).toCollectionModel(polls);
		assertEquals(HttpStatus.OK, allPollsEntity.getStatusCode());
		assertEquals(2, Lists.newArrayList(allPollsEntity.getBody()).size());
	}



	@Test
	public void Should_get_201_When_create_poll_successfully() {
		// Given
		Poll unsaved = Poll.of("您最喜歡什麼顏色?", "紅色", "黑色", "藍色", "白色");
		Poll saved = Poll.of(9l, "您最喜歡什麼顏色?", "紅色", "黑色", "藍色", "白色");		
		when(pollRepository.save(unsaved)).thenReturn(saved);
		
		MockHttpServletRequest request = new MockHttpServletRequest();
	    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(saved.getId())
				.toUri();
	    ResponseEntity<Object> expected = ResponseEntity.created(location).build();
		
		// When
		ResponseEntity<Void> actual = pollController.createPoll(unsaved);
		// Then
		verify(pollRepository, times(1)).save(isA(Poll.class));
		assertEquals(HttpStatus.CREATED, actual.getStatusCode());
		assertEquals(expected, actual);
	}
	

	@Test
	public void Should_get_200_When_replace_poll_successfully() {
		// Given
		long id = 9l;
		Poll asis = Poll.of(id, "asis question", "asis option");
		when(pollRepository.findById(id)).thenReturn(Optional.of(asis));
		
		// When
		Poll tobe = Poll.of("tobe question", "tobe option");
		ResponseEntity<Void> actual = pollController.replacePoll(tobe, id);
		
		// Then
		verify(pollRepository, times(2)).findById(isA(Long.class));
		verify(pollRepository, times(1)).save(isA(Poll.class));
		assertEquals(HttpStatus.OK, actual.getStatusCode());
		// Then - 以ArgumentCaptor確認最後傳入save()的是全部更新的Poll
		ArgumentCaptor<Poll> captor = ArgumentCaptor.forClass(Poll.class);
		verify(pollRepository).save(captor.capture());
		assertEquals(9l, captor.getValue().getId());
		Poll toSave = Poll.of("tobe question", "tobe option");
		assertEquals(toSave, captor.getValue());
	}

	@Test
	public void Should_get_200_When_update_poll_successfully() {
		// Given
		long id = 9l;
		Poll asis = Poll.of(id, "asis question", "asis option");
		when(pollRepository.findById(id)).thenReturn(Optional.of(asis));
		
		// When
		Poll tobe = Poll.of("tobe question", "tobe option");
		ResponseEntity<Void> actual = pollController.updatePoll(tobe, id);
		
		// Then
		verify(pollRepository, times(2)).findById(isA(Long.class));
		verify(pollRepository, times(1)).save(isA(Poll.class));
		assertEquals(HttpStatus.OK, actual.getStatusCode());
		// Then - 以ArgumentCaptor確認最後傳入save()的是局部更新的Poll
		ArgumentCaptor<Poll> captor = ArgumentCaptor.forClass(Poll.class);
		verify(pollRepository).save(captor.capture());
		assertEquals(9l, captor.getValue().getId());
		Poll toSave = Poll.of(id, "tobe question", "asis option", "tobe option");
		assertEquals(toSave, captor.getValue());
	}
	
	@Test
	public void Should_get_200_When_delete_poll_successfully() {
		// Given
		long id = 9l;
		Poll asis = new Poll();
		asis.setId(id);
		when(pollRepository.findById(id)).thenReturn(Optional.of(asis));
		// When
		ResponseEntity<Void> actual = pollController.deletePoll(id);
		// Then
		verify(pollRepository, times(1)).findById(isA(Long.class));
		verify(pollRepository, times(1)).deleteById(9l);
		assertEquals(HttpStatus.OK, actual.getStatusCode());
		ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
		verify(pollRepository).deleteById(captor.capture());
		assertEquals(9l, captor.getValue());
	}
	
	@Test
	public void Should_throws_exception_When_query_poll_not_found() {
		// Given
		long id = 9l;
		when(pollRepository.findById(id)).thenThrow(new ResourceNotFoundException("id not found~"));
		// When & Then
		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
			pollController.getPoll(id);
		});
		assertEquals("id not found~", ex.getMessage());
	}
	
	@Test
	public void Should_throws_exception_When_create_poll_failed() {
		// Given
		Poll unsaved = new Poll();
		Poll saved = new Poll();
		saved.setId(9l);
		when(pollRepository.save(unsaved)).thenThrow(new RuntimeException());	
		// When & Then
		assertThrows(RestControllerException.class, () -> {
			pollController.createPoll(unsaved);
		});
	}

	
	@Test
	public void Should_throws_exception_When_replace_poll_not_found() {
		// Given
		long id = 9l;
		Poll asis = new Poll();
		asis.setId(id);
		when(pollRepository.findById(id)).thenThrow(new ResourceNotFoundException("id not found~"));
		// When & Then
		Poll tobe = Poll.of("tobe question", "tobe option");
		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
			pollController.replacePoll(tobe, id);
		});
		assertEquals("id not found~", ex.getMessage());
	}
	
	@Test
	public void Should_throws_exception_When_replace_poll_failed() {
		// Given
		long id = 9l;
		Poll asis = new Poll();
		asis.setId(id);		
		when(pollRepository.findById(id)).thenReturn(Optional.of(asis));
		Poll tobe = Poll.of(id, "tobe question", "tobe option");		
		when(pollRepository.save(tobe)).thenThrow(new ValidationException());		
		// When & Then
		assertThrows(RestControllerException.class, () -> {
			pollController.replacePoll(tobe, id);
		});
	}
	
	@Test
	public void Should_throws_exception_When_update_poll_not_found() {
		// Given
		long id = 9l;
		Poll asis = new Poll();
		asis.setId(id);
		when(pollRepository.findById(id)).thenThrow(new ResourceNotFoundException("id not found~"));
		// When & Then
		Poll tobe = Poll.of("tobe question", "tobe option");
		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
			pollController.updatePoll(tobe, id);
		});
		assertEquals("id not found~", ex.getMessage());
	}
	
	@Test
	public void Should_throws_exception_When_update_poll_failed() {
		// Given
		long id = 9l;
		Poll asis = new Poll();
		asis.setId(id);		
		when(pollRepository.findById(id)).thenReturn(Optional.of(asis));
		Poll tobe = Poll.of(id, "tobe question", "tobe option");		
		when(pollRepository.save(tobe)).thenThrow(new ValidationException());		
		// When & Then
		assertThrows(RestControllerException.class, () -> {
			pollController.updatePoll(tobe, id);
		});
	}

	@Test
	public void Should_throws_exception_When_delete_poll_not_found() {
		// Given
		long id = 9l;
		when(pollRepository.findById(id)).thenThrow(new ResourceNotFoundException("id not found~"));
		// When & Then
		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
			pollController.deletePoll(id);
		});
		assertEquals("id not found~", ex.getMessage());
	}
	
}
