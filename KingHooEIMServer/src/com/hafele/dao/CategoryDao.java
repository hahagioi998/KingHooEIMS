package com.hafele.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hafele.bean.Category;
import com.hafele.util.StringHelper;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月24日 上午9:34:24
* 联系人分组Dao
*/
public class CategoryDao extends BaseDao {

	/**
	 * 通过Id查询
	 * @param id 分组Id
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
	 * getListByIdAndType: 根据所属者ID和type来获取list	<br/>
	 * @param loginName  所属者ID
	 * @param type     类型（好友、群组）
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
	 * saveCategory: 新增分组	<br/>
	 * @param senderId 所属者Id
	 * @param type 分组类型
	 * @param categoryName 分组名称
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
	 * 获取该用户最新创建的分组（刚新增的时候用到）
	 * @param senderId 所属者Id
	 * @param type 分组类型
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
	 * 重命名分组名称
	 * @param id 主键 分组ID
	 * @param name 分组名称
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

	//组装数据
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
	 * getByCondition: 通过所属者Id、分组类型、分组名称来查询	<br/>
	 * @param ownerId 所属者Id
	 * @param type 分组类型
	 * @param name 分组名称
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
	 * 删除分组
	 * @param id 分组Id
	 */
	public void deleteByOidAndMid(String id) {
		String sql = "delete from Tab_Um_Category where cl_ID = " + Integer.valueOf(id);
		operate(sql);
	}

	
}
