package com.example.demo.entity;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
@Entity
@Data
public class SysPermission implements Serializable {
    @Id@GeneratedValue
    /**
     * 主鍵
     */
    private Integer id;
    /**
     * 權限名稱.
     */
    private String name;

    @Column(columnDefinition="enum('menu','button')")
    /**
     * 資源型態，[menu|button]
     */
    private String resourceType;
    /**
     * 資源路徑
     */
    private String url;
    /**
     * 權限字串
     */
    private String permission;
    // menu實例：role:*，
    // button實例：role:create,role:update,role:delete,role:view
    /**
     * 父編號
     */
    private Long parentId;
    /**
     * 父編號清單
     */
    private String parentIds;
    private Boolean available = Boolean.FALSE;
    @ManyToMany
    @JoinTable(name="SysRolePermission",joinColumns={@JoinColumn(name="permissionId")},inverseJoinColumns={@JoinColumn(name="roleId")})
    private List<SysRole> roles;
}