package com.hafele.bean;

import java.util.List;
import java.util.Map;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月19日 下午1:46:47
* 消息实体类
*/
public class Message {

	/** 发送方ID */
	private String senderId;
	/** 发送方Name */
	private String senderName;
	/** 发送方IP */
	private String senderAddress;
	/** 发送方Port */
	private String senderPort;

	/** 接收方ID */
	private String receiverId;
	/** 接收方Name */
	private String receiverName;
	/** 接收方IP */
	private String receiverAddress;
	/** 接收方Port */
	private String receiverPort;
	
	/** 消息内容 */
	private String content;
	/** 消息类型 */
	private String type;
	/** 回文类型 */
	private String palindType;
	
	// TODO 文字样式
	private Integer size;
	private String family;
	private Integer back;
	private Integer fore;
	private Integer style;
	// 图片
	private String imageMark;
	
	private String status;
	private User friend;
	private Category category;
	private List<String> list;
	
	private User user;
	/** 搜索分组返回值 */
	private List<Category> categoryList;
	
	private List<Map<String, List<User>>> categoryMemberList;
	

	public Message() {
		
	}
	
	public Message(String type, String content) {
		this.type = type;
		this.content = content;
	}
	
	public Message(String type, String palindType, String content) {
		this.type = type;
		this.content = content;
		this.palindType = palindType;
	}

	public Message(String type, User user, List<Category> categoryList, List<Map<String, List<User>>> categoryMemberList) {
		this.type = type;
		this.user = user;
		this.categoryList = categoryList;
		this.categoryMemberList = categoryMemberList;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getSenderPort() {
		return senderPort;
	}

	public void setSenderPort(String senderPort) {
		this.senderPort = senderPort;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getReceiverPort() {
		return receiverPort;
	}

	public void setReceiverPort(String receiverPort) {
		this.receiverPort = receiverPort;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPalindType() {
		return palindType;
	}

	public void setPalindType(String palindType) {
		this.palindType = palindType;
	}
	
	public User getFriend() {
		return friend;
	}

	public void setFriend(User friend) {
		this.friend = friend;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public List<Map<String, List<User>>> getCategoryMemberList() {
		return categoryMemberList;
	}

	public void setCategoryMemberList(List<Map<String, List<User>>> categoryMemberList) {
		this.categoryMemberList = categoryMemberList;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public Integer getBack() {
		return back;
	}

	public void setBack(Integer back) {
		this.back = back;
	}

	public Integer getFore() {
		return fore;
	}

	public void setFore(Integer fore) {
		this.fore = fore;
	}

	public Integer getStyle() {
		return style;
	}

	public void setStyle(Integer style) {
		this.style = style;
	}

	public String getImageMark() {
		return imageMark;
	}

	public void setImageMark(String imageMark) {
		this.imageMark = imageMark;
	}
	
}
