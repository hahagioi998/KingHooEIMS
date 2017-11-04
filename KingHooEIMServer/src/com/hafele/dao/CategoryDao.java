package com.hafele.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hafele.bean.Category;
import com.hafele.util.StringHelper;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version ����ʱ�䣺2017��10��24�� ����9:34:24
* ��ϵ�˷���Dao
*/
public class CategoryDao extends BaseDao {

	/**
	 * ͨ��Id��ѯ
	 * @param id ����Id
	 * @return
	 */
	public Category getById(String id) {
		try {
			String sql = "select * from Tab_Um_Category where cl_ID = " + Integer.valueOf(id);
			ResultSet result = select(sql);
			if (null != result && result.next()) {
				return assembleCategory(result);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * getListByIdAndType: ����������ID��type����ȡlist	<br/>
	 * @param loginName  ������ID
	 * @param type     ���ͣ����ѡ�Ⱥ�飩
	 * @return List	<br/>
	 * @since JDK 1.8
	 * */
	public List<Category> getListByUIdAndType(String loginName, String type) {
		List<Category> list = new ArrayList<Category>();
		ResultSet result;
		try {
			String sql = "select * from Tab_Um_Category C where C.cl_LoginName = "+ loginName;
			if (!StringHelper.isEmpty(type)) {
				sql += " and C.cl_Type = '"+type+"' ";
			}
			result = select(sql);
			while (result != null && result.next()) {
				Category category = assembleCategory(result);
				list.add(category);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * saveCategory: ��������	<br/>
	 * @param senderId ������Id
	 * @param type ��������
	 * @param categoryName ��������
	 * @return	<br/>
	 * @since JDK 1.8
	 * */
	public Category saveCategory(String senderId, String type, String categoryName) {
		String sql = "insert into Tab_Um_Category values('"+categoryName+"',"+senderId+",'"+type+"') ";
		int num = operate(sql);
		if (num > 0) {
			return getRecentCate(senderId, type);
		}
		return null;
	}

	/**
	 * ��ȡ���û����´����ķ��飨��������ʱ���õ���
	 * @param senderId ������Id
	 * @param type ��������
	 * @return Category
	 */
	private Category getRecentCate(String senderId, String type) {
		try {
			String sql = "select max(C.cl_ID) from Tab_Um_Category C "
					+ "where C.cl_LoginName = "+senderId+" and C.cl_Type = '"+type+"' ";
			ResultSet result = select(sql);
			if (null != result && result.next()) {
				String maxId = String.valueOf(result.getInt(1));
				return getById(maxId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ��������������
	 * @param id ���� ����ID
	 * @param name ��������
	 * @return
	 */
	public Category editCategory(String id, String name) {
		String sql = "update Tab_Um_Category set cl_Name = '"+name+"' where cl_ID = " + Integer.valueOf(id);
		int num = operate(sql);
		if (num > 0) {
			return getById(id);
		}
		return null;
	}

	//��װ����
	private Category assembleCategory(ResultSet result) {
		try {
			String id = String.valueOf(result.getInt("cl_ID"));
			String cateName = result.getString("cl_Name");
			String cateLoginName  = result.getString("cl_Loginname");
			String cateType = result.getString("cl_Type");
			return new Category(id, cateName, cateLoginName, cateType);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * getByCondition: ͨ��������Id���������͡�������������ѯ	<br/>
	 * @param ownerId ������Id
	 * @param type ��������
	 * @param name ��������
	 * @return Category	<br/>
	 * @since JDK 1.8
	 */
	public Category getByCondition(String ownerId, String type, String name) {
		String sql = "select * from Tab_Um_Category C where C.cl_LoginName = '" + ownerId +"' and C.cl_Type = '" + type +"' and C.cl_Name = '" + name +"'";
		try {
			ResultSet result = select(sql);
			if (null != result && result.next()) {
				return assembleCategory(result);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ɾ������
	 * @param id ����Id
	 */
	public void deleteByOidAndMid(String id) {
		String sql = "delete from Tab_Um_Category where cl_ID = " + Integer.valueOf(id);
		operate(sql);
	}

	
}
