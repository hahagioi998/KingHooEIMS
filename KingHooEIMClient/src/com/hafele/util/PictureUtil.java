package com.hafele.util;

import javax.swing.ImageIcon;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月17日 下午2:50:15
* 图片工具类
*/
public class PictureUtil {
	/** 获取图片 */
	public static ImageIcon getPicture(String name) {
		ImageIcon icon = new ImageIcon(PictureUtil.class.getClassLoader()
				.getResource("com/hafele/resource/image/" + name));
		return icon;
	}
}
