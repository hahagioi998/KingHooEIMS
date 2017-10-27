package com.hafele.dao;

import java.sql.ResultSet;
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

}
