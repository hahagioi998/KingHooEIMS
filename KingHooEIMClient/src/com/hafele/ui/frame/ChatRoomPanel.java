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
* @version 创建时间：2017年11月5日 下午9:23:39
* 聊天室消息输入和显示面板
*/
@SuppressWarnings("serial")
public class ChatRoomPanel extends JPanel {
	
	/** 好友信息面板 */
	private JPanel friendInfoPane;
	/** 好友头像 */
	private JLabel picture;
	/** 好友昵称 */
	private JLabel nickName;
	/** 好友签名 */
	private JLabel descript;
	/** 历史消息面板 */
	private JPanel history;
	/** 历史消息滚动条 */
	private JScrollPane historyScroll;
	/** 历史消息区域 */
	public JTextPane historyTextPane;
	/** 工具面板 */
	private JPanel tools;
	/** 截屏按钮 */
	private JLabel screen;
	/** 抖动按钮 */
	private JLabel shake;
	/** 表情按钮 */
	private JLabel emoticon;
	/** 字体按钮 */
	private JLabel textFont;
	/** 输入消息面板 */
	private JPanel input;
	/** 输入消息滚动条 */
	private JScrollPane inputScroll;
	/** 输入消息区域 */
	private JTextPane inputTextPane;
	/** 取消按钮 */
	private JButton quitButton;
	/** 发送按钮 */
	private JLabel sendButton;
	
	private JPanel fontPane;
	@SuppressWarnings("rawtypes")
	private JComboBox fontName = null;// 字体名称
	@SuppressWarnings("rawtypes")
	private JComboBox fontSize = null;// 字号大小
	@SuppressWarnings("rawtypes")
	private JComboBox fontStyle = null;// 文字样式
	@SuppressWarnings("rawtypes")
	private JComboBox fontForeColor = null;// 文字颜色
	@SuppressWarnings("rawtypes")
	private JComboBox fontBackColor = null;// 文字背景颜色
	private String[] str_name = { "宋体", "黑体", "Dialog", "Gulim" };
	private String[] str_Size = { "15", "17", "19", "21", "23" };
	private String[] str_Style = { "常规", "斜体", "粗体", "粗斜体" };
	private String[] str_Color = { "黑色", "橙色", "黄色", "绿色" };
	private String[] str_BackColor = { "白色", "灰色", "淡红", "淡蓝", "淡黄", "淡绿" };
	
	private boolean isFonting;// 是否正在编辑字体
	
	public User self;
	public User friend;
	private Client client;
	
	/** 记录图片 */
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
			
			// 好友信息面板
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
			
			// 历史记录
			history = new JPanel();
			add(history);
			history.setBounds(0, 70, 660, 240);
			history.setOpaque(false);
			history.setLayout(null);
			
			historyScroll = new JScrollPane();
			historyScroll.setBounds(0, 0, 660, 240);
			history.add(historyScroll);
			historyTextPane = new JTextPane();
			historyTextPane.setEditable(false);//不允许编辑
			
			historyScroll.setViewportView(historyTextPane);
			historyScroll.getVerticalScrollBar().setUI(new CustomScrollBarUI());
			historyScroll.setBorder(null);
			
			// 工具栏
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
			textFont.setToolTipText("字体");
			textFont.setIcon(PictureUtil.getPicture("text.png"));
			
			emoticon = new JLabel();
			tools.add(emoticon);
			emoticon.setBounds(40, 3, 20, 20);
			emoticon.setToolTipText("表情");
			emoticon.setIcon(PictureUtil.getPicture("emoticon.png"));
			
			shake = new JLabel();
			tools.add(shake);
			shake.setBounds(70, 3, 20, 20);
			shake.setToolTipText("抖动");
			shake.setIcon(PictureUtil.getPicture("shake.png"));
			
			screen = new JLabel();
			tools.add(screen);
			screen.setBounds(100, 3, 20, 20);
			screen.setToolTipText("截屏");
			screen.setIcon(PictureUtil.getPicture("screenCapture.png"));
			
			// 输入框
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
			
			// 取消按钮（关闭）
			quitButton = new JButton();
			add(quitButton);
			quitButton.setBounds(460, 446, 93, 30);
			quitButton.setBorder(Constants.LIGHT_GRAY_BORDER);
			quitButton.setIcon(PictureUtil.getPicture("quitButton.png"));
			
			// 发送按钮
			sendButton = new JLabel();
			add(sendButton);
			sendButton.setBounds(560, 446, 93, 30);
			sendButton.setBorder(Constants.LIGHT_GRAY_BORDER);
			sendButton.setIcon(PictureUtil.getPicture("sendButton.png"));
			
			// 编辑字体（只做了几个示例）
			fontPane = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					try { // 使用Windows的界面风格
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
			
			fontName = new JComboBox(str_name); // 字体名称
			fontName.setFont(Constants.BASIC_FONT);
			fontSize = new JComboBox(str_Size); // 字号
			fontSize.setFont(Constants.BASIC_FONT);
			fontStyle = new JComboBox(str_Style); // 样式
			fontStyle.setFont(Constants.BASIC_FONT);
			fontForeColor = new JComboBox(str_Color); // 颜色
			fontForeColor.setFont(Constants.BASIC_FONT);
			fontBackColor = new JComboBox(str_BackColor); // 背景颜色
			fontBackColor.setFont(Constants.BASIC_FONT);
			
			// 开始将所需组件加入容器
			JLabel jlabel_1 = new JLabel("字体："); 
			jlabel_1.setFont(Constants.BASIC_FONT);
			JLabel jlabel_2 = new JLabel("样式：");
			jlabel_2.setFont(Constants.BASIC_FONT);
			JLabel jlabel_3 = new JLabel("字号：");
			jlabel_3.setFont(Constants.BASIC_FONT);
			JLabel jlabel_4 = new JLabel("颜色：");
			jlabel_4.setFont(Constants.BASIC_FONT);
			JLabel jlabel_5 = new JLabel("背景：");
			jlabel_5.setFont(Constants.BASIC_FONT);
			fontPane.add(jlabel_1); // 加入标签
			fontPane.add(fontName); // 加入组件
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
		// 工具栏-字体
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
		// 工具栏-表情
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
					// 设置控件相对于父窗体的位置
					Point loc = getLocationOnScreen();
					image.setBounds(loc.x + 10, loc.y + 30, 350, 300);
				}
				image.requestFocus();
			}
		});
		// 工具栏-抖动
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
		// 工具栏-截屏
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
		// 回车发送
		inputTextPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				msg = inputTextPane.getText();
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// 发送消息
					if (StringHelper.isEmpty(msg)) {
						CustomOptionPanel.showMessageDialog(client.getRoom(), "发送内容不能为空，请重新输入！", "友情提示");
					} else {
						sendMsg(true);
					}
				}
			}
		});
		// 发送按钮事件
		sendButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// 发送消息
				if (StringHelper.isEmpty(inputTextPane.getText())) {
					CustomOptionPanel.showMessageDialog(client.getRoom(), "发送内容不能为空，请重新输入！", "友情提示");
				} else {
					sendMsg(false);
				}
			}
		});
		// 字体样式的所有事件
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
		// 输入框样式，在获取焦点的时候让其自己选择
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
		// 样式
		message.setSize(Integer.valueOf((String)fontSize.getSelectedItem()));
		message.setFamily((String)fontName.getSelectedItem());
		message.setStyle(fontStyle.getSelectedIndex());
		message.setFore(fontForeColor.getSelectedIndex());
		message.setBack(fontBackColor.getSelectedIndex());
		// 图片
		message.setImageMark(imgBuffer.toString());
		return message;
	}
	
	private SimpleAttributeSet getFontSet(Color color) {
		SimpleAttributeSet set = new SimpleAttributeSet();
		if (null != color) {// 固定头信息
			StyleConstants.setBold(set, false);
			StyleConstants.setItalic(set, false);
			StyleConstants.setFontSize(set, 15);
			StyleConstants.setFontFamily(set, "宋体");
			StyleConstants.setForeground(set, color);
			return set;
		}
		// 字体名称
		StyleConstants.setFontFamily(set, (String)fontName.getSelectedItem());
		// 字号
		StyleConstants.setFontSize(set, Integer.valueOf((String)fontSize.getSelectedItem()));
		// 样式
		int styleIndex = fontStyle.getSelectedIndex();
		if (styleIndex == 0) {// 常规
			StyleConstants.setBold(set, false);
			StyleConstants.setItalic(set, false);
		}
		if (styleIndex == 1) {// 斜体
			StyleConstants.setBold(set, false);
			StyleConstants.setItalic(set, true);
		}
		if (styleIndex == 2) {// 粗体
			StyleConstants.setBold(set, true);
			StyleConstants.setItalic(set, false);
		}
		if (styleIndex == 3) {// 粗斜体
			StyleConstants.setBold(set, true);
			StyleConstants.setItalic(set, true);
		}
		// 字体颜色
		int foreIndex = fontForeColor.getSelectedIndex();
		if (foreIndex == 0) {// 黑色
			StyleConstants.setForeground(set, Color.BLACK);
		}
		if (foreIndex == 1) {// 橙色
			StyleConstants.setForeground(set, Color.ORANGE);
		}
		if (foreIndex == 2) {// 黄色
			StyleConstants.setForeground(set, Color.YELLOW);
		}
		if (foreIndex == 3) {// 绿色
			StyleConstants.setForeground(set, Color.GREEN);
		}
		// 背景颜色
		int backIndex = fontBackColor.getSelectedIndex();
		if (backIndex == 0) {// 白色
			StyleConstants.setBackground(set, Color.WHITE);
		}
		if (backIndex == 1) {// 灰色
			StyleConstants.setBackground(set, new Color(200, 200, 200));
		}
		if (backIndex == 2) {// 淡红
			StyleConstants.setBackground(set, new Color(255, 200, 200));
		}
		if (backIndex == 3) {// 淡蓝
			StyleConstants.setBackground(set, new Color(200, 200, 255));
		}
		if (backIndex == 4) {// 淡黄
			StyleConstants.setBackground(set, new Color(255, 255, 200));
		}
		if (backIndex == 5) {// 淡绿
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
			// 获取set
			SimpleAttributeSet set = getFontSet(null);
			// 这里主要是为了缩进
			StyleConstants.setLeftIndent(set, 10);// 缩进
			// 此处开始缩进
			doc.setParagraphAttributes(doc.getLength(), doc.getLength(), set, true);
			// 展示正文
			doc.insertString(doc.getLength(), str, set);
			// 将缩进还原回来
			StyleConstants.setLeftIndent(set, 0f);
			doc.setParagraphAttributes(doc.getLength(), doc.getLength(), set, true);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	private void sendMsg(boolean enterKey) {
		try {
			// 插入头信息
			insertNameMsg();
			// 需要注意
			if (!enterKey) {
				msg = inputTextPane.getText();
			}
			// 插入消息正文
			for (int i = 0; i < msg.length(); i++) {
				if (inputTextPane.getStyledDocument().getCharacterElement(i).getName().equals("icon")) {
					Icon icon = StyleConstants.getIcon(inputTextPane.getStyledDocument().getCharacterElement(i).getAttributes());
					ChatPic cupic = (ChatPic) icon;
					String fileName = "/feiqq/resource/image/face/" + cupic.getNumber() + ".gif";
					historyTextPane.setCaretPosition(historyTextPane.getStyledDocument().getLength());
					historyTextPane.insertIcon(new ImageIcon(Emoticon.class.getResource(fileName)));
					// 记录图片
					imgBuffer.append(":)" + String.valueOf(cupic.getNumber()) + "|" + String.valueOf(i) + "/");
				}
				if (inputTextPane.getStyledDocument().getCharacterElement(i).getName().equals("content")) {
					sendContentMsg(inputTextPane.getStyledDocument().getText(i, 1));
				}
			}
			// 换行
			sendContentMsg("\n");
			// 发送
			// 先将图片占用的空格替换掉
			String msgContent = msg.replaceAll(" ", "");
			client.sendMsg(createMsg(Constants.GENRAL_MSG, msgContent));
			//清空 
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
