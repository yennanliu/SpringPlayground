package com.yen.webFluxPoc.model;

import org.springframework.data.relational.core.mapping.Table;

import java.sql.Date;

/**
 * CREATE TABLE IF NOT EXISTS user_info (
 *     id INT AUTO_INCREMENT,
 *     mobile_no VARCHAR(50),
 *     sms_code VARCHAR(50),
 *     send_time datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
 *     create_time datetime(3) DEFAULT CURRENT_TIMESTAMP(3),
 *     PRIMARY KEY (id)
 * );
 *
 */

@Table("user_info")
public class UserInfo {
    private Integer id;
    private String mobileNumber;
    private String smsCode;
//    private Date sendTime;
//    private Date createTime;

    public UserInfo(){

    }

    public UserInfo(Integer id, String mobileNumber, String smsCode) {
        this.id = id;
        this.mobileNumber = mobileNumber;
        this.smsCode = smsCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", smsCode='" + smsCode + '\'' +
                '}';
    }

}
