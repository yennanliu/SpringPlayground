package com.yen.pocDownloadService;

// https://www.youtube.com/watch?v=gFz5MLFSQKQ&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=85

import com.yen.data.bean.Task;
import com.yen.data.bean.User;
import org.junit.jupiter.api.Test;

public class test1 {

    @Test
    public void testLoadModule(){

        User u1 = new User(100, "JACK", 1);
        
        Task t1 = new Task(001, new Integer[]{1,2,3}, new String[]{"colA", "colB"},"trade", 200, 300, "running");
        Task t2 = new Task(001, new Integer[]{4,5}, new String[]{"colC", "colD"},"deposit", 500, 600, "completed");

        System.out.println(">>> u1 = " + u1);
        System.out.println(">>> t1 = " + t1);
    }

}
