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
* @version ����ʱ�䣺2017��10��20�� ����4:42:53
* ������
*/
@SuppressWarnings("serial")
public class MainWindow extends JDialog {
	
	/** ����� */
	private JPanel contentPanel;
	/** ������Ϣ��� */
	private JPanel baseInfoPanel;
	/** ��������ʾ��� */
	private JPanel searchLabelPanel;
	/** ������������� */
	private JPanel searchFieldPanel;
	/** ������壨��ϵ�ˡ�Ⱥ�顢�Ự�� */
	private JPanel typeInfoPanel;
	/** ��ʾ��ϵ����� */
	private ContactsPanel contactsPanel;
	/** ��ʾ��������� */
	private DiscussionGroupPanel discussionGroupPanel;
	/** ��ʾ�Ự��� */
	private ConversationPanel conversationPanel;
	/** �ײ���������� */
	private JPanel bottomPanel;
	/** ���Ͻ�ͼ�� */
	private JLabel titlePicture;
	/** ���� */
	private JLabel title;
	/** �رհ�ť */
	private JLabel closeButton;
	/** ��С����ť */
	private JLabel minButton;
	/** ������ť */
	private JLabel skinButton;
	/** ���꣨���ڼ�¼�����קʱ����갴����һ�̵����꣩ */
	private Point point = new Point();
	/** ϵͳ����ͼ�� */
	private TrayIcon icon;
	/** ϵͳ���̶��� */
	private SystemTray tray;
	
	private User user;
	private Client client;
	
	/** �Ƿ�ѡ����ϵ��ѡ��� */
	private boolean isContacts = true;
	/** �Ƿ�ѡ��Ⱥ��������ѡ��� */
	private boolean isGroup = false;
	/** �Ƿ�ѡ�лỰѡ��� */
	private boolean isConversation = false;
	/** �Ƿ�ѡ�ر��������� */
	private boolean isCloseAllVoice = false;
	/** �Ƿ�ѡ�ر�ͷ������ */
	private boolean isCloseHeadFlashing = false;
	
	/** �û�ͷ�� */
	private JLabel headPicture;
	/** �û�״̬��ʾ�� */
	private JLabel state;
	/** �û�����ʾ�� */
	private JLabel userName;
	/** ����ǩ����ʾ�� */
	private JLabel signatureLabel;
	/** ����ǩ������� */
	private JTextField signatureField;
	/** ������ʾ�� */
	private JLabel searchLabel;
	/** ����ͼ�갴ť */
	private JLabel searchButton;
	/** ��������� */
	private JTextField searchField;
	/** �����ر�ͼ�갴ť */
	private JLabel searchCloseButton;
	/** �ײ����˵�ͼ�갴ť */
	private JLabel mainMenuButton;
	/** �ײ�����ͼ�갴ť */
	private JLabel settingButton;
	/** �ײ���Ϣ����ͼ�갴ť */
	private JLabel messageManagerButton;
	/** �ײ�����ͼ�갴ť */
	private JLabel bottomSearchButton;
	/** ��ϵ�ˡ�Ⱥ�������顢�Ự��� */
	private JPanel panel;
	/** ѡ����ϵ����尴ť */
	private JLabel contacts;
	/** ѡ��Ⱥ����������尴ť */
	private JLabel group;
	/** ѡ������Ự��尴ť */
	private JLabel conversation;
	
	public static MainWindow getInstance(Client client) {
		MainWindow mainWindow = new MainWindow(client);
		mainWindow.setVisible(true);
		mainWindow.requestFocus();
		return mainWindow;
	}
	
	public MainWindow(Client client) {
		super();
		// �����������ܣ������ڹ��췽���ﴫ�Σ����ǵ������п����ٴ��õ����������ֵ��
		this.client = client;
		this.user = client.getUser();
		initGUI();
		initTrayIcon();
		initListener();
	}

	//����
	private void initGUI() {
		try {
			this.setBounds(900, 30, 281, 697);
			this.setAlwaysOnTop(true);
			this.setUndecorated(true);
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			
			// �����
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
			
			//��ϵ�����
			contactsPanel = new ContactsPanel(client);
			contactsPanel.setBounds(0, 38, 281, 490);
			contactsPanel.setVisible(true);
			panel.add(contactsPanel);
			
			//Ⱥ�����������
			discussionGroupPanel = new DiscussionGroupPanel();
			discussionGroupPanel.setBounds(0, 38, 281, 490);
			discussionGroupPanel.setVisible(false);
			panel.add(discussionGroupPanel);
			
			//����Ự���
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
				signatureLabel.setText("�༭����ǩ��");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//ϵͳ����
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
				MenuItem closeAllVoice = new MenuItem("�ر���������");
				MenuItem closeHeadFlashing = new MenuItem("�ر�ͷ������");
				MenuItem addStatusInfo = new MenuItem("���״̬��Ϣ");
				MenuItem offline = new MenuItem("����");
				MenuItem hiding = new MenuItem("����");
				MenuItem noDisturbing = new MenuItem("�������");
				MenuItem busy = new MenuItem("æµ");
				MenuItem leave = new MenuItem("�뿪");
				MenuItem QMe = new MenuItem("Q�Ұ�");
				MenuItem online = new MenuItem("����");
				
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
						// ��ȡ����
						requestFocus();
					}
				});
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
	
	//�¼�����
	private void initListener() {
		// �������¼�
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				point.x = e.getX();
				point.y = e.getY();
			}
			@Override  
            public void mouseClicked(MouseEvent e) {// ��굥���¼�  
				if (e.getButton() == MouseEvent.BUTTON1) { // �ж�������������
					
					if(signatureLabel.getText().trim().equals(user.getSignature())) {
						
					} else {
						CustomOptionPane.showMessageDialog(client.getMainWindow(), "ȷ��Ҫ�������ǩ��", "��ʾ");
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
    				
                } else if (e.getButton() == MouseEvent.BUTTON3) { // �ж�������Ҽ�����  
                	
                } else { //����
                	
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
		
		//�رհ�ť
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
		
		//��С����ť
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
		
		//������ť
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
		
		//ͷ��
		headPicture.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				headPicture.setBorder(Constants.GREEN_BORDER);
				headPicture.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));//���ͼ���Ϊ����
			}

			@Override
			public void mouseExited(MouseEvent e) {
				headPicture.setBorder(null);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
			}
		});
		
		//״̬��ť
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
				JMenuItem closeAllVoice = new JMenuItem("�ر���������");
				JMenuItem closeHeadFlashing = new JMenuItem("�ر�ͷ������");
				JMenuItem addStatusInfo = new JMenuItem("���״̬��Ϣ");
				JMenuItem offline = new JMenuItem("����");
				offline.setIcon(PictureUtil.getPicture("state/offline_15px.png"));
				offline.setFont(Constants.BASIC_FONT);
				JMenuItem hiding = new JMenuItem("����");
				hiding.setIcon(PictureUtil.getPicture("state/hiding_15px.png"));
				hiding.setFont(Constants.BASIC_FONT);
				JMenuItem noDisturbing = new JMenuItem("�������");
				noDisturbing.setIcon(PictureUtil.getPicture("state/noDisturbing_15px.png"));
				noDisturbing.setFont(Constants.BASIC_FONT);
				JMenuItem busy = new JMenuItem("æµ");
				busy.setIcon(PictureUtil.getPicture("state/busy_15px.png"));
				busy.setFont(Constants.BASIC_FONT);
				JMenuItem leave = new JMenuItem("�뿪");
				leave.setIcon(PictureUtil.getPicture("state/leave_15px.png"));
				leave.setFont(Constants.BASIC_FONT);
				JMenuItem QMe = new JMenuItem("Q�Ұ�");
				QMe.setIcon(PictureUtil.getPicture("state/QMe_15px.png"));
				QMe.setFont(Constants.BASIC_FONT);
				JMenuItem online = new JMenuItem("����");
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
		//����ǩ����ʾ��
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
		
		//����ǩ������򽹵�
		signatureField.addFocusListener(new FocusListener() {
			//ʧȥ����
			@Override
			public void focusLost(FocusEvent e) {
				
			}
			//�õ�����
			@Override
			public void focusGained(FocusEvent e) {
				
			}
		});
		
		//����ǩ�����������¼�
		signatureField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
                
			}
		});
		
		//������ʾ������¼�
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
		
		//�����رհ�ť������
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
		
		
		//���˵���ť
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
				JMenuItem logoff = new JMenuItem("ע��");
				logoff.setIcon(PictureUtil.getPicture("logoff.png"));
				logoff.setFont(Constants.BASIC_FONT);
				JMenuItem exit = new JMenuItem("�˳�");
				exit.setIcon(PictureUtil.getPicture("exit.png"));
				exit.setFont(Constants.BASIC_FONT);
				JMenuItem help = new JMenuItem("����");
				help.setIcon(PictureUtil.getPicture("help.png"));
				help.setFont(Constants.BASIC_FONT);
				JMenuItem receivefile  = new JMenuItem("�ļ�����Ŀ¼");
				receivefile.setIcon(PictureUtil.getPicture("receivefile.png"));
				receivefile.setFont(Constants.BASIC_FONT);
				JMenuItem updateClient = new JMenuItem("�����ͻ���");
				updateClient.setIcon(PictureUtil.getPicture("updateClient.png"));
				updateClient.setFont(Constants.BASIC_FONT);
				JMenuItem aboutWe = new JMenuItem("����HAFELE");
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
		
		//���ð�ť����¼�
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
		
		//��Ϣ����ť����¼�
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
		
		//�ײ�������ť����¼�
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
		
		//��ϵ��
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
		//Ⱥ��������
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
		//�Ự
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
