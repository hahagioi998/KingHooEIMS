package com.hafele.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hafele.bean.CategoryMember;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月24日 上午11:53:12
* 分组成员数据库操作
*/
public class CategoryMemberDao extends BaseDao {

	/**
	 * getListByCategoryId: 根据分组Id查询下面的成员集合	<br/>
	 * @param cateId	分组Id
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
	 * getByCidAndMid: 根据分组Id和成员Id查询 <br/>
	 * @param categoryId	分组Id
	 * @param loginName	成员Id
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
	 * saveCategoryMember: 保存分组、成员关系	<br/>
	 * @param categoryId	分组Id
	 * @param ownerId	所属者Id
	 * @param memberId	成员Id
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
