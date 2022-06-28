//package com.yen.mapper;
//
//// https://www.tpisoftware.com/tpu/articleDetails/1256
//
//import com.yen.bean.User;
//
//import org.junit.Assert;
//import org.junit.Before;
////import org.junit.jupiter.api.Test;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.junit.Assert.assertEquals;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@DataJpaTest  // use in-memory H2 DB for test
////@ContextConfiguration(classes = DaoConfig.class)
//public class UserMapperTest {
//
//    @Autowired
//    UserMapper userMapper;
//
//    private User u1 = new User("amy", 10);
//
//    @Before
//    public void init(){
//        userMapper.insertUser(u1);
//    }
//
//    @Test
//    public void test1(){
//        User u1 = userMapper.getUserByName("amy");
//        assertEquals("amy", u1.getName());
//    }
//
//}
