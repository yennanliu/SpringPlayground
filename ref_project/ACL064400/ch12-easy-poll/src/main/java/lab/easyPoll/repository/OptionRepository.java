package lab.easyPoll.repository;

import org.springframework.data.repository.CrudRepository;

import lab.easyPoll.domain.Option;

public interface OptionRepository extends CrudRepository<Option, Long> {

}
