package com.vansl.sign.dao;

import com.vansl.sign.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {

    /**
     * 根据名称查询课程
     */
    @Query("from Course c where c.name=:name")
    Course findCourseByName(@Param("name") String name);



}