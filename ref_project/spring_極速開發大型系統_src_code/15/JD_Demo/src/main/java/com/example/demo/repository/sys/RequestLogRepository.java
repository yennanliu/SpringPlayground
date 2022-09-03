package com.example.demo.repository.sys;

import com.example.demo.entity.sys.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author longzhonghua
 * @createdata 3/6/2019 8:41 PM
 * @description 會員登入日志
 */
public interface RequestLogRepository extends JpaRepository<RequestLog,Long> {
    RequestLog findById(long id);

}
