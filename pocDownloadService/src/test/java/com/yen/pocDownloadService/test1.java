package com.yen.pocDownloadService;

// https://www.youtube.com/watch?v=gFz5MLFSQKQ&list=PLmOn9nNkQxJFKh2PMfWbGT7RVuMowsx-u&index=85

import com.yen.data.bean.Task;
import com.yen.data.bean.User;
import org.junit.jupiter.api.Test;

public class test1 {

    @Test
    public void testLoadModule(){
        
        User u1 = new User(100, "JACK", 1);
        Task t1 = new Task(1, 100, 200, "trading", "success");

        System.out.println(">>> u1 = " + u1);
        System.out.println(">>> t1 = " + t1);
    }

}
