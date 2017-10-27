package com.hafele.ui.contacts;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.hafele.bean.User;
import com.hafele.util.Constants;


/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月24日 下午1:45:59
* 自定义好友（成员）节点
*/
@SuppressWarnings("serial")
public class ContactsNode extends DefaultMutableTreeNode {
	public Icon icon;
	public User contacts;
	public JLabel picture;
	public JLabel contactsName;
	public JLabel signature;
	public JPanel userContent = new JPanel();

	public ContactsNode(Icon icon, User contacts) {
		super();
		this.icon = icon;
		this.contacts = contacts;

		userContent.setLayout(null);
		userContent.setBackground(Constants.BACKGROUND_COLOR);
		userContent.setPreferredSize(new Dimension(281, 50));

		picture = new JLabel();
		userContent.add(picture);
		picture.setIcon(icon);
		picture.setBounds(8, 4, 39, 42);

		contactsName = new JLabel();
		userContent.add(contactsName);
		contactsName.setFont(Constants.BASIC_FONTT14);
		
		contactsName.setText(contacts.getName());
		contactsName.setBounds(59, 5, 132, 19);

		signature = new JLabel();
		userContent.add(signature);
		signature.setFont(Constants.BASIC_FONT);
		signature.setForeground(new Color(127, 127, 127));
		signature.setText(contacts.getSignature());
		signature.setBounds(62, 28, 132, 17);
	}

	public Component getView() {
		return userContent;
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public User getContacts() {
		return contacts;
	}

	public void setContacts(User contacts) {
		this.contacts = contacts;
	}
}
