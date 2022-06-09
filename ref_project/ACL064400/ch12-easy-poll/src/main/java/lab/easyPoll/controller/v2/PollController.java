package lab.easyPoll.controller.v2;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

//import lab.easyPoll.controller.PollResource;
import lab.easyPoll.domain.Option;
import lab.easyPoll.domain.Poll;
import lab.easyPoll.dto.error.ErrorDetail;
import lab.easyPoll.exception.ResourceNotFoundException;
import lab.easyPoll.repository.PollRepository;

@RestController("pollControllerV2")
@RequestMapping("/v2")
@Api(value = "polls", description = "Poll API")
public class PollController {
	
	@Autowired
	private PagedResourcesAssembler<Poll> pagedResourcesAssembler;
	
	@Autowired
	@Qualifier("pollModelAssemblerV2")
	private PollModelAssembler pollModelAssembler;
	
	@Autowired
	private PollRepository pollRepository;

	@RequestMapping(value = "/polls", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Creates a new Poll", 
					notes = "The newly created poll Id will be sent in the location response header", 
					response = Void.class)
	@ApiResponses(value = { 
		@ApiResponse(code = 201, message = "Poll Created Successfully", response = Void.class),
		@ApiResponse(code = 500, message = "Error creating Poll", response = ErrorDetail.class) })
	public ResponseEntity<Void> createPoll(@Valid @RequestBody Poll poll) {
		poll = pollRepository.save(poll);

		// Set the location header for the newly created resource
		HttpHeaders responseHeaders = new HttpHeaders();
		URI newPollUri = ServletUriComponentsBuilder
							.fromCurrentRequest()
							.path("/{id}")
							.buildAndExpand(poll.getId())
							.toUri();
		responseHeaders.setLocation(newPollUri);

		return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/polls/{pollId}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Partially updates given Poll", response=Void.class)
	@ApiResponses(value = {
			@ApiResponse(code=200, message="", response=Void.class),  
			@ApiResponse(code=404, message="Unable to find Poll", response=ErrorDetail.class) } )	
	public ResponseEntity<?> updatePoll(@RequestBody Poll poll, @PathVariable Long pollId) {
		verifyPoll(pollId);
		Poll updated = pollRepository.findById(pollId).get();
		updated.setQuestion(poll.getQuestion());
		// only add new option
		for (Option o: poll.getOptions()) {
			if (!updated.getOptions().contains(o)) {
				updated.addOption(o);
			}
		}
		pollRepository.save(updated);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/polls/{pollId}", method = RequestMethod.PUT)
	@ApiOperation(value = "Replace with given Poll", response=Void.class)
	@ApiResponses(value = {
			@ApiResponse(code=200, message="", response=Void.class),  
			@ApiResponse(code=404, message="Unable to find Poll", response=ErrorDetail.class) } )	
	public ResponseEntity<?> replacePoll(@RequestBody Poll poll, @PathVariable Long pollId) {
		verifyPoll(pollId);
		Poll updated = pollRepository.findById(pollId).get();
		updated.setQuestion(poll.getQuestion());
		// replace all options
		updated.setOptions(poll.getOptions());
		pollRepository.save(updated);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

	@RequestMapping(value = "/polls/{pollId}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Deletes given Poll", response=Void.class)
	@ApiResponses(value = {
			@ApiResponse(code=200, message="", response=Void.class),  
			@ApiResponse(code=404, message="Unable to find Poll", response=ErrorDetail.class) } )	
	public ResponseEntity<Void> deletePoll(@PathVariable Long pollId) {
		verifyPoll(pollId);
		pollRepository.deleteById(pollId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

	@RequestMapping(value = "/polls/{pollId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retrieves given Poll", response=Poll.class)
	@ApiResponses(value = {
			@ApiResponse(code=200, message="", response=Poll.class),  
			@ApiResponse(code=404, message="Unable to find Poll", response=ErrorDetail.class) } )	
	public ResponseEntity<EntityModel<Poll>> getPoll(@PathVariable Long pollId) {
		verifyPoll(pollId);
		Poll p = pollRepository.findById(pollId).get();
		
		EntityModel<Poll> ep = pollModelAssembler.toModel(p);
		return new ResponseEntity<>(ep, HttpStatus.OK);
	}
	

	@RequestMapping(value = "/polls", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Retrieves all the polls", response=Poll.class, responseContainer="List")
	public ResponseEntity<PagedModel<EntityModel<Poll>>> getAllPolls(Pageable pageable) {
		Page<Poll> allPolls = pollRepository.findAll(pageable);
		
		PagedModel<EntityModel<Poll>> pep = pagedResourcesAssembler.toModel(allPolls, pollModelAssembler);
		
		return new ResponseEntity<>(pep, HttpStatus.OK);
	}
	
	protected void verifyPoll(Long pollId) throws ResourceNotFoundException {
		Optional<Poll> poll = pollRepository.findById(pollId);
		if(!poll.isPresent()) {
			throw new ResourceNotFoundException("Poll with id " + pollId + " not found"); 
		}
	}

}
