package com.vansl.sign.dao;

import com.vansl.sign.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {

    /**
     * 根据姓名查询学生
     */
    @Query("from Student s where s.name=:name")
    Student findStudentByName(@Param("name") String name);

}