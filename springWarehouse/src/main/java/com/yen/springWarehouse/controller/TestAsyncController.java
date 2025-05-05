package com.yen.springWarehouse.controller;

import com.yen.springWarehouse.service.AsyncTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

// https://blog.csdn.net/u010986241/article/details/138205146

@Controller
@RequestMapping("/test_async")
@Slf4j
public class TestAsyncController {

    @Autowired
    private AsyncTestService asyncTestService;

    @GetMapping("/run_task")
    @ResponseBody
    public String triggerAyncTasks(){

        String result = null;

        /** task 1 */
        Runnable task1 = () -> System.out.println("Execute task 1 ...");
        asyncTestService.executeAsyncTask(task1);

        /** task 2 */
        Callable<String> task2 = () -> {
            Thread.sleep(2000);
            return "Task 2 result";
        };

        Future<String> futureTask = asyncTestService.submitAsyncTask(task2);
        // get async result
        try{
            result = futureTask.get();

        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }

        return result;
    }

}
