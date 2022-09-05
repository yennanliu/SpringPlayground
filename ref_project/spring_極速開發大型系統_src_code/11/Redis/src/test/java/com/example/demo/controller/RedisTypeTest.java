package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Copyright (C), 2019-2019, XXX有限公司
 * FileName: RedisTypeTest
 * Author:   longzhonghua
 * Date:     4/8/2019 9:48 PM
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改時間           版本號              描述
 */
@RunWith(SpringRunner.class)
@SpringBootTest

public class RedisTypeTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void string() {
        redisTemplate.opsForValue().set("num", 123);
        redisTemplate.opsForValue().set("string", "some strings");
        Object s = redisTemplate.opsForValue().get("num");
        Object s2 = redisTemplate.opsForValue().get("string");
        System.out.println(s);
        System.out.println(s2);
    }

    @Test
    public void string2() {
        //設定的是3秒失效，3秒之內查詢有結果，3秒之後傳回為null
        redisTemplate.opsForValue().set("num", "123XYZ", 3, TimeUnit.SECONDS);

        try {
            Object s = redisTemplate.opsForValue().get("num");
            System.out.println(s);
            Thread.currentThread().sleep(2000);
            Object s2 = redisTemplate.opsForValue().get("num");
            System.out.println(s2);
            Thread.currentThread().sleep(5000);
            Object s3 = redisTemplate.opsForValue().get("num");
            System.out.println(s3);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

    }

    @Test
    public void string3() {
        // 覆寫(overwrite) 指定 key 所儲存的字串值，從偏移量 offset 開始
        redisTemplate.opsForValue().set("key", "helloworld", 7);
        System.out.println(redisTemplate.opsForValue().get("key"));

    }

    @Test
    public void string4() {
        //設定鍵的字串值並傳回其舊值
        redisTemplate.opsForValue().set("getSetTest", "test");
        System.out.println(redisTemplate.opsForValue().getAndSet("getSetTest", "test2"));
        System.out.println(redisTemplate.opsForValue().get("getSetTest"));
    }

    @Test
    public void string5() {
        redisTemplate.opsForValue().append("k", "test");
        System.out.println(redisTemplate.opsForValue().get("k"));

        redisTemplate.opsForValue().append("k", "test2");
        System.out.println(redisTemplate.opsForValue().get("k"));

    }

    @Test
    public void string6() {
        redisTemplate.opsForValue().set("key", "1");
        System.out.println(redisTemplate.opsForValue().size("key"));
    }

    @Test
    public void hash1() {
        Map<String, Object> testMap = new HashMap();
        testMap.put("name", "zhonghua");
        testMap.put("sex", "male");
        redisTemplate.opsForHash().putAll("Hash", testMap);
        System.out.println(redisTemplate.opsForHash().entries("Hash"));
    }

    @Test
    public void hash2() {
        redisTemplate.opsForHash().put("redisHash", "name", "hongwei");
        redisTemplate.opsForHash().put("redisHash", "sex", "male");
        System.out.println(redisTemplate.opsForHash().entries("redisHash"));
        System.out.println(redisTemplate.opsForHash().values("redisHash"));
    }

    @Test
    public void hash3() {
        redisTemplate.opsForHash().put("redisHash", "name", "hongwei");
        redisTemplate.opsForHash().put("redisHash", "sex", "male");
        System.out.println(redisTemplate.opsForHash().delete("redisHash", "name"));
        System.out.println(redisTemplate.opsForHash().entries("redisHash"));

    }

    @Test
    public void hash4() {
        redisTemplate.opsForHash().put("redisHash", "name", "hongwei");
        redisTemplate.opsForHash().put("redisHash", "sex", "male");
        System.out.println(redisTemplate.opsForHash().hasKey("redisHash", "name"));
        System.out.println(redisTemplate.opsForHash().hasKey("redisHash", "age"));


    }

    @Test
    //從鍵中的哈希取得指定hashKey的值
    public void hash7() {
        redisTemplate.opsForHash().put("redisHash", "name", "hongwei");
        redisTemplate.opsForHash().put("redisHash", "sex", "male");
        System.out.println(redisTemplate.opsForHash().get("redisHash", "name"));
    }

    @Test
    //取得key所對應的雜湊表的key
    public void hash8() {
        redisTemplate.opsForHash().put("redisHash", "name", "hongwei");
        redisTemplate.opsForHash().put("redisHash", "sex", "male");
        System.out.println(redisTemplate.opsForHash().keys("redisHash"));
    }

    @Test
    //取得key所對應的雜湊表的大小個數
    public void hash9() {
        redisTemplate.opsForHash().put("redisHash", "name", "hongwei");
        redisTemplate.opsForHash().put("redisHash", "sex", "male");
        System.out.println(redisTemplate.opsForHash().size("redisHash"));
    }


    @Test
    //批次把一個陣列插入到清單中
    public void list1() {
        String[] strings = new String[]{"1", "2", "3"};
        redisTemplate.opsForList().leftPushAll("list", strings);
        System.out.println(redisTemplate.opsForList().range("list", 0, -1));
    }

    @Test
    //傳回儲存在鍵中的清單的長度。若果鍵不存在，則將其解釋為空清單，並傳回0。當key儲存的值不是清單時傳回錯誤。
    public void list2() {
        String[] strings = new String[]{"1", "2", "3"};
        redisTemplate.opsForList().leftPushAll("list", strings);
        System.out.println(redisTemplate.opsForList().size("list"));
    }

    @Test
    //將所有特殊的值插入儲存在鍵的清單的頁首。若果鍵不存在，則在執行推送動作之前將其建立為空清單。（從左邊插入）
    public void list3() {

        redisTemplate.opsForList().leftPush("list", "1");
        System.out.println(redisTemplate.opsForList().size("list"));
        redisTemplate.opsForList().leftPush("list", "2");
        System.out.println(redisTemplate.opsForList().size("list"));
        redisTemplate.opsForList().leftPush("list", "3");
        System.out.println(redisTemplate.opsForList().size("list"));
    }

    @Test
    //將所有特殊的值插入儲存在鍵的清單的頁首。若果鍵不存在，則在執行推送動作之前將其建立為空清單。（從右邊插入）
    public void list4() {

        redisTemplate.opsForList().rightPush("listRight", "1");
        System.out.println(redisTemplate.opsForList().size("listRight"));
        redisTemplate.opsForList().rightPush("listRight", "2");
        System.out.println(redisTemplate.opsForList().size("listRight"));
        redisTemplate.opsForList().rightPush("listRight", "3");
        System.out.println(redisTemplate.opsForList().size("listRight"));
    }

    @Test
    //
    public void list5() {

        String[] strings = new String[]{"1", "2", "3"};
        redisTemplate.opsForList().rightPushAll("list", strings);
        System.out.println(redisTemplate.opsForList().range("list", 0, -1));

    }

    @Test
    //在清單中index的位置設定value值
    public void list6() {
        String[] strings = new String[]{"1", "2", "3"};
        redisTemplate.opsForList().rightPushAll("list6", strings);
        System.out.println(redisTemplate.opsForList().range("list6", 0, -1));
        redisTemplate.opsForList().set("list6", 1, "值");
        System.out.println(redisTemplate.opsForList().range("list6", 0, -1));
    }

    @Test
    //移除清單中儲存的清單中第一次次出現的
    public void list7() {
        String[] strings = new String[]{"1", "2", "3"};
        redisTemplate.opsForList().rightPushAll("list7", strings);
        System.out.println(redisTemplate.opsForList().range("list7", 0, -1));
        redisTemplate.opsForList().remove("list7", 1, "2");//將移除清單中儲存的清單中第一次次出現的“2”。
        System.out.println(redisTemplate.opsForList().range("list7", 0, -1));
    }

    @Test
    //根據下表取得清單中的值，索引是從0開始的
    public void list8() {
        String[] strings = new String[]{"1", "2", "3"};
        redisTemplate.opsForList().rightPushAll("list8", strings);
        System.out.println(redisTemplate.opsForList().range("list8", 0, -1));
        System.out.println(redisTemplate.opsForList().index("list8", 2));
    }

    @Test
    //出現最左邊的元素，出現之後該值在清單中將不復存在
    public void list9() {
        String[] strings = new String[]{"1", "2", "3"};
        redisTemplate.opsForList().rightPushAll("list9", strings);
        System.out.println(redisTemplate.opsForList().range("list9", 0, -1));
        System.out.println(redisTemplate.opsForList().leftPop("list9"));
        System.out.println(redisTemplate.opsForList().range("list9", 0, -1));
    }

    @Test
    //出現最右邊的元素，出現之後該值在清單中將不復存在
    public void list10() {
        String[] strings = new String[]{"1", "2", "3"};
        redisTemplate.opsForList().rightPushAll("list10", strings);
        System.out.println(redisTemplate.opsForList().range("list10", 0, -1));
        System.out.println(redisTemplate.opsForList().rightPop("list10"));
        System.out.println(redisTemplate.opsForList().range("list10", 0, -1));
    }

    @Test
    //無序集合中加入元素，傳回加入個數
    public void Set1() {
        String[] strs = new String[]{"str1", "str2"};
        System.out.println(redisTemplate.opsForSet().add("Set1", strs));
        //也可以直接在add裡面加入多個值
        System.out.println(redisTemplate.opsForSet().add("Set1", "1", "2", "3"));
    }

    @Test
    //移除集合中一個或多個成員
    public void Set2() {
        String[] strs = new String[]{"str1", "str2"};
        System.out.println(redisTemplate.opsForSet().add("Set2", strs));
        System.out.println(redisTemplate.opsForSet().remove("set2", strs));
    }

    @Test
    //移除並傳回集合中的一個隨機元素
    public void Set3() {
        String[] strs = new String[]{"str1", "str2"};
        System.out.println(redisTemplate.opsForSet().add("Set3", strs));
        System.out.println(redisTemplate.opsForSet().pop("Set3"));
        System.out.println(redisTemplate.opsForSet().members("Set3"));

    }

    @Test
    //將 member 元素從進行搬移
    public void Set4() {
        String[] strs = new String[]{"str1", "str2"};
        System.out.println(redisTemplate.opsForSet().add("Set4", strs));
        redisTemplate.opsForSet().move("Set4", "str2", "Set4to2");
        System.out.println(redisTemplate.opsForSet().members("Set4"));
        System.out.println(redisTemplate.opsForSet().members("Set4to2"));
    }

    @Test
    //無序集合的大小長度
    public void Set5() {
        String[] strs = new String[]{"str1", "str2"};
        System.out.println(redisTemplate.opsForSet().add("Set5", strs));
        System.out.println(redisTemplate.opsForSet().size("Set5"));
    }

    @Test
    //傳回集合中的所有成員
    public void Set6() {
        String[] strs = new String[]{"str1", "str2"};
        System.out.println(redisTemplate.opsForSet().add("Set6", strs));
        System.out.println(redisTemplate.opsForSet().members("Set6"));
    }

    @Test
    //檢查set
    public void Set7() {
        String[] strs = new String[]{"str1", "str2"};
        System.out.println(redisTemplate.opsForSet().add("Set7", strs));
        Cursor<Object> curosr = redisTemplate.opsForSet().scan("Set7", ScanOptions.NONE);
        while (curosr.hasNext()) {
            System.out.println(curosr.next());
        }

    }

    @Test
    //新增一個有序集合
    public void Zset1() {
        ZSetOperations.TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<>("zset-1", 9.6);
        ZSetOperations.TypedTuple<Object> objectTypedTuple2 = new DefaultTypedTuple<>("zset-2", 9.9);
        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<ZSetOperations.TypedTuple<Object>>();
        tuples.add(objectTypedTuple1);
        tuples.add(objectTypedTuple2);
        System.out.println(redisTemplate.opsForZSet().add("zset1", tuples));
        System.out.println(redisTemplate.opsForZSet().range("zset1", 0, -1));
    }


    @Test
    //新增一個有序集合，存在的話為false，不存在的話為true
    public void Zset2() {
        System.out.println(redisTemplate.opsForZSet().add("zset2", "zset-1", 1.0));
        System.out.println(redisTemplate.opsForZSet().add("zset2", "zset-1", 1.0));
    }

    @Test
    //從有序集合中移除一個或是多個元素
    public void Zset3() {
        System.out.println(redisTemplate.opsForZSet().add("zset3", "zset-1", 1.0));
        System.out.println(redisTemplate.opsForZSet().add("zset3", "zset-2", 1.0));
        System.out.println(redisTemplate.opsForZSet().range("zset3", 0, -1));
        System.out.println(redisTemplate.opsForZSet().remove("zset3", "zset-2"));
        System.out.println(redisTemplate.opsForZSet().range("zset3", 0, -1));

    }

    @Test
    //傳回有序集中指定成員的排名，其中有序整合員按分數值遞增(從小到大)順序排序
    public void Zset4() {
        System.out.println(redisTemplate.opsForZSet().add("zset4", "zset-1", 1.0));
        System.out.println(redisTemplate.opsForZSet().add("zset4", "zset-2", 1.0));
        System.out.println(redisTemplate.opsForZSet().range("zset4", 0, -1));
        System.out.println(redisTemplate.opsForZSet().rank("zset4", "zset-1"));


    }

    @Test
    //透過索引區間傳回有序集合成指定區間內的成員，其中有序整合員按分數值遞增(從小到大)順序排序
    public void Zset5() {
        ZSetOperations.TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<>("zset-1", 9.6);
        ZSetOperations.TypedTuple<Object> objectTypedTuple2 = new DefaultTypedTuple<>("zset-2", 9.1);
        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<ZSetOperations.TypedTuple<Object>>();
        tuples.add(objectTypedTuple1);
        tuples.add(objectTypedTuple2);
        System.out.println(redisTemplate.opsForZSet().add("zset5", tuples));
        System.out.println(redisTemplate.opsForZSet().range("zset5", 0, -1));


    }


    @Test
    //透過分數傳回有序集合指定區間內的成員個數
    public void Zset6() {
        ZSetOperations.TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<>("zset-1", 3.6);
        ZSetOperations.TypedTuple<Object> objectTypedTuple2 = new DefaultTypedTuple<>("zset-2", 4.1);
        ZSetOperations.TypedTuple<Object> objectTypedTuple3 = new DefaultTypedTuple<>("zset-3", 5.7);
        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<ZSetOperations.TypedTuple<Object>>();
        tuples.add(objectTypedTuple1);
        tuples.add(objectTypedTuple2);
        tuples.add(objectTypedTuple3);
        System.out.println(redisTemplate.opsForZSet().add("zset6", tuples));
        System.out.println(redisTemplate.opsForZSet().rangeByScore("zset6", 0, 9));
        System.out.println(redisTemplate.opsForZSet().count("zset6", 0, 5));


    }

    @Test
    //取得有序集合的成員數
    public void Zset7() {
        ZSetOperations.TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<>("zset-1", 3.6);
        ZSetOperations.TypedTuple<Object> objectTypedTuple2 = new DefaultTypedTuple<>("zset-2", 4.1);
        ZSetOperations.TypedTuple<Object> objectTypedTuple3 = new DefaultTypedTuple<>("zset-3", 5.7);
        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<ZSetOperations.TypedTuple<Object>>();
        tuples.add(objectTypedTuple1);
        tuples.add(objectTypedTuple2);
        tuples.add(objectTypedTuple3);
        System.out.println(redisTemplate.opsForZSet().add("zset7", tuples));
        System.out.println(redisTemplate.opsForZSet().size("zset7"));


    }

    @Test
    //取得指定成員的score值
    public void Zset8() {
        ZSetOperations.TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<>("zset-1", 3.6);
        ZSetOperations.TypedTuple<Object> objectTypedTuple2 = new DefaultTypedTuple<>("zset-2", 4.1);
        ZSetOperations.TypedTuple<Object> objectTypedTuple3 = new DefaultTypedTuple<>("zset-3", 5.7);
        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<ZSetOperations.TypedTuple<Object>>();
        tuples.add(objectTypedTuple1);
        tuples.add(objectTypedTuple2);
        tuples.add(objectTypedTuple3);
        System.out.println(redisTemplate.opsForZSet().add("zset8", tuples));
        System.out.println(redisTemplate.opsForZSet().score("zset8", "zset-3"));


    }

    @Test
    //取得指定成員的score值
    public void Zset9() {
        ZSetOperations.TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<>("zset-1", 3.6);
        ZSetOperations.TypedTuple<Object> objectTypedTuple2 = new DefaultTypedTuple<>("zset-2", 5.1);
        ZSetOperations.TypedTuple<Object> objectTypedTuple3 = new DefaultTypedTuple<>("zset-3", 2.7);
        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<ZSetOperations.TypedTuple<Object>>();
        tuples.add(objectTypedTuple1);
        tuples.add(objectTypedTuple2);
        tuples.add(objectTypedTuple3);
        System.out.println(redisTemplate.opsForZSet().add("zset9", tuples));
        System.out.println(redisTemplate.opsForZSet().range("zset9", 0, -1));
        System.out.println(redisTemplate.opsForZSet().removeRange("zset9", 1, 2));
        System.out.println(redisTemplate.opsForZSet().range("zset9", 0, -1));


    }

    @Test
    //檢查zset
    public void Zset10() {
        ZSetOperations.TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<>("zset-1", 3.6);
        ZSetOperations.TypedTuple<Object> objectTypedTuple2 = new DefaultTypedTuple<>("zset-2", 5.1);
        ZSetOperations.TypedTuple<Object> objectTypedTuple3 = new DefaultTypedTuple<>("zset-3", 2.7);
        Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<ZSetOperations.TypedTuple<Object>>();
        tuples.add(objectTypedTuple1);
        tuples.add(objectTypedTuple2);
        tuples.add(objectTypedTuple3);
        System.out.println(redisTemplate.opsForZSet().add("zset10", tuples));
        Cursor<ZSetOperations.TypedTuple<Object>> cursor = redisTemplate.opsForZSet().scan("zset10", ScanOptions.NONE);
        while (cursor.hasNext()) {
            ZSetOperations.TypedTuple<Object> item = cursor.next();
            System.out.println(item.getValue() + ":" + item.getScore());
        }
    }

    @Test
    //檢查zset
    public void Zsetdss() {

        //有十個庫存
        Integer count = 100;
        //新增到redis list中

        for (Integer i = 0; i < count; i++) {
            redisTemplate.opsForList().leftPush("slist", 1);
        }
        System.out.println(redisTemplate.opsForList().range("slist", 0, -1));
    }

    @Test
    //檢查zset
    public void s2() {

        //判斷計數器
        if (redisTemplate.opsForList().size("slist") > 0) {
            long user_id = 1903;
            redisTemplate.opsForList().leftPush("ulist", user_id);
        }
        System.out.println(redisTemplate.opsForList().range("slist", 0, -1));
        System.out.println(redisTemplate.opsForList().range("ulist", 0, -1));
    }
}