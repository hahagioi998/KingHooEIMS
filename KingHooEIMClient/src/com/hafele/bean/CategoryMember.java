package com.hafele.bean;
/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月24日 上午11:50:57
* 分组成员模块
*/
public class CategoryMember {
	private int Id;
	/** 所属者ID */
	private String loginName;
	/** 分组ID */
	private int categoryId;
	/** 成员ID */
	private String memberLoginName;
	
	public CategoryMember(int id, String loginName, int categoryId, String memberLoginName) {
		this.Id = id;
		this.loginName = loginName;
		this.categoryId = categoryId;
		this.memberLoginName = memberLoginName;
	}
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getMemberLoginName() {
		return memberLoginName;
	}
	public void setMemberLoginName(String memberLoginName) {
		this.memberLoginName = memberLoginName;
	}
}
