
package com.example.demo.result;
//實現響應的列舉類別
public enum ExceptionMsg {
	SUCCESS("200", "動作成功"),
	FAILED("999999","動作失敗"),
    ParamError("000001", "參數錯誤！"),
    FileEmpty("000400","上傳檔案為空"),
    LimitPictureSize("000401","圖片大小必須小於2M"),
    LimitPictureType("000402","圖片格式必須為'jpg'、'png'、'jpge'、'gif'、'bmp'")
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

