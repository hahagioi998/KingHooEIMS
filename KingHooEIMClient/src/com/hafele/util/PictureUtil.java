package com.hafele.util;

import javax.swing.ImageIcon;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version ����ʱ�䣺2017��10��17�� ����2:50:15
* ͼƬ������
*/
public class PictureUtil {
	/** ��ȡͼƬ */
	public static ImageIcon getPicture(String name) {
		ImageIcon icon = new ImageIcon(PictureUtil.class.getClassLoader()
				.getResource("com/hafele/resource/image/" + name));
		return icon;
	}
}
