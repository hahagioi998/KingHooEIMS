package com.hafele.bean;
/**
* @author Dragon Wen E-mail:18475536452@163.com
* @CreateDate 创建时间：2017年10月23日 下午11:47:31
* @description 好友分组
*/
public class Category {
	/** 分组Id */
	private String Id;
	/** 分组名称 */
	private String groupName;
	/** 所属者ID */
	private String loginName;
	/** 联系人、群 */
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
