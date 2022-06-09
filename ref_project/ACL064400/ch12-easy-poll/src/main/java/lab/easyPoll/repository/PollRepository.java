package lab.easyPoll.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import lab.easyPoll.domain.Poll;

public interface PollRepository extends PagingAndSortingRepository<Poll, Long> {

}
