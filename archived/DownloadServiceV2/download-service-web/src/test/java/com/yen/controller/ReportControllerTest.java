//package com.yen.controller;
//
//// https://www.tpisoftware.com/tpu/articleDetails/1256
//
//import com.yen.bean.Admin;
//import com.yen.bean.Employee;
//import com.yen.bean.User;
//import com.yen.service.ReportService;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import java.util.HashMap;
//
//import static org.hamcrest.Matchers.is;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class ReportControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    ReportService reportService;
//
//    @MockBean
//    private Admin admin;
//
//    @MockBean
//    private User user;
//
//    private Employee employee;
//
//    @Autowired
//    ReportController reportController;
//
//    @Before
//    public void setup() throws Exception{
//        this.mockMvc = standaloneSetup(this.reportController).build();// Standalone context
//
//        // https://github.com/yennanliu/JavaHelloWorld/blob/main/src/main/java/Advances/MapDemo/demo2.java
//        Admin admin = new Admin("joe", new HashMap());
//
//        User user = new User("may", 17);
//
//        employee = new Employee();
//    }
//
//    @Test
//    public void test1() throws Exception {
//
//        //Mocking
//        when(reportService.getReportField("trade")).thenReturn(employee);
//
//        ResultActions resultActions = mockMvc.perform(get("/employee/randy").contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("name", is("randy")));
//        }
//
//}
