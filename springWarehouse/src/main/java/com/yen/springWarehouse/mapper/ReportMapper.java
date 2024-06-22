package com.yen.springWarehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yen.springWarehouse.bean.Report;

import java.util.List;

public interface ReportMapper extends BaseMapper<Report> {

  List<Report> getReport();
}
