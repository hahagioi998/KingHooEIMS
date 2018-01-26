package com.hafele.ui.frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.hafele.bean.Message;
import com.hafele.bean.User;
import com.hafele.socket.Client;
import com.hafele.ui.common.ChatPic;
import com.hafele.ui.common.CustomOptionPanel;
import com.hafele.ui.common.CustomScrollBarUI;
import com.hafele.ui.common.Emoticon;
import com.hafele.util.Constants;
import com.hafele.util.PictureUtil;
import com.hafele.util.StringHelper;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version ����ʱ�䣺2017��11��5�� ����9:23:39
* ��������Ϣ�������ʾ���
*/
@SuppressWarnings("serial")
public class ChatRoomPanel extends JPanel {
	
	/** ������Ϣ��� */
	private JPanel friendInfoPane;
	/** ����ͷ�� */
	private JLabel picture;
	/** �����ǳ� */
	private JLabel nickName;
	/** ����ǩ�� */
	private JLabel descript;
	/** ��ʷ��Ϣ��� */
	private JPanel history;
	/** ��ʷ��Ϣ������ */
	private JScrollPane historyScroll;
	/** ��ʷ��Ϣ���� */
	public JTextPane historyTextPane;
	/** ������� */
	private JPanel tools;
	/** ������ť */
	private JLabel screen;
	/** ������ť */
	private JLabel shake;
	/** ���鰴ť */
	private JLabel emoticon;
	/** ���尴ť */
	private JLabel textFont;
	/** ������Ϣ��� */
	private JPanel input;
	/** ������Ϣ������ */
	private JScrollPane inputScroll;
	/** ������Ϣ���� */
	private JTextPane inputTextPane;
	/** ȡ����ť */
	private JButton quitButton;
	/** ���Ͱ�ť */
	private JLabel sendButton;
	
	private JPanel fontPane;
	@SuppressWarnings("rawtypes")
	private JComboBox fontName = null;// ��������
	@SuppressWarnings("rawtypes")
	private JComboBox fontSize = null;// �ֺŴ�С
	@SuppressWarnings("rawtypes")
	private JComboBox fontStyle = null;// ������ʽ
	@SuppressWarnings("rawtypes")
	private JComboBox fontForeColor = null;// ������ɫ
	@SuppressWarnings("rawtypes")
	private JComboBox fontBackColor = null;// ���ֱ�����ɫ
	private String[] str_name = { "����", "����", "Dialog", "Gulim" };
	private String[] str_Size = { "15", "17", "19", "21", "23" };
	private String[] str_Style = { "����", "б��", "����", "��б��" };
	private String[] str_Color = { "��ɫ", "��ɫ", "��ɫ", "��ɫ" };
	private String[] str_BackColor = { "��ɫ", "��ɫ", "����", "����", "����", "����" };
	
	private boolean isFonting;// �Ƿ����ڱ༭����
	
	public User self;
	public User friend;
	private Client client;
	
	/** ��¼ͼƬ */
	private StringBuffer imgBuffer = new StringBuffer();
	public Emoticon image;
	private String msg;
	public int position;
	
	public ChatRoomPanel(Client client, User self, User friend) {
		super();
		this.self = self;
		this.client = client;
		this.friend = friend;
		initGUI();
		initListener();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initGUI() {
		try {
			setLayout(null);
			setOpaque(false);
			setSize(660, 445);
			
			// ������Ϣ���
			friendInfoPane = new JPanel();
			add(friendInfoPane);
			friendInfoPane.setBounds(0, 0, 660, 70);
			friendInfoPane.setLayout(null);
			friendInfoPane.setOpaque(false);
			
			picture = new JLabel();
			friendInfoPane.add(picture);
			picture.setBounds(3, 5, 60, 60);
			picture.setBorder(null);
			picture.setIcon(new ImageIcon(PictureUtil.getPicture(friend.getHeadPicture()+"_60px.png")
					.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
			
			nickName = new JLabel();
			friendInfoPane.add(nickName);
			nickName.setFont(Constants.BASIC_FONTT14);
			nickName.setText(friend.getName());
			nickName.setBounds(70, 10, 402, 25);
			
			descript = new JLabel();
			friendInfoPane.add(descript);
			descript.setFont(Constants.BASIC_FONT);
			descript.setForeground(Color.GRAY);
			descript.setBounds(70, 35, 402, 25);
			descript.setText(friend.getSignature());
			
			// ��ʷ��¼
			history = new JPanel();
			add(history);
			history.setBounds(0, 70, 660, 240);
			history.setOpaque(false);
			history.setLayout(null);
			
			historyScroll = new JScrollPane();
			historyScroll.setBounds(0, 0, 660, 240);
			history.add(historyScroll);
			historyTextPane = new JTextPane();
			historyTextPane.setEditable(false);//������༭
			
			historyScroll.setViewportView(historyTextPane);
			historyScroll.getVerticalScrollBar().setUI(new CustomScrollBarUI());
			historyScroll.setBorder(null);
			
			// ������
			tools = new JPanel();
			tools.setBackground(Color.WHITE);
			add(tools);
			tools.setLayout(null);
			tools.setBorder(Constants.LIGHT_GRAY_BORDER);
//			tools.setOpaque(false);
			tools.setBounds(0, 310, 660, 30);
			
			textFont = new JLabel();
			tools.add(textFont);
			textFont.setBounds(10, 3, 20, 20);
			textFont.setToolTipText("����");
			textFont.setIcon(PictureUtil.getPicture("text.png"));
			
			emoticon = new JLabel();
			tools.add(emoticon);
			emoticon.setBounds(40, 3, 20, 20);
			emoticon.setToolTipText("����");
			emoticon.setIcon(PictureUtil.getPicture("emoticon.png"));
			
			shake = new JLabel();
			tools.add(shake);
			shake.setBounds(70, 3, 20, 20);
			shake.setToolTipText("����");
			shake.setIcon(PictureUtil.getPicture("shake.png"));
			
			screen = new JLabel();
			tools.add(screen);
			screen.setBounds(100, 3, 20, 20);
			screen.setToolTipText("����");
			screen.setIcon(PictureUtil.getPicture("screenCapture.png"));
			
			// �����
			input = new JPanel();
			add(input);
			input.setBounds(0, 340, 660, 105);
			input.setLayout(null);
			
			inputScroll = new JScrollPane();
			inputScroll.setBounds(0, 0, 660, 105);
			input.add(inputScroll);
			inputTextPane = new JTextPane();
			inputScroll.setViewportView(inputTextPane);
			inputScroll.getVerticalScrollBar().setUI(new CustomScrollBarUI());
			inputScroll.setBorder(null);
			inputScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			
			// ȡ����ť���رգ�
			quitButton = new JButton();
			add(quitButton);
			quitButton.setBounds(460, 446, 93, 30);
			quitButton.setBorder(Constants.LIGHT_GRAY_BORDER);
			quitButton.setIcon(PictureUtil.getPicture("quitButton.png"));
			
			// ���Ͱ�ť
			sendButton = new JLabel();
			add(sendButton);
			sendButton.setBounds(560, 446, 93, 30);
			sendButton.setBorder(Constants.LIGHT_GRAY_BORDER);
			sendButton.setIcon(PictureUtil.getPicture("sendButton.png"));
			
			// �༭���壨ֻ���˼���ʾ����
			fontPane = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					try { // ʹ��Windows�Ľ�����
						UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			add(fontPane);
			fontPane.setBounds(0, 280, 660, 25);
			fontPane.setBorder(Constants.LIGHT_GRAY_BORDER);
			fontPane.setLayout(new BoxLayout(fontPane, BoxLayout.X_AXIS));
			
			fontName = new JComboBox(str_name); // ��������
			fontName.setFont(Constants.BASIC_FONT);
			fontSize = new JComboBox(str_Size); // �ֺ�
			fontSize.setFont(Constants.BASIC_FONT);
			fontStyle = new JComboBox(str_Style); // ��ʽ
			fontStyle.setFont(Constants.BASIC_FONT);
			fontForeColor = new JComboBox(str_Color); // ��ɫ
			fontForeColor.setFont(Constants.BASIC_FONT);
			fontBackColor = new JComboBox(str_BackColor); // ������ɫ
			fontBackColor.setFont(Constants.BASIC_FONT);
			
			// ��ʼ�����������������
			JLabel jlabel_1 = new JLabel("���壺"); 
			jlabel_1.setFont(Constants.BASIC_FONT);
			JLabel jlabel_2 = new JLabel("��ʽ��");
			jlabel_2.setFont(Constants.BASIC_FONT);
			JLabel jlabel_3 = new JLabel("�ֺţ�");
			jlabel_3.setFont(Constants.BASIC_FONT);
			JLabel jlabel_4 = new JLabel("��ɫ��");
			jlabel_4.setFont(Constants.BASIC_FONT);
			JLabel jlabel_5 = new JLabel("������");
			jlabel_5.setFont(Constants.BASIC_FONT);
			fontPane.add(jlabel_1); // �����ǩ
			fontPane.add(fontName); // �������
			fontPane.add(jlabel_2);
			fontPane.add(fontStyle);
			fontPane.add(jlabel_3);
			fontPane.add(fontSize);
			fontPane.add(jlabel_4);
			fontPane.add(fontForeColor);
			fontPane.add(jlabel_5);
			fontPane.add(fontBackColor);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private void initListener() {
		// ������-����
		textFont.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				if (!isFonting) {
					textFont.setBorder(BorderFactory.createEmptyBorder());
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				textFont.setBorder(Constants.LIGHT_GRAY_BORDER);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (!isFonting) {
					isFonting = true;
					history.setBounds(0, 70, 660, 210);
					textFont.setBorder(Constants.LIGHT_GRAY_BORDER);
				} else {
					isFonting = false;
					textFont.setBorder(BorderFactory.createEmptyBorder());
					history.setBounds(0, 70, 660, 240);
				}
			}
		});
		// ������-����
		emoticon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				emoticon.setBorder(BorderFactory.createEmptyBorder());
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				emoticon.setBorder(Constants.LIGHT_GRAY_BORDER);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (null == image) {
					image = new Emoticon(ChatRoomPanel.this);
					image.setVisible(true);
					// ���ÿؼ�����ڸ������λ��
					Point loc = getLocationOnScreen();
					image.setBounds(loc.x + 10, loc.y + 30, 350, 300);
				}
				image.requestFocus();
			}
		});
		// ������-����
		shake.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				shake.setBorder(BorderFactory.createEmptyBorder());
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				shake.setBorder(Constants.LIGHT_GRAY_BORDER);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				client.sendMsg(createMsg(Constants.SHAKE_MSG, null));
			}
		});
		// ������-����
		screen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				screen.setBorder(BorderFactory.createEmptyBorder());
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				screen.setBorder(Constants.LIGHT_GRAY_BORDER);
			}
		});
		// �س�����
		inputTextPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				msg = inputTextPane.getText();
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// ������Ϣ
					if (StringHelper.isEmpty(msg)) {
						CustomOptionPanel.showMessageDialog(client.getRoom(), "�������ݲ���Ϊ�գ����������룡", "������ʾ");
					} else {
						sendMsg(true);
					}
				}
			}
		});
		// ���Ͱ�ť�¼�
		sendButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// ������Ϣ
				if (StringHelper.isEmpty(inputTextPane.getText())) {
					CustomOptionPanel.showMessageDialog(client.getRoom(), "�������ݲ���Ϊ�գ����������룡", "������ʾ");
				} else {
					sendMsg(false);
				}
			}
		});
		// ������ʽ�������¼�
		fontName.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				updateInputStyle();
			}
		});
		fontSize.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				updateInputStyle();
			}
		});
		fontStyle.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				updateInputStyle();
			}
		});
		fontForeColor.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				updateInputStyle();
			}
		});
		fontBackColor.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				updateInputStyle();
			}
		});
		// �������ʽ���ڻ�ȡ�����ʱ�������Լ�ѡ��
		inputTextPane.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				updateInputStyle();
				if (null != image) {
					image.dispose();
					image = null;
				}
			}
		});
		historyTextPane.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (null != image) {
					image.dispose();
					image = null;
				}
			}
		});
	}
	
	private Message createMsg(String type, String str) {
		Message message = new Message();
		message.setSenderId(self.getLoginName());
		message.setSenderName(self.getName());
		message.setReceiverId(friend.getLoginName());
		message.setReceiverName(friend.getName());
		message.setType(type);
		if (Constants.GENRAL_MSG.equals(type)) {
			message.setContent(str);
		}
		// ��ʽ
		message.setSize(Integer.valueOf((String)fontSize.getSelectedItem()));
		message.setFamily((String)fontName.getSelectedItem());
		message.setStyle(fontStyle.getSelectedIndex());
		message.setFore(fontForeColor.getSelectedIndex());
		message.setBack(fontBackColor.getSelectedIndex());
		// ͼƬ
		message.setImageMark(imgBuffer.toString());
		return message;
	}
	
	private SimpleAttributeSet getFontSet(Color color) {
		SimpleAttributeSet set = new SimpleAttributeSet();
		if (null != color) {// �̶�ͷ��Ϣ
			StyleConstants.setBold(set, false);
			StyleConstants.setItalic(set, false);
			StyleConstants.setFontSize(set, 15);
			StyleConstants.setFontFamily(set, "����");
			StyleConstants.setForeground(set, color);
			return set;
		}
		// ��������
		StyleConstants.setFontFamily(set, (String)fontName.getSelectedItem());
		// �ֺ�
		StyleConstants.setFontSize(set, Integer.valueOf((String)fontSize.getSelectedItem()));
		// ��ʽ
		int styleIndex = fontStyle.getSelectedIndex();
		if (styleIndex == 0) {// ����
			StyleConstants.setBold(set, false);
			StyleConstants.setItalic(set, false);
		}
		if (styleIndex == 1) {// б��
			StyleConstants.setBold(set, false);
			StyleConstants.setItalic(set, true);
		}
		if (styleIndex == 2) {// ����
			StyleConstants.setBold(set, true);
			StyleConstants.setItalic(set, false);
		}
		if (styleIndex == 3) {// ��б��
			StyleConstants.setBold(set, true);
			StyleConstants.setItalic(set, true);
		}
		// ������ɫ
		int foreIndex = fontForeColor.getSelectedIndex();
		if (foreIndex == 0) {// ��ɫ
			StyleConstants.setForeground(set, Color.BLACK);
		}
		if (foreIndex == 1) {// ��ɫ
			StyleConstants.setForeground(set, Color.ORANGE);
		}
		if (foreIndex == 2) {// ��ɫ
			StyleConstants.setForeground(set, Color.YELLOW);
		}
		if (foreIndex == 3) {// ��ɫ
			StyleConstants.setForeground(set, Color.GREEN);
		}
		// ������ɫ
		int backIndex = fontBackColor.getSelectedIndex();
		if (backIndex == 0) {// ��ɫ
			StyleConstants.setBackground(set, Color.WHITE);
		}
		if (backIndex == 1) {// ��ɫ
			StyleConstants.setBackground(set, new Color(200, 200, 200));
		}
		if (backIndex == 2) {// ����
			StyleConstants.setBackground(set, new Color(255, 200, 200));
		}
		if (backIndex == 3) {// ����
			StyleConstants.setBackground(set, new Color(200, 200, 255));
		}
		if (backIndex == 4) {// ����
			StyleConstants.setBackground(set, new Color(255, 255, 200));
		}
		if (backIndex == 5) {// ����
			StyleConstants.setBackground(set, new Color(200, 255, 200));
		}
		return set;
	}

	private void insertNameMsg() {
		StyledDocument doc = historyTextPane.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), StringHelper.createSenderInfo(self.getName()), getFontSet(Color.BLUE));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sendContentMsg(String str) {
		try {
			StyledDocument doc = historyTextPane.getStyledDocument();
			// ��ȡset
			SimpleAttributeSet set = getFontSet(null);
			// ������Ҫ��Ϊ������
			StyleConstants.setLeftIndent(set, 10);// ����
			// �˴���ʼ����
			doc.setParagraphAttributes(doc.getLength(), doc.getLength(), set, true);
			// չʾ����
			doc.insertString(doc.getLength(), str, set);
			// ��������ԭ����
			StyleConstants.setLeftIndent(set, 0f);
			doc.setParagraphAttributes(doc.getLength(), doc.getLength(), set, true);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	private void sendMsg(boolean enterKey) {
		try {
			// ����ͷ��Ϣ
			insertNameMsg();
			// ��Ҫע��
			if (!enterKey) {
				msg = inputTextPane.getText();
			}
			// ������Ϣ����
			for (int i = 0; i < msg.length(); i++) {
				if (inputTextPane.getStyledDocument().getCharacterElement(i).getName().equals("icon")) {
					Icon icon = StyleConstants.getIcon(inputTextPane.getStyledDocument().getCharacterElement(i).getAttributes());
					ChatPic cupic = (ChatPic) icon;
					String fileName = "/feiqq/resource/image/face/" + cupic.getNumber() + ".gif";
					historyTextPane.setCaretPosition(historyTextPane.getStyledDocument().getLength());
					historyTextPane.insertIcon(new ImageIcon(Emoticon.class.getResource(fileName)));
					// ��¼ͼƬ
					imgBuffer.append(":)" + String.valueOf(cupic.getNumber()) + "|" + String.valueOf(i) + "/");
				}
				if (inputTextPane.getStyledDocument().getCharacterElement(i).getName().equals("content")) {
					sendContentMsg(inputTextPane.getStyledDocument().getText(i, 1));
				}
			}
			// ����
			sendContentMsg("\n");
			// ����
			// �Ƚ�ͼƬռ�õĿո��滻��
			String msgContent = msg.replaceAll(" ", "");
			client.sendMsg(createMsg(Constants.GENRAL_MSG, msgContent));
			//��� 
			imgBuffer.setLength(0);
			inputTextPane.setText("");
			inputTextPane.setCaretPosition(0);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateInputStyle() {
		StyledDocument doc = inputTextPane.getStyledDocument();
		doc.setParagraphAttributes(0, doc.getLength(), getFontSet(null), false);
	}
	
	public void insertIcon(ImageIcon icon) {
		inputTextPane.insertIcon(icon);
	}
}
