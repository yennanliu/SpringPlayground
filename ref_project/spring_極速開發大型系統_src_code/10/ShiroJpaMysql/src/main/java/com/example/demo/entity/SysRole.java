package com.example.demo.entity;
import lombok.Data;
import javax.persistence.*;
import java.util.List;
@Entity
@Data
public class SysRole {
    @Id@GeneratedValue
    /**
     *  編號
     */
    private Integer id;
    @Column(unique =true)
    /**
     * 角色標誌程式中判斷使用,如"admin",這個是唯一的
     */
    private String role;
    /**
     *  角色描述,UI界面顯示使用
     */
    private String description;
    /**
     *  是否可用,若果不可用將不會加入給使用者
     */
    private Boolean available = Boolean.FALSE;
 /**
  * 角色權限關系：多對多關系;
  */
    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name="SysRolePermission",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="permissionId")})
    private List<SysPermission> permissions;
  /**
   * 使用者角色關系定義;
   */
    @ManyToMany
    @JoinTable(name="SysUserRole",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="uid")})
   /**
    * 一個角色對應多個使用者
    */
   private List<Admin> admins;

}