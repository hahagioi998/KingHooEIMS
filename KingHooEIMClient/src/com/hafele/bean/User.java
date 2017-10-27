package com.hafele.bean;
/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version ����ʱ�䣺2017��10��19�� ����5:43:34
* �û�������Ϣģ������
*/
public class User {
	/** ��¼�˺�*/
	private String loginName;
	/** �û�����*/
	private String name;
	/** ��¼����*/
	private String password;
	/** ����ǩ��*/
	private String signature;
	/** ͷ�� */
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
