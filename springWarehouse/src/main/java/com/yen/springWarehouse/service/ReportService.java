package com.yen.springWarehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yen.springWarehouse.bean.Report;

import java.util.List;

public interface ReportService extends IService<Report> {

  List<Report> getReport();
}
