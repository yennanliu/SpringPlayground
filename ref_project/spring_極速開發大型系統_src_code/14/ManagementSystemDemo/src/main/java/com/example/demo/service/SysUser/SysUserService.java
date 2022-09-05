package com.example.demo.service.SysUser;

import com.example.demo.entity.sysuser.SysUser;
import org.springframework.data.domain.Page;

/**
 * @author longzhonghua
 * @data 2018/11/04 22:30
 */

public interface SysUserService {
    void save(SysUser adminUser);//儲存使用者
    Page<SysUser> PageByAdminUser(Integer page, Integer size);//對使用者資料進行分頁
    //public SysUser findByUserName(String username);

}
