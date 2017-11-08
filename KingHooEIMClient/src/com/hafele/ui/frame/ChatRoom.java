package com.hafele.ui.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.alee.laf.tabbedpane.TabbedPaneStyle;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.hafele.socket.Client;
import com.hafele.util.Constants;
import com.hafele.util.PictureUtil;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version ����ʱ�䣺2017��11��6�� ����2:31:46
* ������������
*/
@SuppressWarnings("serial")
public class ChatRoom extends JFrame {
	
	/** �����  */
	private JPanel contentPane;
	/** ��С����ť  */
	private JLabel minButton;
	/** ��󻯰�ť  */
	private JLabel exitButton;
	/** ��ʾ��Ϣ����***�����У� */
	public JLabel titleLabel;
	/** �·����촰�� */
	private JPanel downPanel;
	/** ������壨�ɺϲ���  */
	public WebTabbedPane tabbedPane;
	/** ���꣨���ڼ�¼�����קʱ����갴����һ�̵����꣩ */
	private Point point = new Point();
	
	private Client client;
	
	public static ChatRoom getInstance(Client client) {
		ChatRoom frame = new ChatRoom(client);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.requestFocus();
		return frame;
	}
	
	public ChatRoom(Client client) {
		super();
		initGUI();
		initListener();
		// �رմ�����Ҫ������ҳǩ���
		this.client = client;
	}

	private void initGUI() {
		try {
			this.setSize(660, 560);
			this.setUndecorated(true);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setIconImage(PictureUtil.getPicture("QQ_20px.png").getImage());
			
			// �����
			contentPane = new JPanel() {
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(PictureUtil.getPicture("charRomeBackground.png").getImage(), 0, 0, null);
					this.setOpaque(false);
				}
			};
			contentPane.setLayout(null);
			contentPane.setBorder(Constants.LIGHT_GRAY_BORDER);
			setContentPane(contentPane);
			
			titleLabel = new JLabel();
			titleLabel.setFont(Constants.BASIC_FONT);
			titleLabel.setBounds(10, 0, 573, 30);
			contentPane.add(titleLabel);
			
			// ���촰�ںϲ����
			downPanel = new JPanel();
			contentPane.add(downPanel);
			downPanel.setOpaque(false);
			downPanel.setBounds(1, 40, 658, 519);
			downPanel.setLayout(new BorderLayout());
			
			minButton = new JLabel();
			contentPane.add(minButton);
			minButton.setBounds(593, 0, 31, 20);
			minButton.setIcon(PictureUtil.getPicture("minimize.png"));
			
			exitButton = new JLabel();
			contentPane.add(exitButton);
			exitButton.setBounds(621, 0, 39, 20);
			exitButton.setIcon(PictureUtil.getPicture("close.png"));

			tabbedPane = new WebTabbedPane();
			downPanel.add(tabbedPane, BorderLayout.CENTER);
			tabbedPane.setOpaque(false);
			tabbedPane.setTabbedPaneStyle(TabbedPaneStyle.attached);//�������߿�
	        tabbedPane.setTopBg(new Color(240, 240, 240, 60));
	        tabbedPane.setBottomBg(new Color(255, 255, 255, 160));
	        tabbedPane.setSelectedTopBg(new Color(240, 240, 255, 50));
	        tabbedPane.setSelectedBottomBg(new Color(240, 240, 255, 50));
	        tabbedPane.setBackground(new Color(255, 255, 255, 200));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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
		// �������Ҽ��ر�
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				// TODO ��ռ�¼��������
				client.setRoom(null);
				client.tabMap.clear();
			}
		});
		// ��С����ť�¼�
		minButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				minButton.setIcon(PictureUtil.getPicture("minimize.png"));
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				minButton.setIcon(PictureUtil.getPicture("minimize_active.png"));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				setExtendedState(JFrame.ICONIFIED);
			}
		});
		// �˳���ť�¼�
		exitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				exitButton.setIcon(PictureUtil.getPicture("close.png"));
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				exitButton.setIcon(PictureUtil.getPicture("close_active.png"));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				dispose();
				// TODO ��ռ�¼��������
				client.setRoom(null);
				client.tabMap.clear();
			}
		});
	}
}
