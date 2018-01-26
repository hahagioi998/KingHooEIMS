package com.hafele.bean;

import java.util.List;
import java.util.Map;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version ����ʱ�䣺2017��10��19�� ����1:46:47
* ��Ϣʵ����
*/
public class Message {

	/** ���ͷ�ID */
	private String senderId;
	/** ���ͷ�Name */
	private String senderName;
	/** ���ͷ�IP */
	private String senderAddress;
	/** ���ͷ�Port */
	private String senderPort;

	/** ���շ�ID */
	private String receiverId;
	/** ���շ�Name */
	private String receiverName;
	/** ���շ�IP */
	private String receiverAddress;
	/** ���շ�Port */
	private String receiverPort;
	
	/** ��Ϣ���� */
	private String content;
	/** ��Ϣ���� */
	private String type;
	/** �������� */
	private String palindType;
	
	// TODO ������ʽ
	private Integer size;
	private String family;
	private Integer back;
	private Integer fore;
	private Integer style;
	// ͼƬ
	private String imageMark;
	
	private String status;
	private User friend;
	private Category category;
	private List<String> list;
	
	private User user;
	/** �������鷵��ֵ */
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
