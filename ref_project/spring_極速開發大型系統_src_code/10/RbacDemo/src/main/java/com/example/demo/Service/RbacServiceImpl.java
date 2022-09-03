package com.example.demo.Service;

import com.example.demo.entity.sysuser.SysPermission;
import com.example.demo.entity.sysuser.SysRole;
import com.example.demo.entity.sysuser.SysUser;
import com.example.demo.repository.SysUser.SysPermissionRepository;
import com.example.demo.repository.SysUser.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * @author longzhonghua
 * @data 2/26/2019 9:29 AM
 */

@Component("rbacService")
public class RbacServiceImpl implements RbacService {
    private org.springframework.util.AntPathMatcher AntPathMatcher = new AntPathMatcher();
    @Autowired
    private SysPermissionRepository permissionRepository;
    @Autowired
    private SysUserRepository sysUserRepository;
     @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        boolean hasPermission = false;

/**
 *這裡應該植入使用者和該使用者所擁有的權限（權限在登入成功的時候已經快取起來，當需要存取該使用者的權限是，直接從快取取出！）
 *然後驗證該請求是否有權限，有就傳回true，否則則傳回false不容許存取該Url。
 *還傳入了request,可以使用request取得該次請求的型態。
 *根據restful風格使用它來控制我們的權限
 *例如當這個請求是post請求，證明該請求是向伺服器傳送一個新增資源請求，
 *我們可以使用request.getMethod()來取得該請求的模式，然後在配合角色所容許的權限路徑進行判斷和授權動作！
 *若果能取得到Principal物件不為空證明，授權已經透過*/
        if (principal != null && principal instanceof UserDetails) {
            // 登入的使用者名稱
            String userName = ((UserDetails) principal).getUsername();
            //取得請求登入的url
            Set<String> urls = new HashSet<>();//使用者具備的系統資源集合，從資料庫讀取
            Set<String> curds = new HashSet<>();//使用者具備的系統資源集合，從資料庫讀取
            SysUser sysUser = sysUserRepository.findByName(userName);
            try {
                for (SysRole role : sysUser.getRoles()) {
                   for (SysPermission permission : role.getPermissions()) {
                       urls.add(permission.getUrl());
                       //curds.add(permission.getPermission());
                       curds.add(permission.getPermission()+permission.getUrl());
                   }

               }
            } catch (Exception e) {
                e.printStackTrace();
            }


            //urls.add("/sys/user/add");

            for (String url : urls) {
                if (AntPathMatcher.match(url, request.getRequestURI())) {

                    hasPermission = true;
                    break;
                }
            }
        }

        return hasPermission;
    }
}
