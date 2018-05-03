package com.hafele.ui.common;

import java.net.URL;

import javax.swing.ImageIcon;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年11月9日 下午3:26:04
* 表情
*/
@SuppressWarnings("serial")
public class ChatPic extends ImageIcon {
	
	/** 下标 */
	private int index;
	/** 代号 */
	private int number;
	
	public ChatPic(URL url, int im) {
		super(url);
		this.number = im;
	}

	public ChatPic(URL url, int index, int im) {
		super(url);
		this.number = im;
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}

