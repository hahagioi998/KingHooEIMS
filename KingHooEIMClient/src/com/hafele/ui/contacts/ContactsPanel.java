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
* @version ����ʱ�䣺2017��10��22�� ����2:24:26
* ��ϵ��
*/
@SuppressWarnings("serial")
public class ContactsPanel extends JPanel {
	
	private JTree jTree;
	private DefaultTreeModel model;
	private DefaultMutableTreeNode root;
	/** �Լ���client */
	private Client selfClient;
	/** ����������� */
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
		selfClient.setBuddyRoot(root);// �ŵ�client�����࣬��ӷ����õ�
		selfClient.setBuddyModel(model);// �ŵ�client�����࣬ˢ�º���UI�õ�
		
		// ��������
		loadJTree();
		
		jTree = new JTree(model);
		jTree.setBackground(Constants.BACKGROUND_COLOR);
		jTree.setUI(new CustomTreeUI()); // �Զ���UI
		jTree.setCellRenderer(new ContactsNodeRenderer());// �Զ���ڵ���Ⱦ��
		jTree.setRootVisible(false);// ���ظ��ڵ�
		jTree.setToggleClickCount(1);// �������
		jTree.setInvokesStopCellEditing(true);// �޸Ľڵ�����֮����Ч
		selfClient.setBuddyTree(jTree); // �ŵ�client�����࣬���㴦��
		
		scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setViewportView(jTree);
		scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
		// ���κ��������
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		textField = new JTextField();
		textField.setPreferredSize(new Dimension(281, 23));
		textField.setBorder(Constants.LIGHT_GRAY_BORDER);
		jTree.setCellEditor(new DefaultCellEditor(textField));
		this.add(scrollPane, BorderLayout.CENTER);
		
		//textField���̼����¼�
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
							//�����������������
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
		
		//����˶�������
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
		
		//��������
		jTree.addMouseListener(new MouseAdapter() {
			//�����
			@Override
			public void mouseClicked(MouseEvent e) {
				// ����������
				if(e.getButton() == MouseEvent.BUTTON1) {
					TreePath path = jTree.getSelectionPath();
					if (null == path) {
						return;
					}
					// path�е�node�ڵ㣨path��Ϊ�գ������������գ�
					Object object = path.getLastPathComponent();
					if(e.getClickCount() == 1) {
						if (jTree.isEditable()) {
							if (!"".equals(textField.getText())) {
								Message msg = new Message();
								msg.setType(Constants.EDIT_USER_CATE_MSG);
								CategoryNode categoryNode = (CategoryNode) object;
								msg.setContent(categoryNode.category.getId() + Constants.LEFT_SLASH + textField.getText());
								//�����������������
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
						//����node�ڵ㣨����Ⱥ�飩
						if (object instanceof ContactsNode) {
							//�Ӹ��ڵ㿪ʼ��ȡ���нڵ㣨��һ��ȫ���Ƿ��飩
							for(int i = 0; i < root.getChildCount(); i++) {
								//��껬�����node�ڵ�ʱ����Ҫ�������������ɫ�ָ�
								Object category = root.getChildAt(i);
								((CategoryNode)category).categoryContent.setBackground(Constants.BACKGROUND_COLOR);
								model.reload((CategoryNode)category);//ˢ��UI
								//��һ��ȫ�Ǻ���node�ڵ�
								for(int j = 0; j < root.getChildAt(i).getChildCount(); j++) {
									Object friend = root.getChildAt(i).getChildAt(j);
									// �������ѡ�е��Ǹ�
									if(friend != (ContactsNode)object) {
										((ContactsNode)friend).userContent.setBackground(Constants.BACKGROUND_COLOR);
									} else {
										((ContactsNode)friend).userContent.setBackground(selectColor);
										//�������촰��
										Message message = null;
										User user = ((ContactsNode)object).contacts;
										ChatRoom room = selfClient.getRoom() == null ? 
												ChatRoom.getInstance(selfClient) : selfClient.getRoom();
										// ��Ӧ���ѵ�panelû��
										if (!selfClient.tabMap.containsKey(user.getName())) {
											room.setTitle(selfClient.getUser().getName() + " ���� " + user.getName());
											room.titleLabel.setText(selfClient.getUser().getName() + " ���� " + user.getName());
											ChatRoomPanel pane = new ChatRoomPanel(selfClient, selfClient.getUser(), user);
											room.tabbedPane.addTab(user.getName(), null, pane, user.getName());
											// �ػ����tabҳǩ
											room.tabbedPane.setTabComponentAt(room.tabbedPane.indexOfTab(user.getName()), 
													new CustomTabComponent(selfClient.getUser().getName(), user.getName(), room, selfClient));
											int index = room.tabbedPane.indexOfTab(user.getName());
											room.tabbedPane.setSelectedIndex(index);
											// �������������Ϣ��ʾ�������
											if (selfClient.msgQueMap.size() > 0) {
												try {
													while ((message = selfClient.msgQueMap.get(user.getName()).poll()) != null) {
														StyledDocument doc = pane.historyTextPane.getStyledDocument();
														// ���ơ�����
														SimpleAttributeSet nameSet = getAttributeSet(true, null);
														doc.insertString(doc.getLength(), StringHelper.createSenderInfo(message.getSenderName()), nameSet);
														SimpleAttributeSet contentSet = getAttributeSet(false, message);
														// ����
														StyleConstants.setLeftIndent(contentSet, 10);
														// �˴���ʼ����
														doc.setParagraphAttributes(doc.getLength(), doc.getLength(), contentSet, true);
														// ����
														// ���ֻ���ͼ�Ļ��
														if (!StringHelper.isEmpty(message.getContent())) {
															// ��¼��������Ϣ����Ĺ��������
															// �������õ���Ϣ�����
															pane.position = doc.getLength();
															doc.insertString(doc.getLength(), message.getContent(), contentSet);
															if (!StringHelper.isEmpty(message.getImageMark()) && message.getImageMark().split("/").length > 0) {
																for (String str : message.getImageMark().split("/")) {
																	int imgIndex = Integer.valueOf(str.substring(str.indexOf("|")+1));// ͼƬ��λ�ã��±꣩
																	pane.historyTextPane.setCaretPosition(pane.position+imgIndex);// ���
																	String mark = str.substring(str.indexOf(")")+1, str.indexOf("|"));
																	String fileName = "/com/hafele/resource/image/face/" + mark + ".gif";
																	pane.historyTextPane.insertIcon(new ImageIcon(Emoticon.class.getResource(fileName)));
																}
															}
														} else {// ����Ϊ�գ�˵�����͵�ȫ����ͼƬ
															for (String str : message.getImageMark().split("/")) {
																// �˴�Ҫ����ͼƬ
																pane.historyTextPane.setCaretPosition(doc.getLength());// ���
																String mark = str.substring(str.indexOf(")")+1, str.indexOf("|"));
																String fileName = "/com/haele/resource/image/face/" + mark + ".gif";
																pane.historyTextPane.insertIcon(new ImageIcon(Emoticon.class.getResource(fileName)));
															}
														}
														// ����
														doc.insertString(doc.getLength(), "\n", contentSet);
														// ��������ԭ����
														StyleConstants.setLeftIndent(contentSet, 0f);
														doc.setParagraphAttributes(doc.getLength(), doc.getLength(), contentSet, true);
													}
												} catch (BadLocationException e1) {
													e1.printStackTrace();
												}
											}
											// ��room��Ϣ����
											selfClient.setRoom(room);
											selfClient.tabMap.put(user.getName(), pane);
											// ��֪client�����ѽ��ܵ���Ӧ������Ϣ
											selfClient.msgStatusMap.put(user.getName(), false);
//											// ��֪client���´�����Ϣ�˼�����˸
//											selfClient.threadMap.put(user.getName(), false);
										} else {
											room.setTitle(selfClient.getUser().getName() + " ���� " + user.getName());
											room.titleLabel.setText(selfClient.getUser().getName() + " ���� " + user.getName());
											int index = room.tabbedPane.indexOfTab(user.getName());
											room.tabbedPane.setSelectedIndex(index);
											ChatRoomPanel pane = (ChatRoomPanel) room.tabbedPane.getComponentAt(index);
											// �������������Ϣ��ʾ�������
											if (selfClient.msgQueMap.size() > 0) {
												try {
													while ((message = selfClient.msgQueMap.get(user.getName()).poll()) != null) {
														StyledDocument doc = pane.historyTextPane.getStyledDocument();
														// ���ơ�����
														SimpleAttributeSet nameSet = getAttributeSet(true, null);
														doc.insertString(doc.getLength(), StringHelper.createSenderInfo(message.getSenderName()), nameSet);
														SimpleAttributeSet contentSet = getAttributeSet(false, message);
														// ����
														StyleConstants.setLeftIndent(contentSet, 10);
														// �˴���ʼ����
														doc.setParagraphAttributes(doc.getLength(), doc.getLength(), contentSet, true);
														// ����
														// ���ֻ���ͼ�Ļ��
														if (!StringHelper.isEmpty(message.getContent())) {
															// ��¼��������Ϣ����Ĺ��������
															// �������õ���Ϣ�����
															pane.position = doc.getLength();
															doc.insertString(doc.getLength(), message.getContent(), contentSet);
															if (!StringHelper.isEmpty(message.getImageMark()) && message.getImageMark().split("/").length > 0) {
																for (String str : message.getImageMark().split("/")) {
																	int imgIndex = Integer.valueOf(str.substring(str.indexOf("|")+1));// ͼƬ��λ�ã��±꣩
																	pane.historyTextPane.setCaretPosition(pane.position+imgIndex);// ���
																	String mark = str.substring(str.indexOf(")")+1, str.indexOf("|"));
																	String fileName = "/com/hafele/resource/image/face/" + mark + ".gif";
																	pane.historyTextPane.insertIcon(new ImageIcon(Emoticon.class.getResource(fileName)));
																}
															}
														} else {// ����Ϊ�գ�˵�����͵�ȫ����ͼƬ
															for (String str : message.getImageMark().split("/")) {
																// �˴�Ҫ����ͼƬ
																pane.historyTextPane.setCaretPosition(doc.getLength());// ���
																String mark = str.substring(str.indexOf(")")+1, str.indexOf("|"));
																String fileName = "/com/hafele/resource/image/face/" + mark + ".gif";
																pane.historyTextPane.insertIcon(new ImageIcon(Emoticon.class.getResource(fileName)));
															}
														}
														// ����
														doc.insertString(doc.getLength(), "\n", contentSet);
														// ��������ԭ����
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
			
			//����ɿ�
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					TreePath path = jTree.getPathForLocation(e.getX(), e.getY());
					if (null != path) {
						// path�е�node�ڵ㣨path��Ϊ�գ������������գ�
						final Object object = path.getLastPathComponent();
						if (object instanceof CategoryNode) {
							JPopupMenu jpm = new JPopupMenu();
							jpm.setBackground(Constants.BACKGROUND_COLOR);
							jpm.setBorder(Constants.LIGHT_GRAY_BORDER);
							JMenuItem updateContactsList = new JMenuItem("ˢ�º����б�");
							updateContactsList.setOpaque(false);
							updateContactsList.setFont(Constants.BASIC_FONT);
							JMenuItem addContacts = new JMenuItem("��Ӻ���");
							addContacts.setOpaque(false);
							addContacts.setFont(Constants.BASIC_FONT);
							JMenuItem addCategory = new JMenuItem("��ӷ���");
							addCategory.setOpaque(false);
							addCategory.setFont(Constants.BASIC_FONT);
							JMenuItem deleteCategory = new JMenuItem("ɾ������");
							deleteCategory.setOpaque(false);
							deleteCategory.setFont(Constants.BASIC_FONT);
							JMenuItem rename = new JMenuItem("������");
							rename.setOpaque(false);
							rename.setFont(Constants.BASIC_FONT);
							//ˢ�º����б�
							updateContactsList.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									
								}
							});
							//��Ӻ���
							addContacts.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									if(selfClient.getAddContactsWindow() == null) {
										AddContactsWindow inst = AddContactsWindow.getInstance(selfClient, ((CategoryNode)object).category.getId(), selfClient.getUser());
										selfClient.setAddContactsWindow(inst);
									} else {
										CustomOptionPanel.showMessageDialog(selfClient.getAddContactsWindow(), "�����Ѿ��򿪡�", "��ʾ");
										selfClient.getAddContactsWindow().requestFocus();
									}
								}
							});
							//��ӷ���
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
							//ɾ������
							deleteCategory.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									Category category = ((CategoryNode)object).category;
									if(category.getGroupName().equals(Constants.DEFAULT_CATE)) {
										CustomOptionPanel.showMessageDialog(selfClient.getMainWindow(), "Ĭ�Ϸ��鲻��ɾ��", "��ʾ");
										return;
									}
									int result = CustomOptionPanel.showConfirmDialog(selfClient.getMainWindow(), 
											"ɾ������", "ɾ������֮�󣬷�������ĳ�ԱҲ�ᱻɾ����Ҳ�Ὣ���ӶԷ��ĺ����б���ɾ��!", "okButton", "cancelButton");
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
							//����������
							rename.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									CategoryNode categoryNode = (CategoryNode) object;
									if(categoryNode.category.getGroupName().equals(Constants.DEFAULT_CATE)) {
										CustomOptionPanel.showMessageDialog(selfClient.getMainWindow(), "Ĭ�Ϸ��鲻����������", "��ʾ");
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
							JMenuItem mit1 = new JMenuItem("���ͼ�ʱ��Ϣ");
							mit1.setOpaque(false);
							mit1.setFont(Constants.BASIC_FONT);
							JMenuItem mit2 = new JMenuItem("���͵����ʼ�");
							mit2.setOpaque(false);
							mit2.setFont(Constants.BASIC_FONT);
							JMenuItem mit3 = new JMenuItem("�鿴����");
							mit3.setOpaque(false);
							mit3.setFont(Constants.BASIC_FONT);
							JMenuItem mit4 = new JMenuItem("��Ϣ��¼");
							mit4.setOpaque(false);
							mit4.setFont(Constants.BASIC_FONT);
							JMenu mit5 = new JMenu("����Ȩ��");
							mit5.setOpaque(false);
							mit5.setFont(Constants.BASIC_FONT);
							JMenuItem mit6 = new JMenuItem("���δ�����Ϣ");
							mit6.setOpaque(false);
							mit6.setFont(Constants.BASIC_FONT);
							JMenuItem mit7 = new JMenuItem("ɾ������");
							mit7.setOpaque(false);
							mit7.setFont(Constants.BASIC_FONT);
							
							jpm.add(mit1);
							jpm.add(mit2);
							jpm.addSeparator();//���ӷָ��ߣ�����
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
			
			//����뿪
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
	
	//��������
	private void loadJTree() {
		for (Category category : selfClient.getCategoryList()) {
			CategoryNode categoryNode = new CategoryNode(PictureUtil.getPicture("arrow_left.png"), category);
			for (Map<String, List<User>> map : selfClient.getCategoryMemberList()) {
				List<User> list = map.get(category.getId());
				if (null != list && list.size() > 0) {
					for (User friend : list) {
						ContactsNode buddy = new ContactsNode(PictureUtil.getPicture(friend.getHeadPicture() + "_40px.png"), friend);
						categoryNode.add(buddy);
						// ����client�к��ѽڵ��map���ŵ�client�У�Ϊ�˷���ͳһ����
						selfClient.buddyNodeMap.put(friend.getName(), buddy);
					}
				}
			}
			root.add(categoryNode);
			// ����client�к��ѷ����map���ŵ�client�У�Ϊ�˷���ͳһ����
			selfClient.cateNodeMap.put(categoryNode.category.getId(), categoryNode);
		}
	}
	
	private SimpleAttributeSet getAttributeSet(boolean isDefault, Message message) {
		SimpleAttributeSet set = new SimpleAttributeSet();
		if (isDefault) {
			StyleConstants.setBold(set, false);
			StyleConstants.setItalic(set, false);
			StyleConstants.setFontSize(set, 15);
			StyleConstants.setFontFamily(set, "����");
			StyleConstants.setForeground(set, Color.RED);
		} else {
			// ��������
			StyleConstants.setFontFamily(set, message.getFamily());
			// �ֺ�
			StyleConstants.setFontSize(set, message.getSize());
			// ��ʽ
			int styleIndex = message.getStyle();
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
			int foreIndex = message.getFore();
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
			int backIndex = message.getBack();
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
		}
		return set;
	}
}
