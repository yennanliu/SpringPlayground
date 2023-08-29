package com.yen.SpringAssignmentSystem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Authorities {
    private static final long serialVersionID = 3454311984654654654L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authority;
    @ManyToOne
    private User user;

}


//
//import lombok.Data;
//import lombok.ToString;
//import org.springframework.security.core.GrantedAuthority;
//
//import javax.persistence.*;
//
//// https://youtu.be/TOQjvskdl3g?si=QdZ68xt7QIctwdCn&t=1119
//// https://github.com/tp02ga/AssignmentSubmissionApp/blob/master/back-end/src/main/java/com/coderscampus/AssignmentSubmissionApp/domain/Authorities.java
//@Entity
//@Data
//@ToString
//public class Authority implements GrantedAuthority {
//
//    private static final long serialVersionID = 3454311984654654654L;
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String authority;
//    @ManyToOne
//    private User user;
//
//    @Override
//    public String getAuthority() {
//        return this.authority;
//    }
//
//    // constructor
//    // https://youtu.be/TOQjvskdl3g?si=bEBXwqVA509URwaD&t=1344
//    public Authority(){
//
//    }
//
//    public Authority(String authority){
//        this.authority = authority;
//    }
//
//}
