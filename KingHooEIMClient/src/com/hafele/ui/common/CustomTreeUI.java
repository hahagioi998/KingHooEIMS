package com.hafele.ui.common;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicTreeUI;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月23日 下午1:37:40
* 自定义TreeUI
*/
public class CustomTreeUI extends BasicTreeUI {
	// 实现去除JTree的垂直线和水平线
	@Override
	protected void paintVerticalLine(Graphics g, JComponent c, int x, int top,
			int bottom) {
	}

	@Override
	protected void paintHorizontalLine(Graphics g, JComponent c, int y,
			int left, int right) {
	}

	// 实现父节点与子节点对齐
	@Override
	public void setLeftChildIndent(int newAmount) {

	}

	@Override
	public void setRightChildIndent(int newAmount) {

	}
}
