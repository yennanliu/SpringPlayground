package com.example.demo.entity.sys;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author longzhonghua
 * @createdata 3/7/2019 10:36 PM
 * @description 請求記錄
 */
@Entity
@Data
public class RequestLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//由資料庫控制,auto是程式統一控制
    private long id;
    /** 使用者階段id */
    private String sessionId;
    /** session建立時間 */
    private long creationTimes;

    /** session最後存取時間 */
    private long lastAccessedTime;



    /** 部門名稱 */
    private String deptName;

    /** 登入名稱 */
    private String loginName;

    /** 登入IP位址 */
    private String ipaddr;

    /** 登入位址 */
    private String loginLocation;

    /** 瀏覽器型態 */
    private String browser;

    /** 動作系統 */
    private String os;



    /** 逾時時間，單位為分鍾 */
    private Long expireTime;

    private String referer;
    private String accept;
    private String method;
    private String url;
    private String querystring;
    /** 線上狀態 */

}
