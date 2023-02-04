package com.yen.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yen.common.utils.PageUtils;
import com.yen.gulimall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 
 *
 * @author yen
 * @email yen_dev@gmail.com
 * @date 2023-02-04 10:20:55
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

