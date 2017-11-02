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
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
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
import com.hafele.ui.common.CustomTreeUI;
import com.hafele.ui.frame.AddContactsWindow;
import com.hafele.util.Constants;
import com.hafele.util.PictureUtil;

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
						if (object instanceof ContactsNode) {
							
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
											"ɾ������", "ɾ������֮�󣬷�������ĳ�ԱҲ�ᱻɾ����Ҳ�Ὣ���ӶԷ��ĺ����б���ɾ��!", "ȷ��", "ȡ��");
									if(result == Constants.YES_OPTION) {
										Message message = new Message();
										message.setType(Constants.DELETE_USER_CATE_MSG);
										message.setSenderId(selfClient.getUser().getLoginName());
										message.setSenderName(selfClient.getUser().getName());
										message.setContent(category.getGroupName() + Constants.LEFT_SLASH + category.getGroupName());
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
							jpm.addSeparator();//���ӷָ��ߣ�����
							
							
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
}
