package com.hafele.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hafele.bean.User;


/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version ����ʱ�䣺2017��10��19�� ����5:42:33
* �û������ݲ�����
*/
public class UserDao extends BaseDao {

	/**
	 * login: ��½   <br/>
	 * @param name	�˺�
	 * @param pass	����
	 * @return	User <br/>
	 * @since JDK 1.8
	 */
	public User login(String name, String pass) {
		String sql = "select * from Tab_Um_User U where U.cl_LoginName = '"+name+"' and U.cl_Password = '"+pass+"'";
		ResultSet result = select(sql);
		return assembleUser(result);
	}
	
	/**
	 * getByUserName: ͨ���˺Ų�ѯ 	<br/>
	 * @param value	�˺�
	 * @return User	<br/>
	 * @since JDK 1.8
	 */
	public User getByUserName(String value) {
		String sql = "select * from Tab_Um_User where cl_LoginName = '"+value+"'";
		ResultSet result;
		try {
			result = select(sql);
			return assembleUser(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/** ��װUser���� */
	private User assembleUser(ResultSet result) {
		// TODO Auto-generated method stub
		try {
			if (null != result && result.next()) {
				String loginName = result.getString("cl_LoginName");
				String userName = result.getString("cl_Name");
				String signature = result.getString("cl_Note");
				String headPicture = result.getString("cl_HeadPicture");
				System.out.println("��װUser����");
				return new User(loginName, userName, signature, headPicture);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * getById: ͨ��ID��ѯ	<br/>
	 * @param id	�û�ID�����ݿ��¼ID��
	 * @return User	<br/>
	 * @since JDK 1.8
	 */
	public User getById(String memberLoginName) {
		String sql = "select * from Tab_Um_User U where U.cl_LoginName = " + memberLoginName;
		ResultSet result = select(sql);
		return assembleUser(result);
	}

	/**
	 * getListByUIdAndName: �����û�	<br/>
	 * @param content	�û�ID���û����ƣ����ݿ��¼ID��
	 * @return User	<br/>
	 * @since JDK 1.8
	 */
	public User getListByUIdAndName(String content) {
		String sql = "select cl_LoginName, cl_Name, cl_Note, cl_HeadPicture from Tab_Um_User where cl_LoginName = '"+content+"' or cl_Name = '"+content+"'";
		ResultSet result = select(sql);
		return assembleUser(result);
	}
}
