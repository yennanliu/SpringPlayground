package com.vansl.sign.service;

import com.vansl.sign.dao.SignRepository;
import com.vansl.sign.entity.Course;
import com.vansl.sign.entity.Sign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignService {

    @Autowired
    SignRepository signRepository;

    public Sign findSignById(Long id){
        return signRepository.findById(id).get();
    }

    public void save(Sign sign){
        signRepository.save(sign);
    }

    public List<Sign> findSignsByCourse(Long courseId){
        return signRepository.findSignsByCourse(courseId);
    }

    public Boolean signIsEnd(Long id){
        return signRepository.findById(id).get().getEnd();
    }

    public List<Sign> findSignsNotEndByCourse(Long courseId){
        return signRepository.findSignsNotEndByCourse(courseId);
    };
}
