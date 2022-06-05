package com.vansl.sign.dao;

import com.vansl.sign.entity.Course;
import com.vansl.sign.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository  extends JpaRepository<Teacher,Long> {

    /**
     * 根据姓名查询教师
     * @param name
     * @return
     */
    @Query("from Teacher t where t.name=:name")
    Teacher findTeacherByName(@Param("name") String name);

    @Query("from Course c where c.teacherId=:teacherId")
    List<Course> findCoursesByTeacher(@Param("teacherId") Long teacherId);
}


