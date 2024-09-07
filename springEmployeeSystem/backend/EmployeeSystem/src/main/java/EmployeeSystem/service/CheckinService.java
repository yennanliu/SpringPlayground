package EmployeeSystem.service;

import EmployeeSystem.model.Checkin;
import EmployeeSystem.repository.CheckinRepository;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CheckinService {

  @Autowired CheckinRepository checkinRepository;

  public Flux<Checkin> getCheckIns() {

    return checkinRepository.findAll();
  }

  public Mono<Stream<Checkin>> getCheckinByUserId(Integer userId) {

    Flux<Checkin> checkinList = checkinRepository.findAll();
    return Mono.just(checkinList.toStream()
            .filter(
                    x -> {
                      return x.getUserId().equals(userId);
                    }));
  }

  public void addCheckin(Integer userID) {

    Checkin checkin = new Checkin();
    checkin.setUserId(userID);
    checkin.setCreateTime(new Date());
    checkinRepository.save(checkin);
  }
}
