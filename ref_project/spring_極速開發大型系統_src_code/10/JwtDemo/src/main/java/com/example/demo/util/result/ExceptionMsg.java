
package com.example.demo.util.result;

public enum ExceptionMsg {
	SUCCESS("200", "動作成功"),
	FAILED("999999","動作失敗"),
    ParamError("000001", "參數錯誤！"),
    LoginNameOrPassWordError("000100", "使用者名稱或是密碼錯誤！"),
    EmailUsed("000101","該信箱已被登錄"),
    UserNameUsed("000102","該登入名稱已存在"),
    EmailNotRegister("000103","該信箱位址未登錄"),
    PassWordError("000105","密碼輸入錯誤"),
    LoginNameNotExists("000107","該使用者未登錄"),
    UserNameSame("000108","新使用者名稱與原使用者名稱一致"),
    MobileUsed("000109","該手機號已被登錄"),
    FileEmpty("000500","上傳檔案為空"),
    LimitPictureSize("000501","圖片大小必須小於2M"),
    LimitPictureType("000502","圖片格式必須為'jpg'、'png'、'jpge'、'gif'、'bmp'")
    ;
   private ExceptionMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    private String code;
    private String msg;
    
	public String getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}

    
}

