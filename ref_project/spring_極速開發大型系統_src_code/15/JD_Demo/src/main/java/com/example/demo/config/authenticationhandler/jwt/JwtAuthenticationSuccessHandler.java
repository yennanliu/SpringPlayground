package com.example.demo.config.authenticationhandler.jwt;

import com.example.demo.entity.sys.MemberLoginLog;
import com.example.demo.repository.sys.MemberLoginLogRepository;
import com.example.demo.util.IpUtils;
import com.example.demo.util.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * @author longzhonghua
 * @data 2/26/2019 6:48 PM
 */

@Component("jwtAuthenticationSuccessHandler")
public class JwtAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private MemberLoginLogRepository memberLoginLogRepository;
    //使用者名稱和密碼正確執行
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal != null && principal instanceof UserDetails) {
            UserDetails user = (UserDetails) principal;
            //1、加入 Session
            httpServletRequest.getSession().setAttribute("userDetail", user);

            //2、寫入日志
//                            logger.info("【使用者已登入】" + user.getUsername());
            //3、寫入資料庫login_record表
            MemberLoginLog loginRecord = new MemberLoginLog();
            loginRecord.setLoginip(IpUtils.getIpAddr(httpServletRequest));
            loginRecord.setLogintime(System.currentTimeMillis());
            loginRecord.setUsername(user.getUsername());
            loginRecord.setStates(1);
            loginRecord.setWay(2);
            memberLoginLogRepository.save(loginRecord);
            //4、頁面跳躍到首頁
            // httpServletResponse.sendRedirect(ctx);//即 /forum


         /*   String token = JwtTokenUtils.createToken(user.getUsername(),false);

            System.out.println("【登入成功，token->】"+JwtTokenUtils.TOKEN_PREFIX+token);
            httpServletResponse.addHeader(JwtTokenUtils.TOKEN_HEADER,JwtTokenUtils.TOKEN_PREFIX+token);*/
           /* //jwt組態

            String token = JwtTokenUtils.createToken(user.getUsername(), false);
            httpServletResponse.addHeader(JwtTokenUtils.TOKEN_HEADER, JwtTokenUtils.TOKEN_PREFIX + token);*/
          //  String role = JwtTokenUtils.getUserRole(token);
            //System.out.println(role+token+user.getUsername());
           String role = "";
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
            for (GrantedAuthority authority : authorities){
                role = authority.getAuthority();
            }


            String token = JwtTokenUtils.createToken(user.getUsername(), role, true);
            System.out.println("role"+role);
//        String token = JwtTokenUtils.createToken(jwtUser.getUsername(), false);
            // 傳回建立成功的token
            // 但是這裡建立的token只是單純的token
            // 按照jwt的規定，最後請求的時候應該是 `Bearer token`

            httpServletResponse.setHeader("token", JwtTokenUtils.TOKEN_PREFIX + token);
            httpServletResponse.setContentType("application/json;charset=utf-8");
            PrintWriter out = httpServletResponse.getWriter();
            out.write("{\"status\":\"ok\",\"message\":\"登入成功\"}");
            out.flush();
            out.close();
        }

    }

}
