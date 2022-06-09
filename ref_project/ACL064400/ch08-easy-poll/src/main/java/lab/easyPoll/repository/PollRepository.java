package lab.easyPoll.repository;

import org.springframework.data.repository.CrudRepository;

import lab.easyPoll.domain.Poll;

public interface PollRepository extends CrudRepository<Poll, Long> {

}
