package com.yen.controller;

import com.yen.bean.Car;
import com.yen.service.CarService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class CarControllerTest {

    private MockMvc mockMvc;

    @Autowired
    CarController carController;

    @MockBean
    private CarService carService;

    private Car car;

    @Before
    public void setup() throws Exception {
        this.mockMvc = standaloneSetup(this.carController).build(); // Standalone context

        Car car1 = new Car("ZJZ", 1000);
    }

}
