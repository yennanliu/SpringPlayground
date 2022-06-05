package com.vansl.sign.dao;

import com.vansl.sign.entity.Sign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author: vansl
 * @create: 18-8-6 下午9:55
 */
public interface SignRepository extends JpaRepository<Sign,Long> {

    /**
     * 根据课程查询发起的签到
     */
    @Query("from Sign s where s.courseId=:courseId")
    List<Sign> findSignsByCourse(@Param("courseId") Long courseId);

    /**
     *
     * 查找未结束的发起的签到
     */
    @Query("from Sign s where s.courseId=:courseId and s.end=false")
    List<Sign> findSignsNotEndByCourse(@Param("courseId") Long courseId);
}
