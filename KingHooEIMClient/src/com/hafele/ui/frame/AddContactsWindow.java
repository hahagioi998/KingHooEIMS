package com.hafele.ui.frame;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.hafele.bean.Message;
import com.hafele.bean.User;
import com.hafele.socket.Client;
import com.hafele.ui.common.CustomOptionPanel;
import com.hafele.util.Constants;
import com.hafele.util.PictureUtil;
import com.hafele.util.StringHelper;


/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月28日 下午6:40:15
* 添加联系人窗口
*/
@SuppressWarnings("serial")
public class AddContactsWindow extends JDialog{
	
	private Point point = new Point();
	private Client client;
	private User user;
	private String categoryId;
	private JPanel panel;
	private JLabel searchIcon;
	private JLabel searchUserTitle;
	private JLabel closeButton;
	private JTextField searchField;
	private JLabel addButton;
	private JLabel cancelButton;
	private JLabel label;
	private JPanel topPanel;
	
	public static AddContactsWindow getInstance(Client client, String categoryId, User self) {
		AddContactsWindow inst = new AddContactsWindow(client, categoryId, self);
		inst.setLocationRelativeTo(null);
		inst.setVisible(true);
		return inst;
	}
	
	public AddContactsWindow(Client client, String categoryId, User user) {
		initGUI();
		initListener();
		this.user = user;
		this.client = client;
		this.categoryId = categoryId;
	}

	//窗体搭建
	private void initGUI() {
		this.setSize(400, 150);
		this.setUndecorated(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.setBackground(Constants.BACKGROUND_COLOR);
		panel.setBounds(0, 30, 400, 120);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		searchField = new JTextField();
		searchField.setBounds(144, 16, 222, 30);
		searchField.setBackground(Constants.BACKGROUND_COLOR);
		searchField.setBorder(Constants.LIGHT_GRAY_BORDER);
		panel.add(searchField);
		
		addButton = new JLabel("");
		addButton.setBounds(104, 78, 80, 32);
		addButton.setIcon(PictureUtil.getPicture("addButton.png"));
		panel.add(addButton);
		
		cancelButton = new JLabel("");
		cancelButton.setBounds(239, 78, 80, 32);
		cancelButton.setIcon(PictureUtil.getPicture("cancelButton.png"));
		panel.add(cancelButton);
		
		label = new JLabel("查找用户账号：");
		label.setBounds(27, 16, 98, 30);
		label.setFont(Constants.BASIC_FONTT14);
		panel.add(label);
		
		topPanel = new JPanel();
		topPanel.setBackground(new Color(6, 157, 213));
		topPanel.setBounds(0, 0, 400, 30);
		getContentPane().add(topPanel);
		topPanel.setLayout(null);
		
		searchIcon = new JLabel("");
		searchIcon.setBounds(10, 5, 20, 20);
		topPanel.add(searchIcon);
		searchIcon.setIcon(PictureUtil.getPicture("searchUser_20px.png"));
		
		searchUserTitle = new JLabel("查找用户");
		searchUserTitle.setBounds(35, 5, 85, 20);
		topPanel.add(searchUserTitle);
		searchUserTitle.setFont(Constants.BASIC_FONTT14);
		searchUserTitle.setForeground(Color.WHITE);
		
		closeButton = new JLabel("");
		closeButton.setBounds(370, 0, 30, 30);
		topPanel.add(closeButton);
		closeButton.setIcon(PictureUtil.getPicture("close_30px.png"));
		
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
				closeButton.setIcon(PictureUtil.getPicture("close_30px.png"));
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				closeButton.setIcon(PictureUtil.getPicture("close_active_30px.png"));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				dispose();
				client.setAddContactsWindow(null);
			}
		});
		
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				cancelButton.setIcon(PictureUtil.getPicture("cancelButton.png"));
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				cancelButton.setIcon(PictureUtil.getPicture("cancelButton_Active.png"));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				dispose();
				client.setAddContactsWindow(null);
			}
		});
		
		addButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				addButton.setIcon(PictureUtil.getPicture("addButton.png"));
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				addButton.setIcon(PictureUtil.getPicture("addButton_Active.png"));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				String account = searchField.getText().trim();
				if(StringHelper.isEmpty(account)) {
					CustomOptionPanel.showMessageDialog(client.getAddContactsWindow(), "请输入账号或昵称。", "提示");
				}
				Message message = new Message();
				message.setSenderId(user.getLoginName());
				message.setSenderName(user.getName());
				message.setType(Constants.REQUEST_ADD_MSG);
				message.setContent(categoryId + Constants.LEFT_SLASH + account);
				client.sendMsg(message);
			}
		});
	}
}
