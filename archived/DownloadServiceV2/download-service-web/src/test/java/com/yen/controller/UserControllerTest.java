package com.yen.controller;

// https://www.tpisoftware.com/tpu/articleDetails/1256

import com.yen.bean.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    private User user;

    @Before
    public void setup() throws Exception {
        this.mockMvc = standaloneSetup(this.userController).build();// Standalone context

        User u1 = new User("may", 15);
    }

    @Test
    public void testGetUser() throws Exception{

        // Mocking
        //when(this.employeeService.getEmployeeByName("randy")).thenReturn(employee);

        ResultActions resultActions = mockMvc.perform(get("/user").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful());
    }

}
