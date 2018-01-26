package com.hafele.ui.contacts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.hafele.bean.Category;
import com.hafele.bean.Message;
import com.hafele.bean.User;
import com.hafele.socket.Client;
import com.hafele.ui.common.CategoryNode;
import com.hafele.ui.common.CustomOptionPanel;
import com.hafele.ui.common.CustomScrollBarUI;
import com.hafele.ui.common.CustomTabComponent;
import com.hafele.ui.common.CustomTreeUI;
import com.hafele.ui.common.Emoticon;
import com.hafele.ui.frame.AddContactsWindow;
import com.hafele.ui.frame.ChatRoom;
import com.hafele.ui.frame.ChatRoomPanel;
import com.hafele.util.Constants;
import com.hafele.util.PictureUtil;
import com.hafele.util.StringHelper;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月22日 下午2:24:26
* 联系人
*/
@SuppressWarnings("serial")
public class ContactsPanel extends JPanel {
	
	private JTree jTree;
	private DefaultTreeModel model;
	private DefaultMutableTreeNode root;
	/** 自己的client */
	private Client selfClient;
	/** 带滚动条面板 */
	private JScrollPane scrollPane;
	private JTextField textField;
	private static Color inColor = new Color(252, 240, 193); 
	private static Color selectColor = new Color(253, 236, 170);
	
	public ContactsPanel(Client client) {
		this.selfClient = client;
		this.setLayout(new BorderLayout());
		this.setBackground(new Color(232, 245, 252));
		this.setOpaque(false);
		
		root = new DefaultMutableTreeNode();
		model = new DefaultTreeModel(root);
		selfClient.setBuddyRoot(root);// 放到client核心类，添加分组用到
		selfClient.setBuddyModel(model);// 放到client核心类，刷新好友UI用到
		
		// 加载数据
		loadJTree();
		
		jTree = new JTree(model);
		jTree.setBackground(Constants.BACKGROUND_COLOR);
		jTree.setUI(new CustomTreeUI()); // 自定义UI
		jTree.setCellRenderer(new ContactsNodeRenderer());// 自定义节点渲染器
		jTree.setRootVisible(false);// 隐藏根节点
		jTree.setToggleClickCount(1);// 点击次数
		jTree.setInvokesStopCellEditing(true);// 修改节点文字之后生效
		selfClient.setBuddyTree(jTree); // 放到client核心类，方便处理
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setViewportView(jTree);
		scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
		// 屏蔽横向滚动条
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		textField = new JTextField();
		textField.setPreferredSize(new Dimension(281, 23));
		textField.setBorder(Constants.LIGHT_GRAY_BORDER);
		jTree.setCellEditor(new DefaultCellEditor(textField));
		this.add(scrollPane, BorderLayout.CENTER);
		
		//textField键盘监听事件
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					TreePath path = jTree.getSelectionPath();
					Object object = path.getLastPathComponent();
					if (jTree.isEditable()) {
						if (!"".equals(textField.getText())) {
							Message msg = new Message();
							msg.setType(Constants.EDIT_USER_CATE_MSG);
							CategoryNode categoryNode = (CategoryNode) object;
							msg.setContent(categoryNode.category.getId() + Constants.LEFT_SLASH + textField.getText());
							//向服务器发送请求处理
							selfClient.sendMsg(msg);
							categoryNode.categoryName.setText(textField.getText());
							categoryNode.category.setGroupName(textField.getText());
							model.reload(categoryNode);
						}
						jTree.stopEditing();
						jTree.setEditable(false);
					}
				}
			}
		});
		
		//鼠标运动监听器
		jTree.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				TreePath path = jTree.getPathForLocation(e.getX(), e.getY());
				if(path != null) {
					Object object = path.getLastPathComponent();
					if(object instanceof ContactsNode) {
						for(int i = 0; i < root.getChildCount(); i++) {
							Object category = root.getChildAt(i);
							((CategoryNode)category).categoryContent.setBackground(Constants.BACKGROUND_COLOR);
							model.reload((CategoryNode) category);
							for (int j = 0; j < root.getChildAt(i).getChildCount(); j++) {
								Object contacts = root.getChildAt(i).getChildAt(j);
								if (((ContactsNode)contacts).userContent.getBackground() != selectColor) {
									if (contacts == ((ContactsNode)object)) {
										((ContactsNode)contacts).userContent.setBackground(inColor);
									} else {
										((ContactsNode)contacts).userContent.setBackground(Constants.BACKGROUND_COLOR);
									}
									model.reload(((ContactsNode)contacts));
								}
							}
						}
					}
					if(object instanceof CategoryNode) {
						for (int i = 0; i < root.getChildCount(); i++) {
							Object category = root.getChildAt(i);
							if (category == ((CategoryNode)object)) {
								((CategoryNode)category).categoryContent.setBackground(inColor);
							} else {
								((CategoryNode)category).categoryContent.setBackground(Constants.BACKGROUND_COLOR);
							}
							model.reload((CategoryNode)category);
							for (int j = 0; j < root.getChildAt(i).getChildCount(); j++) {
								Object contacts = root.getChildAt(i).getChildAt(j);
								if (((ContactsNode)contacts).userContent.getBackground() != selectColor) {
									((ContactsNode)contacts).userContent.setBackground(Constants.BACKGROUND_COLOR);
									model.reload(((ContactsNode)contacts));
								}
							}
						}
					}
				}
			}
		});
		
		//鼠标监听器
		jTree.addMouseListener(new MouseAdapter() {
			//鼠标点击
			@Override
			public void mouseClicked(MouseEvent e) {
				// 监听鼠标左键
				if(e.getButton() == MouseEvent.BUTTON1) {
					TreePath path = jTree.getSelectionPath();
					if (null == path) {
						return;
					}
					// path中的node节点（path不为空，这里基本不会空）
					Object object = path.getLastPathComponent();
					if(e.getClickCount() == 1) {
						if (jTree.isEditable()) {
							if (!"".equals(textField.getText())) {
								Message msg = new Message();
								msg.setType(Constants.EDIT_USER_CATE_MSG);
								CategoryNode categoryNode = (CategoryNode) object;
								msg.setContent(categoryNode.category.getId() + Constants.LEFT_SLASH + textField.getText());
								//向服务器发送请求处理
								selfClient.sendMsg(msg);
								categoryNode.categoryName.setText(textField.getText());
								categoryNode.category.setGroupName(textField.getText());
								model.reload(categoryNode);
							}
							jTree.stopEditing();
							jTree.setEditable(false);
						}
					}
					if(e.getClickCount() == 2) {
						//好友node节点（区分群组）
						if (object instanceof ContactsNode) {
							//从根节点开始获取所有节点（第一层全部是分组）
							for(int i = 0; i < root.getChildCount(); i++) {
								//鼠标滑入好友node节点时，需要将分组上面的颜色恢复
								Object category = root.getChildAt(i);
								((CategoryNode)category).categoryContent.setBackground(Constants.BACKGROUND_COLOR);
								model.reload((CategoryNode)category);//刷新UI
								//这一层全是好友node节点
								for(int j = 0; j < root.getChildAt(i).getChildCount(); j++) {
									Object friend = root.getChildAt(i).getChildAt(j);
									// 不是鼠标选中的那个
									if(friend != (ContactsNode)object) {
										((ContactsNode)friend).userContent.setBackground(Constants.BACKGROUND_COLOR);
									} else {
										((ContactsNode)friend).userContent.setBackground(selectColor);
										//开启聊天窗口
										Message message = null;
										User user = ((ContactsNode)object).contacts;
										ChatRoom room = selfClient.getRoom() == null ? 
												ChatRoom.getInstance(selfClient) : selfClient.getRoom();
										// 相应好友的panel没打开
										if (!selfClient.tabMap.containsKey(user.getName())) {
											room.setTitle(selfClient.getUser().getName() + " —— " + user.getName());
											room.titleLabel.setText(selfClient.getUser().getName() + " —— " + user.getName());
											ChatRoomPanel pane = new ChatRoomPanel(selfClient, selfClient.getUser(), user);
											room.tabbedPane.addTab(user.getName(), null, pane, user.getName());
											// 重绘过的tab页签
											room.tabbedPane.setTabComponentAt(room.tabbedPane.indexOfTab(user.getName()), 
													new CustomTabComponent(selfClient.getUser().getName(), user.getName(), room, selfClient));
											int index = room.tabbedPane.indexOfTab(user.getName());
											room.tabbedPane.setSelectedIndex(index);
											// 将队列里面的消息显示在面板上
											if (selfClient.msgQueMap.size() > 0) {
												try {
													while ((message = selfClient.msgQueMap.get(user.getName()).poll()) != null) {
														StyledDocument doc = pane.historyTextPane.getStyledDocument();
														// 名称、日期
														SimpleAttributeSet nameSet = getAttributeSet(true, null);
														doc.insertString(doc.getLength(), StringHelper.createSenderInfo(message.getSenderName()), nameSet);
														SimpleAttributeSet contentSet = getAttributeSet(false, message);
														// 缩进
														StyleConstants.setLeftIndent(contentSet, 10);
														// 此处开始缩进
														doc.setParagraphAttributes(doc.getLength(), doc.getLength(), contentSet, true);
														// 正文
														// 文字或者图文混合
														if (!StringHelper.isEmpty(message.getContent())) {
															// 记录下这行消息插入的光标在哪里
															// 将光标放置到消息的最后
															pane.position = doc.getLength();
															doc.insertString(doc.getLength(), message.getContent(), contentSet);
															if (!StringHelper.isEmpty(message.getImageMark()) && message.getImageMark().split("/").length > 0) {
																for (String str : message.getImageMark().split("/")) {
																	int imgIndex = Integer.valueOf(str.substring(str.indexOf("|")+1));// 图片的位置（下标）
																	pane.historyTextPane.setCaretPosition(pane.position+imgIndex);// 光标
																	String mark = str.substring(str.indexOf(")")+1, str.indexOf("|"));
																	String fileName = "/com/hafele/resource/image/face/" + mark + ".gif";
																	pane.historyTextPane.insertIcon(new ImageIcon(Emoticon.class.getResource(fileName)));
																}
															}
														} else {// 文字为空，说明发送的全部是图片
															for (String str : message.getImageMark().split("/")) {
																// 此处要插入图片
																pane.historyTextPane.setCaretPosition(doc.getLength());// 光标
																String mark = str.substring(str.indexOf(")")+1, str.indexOf("|"));
																String fileName = "/com/haele/resource/image/face/" + mark + ".gif";
																pane.historyTextPane.insertIcon(new ImageIcon(Emoticon.class.getResource(fileName)));
															}
														}
														// 换行
														doc.insertString(doc.getLength(), "\n", contentSet);
														// 将缩进还原回来
														StyleConstants.setLeftIndent(contentSet, 0f);
														doc.setParagraphAttributes(doc.getLength(), doc.getLength(), contentSet, true);
													}
												} catch (BadLocationException e1) {
													e1.printStackTrace();
												}
											}
											// 将room信息返回
											selfClient.setRoom(room);
											selfClient.tabMap.put(user.getName(), pane);
											// 告知client，我已接受到相应好友消息
											selfClient.msgStatusMap.put(user.getName(), false);
//											// 告知client，下次来消息了继续闪烁
//											selfClient.threadMap.put(user.getName(), false);
										} else {
											room.setTitle(selfClient.getUser().getName() + " —— " + user.getName());
											room.titleLabel.setText(selfClient.getUser().getName() + " —— " + user.getName());
											int index = room.tabbedPane.indexOfTab(user.getName());
											room.tabbedPane.setSelectedIndex(index);
											ChatRoomPanel pane = (ChatRoomPanel) room.tabbedPane.getComponentAt(index);
											// 将队列里面的消息显示在面板上
											if (selfClient.msgQueMap.size() > 0) {
												try {
													while ((message = selfClient.msgQueMap.get(user.getName()).poll()) != null) {
														StyledDocument doc = pane.historyTextPane.getStyledDocument();
														// 名称、日期
														SimpleAttributeSet nameSet = getAttributeSet(true, null);
														doc.insertString(doc.getLength(), StringHelper.createSenderInfo(message.getSenderName()), nameSet);
														SimpleAttributeSet contentSet = getAttributeSet(false, message);
														// 缩进
														StyleConstants.setLeftIndent(contentSet, 10);
														// 此处开始缩进
														doc.setParagraphAttributes(doc.getLength(), doc.getLength(), contentSet, true);
														// 正文
														// 文字或者图文混合
														if (!StringHelper.isEmpty(message.getContent())) {
															// 记录下这行消息插入的光标在哪里
															// 将光标放置到消息的最后
															pane.position = doc.getLength();
															doc.insertString(doc.getLength(), message.getContent(), contentSet);
															if (!StringHelper.isEmpty(message.getImageMark()) && message.getImageMark().split("/").length > 0) {
																for (String str : message.getImageMark().split("/")) {
																	int imgIndex = Integer.valueOf(str.substring(str.indexOf("|")+1));// 图片的位置（下标）
																	pane.historyTextPane.setCaretPosition(pane.position+imgIndex);// 光标
																	String mark = str.substring(str.indexOf(")")+1, str.indexOf("|"));
																	String fileName = "/com/hafele/resource/image/face/" + mark + ".gif";
																	pane.historyTextPane.insertIcon(new ImageIcon(Emoticon.class.getResource(fileName)));
																}
															}
														} else {// 文字为空，说明发送的全部是图片
															for (String str : message.getImageMark().split("/")) {
																// 此处要插入图片
																pane.historyTextPane.setCaretPosition(doc.getLength());// 光标
																String mark = str.substring(str.indexOf(")")+1, str.indexOf("|"));
																String fileName = "/com/hafele/resource/image/face/" + mark + ".gif";
																pane.historyTextPane.insertIcon(new ImageIcon(Emoticon.class.getResource(fileName)));
															}
														}
														// 换行
														doc.insertString(doc.getLength(), "\n", contentSet);
														// 将缩进还原回来
														StyleConstants.setLeftIndent(contentSet, 0f);
														doc.setParagraphAttributes(doc.getLength(), doc.getLength(), contentSet, true);
													}
												} catch (BadLocationException e1) {
													e1.printStackTrace();
												}
											}
										}
									}
									model.reload(((ContactsNode)friend));
								}
							}
						}
					}
				}
			}
			
			//鼠标松开
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					TreePath path = jTree.getPathForLocation(e.getX(), e.getY());
					if (null != path) {
						// path中的node节点（path不为空，这里基本不会空）
						final Object object = path.getLastPathComponent();
						if (object instanceof CategoryNode) {
							JPopupMenu jpm = new JPopupMenu();
							jpm.setBackground(Constants.BACKGROUND_COLOR);
							jpm.setBorder(Constants.LIGHT_GRAY_BORDER);
							JMenuItem updateContactsList = new JMenuItem("刷新好友列表");
							updateContactsList.setOpaque(false);
							updateContactsList.setFont(Constants.BASIC_FONT);
							JMenuItem addContacts = new JMenuItem("添加好友");
							addContacts.setOpaque(false);
							addContacts.setFont(Constants.BASIC_FONT);
							JMenuItem addCategory = new JMenuItem("添加分组");
							addCategory.setOpaque(false);
							addCategory.setFont(Constants.BASIC_FONT);
							JMenuItem deleteCategory = new JMenuItem("删除分组");
							deleteCategory.setOpaque(false);
							deleteCategory.setFont(Constants.BASIC_FONT);
							JMenuItem rename = new JMenuItem("重命名");
							rename.setOpaque(false);
							rename.setFont(Constants.BASIC_FONT);
							//刷新好友列表
							updateContactsList.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									
								}
							});
							//添加好友
							addContacts.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									if(selfClient.getAddContactsWindow() == null) {
										AddContactsWindow inst = AddContactsWindow.getInstance(selfClient, ((CategoryNode)object).category.getId(), selfClient.getUser());
										selfClient.setAddContactsWindow(inst);
									} else {
										CustomOptionPanel.showMessageDialog(selfClient.getAddContactsWindow(), "窗体已经打开。", "提示");
										selfClient.getAddContactsWindow().requestFocus();
									}
								}
							});
							//添加分组
							addCategory.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									Message msg = new Message();
									msg.setType(Constants.ADD_USER_CATE_MSG);
									msg.setSenderId(selfClient.getUser().getLoginName());
									msg.setContent(Constants.NONAME_CATE);
									selfClient.sendMsg(msg);
								}
							});
							//删除分组
							deleteCategory.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									Category category = ((CategoryNode)object).category;
									if(category.getGroupName().equals(Constants.DEFAULT_CATE)) {
										CustomOptionPanel.showMessageDialog(selfClient.getMainWindow(), "默认分组不能删除", "提示");
										return;
									}
									int result = CustomOptionPanel.showConfirmDialog(selfClient.getMainWindow(), 
											"删除分组", "删除分组之后，分组下面的成员也会被删掉，也会将您从对方的好友列表里删除!", "okButton", "cancelButton");
									if(result == Constants.YES_OPTION) {
										Message message = new Message();
										message.setType(Constants.DELETE_USER_CATE_MSG);
										message.setSenderId(selfClient.getUser().getLoginName());
										message.setSenderName(selfClient.getUser().getName());
										message.setContent(category.getId() + Constants.LEFT_SLASH + category.getGroupName());
										selfClient.sendMsg(message);
									}else {
										return;
									}
								}
							});
							//重命名分组
							rename.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									CategoryNode categoryNode = (CategoryNode) object;
									if(categoryNode.category.getGroupName().equals(Constants.DEFAULT_CATE)) {
										CustomOptionPanel.showMessageDialog(selfClient.getMainWindow(), "默认分组不允许重命名", "提示");
										return;
									}
									jTree.setEditable(true);
									jTree.startEditingAtPath(new TreePath(categoryNode.getPath()));
								}
							});
							
							jpm.add(updateContactsList);
							jpm.add(addContacts);
							jpm.add(addCategory);
							jpm.add(deleteCategory);
							jpm.add(rename);
							jpm.show(jTree, e.getX(), e.getY());
						}
						if (object instanceof ContactsNode) {
							JPopupMenu jpm = new JPopupMenu();
							jpm.setBackground(Constants.BACKGROUND_COLOR);
							jpm.setBorder(Constants.LIGHT_GRAY_BORDER);
							JMenuItem mit1 = new JMenuItem("发送即时消息");
							mit1.setOpaque(false);
							mit1.setFont(Constants.BASIC_FONT);
							JMenuItem mit2 = new JMenuItem("发送电子邮件");
							mit2.setOpaque(false);
							mit2.setFont(Constants.BASIC_FONT);
							JMenuItem mit3 = new JMenuItem("查看资料");
							mit3.setOpaque(false);
							mit3.setFont(Constants.BASIC_FONT);
							JMenuItem mit4 = new JMenuItem("消息记录");
							mit4.setOpaque(false);
							mit4.setFont(Constants.BASIC_FONT);
							JMenu mit5 = new JMenu("设置权限");
							mit5.setOpaque(false);
							mit5.setFont(Constants.BASIC_FONT);
							JMenuItem mit6 = new JMenuItem("屏蔽此人消息");
							mit6.setOpaque(false);
							mit6.setFont(Constants.BASIC_FONT);
							JMenuItem mit7 = new JMenuItem("删除好友");
							mit7.setOpaque(false);
							mit7.setFont(Constants.BASIC_FONT);
							
							jpm.add(mit1);
							jpm.add(mit2);
							jpm.addSeparator();//增加分割线！！！
							jpm.add(mit3);
							jpm.add(mit4);
							jpm.add(mit5);
							mit5.add(mit6);
							jpm.add(mit7);
							
							jpm.show(jTree, e.getX(), e.getY());
						}	
					}
				}
			}
			
			//鼠标离开
			@Override
			public void mouseExited(MouseEvent e) {
				for (int i = 0; i < root.getChildCount(); i++) {
					Object category = root.getChildAt(i);
					((CategoryNode)category).categoryContent.setBackground(Constants.BACKGROUND_COLOR);
					model.reload(((CategoryNode)category));
					for (int j = 0; j < root.getChildAt(i).getChildCount(); j++) {
						Object contacts = root.getChildAt(i).getChildAt(j);
						((ContactsNode)contacts).userContent.setBackground(Constants.BACKGROUND_COLOR);
						model.reload(((ContactsNode)contacts));
					}
				}
			}
		});
	}
	
	//加载数据
	private void loadJTree() {
		for (Category category : selfClient.getCategoryList()) {
			CategoryNode categoryNode = new CategoryNode(PictureUtil.getPicture("arrow_left.png"), category);
			for (Map<String, List<User>> map : selfClient.getCategoryMemberList()) {
				List<User> list = map.get(category.getId());
				if (null != list && list.size() > 0) {
					for (User friend : list) {
						ContactsNode buddy = new ContactsNode(PictureUtil.getPicture(friend.getHeadPicture() + "_40px.png"), friend);
						categoryNode.add(buddy);
						// 更新client中好友节点的map，放到client中，为了方便统一调用
						selfClient.buddyNodeMap.put(friend.getName(), buddy);
					}
				}
			}
			root.add(categoryNode);
			// 更新client中好友分组的map，放到client中，为了方便统一调用
			selfClient.cateNodeMap.put(categoryNode.category.getId(), categoryNode);
		}
	}
	
	private SimpleAttributeSet getAttributeSet(boolean isDefault, Message message) {
		SimpleAttributeSet set = new SimpleAttributeSet();
		if (isDefault) {
			StyleConstants.setBold(set, false);
			StyleConstants.setItalic(set, false);
			StyleConstants.setFontSize(set, 15);
			StyleConstants.setFontFamily(set, "宋体");
			StyleConstants.setForeground(set, Color.RED);
		} else {
			// 字体名称
			StyleConstants.setFontFamily(set, message.getFamily());
			// 字号
			StyleConstants.setFontSize(set, message.getSize());
			// 样式
			int styleIndex = message.getStyle();
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
			int foreIndex = message.getFore();
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
			int backIndex = message.getBack();
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
		}
		return set;
	}
}
