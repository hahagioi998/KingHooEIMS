package com.hafele.ui.frame;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.hafele.bean.Message;
import com.hafele.socket.Client;
import com.hafele.ui.common.CustomOptionPane;
import com.hafele.util.Constants;
import com.hafele.util.PictureUtil;
import com.hafele.util.StringHelper;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月17日 下午2:02:32
* 登录界面
*/
@SuppressWarnings("serial")
public class LoginWindow extends JDialog {
	
	/** 关闭按钮 */
	private JLabel closeButton;
	/** 最小化按钮 */
	private JLabel minButton;
	/** 公司标志 */
	private JLabel logoLabel;
	/** 头像 */
	private JLabel pictureLabel;
	/** 登录按钮 */
	private JLabel loginButton;
	/** 保存密码选项框 */
	private JLabel savePassCheckBox;
	/** 保存密码 */
	private JLabel savePassLabel;
	/** 自动登录选项框 */
	private JLabel autoLoginCheckBox;
	/** 在佛那个登陆 */
	private JLabel autoLoginLabel;
	/** 用户名Icon */
	private JLabel userNameIcon;
	/** 密码Icon */
	private JLabel passwordIcon;
	/** 账号 */
	private JTextField userNameField;
	/** 密码 */
	private JPasswordField passWordField;
	/** 坐标（拖动记录） */
	private Point point = new Point();
	/** 是否选中保存密码选项框 */
	private boolean isSavePass;
	/** 是否选中自动登录选项框 */
	private boolean isAutoLogin;
	/** 系统托盘图标 */
	private TrayIcon icon;
	/** 系统托盘对象 */
	private SystemTray tray;
	/** 客户端核心 */
	private Client client;
	
	public static LoginWindow getInstance(Client client) {
		LoginWindow inst = new LoginWindow(client);
		inst.setLocationRelativeTo(null);//设置窗体居中
		inst.setVisible(true);// 设置窗体可见
		return inst;
	}
	
	public LoginWindow(Client client) {
		super();
		this.client = client;
		initGUI();
		initListener();
		initTrayIcon();
	}

	//隐藏菜单栏里的信息
	private void initTrayIcon() {
		if (SystemTray.isSupported()) {
			try {
				tray = SystemTray.getSystemTray(); //系统托盘类采用的是单例模式。获取系统托盘对象
				icon = new TrayIcon(PictureUtil.getPicture("icon_120px.png").getImage(), "HAFELE—IM");
				icon.setImageAutoSize(true); //自动适应大小
				icon.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						if (e.getButton() == MouseEvent.BUTTON1) {
							setVisible(true);
							// 获取焦点
							requestFocus();
						}
					}
				});
				
				PopupMenu pm = new PopupMenu();
				MenuItem exitMI = new MenuItem("退出");
				MenuItem openMainPaneMI = new MenuItem("打开主面板");
				exitMI.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						tray.remove(icon);
						System.exit(0);
					}
				});
				openMainPaneMI.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						setVisible(true);
						// 获取焦点
						requestFocus();
					}
				});
				pm.add(openMainPaneMI);
				pm.add(exitMI);
				icon.setPopupMenu(pm);
				tray.add(icon); //最小化软件时向系统托盘添加系统图标
				// 放到client类中
				client.setIcon(icon);
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//登录窗体
	private void initGUI() {
		try {
			this.setSize(430,330);
			this.setUndecorated(true);// 去除边框
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setAlwaysOnTop(true);// 窗体一直处于屏幕的最前端
			//设置UI
			try {
				UIManager.setLookAndFeel(new NimbusLookAndFeel());
			} catch (UnsupportedLookAndFeelException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			JPanel contentPanel = new JPanel(){
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(PictureUtil.getPicture("loginBackground.jpg").getImage(), 0, 0, null);
					this.setOpaque(false);
				}
			};
			contentPanel.setBackground(Color.WHITE);
			getContentPane().add(contentPanel, BorderLayout.CENTER);
			contentPanel.setLayout(null);
			contentPanel.setBorder(Constants.GRAY_BORDER);
			
			//关闭按钮
			closeButton = new JLabel("");
			closeButton.setIcon(PictureUtil.getPicture("close.png"));
			closeButton.setBounds(390, 0, 40, 20);
			contentPanel.add(closeButton);
			
			//最小化按钮
			minButton = new JLabel("");
			minButton.setIcon(PictureUtil.getPicture("minimize.png"));
			minButton.setBounds(363, 0, 30, 20);
			contentPanel.add(minButton);
			
			//LOGO
			logoLabel = new JLabel("");
			logoLabel.setIcon(PictureUtil.getPicture("hafele.png"));
			logoLabel.setBounds(52, 53, 329, 70);
			contentPanel.add(logoLabel);
			
			//头像
			pictureLabel = new JLabel("");
			pictureLabel.setIcon(PictureUtil.getPicture("HeadPortraits_100px.png"));
			pictureLabel.setBounds(42, 175, 100, 100);
			pictureLabel.setBorder(Constants.LIGHT_GRAY_BORDER);
			contentPanel.add(pictureLabel);
			
			//用户名文本框
			userNameField = new JTextField();
			userNameField.setBounds(199, 175, 182, 31);
			userNameField.setBorder(Constants.LIGHT_GRAY_BORDER);
			contentPanel.add(userNameField);
			
			//密码文本框
			passWordField = new JPasswordField();
			passWordField.setBounds(199, 208, 182, 31);
			passWordField.setBorder(Constants.LIGHT_GRAY_BORDER);
			contentPanel.add(passWordField);
			
			//登录按钮
			loginButton = new JLabel("");
			loginButton.setIcon(PictureUtil.getPicture("loginButton.png"));
			loginButton.setBounds(171, 275, 196, 32);
			contentPanel.add(loginButton);
			
			//记住密码选项框
			savePassCheckBox = new JLabel("");
			savePassCheckBox.setIcon(PictureUtil.getPicture("buxuanzhong.png"));
			savePassCheckBox.setBounds(157, 250, 20, 20);
			contentPanel.add(savePassCheckBox);
			
			savePassLabel = new JLabel("记住密码");
			savePassLabel.setBounds(184, 249, 60, 20);
			contentPanel.add(savePassLabel);
			
			//自动登录选项框
			autoLoginCheckBox = new JLabel("");
			autoLoginCheckBox.setIcon(PictureUtil.getPicture("buxuanzhong.png"));
			autoLoginCheckBox.setBounds(305, 250, 20, 20);
			contentPanel.add(autoLoginCheckBox);
			
			autoLoginLabel = new JLabel("自动登录");
			autoLoginLabel.setBounds(333, 249, 60, 20);
			contentPanel.add(autoLoginLabel);
			
			userNameIcon = new JLabel("");
			userNameIcon.setIcon(PictureUtil.getPicture("userNameIcon_30px.png"));
			userNameIcon.setBounds(157, 175, 30, 30);
			contentPanel.add(userNameIcon);
			
			passwordIcon = new JLabel("");
			passwordIcon.setIcon(PictureUtil.getPicture("passwordIcon_30px.png"));
			passwordIcon.setBounds(157, 208, 30, 30);
			contentPanel.add(passwordIcon);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//事件监听
	private void initListener() {
		// 主窗体事件
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
		
		// 退出按钮事件
		closeButton.addMouseListener(new MouseAdapter() {
			
			//鼠标离开组件时调用。
			@Override
			public void mouseExited(MouseEvent e) {
				closeButton.setIcon(PictureUtil.getPicture("close.png"));
			}
			
			//鼠标进入到组件上时调用。
			@Override
			public void mouseEntered(MouseEvent e) {
				closeButton.setIcon(PictureUtil.getPicture("close_active.png"));
			}
			
			//鼠标按钮在组件上释放时调用。
			@Override
			public void mouseReleased(MouseEvent e) {
				System.exit(0);
			}
		});
		
		// 最小化按钮事件
		minButton.addMouseListener(new MouseAdapter() {
			
			//鼠标离开组件时调用。
			@Override
			public void mouseExited(MouseEvent e) {
				minButton.setIcon(PictureUtil.getPicture("minimize.png"));
			}
			
			//鼠标进入到组件上时调用。
			@Override
			public void mouseEntered(MouseEvent e) {
				minButton.setIcon(PictureUtil.getPicture("minimize_active.png"));
			}
			
			//鼠标按钮在组件上释放时调用。
			@Override
			public void mouseReleased(MouseEvent e) {
				setVisible(false);
			}
		});
		
		// 账号输入框焦点事件
		userNameField.addFocusListener(new FocusListener() {
			//失去焦点
			@Override
			public void focusLost(FocusEvent e) {
				userNameField.setBorder(Constants.LIGHT_GRAY_BORDER);
			}
			//得到焦点
			@Override
			public void focusGained(FocusEvent e) {
				userNameField.setBorder(Constants.GREEN_BORDER);
			}
		});
		userNameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					login();
				}
			}
		});
		// 密码输入框焦点事件
		passWordField.addFocusListener(new FocusListener() {
			//失去焦点
			@Override
			public void focusLost(FocusEvent e) {
				passWordField.setBorder(Constants.LIGHT_GRAY_BORDER);
			}
			//得到焦点
			@Override
			public void focusGained(FocusEvent e) {
				passWordField.setBorder(Constants.GREEN_BORDER);
			}
		});
		passWordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					login();
				}
			}
		});
		// 保存密码复选框事件
		savePassCheckBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (isSavePass) {
					savePassCheckBox.setIcon(PictureUtil.getPicture("buxuanzhong.png"));
					isSavePass = false;
					autoLoginCheckBox.setIcon(PictureUtil.getPicture("buxuanzhong.png"));
					isAutoLogin = false;
				} else {
					savePassCheckBox.setIcon(PictureUtil.getPicture("xuanzhong_20px.png"));
					isSavePass = true;
				}
			}
		});
		// 保存密码字体事件
		savePassLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (isSavePass) {
					savePassCheckBox.setIcon(PictureUtil.getPicture("buxuanzhong.png"));
					isSavePass = false;
					autoLoginCheckBox.setIcon(PictureUtil.getPicture("buxuanzhong.png"));
					isAutoLogin = false;
				} else {
					savePassCheckBox.setIcon(PictureUtil.getPicture("xuanzhong_20px.png"));
					isSavePass = true;
				}
			}
		});
		// 自动登录复选框事件
		autoLoginCheckBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (isAutoLogin) {
					autoLoginCheckBox.setIcon(PictureUtil.getPicture("buxuanzhong.png"));
					isAutoLogin = false;
				} else {
					autoLoginCheckBox.setIcon(PictureUtil.getPicture("xuanzhong_20px.png"));
					isAutoLogin = true;
					savePassCheckBox.setIcon(PictureUtil.getPicture("xuanzhong_20px.png"));
					isSavePass = true;
				}
			}
		});
		// 自动登录字体事件
		autoLoginLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (isAutoLogin) {
					autoLoginCheckBox.setIcon(PictureUtil.getPicture("buxuanzhong.png"));
					isAutoLogin = false;
				} else {
					autoLoginCheckBox.setIcon(PictureUtil.getPicture("xuanzhong_20px.png"));
					isAutoLogin = true;
					savePassCheckBox.setIcon(PictureUtil.getPicture("xuanzhong_20px.png"));
					isSavePass = true;
				}
			}
		});
		// 登陆按钮事件
		loginButton.addMouseListener(new MouseAdapter() {
			//鼠标离开组件时调用。
			@Override
			public void mouseExited(MouseEvent e) {
				loginButton.setIcon(PictureUtil.getPicture("loginButton.png"));
			}
			//鼠标进入到组件上时调用。
			@Override
			public void mouseEntered(MouseEvent e) {
				loginButton.setIcon(PictureUtil.getPicture("loginActive.png"));
			}
			//鼠标按键在组件上单击（按下并释放）时调用。
			@Override
			public void mouseClicked(MouseEvent e) {
				login();
			}
		});
	}

	//登录
	private void login() {
		String userName = userNameField.getText();
		String password = String.valueOf(passWordField.getPassword());
		if(StringHelper.isEmpty(userName)) {
			CustomOptionPane.showMessageDialog(client.getLogin(), "请输入账号！", "友情提示");
			return;
		}
		if(StringHelper.isEmpty(password)) {
			CustomOptionPane.showMessageDialog(client.getLogin(), "请输入密码！", "友情提示");
			return;
		}
		String str = userName + Constants.LEFT_SLASH + password;
		// 登录服务器
		client.sendMsg(new Message(Constants.LOGIN_MSG, str));
	}
}
