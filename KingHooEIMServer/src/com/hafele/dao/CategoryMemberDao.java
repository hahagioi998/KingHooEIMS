package com.hafele.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hafele.bean.CategoryMember;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version ����ʱ�䣺2017��10��24�� ����11:53:12
* �����Ա���ݿ����
*/
public class CategoryMemberDao extends BaseDao {

	/**
	 * getListByCategoryId: ���ݷ���Id��ѯ����ĳ�Ա����	<br/>
	 * @param cateId	����Id
	 * @return List	<br/>
	 * @since JDK 1.8
	 */
	public List<CategoryMember> getListByCategoryId(String categoryId) {
		List<CategoryMember> list = new ArrayList<CategoryMember>();
		try {
			String sql = "select * from Tab_Um_CategoryMember CM where CM.cl_CategoryID = " + Integer.valueOf(categoryId);
			ResultSet result = select(sql);
			while (null != result && result.next()) {
				int id = result.getInt("cl_ID");
				String loginName = result.getString("cl_LoginName");
				int categoryID = result.getInt("cl_CategoryID");
				String menberLoginName = result.getString("cl_Menber_LoginName");
				CategoryMember cm = new CategoryMember(id, loginName, categoryID, menberLoginName);
				list.add(cm);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * getByCidAndMid: ���ݷ���Id�ͳ�ԱId��ѯ <br/>
	 * @param categoryId	����Id
	 * @param loginName	��ԱId
	 * @return CategoryMember	<br/>
	 * @since JDK 1.8
	 */
	public CategoryMember getByCidAndMid(String categoryId, String menberName) {
		try {
			String sql = "select * from Tab_Um_CategoryMember CM where CM.cl_CategoryID = '" + Integer.valueOf(categoryId) +"' and CM.cl_Menber_LoginName = '"+menberName+"'";
			ResultSet result = select(sql);
			if(result != null && result.next()) {
				int id = result.getInt("cl_ID");
				String loginName = result.getString("cl_LoginName");
				int categoryID = result.getInt("cl_CategoryID");
				String menberLoginName = result.getString("cl_Menber_LoginName");
				return new CategoryMember(id, loginName, categoryID, menberLoginName);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * saveCategoryMember: ������顢��Ա��ϵ	<br/>
	 * @param categoryId	����Id
	 * @param ownerId	������Id
	 * @param memberId	��ԱId
	 * @return CategoryMember	<br/>
	 * @since JDK 1.8
	 */
	public CategoryMember saveCategoryMember(String ownerId, String categoryId, String memberId) {
		CategoryMember categoryMember = getByCidAndMid(categoryId, memberId);
		if(categoryMember != null) {
			return categoryMember;
		}
		String sql = "insert into Tab_Um_CategoryMember values ( " + ownerId + "," + Integer.valueOf(categoryId) + "," + memberId + ")";
		int num = operate(sql);
		if(num > 0) {
			return getByCidAndMid(categoryId, memberId);
		}
		return null;
	}

}
