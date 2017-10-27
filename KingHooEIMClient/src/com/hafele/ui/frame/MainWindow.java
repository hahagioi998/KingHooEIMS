package com.hafele.ui.frame;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.hafele.bean.User;
import com.hafele.socket.Client;
import com.hafele.ui.common.CustomOptionPane;
import com.hafele.ui.contacts.ContactsPanel;
import com.hafele.ui.conversation.ConversationPanel;
import com.hafele.ui.discussiongroup.DiscussionGroupPanel;
import com.hafele.util.Constants;
import com.hafele.util.PictureUtil;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月20日 下午4:42:53
* 主窗体
*/
@SuppressWarnings("serial")
public class MainWindow extends JDialog {
	
	/** 主面板 */
	private JPanel contentPanel;
	/** 基本信息面板 */
	private JPanel baseInfoPanel;
	/** 搜索栏显示面板 */
	private JPanel searchLabelPanel;
	/** 搜索栏输入面板 */
	private JPanel searchFieldPanel;
	/** 分类面板（联系人、群组、会话） */
	private JPanel typeInfoPanel;
	/** 显示联系人面板 */
	private ContactsPanel contactsPanel;
	/** 显示讨论组面板 */
	private DiscussionGroupPanel discussionGroupPanel;
	/** 显示会话面板 */
	private ConversationPanel conversationPanel;
	/** 底部工具栏面板 */
	private JPanel bottomPanel;
	/** 右上角图标 */
	private JLabel titlePicture;
	/** 标题 */
	private JLabel title;
	/** 关闭按钮 */
	private JLabel closeButton;
	/** 最小化按钮 */
	private JLabel minButton;
	/** 换肤按钮 */
	private JLabel skinButton;
	/** 坐标（用于记录鼠标拖拽时，鼠标按下那一刻的坐标） */
	private Point point = new Point();
	/** 系统托盘图标 */
	private TrayIcon icon;
	/** 系统托盘对象 */
	private SystemTray tray;
	
	private User user;
	private Client client;
	
	/** 是否选中联系人选项框 */
	private boolean isContacts = true;
	/** 是否选中群、讨论组选项框 */
	private boolean isGroup = false;
	/** 是否选中会话选项框 */
	private boolean isConversation = false;
	/** 是否选关闭所有声音 */
	private boolean isCloseAllVoice = false;
	/** 是否选关闭头像闪动 */
	private boolean isCloseHeadFlashing = false;
	
	/** 用户头像 */
	private JLabel headPicture;
	/** 用户状态显示栏 */
	private JLabel state;
	/** 用户名显示框 */
	private JLabel userName;
	/** 个性签名显示框 */
	private JLabel signatureLabel;
	/** 个性签名输入框 */
	private JTextField signatureField;
	/** 搜索显示框 */
	private JLabel searchLabel;
	/** 搜索图标按钮 */
	private JLabel searchButton;
	/** 搜索输入框 */
	private JTextField searchField;
	/** 搜索关闭图标按钮 */
	private JLabel searchCloseButton;
	/** 底部主菜单图标按钮 */
	private JLabel mainMenuButton;
	/** 底部设置图标按钮 */
	private JLabel settingButton;
	/** 底部消息管理图标按钮 */
	private JLabel messageManagerButton;
	/** 底部查找图标按钮 */
	private JLabel bottomSearchButton;
	/** 联系人、群、讨论组、会话面板 */
	private JPanel panel;
	/** 选择联系人面板按钮 */
	private JLabel contacts;
	/** 选择群、讨论组面板按钮 */
	private JLabel group;
	/** 选择最近会话面板按钮 */
	private JLabel conversation;
	
	public static MainWindow getInstance(Client client) {
		MainWindow mainWindow = new MainWindow(client);
		mainWindow.setVisible(true);
		mainWindow.requestFocus();
		return mainWindow;
	}
	
	public MainWindow(Client client) {
		super();
		// 声明变量接受，而不在构造方法里传参（考虑到界面有可能再次用到对象里面的值）
		this.client = client;
		this.user = client.getUser();
		initGUI();
		initTrayIcon();
		initListener();
	}

	//窗体搭建
	private void initGUI() {
		try {
			this.setBounds(900, 30, 281, 697);
			this.setAlwaysOnTop(true);
			this.setUndecorated(true);
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			
			// 主面板
			contentPanel = new JPanel() {
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(PictureUtil.getPicture("mainWindowBackground.jpg").getImage(), 0, 0, null);
					this.setOpaque(false);
				}
			};
			contentPanel.setLayout(null);
			getContentPane().add(contentPanel, BorderLayout.CENTER);
			
			JPanel titlePanel = new JPanel();
			titlePanel.setBounds(0, 0, 281, 25);
			titlePanel.setOpaque(false);
			contentPanel.add(titlePanel);
			titlePanel.setLayout(null);
			
			titlePicture = new JLabel("");
			titlePicture.setBounds(10, 5, 20, 15);
			titlePicture.setIcon(PictureUtil.getPicture("A.png"));
			titlePanel.add(titlePicture);
			
			title = new JLabel("H'A'FELE");
			title.setForeground(Color.WHITE);
			title.setFont(Constants.BASIC_FONTT14);
			title.setBounds(40, 2, 60, 20);
			titlePanel.add(title);
			
			closeButton = new JLabel("");
			closeButton.setBounds(241, 0, 40, 20);
			closeButton.setIcon(PictureUtil.getPicture("close.png"));
			titlePanel.add(closeButton);
			
			minButton = new JLabel("");
			minButton.setBounds(211, 0, 30, 20);
			minButton.setIcon(PictureUtil.getPicture("minimize.png"));
			titlePanel.add(minButton);
			
			skinButton = new JLabel("");
			skinButton.setBounds(181, 0, 30, 20);
			skinButton.setIcon(PictureUtil.getPicture("skin.png"));
			titlePanel.add(skinButton);
			
			baseInfoPanel = new JPanel();
			baseInfoPanel.setBounds(0, 25, 281, 93);
			baseInfoPanel.setOpaque(false);
			contentPanel.add(baseInfoPanel);
			baseInfoPanel.setLayout(null);
			
			headPicture = new JLabel("");
			headPicture.setBounds(10, 23, 60, 60);
			headPicture.setIcon(PictureUtil.getPicture("HeadPortraits_60px.png"));
			baseInfoPanel.add(headPicture);
			
			state = new JLabel("");
			state.setBounds(80, 20, 32, 20);
			state.setIcon(PictureUtil.getPicture("state/online.png"));
			baseInfoPanel.add(state);
			
			userName = new JLabel("");
			userName.setFont(Constants.BASIC_FONTT14);
			userName.setForeground(Color.WHITE);
			userName.setBounds(122, 20, 149, 20);
			userName.setText(user.getName());
			baseInfoPanel.add(userName);
			
			signatureLabel = new JLabel();
			signatureLabel.setFont(Constants.BASIC_FONT);
			signatureLabel.setForeground(Color.WHITE);
			signatureLabel.setBorder(null);
			signatureLabel.setBounds(80, 40, 191, 20);
			baseInfoPanel.add(signatureLabel);
			
			signatureField = new JTextField();
			signatureField.setFont(Constants.BASIC_FONT);
			signatureField.setBounds(80, 40, 191, 20);
			signatureField.setBorder(null);
			signatureField.setText(user.getSignature());
			signatureField.setVisible(false);
			baseInfoPanel.add(signatureField);
			
			searchLabelPanel = new JPanel();
			searchLabelPanel.setBounds(0, 118, 281, 30);
			searchLabelPanel.setBackground(new Color(21, 127, 178));
			searchLabelPanel.setLayout(null);
			contentPanel.add(searchLabelPanel);
			
			searchLabel = new JLabel("	"+Constants.SEARCH_TXT);
			searchLabel.setFont(Constants.BASIC_FONTT14);
			searchLabel.setBounds(5, 0, 276, 30);
			searchLabelPanel.add(searchLabel);
			
			searchButton = new JLabel("");
			searchButton.setIcon(PictureUtil.getPicture("searchButton.png"));
			searchButton.setBounds(245, 0, 30, 30);
			searchLabel.add(searchButton);
			
			searchFieldPanel = new JPanel();
			searchFieldPanel.setBounds(0, 118, 281, 30);
			searchFieldPanel.setVisible(false);
			searchFieldPanel.setBorder(Constants.WHITE_BORDER);
			searchFieldPanel.setBackground(Color.WHITE);
			searchFieldPanel.setLayout(null);
			contentPanel.add(searchFieldPanel);
			
			searchField = new JTextField();
			searchField.setFont(Constants.BASIC_FONTT14);
			searchField.setBounds(0, 0, 251, 30);
			searchField.setBorder(null);
			searchFieldPanel.add(searchField);
			
			searchCloseButton = new JLabel("");
			searchFieldPanel.add(searchCloseButton);
			searchCloseButton.setIcon(PictureUtil.getPicture("searchCloseButton.png"));
			searchCloseButton.setBounds(251, 0, 30, 30);
			
			panel = new JPanel();
			panel.setBackground(new Color(232, 245, 252));
			panel.setBounds(0, 148, 281, 520);
			contentPanel.add(panel);
			panel.setLayout(null);
			
			typeInfoPanel = new JPanel();
			typeInfoPanel.setBounds(0, 0, 281, 38);
			typeInfoPanel.setOpaque(false);
			typeInfoPanel.setBorder(null);
			panel.add(typeInfoPanel);
			typeInfoPanel.setLayout(null);
			
			contacts = new JLabel("");
			contacts.setBounds(0, 0, 94, 38);
			contacts.setIcon(PictureUtil.getPicture("contacts_click.png"));
			typeInfoPanel.add(contacts);
			
			group = new JLabel("");
			group.setBounds(94, 0, 94, 38);
			group.setIcon(PictureUtil.getPicture("group.png"));
			typeInfoPanel.add(group);
			
			conversation = new JLabel("");
			conversation.setBounds(188, 0, 93, 38);
			conversation.setIcon(PictureUtil.getPicture("conversation.png"));
			typeInfoPanel.add(conversation);
			
			//联系人面板
			contactsPanel = new ContactsPanel(client);
			contactsPanel.setBounds(0, 38, 281, 490);
			contactsPanel.setVisible(true);
			panel.add(contactsPanel);
			
			//群、讨论组面板
			discussionGroupPanel = new DiscussionGroupPanel();
			discussionGroupPanel.setBounds(0, 38, 281, 490);
			discussionGroupPanel.setVisible(false);
			panel.add(discussionGroupPanel);
			
			//最近会话面板
			conversationPanel = new ConversationPanel();
			conversationPanel.setBounds(0, 38, 281, 490);
			conversationPanel.setVisible(false);
			panel.add(conversationPanel);
			
			bottomPanel = new JPanel();
			bottomPanel.setBackground(new Color(203, 233, 247));
			bottomPanel.setBounds(0, 667, 281, 30);
			contentPanel.add(bottomPanel);
			bottomPanel.setLayout(null);
			
			mainMenuButton = new JLabel("");
			mainMenuButton.setBounds(5, 0, 30, 30);
			mainMenuButton.setIcon(PictureUtil.getPicture("mainMenu.png"));
			bottomPanel.add(mainMenuButton);
			
			settingButton = new JLabel("");
			settingButton.setBounds(37, 0, 30, 30);
			settingButton.setIcon(PictureUtil.getPicture("setting.png"));
			bottomPanel.add(settingButton);
			
			messageManagerButton = new JLabel("");
			messageManagerButton.setBounds(67, 0, 30, 30);
			messageManagerButton.setIcon(PictureUtil.getPicture("messageManager.png"));
			bottomPanel.add(messageManagerButton);
			
			bottomSearchButton = new JLabel("");
			bottomSearchButton.setBounds(97, 0, 30, 30);
			bottomSearchButton.setIcon(PictureUtil.getPicture("search.png"));
			bottomPanel.add(bottomSearchButton);
			
			if(user.getSignature() != null) {
				signatureLabel.setText(user.getSignature());
			}else {
				signatureLabel.setText("编辑个性签名");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//系统托盘
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
				MenuItem closeAllVoice = new MenuItem("关闭所有声音");
				MenuItem closeHeadFlashing = new MenuItem("关闭头像闪动");
				MenuItem addStatusInfo = new MenuItem("添加状态信息");
				MenuItem offline = new MenuItem("离线");
				MenuItem hiding = new MenuItem("隐身");
				MenuItem noDisturbing = new MenuItem("请勿打扰");
				MenuItem busy = new MenuItem("忙碌");
				MenuItem leave = new MenuItem("离开");
				MenuItem QMe = new MenuItem("Q我吧");
				MenuItem online = new MenuItem("在线");
				
				pm.add(online);
				pm.add(QMe);
				pm.add(leave);
				pm.add(busy);
				pm.add(noDisturbing);
				pm.add(hiding);
				pm.add(offline);
				pm.add(addStatusInfo);
				pm.add(closeHeadFlashing);
				pm.add(closeAllVoice);
				pm.add(openMainPaneMI);
				pm.add(exitMI);
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
	
	//事件监听
	private void initListener() {
		// 主窗体事件
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				point.x = e.getX();
				point.y = e.getY();
			}
			@Override  
            public void mouseClicked(MouseEvent e) {// 鼠标单击事件  
				if (e.getButton() == MouseEvent.BUTTON1) { // 判断是鼠标左键按下
					
					if(signatureLabel.getText().trim().equals(user.getSignature())) {
						
					} else {
						CustomOptionPane.showMessageDialog(client.getMainWindow(), "确定要保存个性签名", "提示");
						user.setSignature(signatureField.getText().trim());
						signatureLabel.setText(user.getSignature());
					}
					if(searchField.getText().equals("")) {
						searchLabelPanel.setVisible(true);
						searchFieldPanel.setVisible(false);
					} else {
						System.err.println("false");
					}
                	signatureField.requestFocus(false);
                	signatureLabel.setVisible(true);
    				signatureField.setVisible(false);
    				
                } else if (e.getButton() == MouseEvent.BUTTON3) { // 判断是鼠标右键按下  
                	
                } else { //滚轴
                	
                }  
			}
		});
		this.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				Point p = getLocation();
				setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
			}
		});
		
		//关闭按钮
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				closeButton.setIcon(PictureUtil.getPicture("close_active.png"));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				closeButton.setIcon(PictureUtil.getPicture("close.png"));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				tray.remove(icon);
				System.exit(0);
			}
			
		});
		
		//最小化按钮
		minButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				minButton.setIcon(PictureUtil.getPicture("minimize_active.png"));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				minButton.setIcon(PictureUtil.getPicture("minimize.png"));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				setVisible(false);
			}
		});
		
		//换肤按钮
		skinButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				skinButton.setIcon(PictureUtil.getPicture("skin_active.png"));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				skinButton.setIcon(PictureUtil.getPicture("skin.png"));
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
		});
		
		//头像
		headPicture.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				headPicture.setBorder(Constants.GREEN_BORDER);
				headPicture.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//鼠标图标改为手型
			}

			@Override
			public void mouseExited(MouseEvent e) {
				headPicture.setBorder(null);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
		});
		
		//状态按钮
		state.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				state.setBorder(Constants.LIGHT_GRAY_BORDER);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				state.setBorder(null);
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				JPopupMenu jpm = new JPopupMenu();
				jpm.setBackground(Constants.BACKGROUND_COLOR);
				jpm.setBorder(Constants.LIGHT_GRAY_BORDER);
				JMenuItem closeAllVoice = new JMenuItem("关闭所有声音");
				JMenuItem closeHeadFlashing = new JMenuItem("关闭头像闪动");
				JMenuItem addStatusInfo = new JMenuItem("添加状态信息");
				JMenuItem offline = new JMenuItem("离线");
				offline.setIcon(PictureUtil.getPicture("state/offline_15px.png"));
				offline.setFont(Constants.BASIC_FONT);
				JMenuItem hiding = new JMenuItem("隐身");
				hiding.setIcon(PictureUtil.getPicture("state/hiding_15px.png"));
				hiding.setFont(Constants.BASIC_FONT);
				JMenuItem noDisturbing = new JMenuItem("请勿打扰");
				noDisturbing.setIcon(PictureUtil.getPicture("state/noDisturbing_15px.png"));
				noDisturbing.setFont(Constants.BASIC_FONT);
				JMenuItem busy = new JMenuItem("忙碌");
				busy.setIcon(PictureUtil.getPicture("state/busy_15px.png"));
				busy.setFont(Constants.BASIC_FONT);
				JMenuItem leave = new JMenuItem("离开");
				leave.setIcon(PictureUtil.getPicture("state/leave_15px.png"));
				leave.setFont(Constants.BASIC_FONT);
				JMenuItem QMe = new JMenuItem("Q我吧");
				QMe.setIcon(PictureUtil.getPicture("state/QMe_15px.png"));
				QMe.setFont(Constants.BASIC_FONT);
				JMenuItem online = new JMenuItem("在线");
				online.setIcon(PictureUtil.getPicture("state/online_15px.png"));
				online.setFont(Constants.BASIC_FONT);
				
				jpm.add(online);
				jpm.add(QMe);
				jpm.add(leave);
				jpm.add(busy);
				jpm.add(noDisturbing);
				jpm.add(hiding);
				jpm.add(offline);
				jpm.addSeparator();
				jpm.add(addStatusInfo);
				jpm.add(closeHeadFlashing);
				jpm.add(closeAllVoice);
				jpm.show(state, e.getX(), e.getY());
			}
		});
		//个性签名显示框
		signatureLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				signatureLabel.setBorder(Constants.LIGHT_GRAY_BORDER);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				signatureLabel.setBorder(null);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				signatureLabel.setVisible(false);
				signatureField.setVisible(true);
				signatureField.requestFocus(true);
			}
		});
		
		//个性签名输入框焦点
		signatureField.addFocusListener(new FocusListener() {
			//失去焦点
			@Override
			public void focusLost(FocusEvent e) {
				
			}
			//得到焦点
			@Override
			public void focusGained(FocusEvent e) {
				
			}
		});
		
		//个性签名输入框鼠标事件
		signatureField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
                
			}
		});
		
		//搜索显示框鼠标事件
		searchLabelPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
                
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				searchLabelPanel.setVisible(false);
				searchFieldPanel.setVisible(true);
				searchField.requestFocus(true);
			}
		});
		
		//搜索关闭按钮鼠标监听
		searchCloseButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
                
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				searchLabelPanel.setVisible(true);
				searchFieldPanel.setVisible(false);
			}
		});
		
		
		//主菜单按钮
		mainMenuButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				mainMenuButton.setIcon(PictureUtil.getPicture("mainMenu_active.png"));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				mainMenuButton.setIcon(PictureUtil.getPicture("mainMenu.png"));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				JPopupMenu jpm = new JPopupMenu();
				jpm.setBackground(Constants.BACKGROUND_COLOR);
				jpm.setBorder(Constants.LIGHT_GRAY_BORDER);
				JMenuItem logoff = new JMenuItem("注销");
				logoff.setIcon(PictureUtil.getPicture("logoff.png"));
				logoff.setFont(Constants.BASIC_FONT);
				JMenuItem exit = new JMenuItem("退出");
				exit.setIcon(PictureUtil.getPicture("exit.png"));
				exit.setFont(Constants.BASIC_FONT);
				JMenuItem help = new JMenuItem("帮助");
				help.setIcon(PictureUtil.getPicture("help.png"));
				help.setFont(Constants.BASIC_FONT);
				JMenuItem receivefile  = new JMenuItem("文件接收目录");
				receivefile.setIcon(PictureUtil.getPicture("receivefile.png"));
				receivefile.setFont(Constants.BASIC_FONT);
				JMenuItem updateClient = new JMenuItem("升级客户端");
				updateClient.setIcon(PictureUtil.getPicture("updateClient.png"));
				updateClient.setFont(Constants.BASIC_FONT);
				JMenuItem aboutWe = new JMenuItem("关于HAFELE");
				aboutWe.setIcon(PictureUtil.getPicture("aboutWe.png"));
				aboutWe.setFont(Constants.BASIC_FONT);
				jpm.add(logoff);
				jpm.add(exit);
				jpm.add(help);
				jpm.add(receivefile);
				jpm.add(updateClient);
				jpm.add(aboutWe);
				jpm.show(mainMenuButton, e.getX(), e.getY());
			}
		});
		
		//设置按钮鼠标事件
		settingButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				settingButton.setIcon(PictureUtil.getPicture("setting_active.png"));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				settingButton.setIcon(PictureUtil.getPicture("setting.png"));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
		});
		
		//消息管理按钮鼠标事件
		messageManagerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				messageManagerButton.setIcon(PictureUtil.getPicture("messageManager_active.png"));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				messageManagerButton.setIcon(PictureUtil.getPicture("messageManager.png"));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
		});
		
		//底部搜索按钮鼠标事件
		bottomSearchButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				bottomSearchButton.setIcon(PictureUtil.getPicture("search_active.png"));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				bottomSearchButton.setIcon(PictureUtil.getPicture("search.png"));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
		});
		
		//联系人
		contacts.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if(!isContacts) {
					contacts.setIcon(PictureUtil.getPicture("contacts_active.png"));
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if(!isContacts) {
					contacts.setIcon(PictureUtil.getPicture("contacts.png"));
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if(!isContacts) {
					contacts.setIcon(PictureUtil.getPicture("contacts_click.png"));
					group.setIcon(PictureUtil.getPicture("group.png"));
					conversation.setIcon(PictureUtil.getPicture("conversation.png"));
					isContacts = true;
					isConversation = false;
					isGroup = false;
					contactsPanel.setVisible(true);
					discussionGroupPanel.setVisible(false);
					conversationPanel.setVisible(false);
				}
				
			}
		});
		//群、讨论组
		group.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if(!isGroup) {
					group.setIcon(PictureUtil.getPicture("group_active.png"));
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if(!isGroup) {
					group.setIcon(PictureUtil.getPicture("group.png"));
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if(!isGroup) {
					group.setIcon(PictureUtil.getPicture("group_click.png"));
					contacts.setIcon(PictureUtil.getPicture("contacts.png"));
					conversation.setIcon(PictureUtil.getPicture("conversation.png"));
					isContacts = false;
					isConversation = false;
					isGroup = true;
					contactsPanel.setVisible(false);
					discussionGroupPanel.setVisible(true);
					conversationPanel.setVisible(false);
				}
				
			}
		});
		//会话
		conversation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if(!isConversation) {
					conversation.setIcon(PictureUtil.getPicture("conversation_active.png"));
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if(!isConversation) {
					conversation.setIcon(PictureUtil.getPicture("conversation.png"));
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if(!isConversation) {
					conversation.setIcon(PictureUtil.getPicture("conversation_click.png"));
					group.setIcon(PictureUtil.getPicture("group.png"));
					contacts.setIcon(PictureUtil.getPicture("contacts.png"));
					isContacts = false;
					isConversation = true;
					isGroup = false;
					contactsPanel.setVisible(false);
					discussionGroupPanel.setVisible(false);
					conversationPanel.setVisible(true);
				}
			}
		});
	}
}
