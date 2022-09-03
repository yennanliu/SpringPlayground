package com.example.demo.service.member;


import com.example.demo.entity.member.User;
import com.example.demo.repository.member.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author longzhonghua
 * @data 2018/11/04 22:30
 */

//@Service
public class UserSecurityService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
    	 User user = userRepository.findByName(name);
        if (user == null) {
            User mobileUser = userRepository.findByMobile(name);
            }
            else {
                  user = userRepository.findByMobile(name);
            }




     /*   else if("locked".equals(user.getStatus())) { //被鎖定，無法登入
            throw new LockedException("使用者被鎖定");
        }*/
        return user;

    }
}
