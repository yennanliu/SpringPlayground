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

import static org.mockito.Mockito.when;
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

        Car car1 = new Car("benz", 1000);
    }

    @Test
    public void testGetUser() throws Exception{

        // Mocking
        Car car1 = new Car("benz", 1000);
        when(this.carService.getCarByBrand("benz")).thenReturn(car1);

        ResultActions resultActions = mockMvc.perform(get("/car?brand=benz").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful());
    }

}
