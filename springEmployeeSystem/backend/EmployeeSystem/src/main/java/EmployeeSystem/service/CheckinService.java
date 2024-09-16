package EmployeeSystem.service;

import EmployeeSystem.model.Checkin;
import EmployeeSystem.model.dto.AddCheckinDto;
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

  public Mono<Checkin> addCheckin(AddCheckinDto addCheckinDto) {

    Checkin checkin = new Checkin();
    checkin.setUserId(addCheckinDto.getUserId());
    checkin.setCreateTime(new Date());
    //return checkinRepository.save(checkin);
    return checkinRepository.save(checkin)
            .doOnSuccess(savedCheckin -> {
              System.out.println("checkin saved: " + savedCheckin);
            });
  }
}
