package EmployeeSystem.service;

import EmployeeSystem.model.Checkin;
import EmployeeSystem.repository.CheckinRepository;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckinService {

  @Autowired CheckinRepository checkinRepository;

  public List<Checkin> getCheckIns() {

    return checkinRepository.findAll();
  }

  public List<Checkin> getCheckinByUserId(Integer userId) {

    List<Checkin> checkinList = checkinRepository.findAll();
    return checkinList.stream()
        .filter(
            x -> {
              return x.getUserId().equals(userId);
            })
        .collect(Collectors.toList());
  }

  public void addCheckin(Integer userID) {

    Checkin checkin = new Checkin();
    checkin.setUserId(userID);
    checkin.setCreateTime(new Date());
    checkinRepository.save(checkin);
  }
}
