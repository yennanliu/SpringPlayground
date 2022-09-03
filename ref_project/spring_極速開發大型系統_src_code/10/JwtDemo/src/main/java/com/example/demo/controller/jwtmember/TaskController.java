package com.example.demo.controller.jwtmember;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author longzhonghua
 * @data 3/5/2019 8:52 PM
 */
@RestController
@RequestMapping("/jwt/tasks")
public class TaskController {

    @GetMapping
    public String listTasks(){
        return "工作清單";
    }

    @PostMapping

   @PreAuthorize("hasRole('USER')")
    public String newTasks(){
        return "角色ROLE,建立了一個新的工作";
    }

    @PutMapping("/{taskId}")
    public String updateTasks(@PathVariable("taskId")Integer id){
        return "更新了一下id為:"+id+"的工作";
    }

    @DeleteMapping("/{taskId}")
    public String deleteTasks(@PathVariable("taskId")Integer id){
        return "移除了id為:"+id+"的工作";
    }
}