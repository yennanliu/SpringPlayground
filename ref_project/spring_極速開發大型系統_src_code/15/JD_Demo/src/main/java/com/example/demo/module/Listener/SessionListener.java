package com.example.demo.module.Listener;

import com.example.demo.entity.sys.MemberOnline;
import com.example.demo.repository.sys.MemberOnlineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.util.HashSet;

/**
 * @author longzhonghua
 * @data 2/28/2019 2:51 PM
 */

@WebListener
public class SessionListener implements HttpSessionListener, HttpSessionAttributeListener {
    public static final Logger logger= LoggerFactory.getLogger(SessionListener.class);
    public static  int onlineNum=0;
    @Autowired
    private MemberOnlineRepository memberOnlineRepository;

    @Override
    public void  attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {
            }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {

        HttpSession session = event.getSession();
        ServletContext application = session.getServletContext();
        // 在application範圍由一個HashSet集儲存所有的session
        HashSet sessions = (HashSet) application.getAttribute("sessions");
        if (sessions == null) {
            sessions = new HashSet();
            application.setAttribute("sessions", sessions);

        }

        MemberOnline memberOnline= new MemberOnline();
        memberOnline.setSessionId(session.getId());
        memberOnline.setCreationTimes(session.getCreationTime());
        memberOnline.setLastAccessedTime(session.getLastAccessedTime());
        memberOnlineRepository.save(memberOnline);
        // 新建立的session均新增到HashSet集中
        sessions.add(session);
        onlineNum=sessions.size();
        // 可以在別處從application範圍中取出sessions集合
        // 然後使用sessions.size()取得目前活動的session數，即為“線上人數”
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) throws ClassCastException {
        HttpSession session = event.getSession();
        ServletContext application = session.getServletContext();
        HashSet sessions = (HashSet) application.getAttribute("sessions");
        // 銷毀的session均從HashSet集中移除
        sessions.remove(session);
        memberOnlineRepository.deleteBySessionId("363C2ED4CA55BD8F6F2E25934FF9B0B9");
    }

}
