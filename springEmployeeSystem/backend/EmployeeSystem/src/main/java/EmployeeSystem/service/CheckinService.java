package EmployeeSystem.service;

import EmployeeSystem.model.Checkin;
import EmployeeSystem.repository.CheckinRepository;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckinService {

  @Autowired CheckinRepository checkinRepository;

  public List<Checkin> getCheckIns() {

    return checkinRepository.findAll();
  }

  public Page<Checkin> getCheckInsPage(int page, int size) {
    return checkinRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
  }

  public List<Checkin> getCheckinByUserId(Integer userId) {
    return checkinRepository.findByUserId(userId);
  }

  @Transactional
  public void addCheckin(Integer userID) {

    Checkin checkin = new Checkin();
    checkin.setUserId(userID);
    checkin.setCreateTime(new Date());
    checkinRepository.save(checkin);
  }
}
