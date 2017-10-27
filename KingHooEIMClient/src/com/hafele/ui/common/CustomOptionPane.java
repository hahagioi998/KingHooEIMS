package com.hafele.ui.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.hafele.util.Constants;
import com.hafele.util.PictureUtil;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月18日 下午3:52:18
* 自定义弹出框
*/
public class CustomOptionPane {
//	private static int result = 0;

	/**
	 * 消息框
	 * @param owner 父窗体
	 * @param title 标题
	 * @param content 内容
	 */
	public static void showMessageDialog(Component owner, String content, String title) {
		JDialog dialog = createMessageDialog(owner, title, content);
		if (owner == null) {
			dialog.setLocationRelativeTo(null);
		} else {
			dialog.setLocationRelativeTo(owner);
		}
		dialog.setVisible(true);
	}

	/** 创建消息提示框*/
	private static JDialog createMessageDialog(Component owner, String title, String content) {
		final Point point = new Point();
		final JDialog dialog = getDialog(owner, title);
		
		dialog.setUndecorated(true);
		dialog.setSize(320, 180);
		dialog.getContentPane().setLayout(null);
		
		final JPanel panel = new JPanel(); 
		panel.setBounds(0, 0, 320, 180);
		panel.setLayout(null);
		dialog.add(panel);
		
		JPanel topPanel = new JPanel();
		topPanel.setBounds(0, 0, 320, 30);
		topPanel.setBackground(new Color(6, 157, 213));
		panel.add(topPanel);
		topPanel.setLayout(null);
		
		JLabel titlePicture = new JLabel();
		titlePicture.setIcon(PictureUtil.getPicture("remind_20px.png"));
		titlePicture.setBounds(6, 4, 20, 20);
		topPanel.add(titlePicture);
		
		JLabel titleLabel = new JLabel(title);
		titleLabel.setBounds(38, 4, 66, 19);
		titleLabel.setFont(Constants.BASIC_FONT);
		titleLabel.setForeground(Color.WHITE);
		topPanel.add(titleLabel);
		
		JLabel closeButton = new JLabel("");
		closeButton.setIcon(PictureUtil.getPicture("close_30px.png"));
		closeButton.setBounds(291, 0, 30, 30);
		topPanel.add(closeButton);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setBounds(0, 29, 320, 107);
		centerPanel.setBackground(new Color(255, 255, 255));
		centerPanel.setLayout(null);
		panel.add(centerPanel);
		
		JLabel mesagePicture = new JLabel();
		mesagePicture.setIcon(PictureUtil.getPicture("picture.png"));
		mesagePicture.setBounds(45, 29, 40, 40);
		centerPanel.add(mesagePicture);
		
		JTextArea contentTxt = new JTextArea(content);
		contentTxt.setFont(Constants.DIALOG_FONT);// 字体
		contentTxt.setBounds(124, 29, 153, 41);
		contentTxt.setBorder(null);
		contentTxt.setEditable(false);// 不允许编辑
		contentTxt.setLineWrap(true);// 激活自动换行功能
		contentTxt.setWrapStyleWord(true);// 激活断行不断字功能
		centerPanel.add(contentTxt);
		
		JPanel downPanel = new JPanel();
		downPanel.setBounds(0, 135, 320, 45);
		downPanel.setBackground(new Color(229, 237, 246));
		downPanel.setLayout(null);
		panel.add(downPanel);
		
		final JLabel okButton = new JLabel();
		okButton.setIcon(PictureUtil.getPicture("okbutton.png"));
		okButton.setBounds(127, 7, 80, 32);
		downPanel.add(okButton);
		
		dialog.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				point.x = e.getX();
				point.y = e.getY();
			}
		});
		dialog.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				Point p = dialog.getLocation();
				dialog.setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
			}
		});
		
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				closeButton.setIcon(PictureUtil.getPicture("close_30px.png"));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				closeButton.setIcon(PictureUtil.getPicture("close_active_30px.png"));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				dialog.dispose();
			}
		});
		
		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				okButton.setIcon(PictureUtil.getPicture("okButtonActive.png"));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				okButton.setIcon(PictureUtil.getPicture("okButton.png"));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				dialog.dispose();
			}
		});
		
		return dialog;
	}

	private static JDialog getDialog(Component owner, String title) {
		JDialog dialog = null;
		//父窗体继承JFrame
		if (owner instanceof JFrame) {
			dialog = new JDialog((JFrame)owner, title, true);
		}
		//父窗体继承JDialog
		if (owner instanceof JDialog) {
			dialog = new JDialog((JDialog)owner, title, true);
		} 
		//父窗体为空
		if (owner == null) {
			dialog = new JDialog();
			dialog.setTitle(title);
			dialog.setLocationRelativeTo(null);
		}
		return dialog;
	}
	
	
}
