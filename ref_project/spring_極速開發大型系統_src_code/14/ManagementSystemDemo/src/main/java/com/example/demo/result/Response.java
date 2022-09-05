package com.example.demo.result;

public class Response {
	/** 傳回訊息碼*/
	private String rspCode="000000";
	/** 傳回訊息內容*/
	private String rspMsg="動作成功";

	public Response() {
	}
	
	public Response(ExceptionMsg msg){
		this.rspCode=msg.getCode();
		this.rspMsg=msg.getMsg();
	}
	
	public Response(String rspCode) {
		this.rspCode = rspCode;
		this.rspMsg = "";
	}

	public Response(String rspCode, String rspMsg) {
		this.rspCode = rspCode;
		this.rspMsg = rspMsg;
	}
	public String getRspCode() {
		return rspCode;
	}
	public void setRspCode(String rspCode) {
		this.rspCode = rspCode;
	}
	public String getRspMsg() {
		return rspMsg;
	}
	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}

	@Override
	public String toString() {
		return "Response{" +
				"rspCode='" + rspCode + '\'' +
				", rspMsg='" + rspMsg + '\'' +
				'}';
	}
}


/*
	public Response regist(User user) {
		try {

			User userNameUser = userRepository.findByName(user.getName());
			AdminUser admingusername = adminUserRepository.findByName(user.getName());
			if (null != userNameUser || null != admingusername) {
				return result(ExceptionMsg.UserNameUsed);
			}*/
