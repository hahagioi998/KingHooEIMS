package com.hafele.bean;
/**
* @author Dragon Wen E-mail:18475536452@163.com
* @CreateDate ����ʱ�䣺2017��10��23�� ����11:47:31
* @description ���ѷ���
*/
public class Category {
	/** ����Id */
	private String Id;
	/** �������� */
	private String groupName;
	/** ������ID */
	private String loginName;
	/** ��ϵ�ˡ�Ⱥ */
	private String type;
	
	public Category(String groupName) {
		this.groupName = groupName;
	}
	
	public Category(String id, String groupName) {
		this.Id = id;
		this.groupName = groupName;
	}

	public Category(String id, String groupName, String loginName) {
		this.Id = id;
		this.groupName = groupName;
		this.loginName = loginName;
	}

	public Category(String id, String groupName, String loginName, String type) {
		this.Id = id;
		this.groupName = groupName;
		this.loginName = loginName;
		this.type = type;
	}

	public String getId() {
		return Id;
	}
	
	public void setId(String id) {
		Id = id;
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public String getLoginName() {
		return loginName;
	}
	
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
