package com.hafele.ui.common;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.hafele.bean.Category;
import com.hafele.util.Constants;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月24日 下午1:13:35
* @description 自定义分组节点
*/
@SuppressWarnings("serial")
public class CategoryNode extends DefaultMutableTreeNode {
	public Icon icon;
	public JLabel picture;
	public JLabel categoryName;
	public Category category;
	public JPanel categoryContent = new JPanel();

	public CategoryNode(Icon icon, Category category) {
		super();
		this.icon = icon;
		this.category = category;

		categoryContent.setLayout(null);
		categoryContent.setBackground(Constants.BACKGROUND_COLOR);
		categoryContent.setPreferredSize(new Dimension(281, 25));

		picture = new JLabel();
		categoryContent.add(picture);
		picture.setIcon(icon);
		picture.setBounds(6, 5, 20, 16);

		categoryName = new JLabel();
		categoryContent.add(categoryName);
		categoryName.setFont(Constants.BASIC_FONT);
		categoryName.setText(category.getGroupName());
		categoryName.setBounds(19, 0, 132, 28);
		
	}

	public Component getView() {
		return categoryContent;
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
