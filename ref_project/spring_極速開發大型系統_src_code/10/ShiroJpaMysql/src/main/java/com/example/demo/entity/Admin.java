package com.example.demo.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity

public class Admin implements Serializable {
    @Id
    @GeneratedValue
    private Integer uid;
    @Column(unique =true)
    /**
     * 賬號
     */
    private String username;
    /**
     * 名稱
     */
    private String name;
    /**
     * 密碼
     */
    private String password;
    /**
     * 加密密碼的鹽
     */
    private String salt;
    /**
     * 使用者狀態,0:建立未認證（例如沒有啟動，沒有輸入驗證碼等等）--等待驗證的使用者 , 1:標準狀態,2：使用者被鎖定.
     */
    private byte state;
    /**
     * 立即從資料庫中進行載入資料;
     */
    @ManyToMany(fetch= FetchType.EAGER)//
    @JoinTable(name = "SysUserRole", joinColumns = { @JoinColumn(name = "uid") }, inverseJoinColumns ={@JoinColumn(name = "roleId") })
    /**
     * 一個使用者具有多個角色
     */
    private List<SysRole> roleList;//

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public List<SysRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SysRole> roleList) {
        this.roleList = roleList;
    }

    /**
     * 密碼鹽.
     */
    public String getCredentialsSalt(){

      return this.username+this.salt;
    }
    //密碼鹽透過username+salt進行加密,實際可以看控制器中的加入使用者功能
    public String setCredentialsSalt(){
        return this.username+this.salt;
    }
}