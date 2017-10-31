package com.hafele.ui.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.hafele.bean.User;
import com.hafele.socket.Client;
import com.hafele.ui.common.CustomScrollBarUI;
import com.hafele.util.Constants;
import com.hafele.util.PictureUtil;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月28日 下午6:40:15
* 添加联系人窗口
*/
@SuppressWarnings("serial")
public class AddContactsWindow extends JDialog{
	
	private JPanel contentPanel;
	
	private Point point = new Point();
	private Client client;
	private User self;
	private String categoryId;
	private JPanel panel;
	private JLabel searchIcon;
	private JLabel searchUserTitle;
	private JLabel closeButton;
	private JScrollPane scrollPane;
	private JTextField searchField;
	private JLabel searchButton;
	
	public static AddContactsWindow getInstance(Client client, String categoryId, User self) {
		AddContactsWindow inst = new AddContactsWindow(client, categoryId, self);
		inst.setLocationRelativeTo(null);
		inst.setVisible(true);
		return inst;
	}
	
	public AddContactsWindow(Client client, String categoryId, User self) {
		initGUI();
		initListener();
		this.self = self;
		this.client = client;
		this.categoryId = categoryId;
	}

	//窗体搭建
	private void initGUI() {
		this.setSize(545, 355);
		this.setUndecorated(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		contentPanel = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(PictureUtil.getPicture("searchImgBackground.jpg").getImage(), 0, 0, null);
				this.setOpaque(false);
			}
		};
		contentPanel.setLayout(null);
		contentPanel.setBorder(Constants.LIGHT_GRAY_BORDER);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 545, 82);
		panel.setBackground(new Color(6, 157, 213));
		contentPanel.add(panel);
		panel.setLayout(null);
		
		searchIcon = new JLabel("");
		searchIcon.setBounds(10, 2, 20, 20);
		searchIcon.setIcon(PictureUtil.getPicture("searchUser_20px.png"));
		panel.add(searchIcon);
		
		searchUserTitle = new JLabel("查找用户");
		searchUserTitle.setBounds(40, 2, 85, 20);
		searchUserTitle.setFont(Constants.BASIC_FONT);
		searchUserTitle.setForeground(Color.WHITE);
		panel.add(searchUserTitle);
		
		closeButton = new JLabel("");
		closeButton.setBounds(515, 0, 30, 30);
		closeButton.setIcon(PictureUtil.getPicture("close_30px.png"));
		panel.add(closeButton);
		
		searchField = new JTextField();
		searchField.setBounds(10, 35, 430, 32);
		panel.add(searchField);
		searchField.setColumns(10);
		
		searchButton = new JLabel("");
		searchButton.setBounds(450, 35, 80, 32);
		searchButton.setIcon(PictureUtil.getPicture("searchUserButton.png"));
		panel.add(searchButton);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 82, 545, 273);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
		// 屏蔽横向滚动条
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentPanel.add(scrollPane);
		
	}

	//事件监听
	private void initListener() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				point.x = e.getX();
				point.y = e.getY();
			}
		});
		this.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				Point p = getLocation();
				setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
			}
		});
		
		//关闭按钮点击事件
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				closeButton.setIcon(PictureUtil.getPicture("close.png"));
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				closeButton.setIcon(PictureUtil.getPicture("close_active.png"));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				dispose();
			}
		});
		
		//搜索框
		searchField.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				searchField.setBorder(Constants.LIGHT_GRAY_BORDER);
			}
			@Override
			public void focusGained(FocusEvent e) {
				searchField.setBorder(Constants.GRAY_BORDER);
			}
		});
		
		searchButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				searchButton.setIcon(PictureUtil.getPicture("searchUserButton.png"));
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				searchButton.setIcon(PictureUtil.getPicture("searchUserButton_Active.png"));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
		});
	}
}
