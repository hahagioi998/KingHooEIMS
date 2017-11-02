package com.hafele.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hafele.bean.User;


/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月19日 下午5:42:33
* 用户表数据操作类
*/
public class UserDao extends BaseDao {

	/**
	 * login: 登陆   <br/>
	 * @param name	账号
	 * @param pass	密码
	 * @return	User <br/>
	 * @since JDK 1.8
	 */
	public User login(String name, String pass) {
		String sql = "select * from Tab_Um_User U where U.cl_LoginName = '"+name+"' and U.cl_Password = '"+pass+"'";
		ResultSet result = select(sql);
		return assembleUser(result);
	}
	
	/**
	 * getByUserName: 通过账号查询 	<br/>
	 * @param value	账号
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

	/** 组装User对象 */
	private User assembleUser(ResultSet result) {
		// TODO Auto-generated method stub
		try {
			if (null != result && result.next()) {
				String loginName = result.getString("cl_LoginName");
				String userName = result.getString("cl_Name");
				String signature = result.getString("cl_Note");
				String headPicture = result.getString("cl_HeadPicture");
				System.out.println("组装User对象");
				return new User(loginName, userName, signature, headPicture);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * getById: 通过ID查询	<br/>
	 * @param id	用户ID（数据库记录ID）
	 * @return User	<br/>
	 * @since JDK 1.8
	 */
	public User getById(String memberLoginName) {
		String sql = "select * from Tab_Um_User U where U.cl_LoginName = " + memberLoginName;
		ResultSet result = select(sql);
		return assembleUser(result);
	}

	/**
	 * getListByUIdAndName: 搜索用户	<br/>
	 * @param content	用户ID或用户名称（数据库记录ID）
	 * @return User	<br/>
	 * @since JDK 1.8
	 */
	public User getListByUIdAndName(String content) {
		String sql = "select cl_LoginName, cl_Name, cl_Note, cl_HeadPicture from Tab_Um_User where cl_LoginName = '"+content+"' or cl_Name = '"+content+"'";
		ResultSet result = select(sql);
		return assembleUser(result);
	}
}
