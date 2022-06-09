package lab.easyPoll.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lab.easyPoll.domain.Option;
import lab.easyPoll.domain.Poll;
import lab.easyPoll.repository.PollRepository;

@RestController
public class PollController {

	@Autowired
	private PollRepository pollRepository;

/*	
{
   "question":"What brand of COVID-19 vaccine do you most want to get?  ",
   "options":[
      {"value":"AstraZeneca"},
      {"value":"Moderna"},
      {"value":"BioNTech"},
      {"value":"MVC"}
   ]
}	
*/	
	@RequestMapping(value = "/polls", method = RequestMethod.POST)
//	@PostMapping(value = "/polls")
	public ResponseEntity<?> createPoll(@RequestBody Poll poll) {
		poll = pollRepository.save(poll);

		// Set the location header for the newly created resource
		HttpHeaders responseHeaders = new HttpHeaders();
		URI newPollUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(poll.getId())
				.toUri();
		responseHeaders.setLocation(newPollUri);

		return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/polls", method = RequestMethod.GET)
//	@GetMapping(value = "/polls")	
	public ResponseEntity<Iterable<Poll>> getAllPolls() {
		Iterable<Poll> allPolls = pollRepository.findAll();
		return new ResponseEntity<>(allPolls, HttpStatus.OK);
	}

	@RequestMapping(value = "/polls/{pollId}", method = RequestMethod.GET)
	public ResponseEntity<?> getPoll(@PathVariable Long pollId) {
		Poll p = pollRepository.findById(pollId).orElse(null);
		return new ResponseEntity<>(p, HttpStatus.OK);
	}

	@RequestMapping(value = "/polls/{pollId}", method = RequestMethod.PUT)
//	@PutMapping(value = "/polls/{pollId}")
	public ResponseEntity<?> replacePoll(@RequestBody Poll poll, @PathVariable Long pollId) {
		Poll updated = pollRepository.findById(pollId).get();
		updated.setQuestion(poll.getQuestion());
		// replace all options
		updated.setOptions(poll.getOptions());
		pollRepository.save(updated);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/polls/{pollId}", method = RequestMethod.PATCH)
//	@PatchMapping(value = "/polls/{pollId}")
	public ResponseEntity<?> updatePoll(@RequestBody Poll poll, @PathVariable Long pollId) {
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

	@RequestMapping(value = "/polls/{pollId}", method = RequestMethod.DELETE)
//	@DeleteMapping(value = "/polls/{pollId}")
	public ResponseEntity<?> deletePoll(@PathVariable Long pollId) {
		pollRepository.deleteById(pollId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
