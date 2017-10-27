package com.hafele.ui.contacts;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.hafele.ui.common.CategoryNode;
import com.hafele.util.PictureUtil;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月24日 下午2:02:39
* 自定义联系人节点渲染器
*/
@SuppressWarnings("serial")
public class ContactsNodeRenderer extends DefaultTreeCellRenderer {
	
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		if (value instanceof ContactsNode) {
			return ((ContactsNode)value).getView();
		}
		if (value instanceof CategoryNode) {
			if (expanded) {
				((CategoryNode)value).picture.setIcon(PictureUtil.getPicture("arrow_down.png"));
			} else {
				((CategoryNode)value).picture.setIcon(PictureUtil.getPicture("arrow_left.png"));
			}
			return ((CategoryNode)value).getView();
		}
		return this;
	}
}
