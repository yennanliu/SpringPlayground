package com.yen.springMybatisDemo1.util;

// https://www.youtube.com/watch?v=DACvS6eOiGI&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=12
// https://www.youtube.com/watch?v=EYMDtHRLyCM&list=PLmOn9nNkQxJEWFBs6hVmDC5m8SbbIiDwY&index=13

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlUtil {

    public SqlSession sqlBuilder(){

        // load conf
        InputStream is = null;
        try {
            is = Resources.getResourceAsStream("mybatis-config.xml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // get SqlSessionFactoryBuilder
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();

        // get sqlSessionFactoryBuilder
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
        
        // get session
        /**
         *  SqlSession by default is non auto commit
         *  -> we can enable auto commit via sqlSessionFactory.openSession(true);
         */
        SqlSession sqlSession = sqlSessionFactory.openSession(true); // true : auto commit

        return sqlSession;
    }

}
