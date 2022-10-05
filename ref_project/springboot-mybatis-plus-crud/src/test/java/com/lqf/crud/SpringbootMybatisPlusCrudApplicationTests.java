package com.lqf.crud;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lqf.crud.bean.crm.User;
import com.lqf.crud.dao.crm.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootMybatisPlusCrudApplicationTests {

    @Autowired
    private UserMapper mapper;

    /**
     * <p>
     *     insert 插入测试
     * </p>
     */
    @Test
    public void insertLoads() {
        User user = new User();
        user.setEmail("lqf@163.com");
        user.setAge(12);

        Integer insert = mapper.insert(user);
        System.out.println("return insert value = " + insert);
    }

    /**
     *<P>
     *     通过id更新信息
     *</P>
     */
    @Test
    public void updateByIdLoads() {
        User user = new User();
        user.setAge(123);
        user.setEmail("weqee@163.com");
        user.setId(1L);

        Integer insert = mapper.updateById(user);
        System.out.println("return insert value = " + insert);
    }


    /**
     * <P>
     *     deleteBatchIds 根据id批量删除
     * </P>
     */
    @Test
    public void deleteLoads() {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        list.add(3L);

        Integer insert = mapper.deleteBatchIds(list);
        System.out.println("return deleteBatchIds value = " + insert);
    }

    /**
     * <P>
     *     deleteById 根据id删除
     * </P>
     */
    @Test
    public void deleteByIdLoads() {
        Integer deleteById = mapper.deleteById(4L);
        System.out.println("return deleteById value = " + deleteById);
    }

    /**
     * <p>
     * deleteByMap 根据map条件进行删除
     * </P>
     */
    @Test
    public void deleteByMapsLoads() {
        HashMap<String, Object> map = new HashMap<>(16);
        map.put("email", "lqf@163.com");
        map.put("age", 12);

        Integer insert = mapper.deleteByMap(map);
        System.out.println("return deleteByMap value = " + insert);
    }


    /**
     * <p>
     * 通过id查询对象
     * </P>
     */
    @Test
    public void selectByIdLoads() {
        User user = mapper.selectById(4L);
        System.out.println("return insert value = " + user);
    }

    /**
     * <p>
     * 通过多个id进行查询
     * </P>
     */
    @Test
    public void selectBatchIdsLoads() {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        list.add(3L);
        List<User> list1 = mapper.selectBatchIds(list);
        System.out.println("return selectBatchIds value = " + list1);
    }

    /**
     * <p>
     * 通过条件进行实体list查询
     * </P>
     */
    @Test
    public void selectByMapLoads() {
        HashMap<String, Object> map = new HashMap<>(16);
        map.put("email", "lqf@163.com");
        map.put("age", 12);
        List<User> list = mapper.selectByMap(map);
        System.out.println("return selectByMap value = " + list);
    }

    /**
     * <p>
     * 分页查询
     * </P>
     */
    @Test
    public void selectPageLoads() {
        Page<User> page = new Page<>(1,5);
        IPage<User> lstUser = mapper.selectPage(page, null);
        System.out.println("return selectPageLoads value = " + lstUser);
    }

}
