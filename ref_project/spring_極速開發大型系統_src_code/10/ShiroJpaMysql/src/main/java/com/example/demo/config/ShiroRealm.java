package com.example.demo.config;

import com.example.demo.dao.AdminDao;
import com.example.demo.entity.Admin;
import com.example.demo.entity.SysPermission;
import com.example.demo.entity.SysRole;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;

public class ShiroRealm extends AuthorizingRealm {
    @Resource
    private AdminDao adminDao;

    @Override
    /**
     * 權限組態
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //拿到使用者訊息
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Admin adminInfo = (Admin) principals.getPrimaryPrincipal();

        for (SysRole role : adminInfo.getRoleList()) {
            //將角色放入SimpleAuthorizationInfo
            info.addRole(role.getRole());
            //使用者擁有的權限
            for (SysPermission p : role.getPermissions()) {
                info.addStringPermission(p.getPermission());
            }
        }
        return info;
    }

    /**
     * 進行身份認證,判斷使用者名稱密碼是否比對正確
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        //取得使用者的輸入的賬號
        String username = (String) token.getPrincipal();
        System.out.println(token.getCredentials());
        //透過username從資料庫中查詢 User物件，若果找到，沒找到.
        //Shiro有時間間隔機制，2分鍾內不會重復執行該方法
        //取得使用者訊息
        Admin adminInfo = adminDao.findByUsername(username);

        if (adminInfo == null) {
            return null;
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                /**
                 * 使用者名稱
                 */
                adminInfo,
                /**
                 * 密碼
                 */
                adminInfo.getPassword(),
                ByteSource.Util.bytes(adminInfo.getCredentialsSalt()),
                /**
                 * realm name
                 */
                getName()
        );
        return info;
    }

}