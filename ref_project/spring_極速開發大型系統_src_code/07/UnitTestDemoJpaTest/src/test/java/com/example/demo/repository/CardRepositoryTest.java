package com.example.demo.repository;

import com.example.demo.entity.Card;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: CardRepositoryTest
 * Author:   longzhonghua
 * Date:     4/6/2019 3:07 PM
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */
@RunWith(SpringRunner.class)
// SpringJUnit支援，由此引入Spring-Test框架支援！
//啟動整個spring的專案
@SpringBootTest
//@DataJpaTest
@Transactional
//@Rollback(false)
public class CardRepositoryTest {
    @Autowired
    private  CardRepository  cardRepository;
    @Test
  public void testQuery() {
   // 查詢動作
  List<Card> list = cardRepository.findAll();
        for (Card card : list) {
            System.out.println(card);
        }
   }
    @Test
    public void testRollBank() {
        // 查詢動作
        Card card=new Card();

        card.setNum(3);
        cardRepository.save(card);
        //throw new RuntimeException();
    }




}