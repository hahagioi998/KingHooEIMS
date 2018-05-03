package com.hafele.ui.common;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.hafele.socket.Client;
import com.hafele.ui.frame.ChatRoom;
import com.hafele.ui.frame.ChatRoomPanel;
import com.hafele.util.Constants;
import com.hafele.util.PictureUtil;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version ����ʱ�䣺2017��11��9�� ����4:24:41
* ��˵��
*/
public class CustomTabComponent extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JLabel text;
	private JLabel icon;
	private String selfName;
	private String friendName;
	private ChatRoom room;
	private Client client;
	
	public CustomTabComponent(String selfName, String friendName, ChatRoom room, Client client) {
		this.room = room;
		this.client = client;
		this.selfName = selfName;
		this.friendName = friendName;
		initGUI();
		initListener();
	}
	
	private void initGUI() {
		setOpaque(false);
		setLayout(new FlowLayout());
		text = new JLabel(friendName);
		text.setFont(Constants.BASIC_FONT);
		icon = new JLabel(PictureUtil.getPicture("empty.png"));
		add(text);
		add(icon);
	}

	private void initListener() {
		// TODO ��ѽ������ط������Լ��������Ȼ��ԭʼ������л������������
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				icon.setIcon(PictureUtil.getPicture("empty.png"));
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				icon.setIcon(PictureUtil.getPicture("close_tab.png"));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = room.tabbedPane.indexOfTab(friendName);
				room.tabbedPane.setSelectedIndex(index);
				
				room.setTitle(selfName + " ���� " + friendName);
				room.titleLabel.setText(selfName + " ���� " + friendName);
			}
		});
		icon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				icon.setIcon(PictureUtil.getPicture("empty.png"));
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				icon.setIcon(PictureUtil.getPicture("close_tab_active.png"));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = room.tabbedPane.indexOfTab(friendName);
				room.tabbedPane.remove(room.tabbedPane.getComponentAt(index));
				// TODO ��map��¼������Ƴ�
				client.tabMap.remove(friendName);
				// ���ر�����
				if (client.tabMap.size() < 1) {
					room.dispose();
					// TODO ��ռ�¼��������
					client.setRoom(null);
					client.tabMap.clear();
				} else {
					ChatRoomPanel pane = (ChatRoomPanel) room.tabbedPane.getSelectedComponent();
					room.setTitle(pane.self.getName() + " ���� " + pane.friend.getName());
					room.titleLabel.setText(pane.self.getName() + " ���� " + pane.friend.getName());
				}
			}
		});
	}
	
}

