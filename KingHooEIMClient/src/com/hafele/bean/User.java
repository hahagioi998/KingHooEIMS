package com.hafele.bean;
/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月19日 下午5:43:34
* 用户基本信息模型驱动
*/
public class User {
	/** 登录账号*/
	private String loginName;
	/** 用户姓名*/
	private String name;
	/** 登录密码*/
	private String password;
	/** 个性签名*/
	private String signature;
	/** 头像 */
	private String headPicture;
	
	public User(String loginName, String userName, String signature, String headPicture) {
		this.loginName = loginName;
		this.name = userName;
		this.signature = signature;
		this.headPicture = headPicture;
	}

	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
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
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getHeadPicture() {
		return headPicture;
	}
	public void setHeadPicture(String headPicture) {
		this.headPicture = headPicture;
	}

}
