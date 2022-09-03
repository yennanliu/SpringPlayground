package com.example.demo.config.authenticationhandler.sysuser;

import com.example.demo.entity.sys.SysUserLoginLog;
import com.example.demo.repository.sys.SysUserLoginLogRepository;
import com.example.demo.util.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author longzhonghua
 * @data 2/26/2019 6:48 PM
 */

@Component("myAuthenticationSuccessHandler")
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private SysUserLoginLogRepository loginRecordRepository;

    //使用者名稱和密碼正確執行
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal != null && principal instanceof UserDetails) {


                        /*    UserDetails user = (UserDetails) principal;
                            //1、加入 Session
                            httpServletRequest.getSession().setAttribute("userDetail", user);
                            //2、寫入日志
                            logger.info("【使用者已登入】" + user.getUsername());
                            //3、寫入資料庫login_record表
                            LoginRecord loginRecord = new LoginRecord();
                            loginRecord.setLoginIp(IPUtil.getIpAddr(httpServletRequest));
                            loginRecord.setLoginTime(System.currentTimeMillis());
                            loginRecord.setUser((User) user);
                            loginRecordRepository.save(loginRecord);
                            //4、頁面跳躍到首頁
                            httpServletResponse.sendRedirect(ctx);//即 /forum*/
            UserDetails user = (UserDetails) principal;
            //1、加入 Session
            httpServletRequest.getSession().setAttribute("userDetail", user);

            //2、寫入日志
//                            logger.info("【使用者已登入】" + user.getUsername());
            //3、寫入資料庫login_record表
            SysUserLoginLog loginRecord = new SysUserLoginLog();
            loginRecord.setLoginip(IpUtils.getIpAddr(httpServletRequest));
            loginRecord.setLogintime(System.currentTimeMillis());
            loginRecord.setUsername(user.getUsername());
            loginRecord.setStates(1);
            loginRecordRepository.save(loginRecord);
            //4、頁面跳躍到首頁
            // httpServletResponse.sendRedirect(ctx);//即 /forum
       /*  jwt組態   String token = JwtTokenUtils.createToken(user.getUsername(),false);
            httpServletResponse.addHeader(JwtTokenUtils.TOKEN_HEADER,JwtTokenUtils.TOKEN_PREFIX+token);*/
            httpServletResponse.setContentType("application/json;charset=utf-8");
            PrintWriter out = httpServletResponse.getWriter();
            out.write("{\"status\":\"ok\",\"message\":\"登入成功\"}");
            out.flush();
            out.close();

        }

    }

}
