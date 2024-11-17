package com.yen.SpringAssignmentSystem.proffesso.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authority{

    private static final long serialVersionUID = -6520888182797362903L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authority;
    @ManyToOne(optional = false)
    @JsonIgnore
    private ProffessoUser user;
}

//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import org.springframework.security.core.GrantedAuthority;
//
//import javax.persistence.*;
//
//@Entity
//public class Authority implements GrantedAuthority {
//    private static final long serialVersionUID = -6520888182797362903L;
//
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String authority;
//    @ManyToOne(optional = false)
//    @JsonIgnore
//    private ProffessoUser user;
//
//    public Authority () {}
//
//    public Authority (String authority) {
//        this.authority = authority;
//    }
//
//    public Long getId() {
//        return id;
//    }
//    public void setId(Long id) {
//        this.id = id;
//    }
//    @Override
//    public String getAuthority() {
//        return authority;
//    }
//    public void setAuthority(String authority) {
//        this.authority = authority;
//    }
//
//    public ProffessoUser getUser() {
//        return user;
//    }
//    public void setUser(ProffessoUser user) {
//        this.user = user;
//    }
//
//}
