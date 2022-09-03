package com.example.demo.entity.sysuser;
/**
 * @author longzhonghua
 * @data 2018/11/04 22:30
 */

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 使用者實體類別，透過實現UserDetails接口實現認證及授權
 */
@Entity
//@Table(name = "adminuser") //設定對應表名字
public class SysUser implements UserDetails {

    //主鍵及自動增長
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false, unique = true)
    private String name;

    private String password;
    private String cnname;

    private Boolean enabled = Boolean.TRUE;

    //多對多映射，使用者角色
    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private List<SysRole> roles;

    public long getId() {
        return id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getCnname() {
        return cnname;
    }

    public void setCnname(String cnname) {
        this.cnname = cnname;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    //取得目前使用者案例的password
    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SysUser(String name, String password) {
        this.name = name;
        this.password = password;
    }


    public SysUser() {
    }

    //根據自訂邏輯來傳回使用者權限，若果使用者權限傳回空或是和攔截路徑對應權限不同，驗證不透過
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<SysRole> roles = this.getRoles();
        for (SysRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return authorities;
    }

    //取得目前使用者案例的name
    @Override
    public String getUsername() {
        return this.name;
    }


    //帳號是否不過期，false則驗證不透過
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //帳號是否不鎖定，false則驗證不透過
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //憑證是否不過期，false則驗證不透過
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //該帳號是否啟用，false則驗證不透過
    @Override
    public boolean isEnabled() {
        return true;
    }

}

