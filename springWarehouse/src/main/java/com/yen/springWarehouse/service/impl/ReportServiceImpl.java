package com.yen.springWarehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.springWarehouse.bean.Report;
import com.yen.springWarehouse.mapper.ReportMapper;
import com.yen.springWarehouse.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

  @Autowired ReportMapper reportMapper;

  @Override
  public List<Report> getReport() {

    return reportMapper.getReport();
  }
}
