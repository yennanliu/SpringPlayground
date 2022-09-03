package com.example.demo.entity.sysuser;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author longzhonghua
 * @data 2019/01/26 22:30
 */
@Data
@Entity

public class SysPermission implements Serializable {
    @Id
    @GeneratedValue
    /*
    主鍵
    */
    private Integer id;
    //名稱.
    private String name;
    @Column(columnDefinition="enum('menu','button')")
    //資源型態，[menu|button]
    private String resourceType;
    private String url;//資源路徑.
    //權限字串,menu實例：role:*，button實例：role:create,role:update,role:delete,role:view
    private String permission;
    private Long parentId; //父編號
    private String parentIds; //父編號清單
    private Boolean available = Boolean.FALSE;
    @Transient
    private List permissions;
    @ManyToMany
    @JoinTable(name="SysRolePermission",joinColumns={@JoinColumn(name="permissionId")},inverseJoinColumns={@JoinColumn(name="roleId")})
    private List<SysRole> roles;
    public List getPermissions() {
        return Arrays.asList(this.permission.trim().split("|"));
    }

    public void setPermissions(List permissions) {
        this.permissions = permissions;
    }

}
