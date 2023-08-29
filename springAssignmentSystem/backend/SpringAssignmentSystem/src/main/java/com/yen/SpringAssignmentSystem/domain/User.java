package com.yen.SpringAssignmentSystem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="users") // rename table as "users" in DB, since User is a reserved name in Mysql
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User{

    private static final long serialVersionID = 34547799654654L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private LocalDate cohortStartDate;
    private String password;
    @ElementCollection(targetClass=Assignment.class) // https://stackoverflow.com/questions/3774198/org-hibernate-mappingexception-could-not-determine-type-for-java-util-list-at
    private List<Assignment> assignments =  new ArrayList<>();

}


//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.ToString;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import javax.persistence.*;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//// https://youtu.be/KMecT1HBm4c?si=ZvXY7O_2RNviIrYq&t=180
//
//@Entity
//@Table(name="users") // rename table as "users" in DB, since User is a reserved name in Mysql
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@ToString
//public class User implements UserDetails { // in order to make user can be return in UserDetailService : https://youtu.be/TOQjvskdl3g?si=6OwdEu-esgjdk6B4&t=772
//
//    private static final long serialVersionID = 34547799654654L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String username;
//    private LocalDate cohortStartDate;
//    private String password;
//    @ElementCollection(targetClass=Assignment.class) // https://stackoverflow.com/questions/3774198/org-hibernate-mappingexception-could-not-determine-type-for-java-util-list-at
//    private List<Assignment> assignments =  new ArrayList<>();
//
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        //return null;
//        List<GrantedAuthority> roles = new ArrayList<>();
//        roles.add(new Authority("ROLE_STUDENT"));
//        return roles;
//    }
//
//    @Override
//    public String getUsername() {
//        return getUsername();
//    }
//
//    /**
//     *  Prevent user account be locked
//     *    https://stackoverflow.com/questions/17377636/spring-security-pre-authentication-account-lock-check
//     */
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//}
