package com.yen.mdblog.controller;

import com.yen.mdblog.entity.Po.Comment;
import com.yen.mdblog.service.CommentService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    public void testCreateComment() throws Exception {

        // mock service (productService.getProduct)
        when(commentService
                .insertComment(anyString(), anyLong(), anyString()))
                .thenReturn(true);

        // TODO : fix below
        // test post call
        // https://stackoverflow.com/questions/21749781/why-i-received-an-error-403-with-mockmvc-and-junit
//        var TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
//        var httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
//        var csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
//
//        mockMvc.perform(post("/comment/create")
//                        .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
//                        .param(csrfToken.getParameterName(), csrfToken.getToken())
//                ).andExpect(status().isOk());

    }

}
