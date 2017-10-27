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
* @version ����ʱ�䣺2017��10��17�� ����2:02:32
* ��¼����
*/
@SuppressWarnings("serial")
public class LoginWindow extends JDialog {
	
	/** �رհ�ť */
	private JLabel closeButton;
	/** ��С����ť */
	private JLabel minButton;
	/** ��˾��־ */
	private JLabel logoLabel;
	/** ͷ�� */
	private JLabel pictureLabel;
	/** ��¼��ť */
	private JLabel loginButton;
	/** ��������ѡ��� */
	private JLabel savePassCheckBox;
	/** �������� */
	private JLabel savePassLabel;
	/** �Զ���¼ѡ��� */
	private JLabel autoLoginCheckBox;
	/** �ڷ��Ǹ���½ */
	private JLabel autoLoginLabel;
	/** �û���Icon */
	private JLabel userNameIcon;
	/** ����Icon */
	private JLabel passwordIcon;
	/** �˺� */
	private JTextField userNameField;
	/** ���� */
	private JPasswordField passWordField;
	/** ���꣨�϶���¼�� */
	private Point point = new Point();
	/** �Ƿ�ѡ�б�������ѡ��� */
	private boolean isSavePass;
	/** �Ƿ�ѡ���Զ���¼ѡ��� */
	private boolean isAutoLogin;
	/** ϵͳ����ͼ�� */
	private TrayIcon icon;
	/** ϵͳ���̶��� */
	private SystemTray tray;
	/** �ͻ��˺��� */
	private Client client;
	
	public static LoginWindow getInstance(Client client) {
		LoginWindow inst = new LoginWindow(client);
		inst.setLocationRelativeTo(null);//���ô������
		inst.setVisible(true);// ���ô���ɼ�
		return inst;
	}
	
	public LoginWindow(Client client) {
		super();
		this.client = client;
		initGUI();
		initListener();
		initTrayIcon();
	}

	//���ز˵��������Ϣ
	private void initTrayIcon() {
		if (SystemTray.isSupported()) {
			try {
				tray = SystemTray.getSystemTray(); //ϵͳ��������õ��ǵ���ģʽ����ȡϵͳ���̶���
				icon = new TrayIcon(PictureUtil.getPicture("icon_120px.png").getImage(), "HAFELE��IM");
				icon.setImageAutoSize(true); //�Զ���Ӧ��С
				icon.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent e) {
						if (e.getButton() == MouseEvent.BUTTON1) {
							setVisible(true);
							// ��ȡ����
							requestFocus();
						}
					}
				});
				
				PopupMenu pm = new PopupMenu();
				MenuItem exitMI = new MenuItem("�˳�");
				MenuItem openMainPaneMI = new MenuItem("�������");
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
						// ��ȡ����
						requestFocus();
					}
				});
				pm.add(openMainPaneMI);
				pm.add(exitMI);
				icon.setPopupMenu(pm);
				tray.add(icon); //��С�����ʱ��ϵͳ�������ϵͳͼ��
				// �ŵ�client����
				client.setIcon(icon);
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//��¼����
	private void initGUI() {
		try {
			this.setSize(430,330);
			this.setUndecorated(true);// ȥ���߿�
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setAlwaysOnTop(true);// ����һֱ������Ļ����ǰ��
			//����UI
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
			
			//�رհ�ť
			closeButton = new JLabel("");
			closeButton.setIcon(PictureUtil.getPicture("close.png"));
			closeButton.setBounds(390, 0, 40, 20);
			contentPanel.add(closeButton);
			
			//��С����ť
			minButton = new JLabel("");
			minButton.setIcon(PictureUtil.getPicture("minimize.png"));
			minButton.setBounds(363, 0, 30, 20);
			contentPanel.add(minButton);
			
			//LOGO
			logoLabel = new JLabel("");
			logoLabel.setIcon(PictureUtil.getPicture("hafele.png"));
			logoLabel.setBounds(52, 53, 329, 70);
			contentPanel.add(logoLabel);
			
			//ͷ��
			pictureLabel = new JLabel("");
			pictureLabel.setIcon(PictureUtil.getPicture("HeadPortraits_100px.png"));
			pictureLabel.setBounds(42, 175, 100, 100);
			pictureLabel.setBorder(Constants.LIGHT_GRAY_BORDER);
			contentPanel.add(pictureLabel);
			
			//�û����ı���
			userNameField = new JTextField();
			userNameField.setBounds(199, 175, 182, 31);
			userNameField.setBorder(Constants.LIGHT_GRAY_BORDER);
			contentPanel.add(userNameField);
			
			//�����ı���
			passWordField = new JPasswordField();
			passWordField.setBounds(199, 208, 182, 31);
			passWordField.setBorder(Constants.LIGHT_GRAY_BORDER);
			contentPanel.add(passWordField);
			
			//��¼��ť
			loginButton = new JLabel("");
			loginButton.setIcon(PictureUtil.getPicture("loginButton.png"));
			loginButton.setBounds(171, 275, 196, 32);
			contentPanel.add(loginButton);
			
			//��ס����ѡ���
			savePassCheckBox = new JLabel("");
			savePassCheckBox.setIcon(PictureUtil.getPicture("buxuanzhong.png"));
			savePassCheckBox.setBounds(157, 250, 20, 20);
			contentPanel.add(savePassCheckBox);
			
			savePassLabel = new JLabel("��ס����");
			savePassLabel.setBounds(184, 249, 60, 20);
			contentPanel.add(savePassLabel);
			
			//�Զ���¼ѡ���
			autoLoginCheckBox = new JLabel("");
			autoLoginCheckBox.setIcon(PictureUtil.getPicture("buxuanzhong.png"));
			autoLoginCheckBox.setBounds(305, 250, 20, 20);
			contentPanel.add(autoLoginCheckBox);
			
			autoLoginLabel = new JLabel("�Զ���¼");
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
	
	//�¼�����
	private void initListener() {
		// �������¼�
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
		
		// �˳���ť�¼�
		closeButton.addMouseListener(new MouseAdapter() {
			
			//����뿪���ʱ���á�
			@Override
			public void mouseExited(MouseEvent e) {
				closeButton.setIcon(PictureUtil.getPicture("close.png"));
			}
			
			//�����뵽�����ʱ���á�
			@Override
			public void mouseEntered(MouseEvent e) {
				closeButton.setIcon(PictureUtil.getPicture("close_active.png"));
			}
			
			//��갴ť��������ͷ�ʱ���á�
			@Override
			public void mouseReleased(MouseEvent e) {
				System.exit(0);
			}
		});
		
		// ��С����ť�¼�
		minButton.addMouseListener(new MouseAdapter() {
			
			//����뿪���ʱ���á�
			@Override
			public void mouseExited(MouseEvent e) {
				minButton.setIcon(PictureUtil.getPicture("minimize.png"));
			}
			
			//�����뵽�����ʱ���á�
			@Override
			public void mouseEntered(MouseEvent e) {
				minButton.setIcon(PictureUtil.getPicture("minimize_active.png"));
			}
			
			//��갴ť��������ͷ�ʱ���á�
			@Override
			public void mouseReleased(MouseEvent e) {
				setVisible(false);
			}
		});
		
		// �˺�����򽹵��¼�
		userNameField.addFocusListener(new FocusListener() {
			//ʧȥ����
			@Override
			public void focusLost(FocusEvent e) {
				userNameField.setBorder(Constants.LIGHT_GRAY_BORDER);
			}
			//�õ�����
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
		// ��������򽹵��¼�
		passWordField.addFocusListener(new FocusListener() {
			//ʧȥ����
			@Override
			public void focusLost(FocusEvent e) {
				passWordField.setBorder(Constants.LIGHT_GRAY_BORDER);
			}
			//�õ�����
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
		// �������븴ѡ���¼�
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
		// �������������¼�
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
		// �Զ���¼��ѡ���¼�
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
		// �Զ���¼�����¼�
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
		// ��½��ť�¼�
		loginButton.addMouseListener(new MouseAdapter() {
			//����뿪���ʱ���á�
			@Override
			public void mouseExited(MouseEvent e) {
				loginButton.setIcon(PictureUtil.getPicture("loginButton.png"));
			}
			//�����뵽�����ʱ���á�
			@Override
			public void mouseEntered(MouseEvent e) {
				loginButton.setIcon(PictureUtil.getPicture("loginActive.png"));
			}
			//��갴��������ϵ��������²��ͷţ�ʱ���á�
			@Override
			public void mouseClicked(MouseEvent e) {
				login();
			}
		});
	}

	//��¼
	private void login() {
		String userName = userNameField.getText();
		String password = String.valueOf(passWordField.getPassword());
		if(StringHelper.isEmpty(userName)) {
			CustomOptionPane.showMessageDialog(client.getLogin(), "�������˺ţ�", "������ʾ");
			return;
		}
		if(StringHelper.isEmpty(password)) {
			CustomOptionPane.showMessageDialog(client.getLogin(), "���������룡", "������ʾ");
			return;
		}
		String str = userName + Constants.LEFT_SLASH + password;
		// ��¼������
		client.sendMsg(new Message(Constants.LOGIN_MSG, str));
	}
}
