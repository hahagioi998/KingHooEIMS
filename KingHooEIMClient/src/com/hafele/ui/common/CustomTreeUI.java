package com.hafele.ui.common;

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicTreeUI;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version ����ʱ�䣺2017��10��23�� ����1:37:40
* �Զ���TreeUI
*/
public class CustomTreeUI extends BasicTreeUI {
	// ʵ��ȥ��JTree�Ĵ�ֱ�ߺ�ˮƽ��
	@Override
	protected void paintVerticalLine(Graphics g, JComponent c, int x, int top,
			int bottom) {
	}

	@Override
	protected void paintHorizontalLine(Graphics g, JComponent c, int y,
			int left, int right) {
	}

	// ʵ�ָ��ڵ����ӽڵ����
	@Override
	public void setLeftChildIndent(int newAmount) {

	}

	@Override
	public void setRightChildIndent(int newAmount) {

	}
}
