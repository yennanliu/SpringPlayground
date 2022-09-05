package com.example.demo.entity;

import com.example.demo.repository.CardRepository;
import com.example.demo.repository.StudentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: Test
 * Author:   longzhonghua
 * Date:     2019/4/27 20:30
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class oneToOneTest {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CardRepository cardRepository;

    @Test
    public void testOneToOne() {
        Student student1 = new Student();
        student1.setName("趙大偉");
        student1.setSex("male");
        Student student2 = new Student();
        student2.setName("趙大寶");
        student2.setSex("male");

        Card card1 = new Card();
        card1.setNum(422802);
        student1.setCard(card1);
        studentRepository.save(student1);
        studentRepository.save(student2);
        Card card2 = new Card();
        card2.setNum(422803);
        cardRepository.save(card2);
        /**
         * Description: 取得加入之後的id
         */
        Long id = student1.getId();
        /**
         * Description: 移除剛剛加入的student1
         */
        studentRepository.deleteById(id);
    }


}