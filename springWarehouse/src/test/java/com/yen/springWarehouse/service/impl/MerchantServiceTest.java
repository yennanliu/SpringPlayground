package com.yen.springWarehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yen.springWarehouse.bean.Merchant;
import com.yen.springWarehouse.mapper.MerchantMapper;
import com.yen.springWarehouse.service.MerchantService;
import com.yen.springWarehouse.util.MerchantQueryHelper;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
//@ExtendWith(MockitoExtension.class)
public class MerchantServiceTest {

//    private MerchantServiceImpl merchantService;

    @Mock
    private MerchantMapper merchantMapper;

    @BeforeEach
    public void setUp(){
         // TODO : check it
        //merchantService = new MerchantServiceImpl(merchantMapper);
    }

    @Test
    public void testGetMerchantPage(){

        // init
        Page<Merchant> page = new Page<>(1,2);
        QueryWrapper<Merchant> merchantWrapper = new QueryWrapper<>();
        merchantWrapper.like("name", new MerchantQueryHelper().getQryMerchantName());

        // arrange
        given(merchantMapper.getMerchantList(page, merchantWrapper)).willReturn(new ArrayList<>());

        // act and assert
//        MerchantQueryHelper helper = new MerchantQueryHelper();
//        Page<Merchant> merchantPage = merchantService.getMerchantPage(helper,1,1);
//        assertThat(merchantPage.getPages()).isEqualTo(1);
    }

}