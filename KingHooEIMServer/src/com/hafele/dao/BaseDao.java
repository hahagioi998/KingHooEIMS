package com.hafele.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.hafele.db.DataConnect;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年10月19日 下午11:55:28
* 数据库基本操作
*/
public class BaseDao {
	private static Connection connect;
	private static Statement statement;
	private static ResultSet resultSet;

	/** 查询 */
	public static ResultSet select(String sql) {
		try {
			System.out.println(sql);
			connect = DataConnect.getConnect();
			statement = connect.createStatement();
			resultSet = statement.executeQuery(sql);
			return resultSet;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			closeConnect(connect, statement, resultSet);
		}
		return null;
	}

	/** 操作（新增、修改、删除） */
	public int operate(String sql) {
		int number = 0;
		try {
			System.out.println(sql);
			connect = DataConnect.getConnect();
			statement = connect.createStatement();
			number = statement.executeUpdate(sql);
			// 设置事务为手动，方便回滚
			connect.setAutoCommit(false);
			connect.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				connect.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
//			closeConnect(connect, statement, resultSet);
		}
		return number;
	}

	/** 关闭连接 */
	public void closeConnect(Connection connect, Statement statement,
			ResultSet resultSet) {
		try {
			if (null != resultSet) {
				resultSet.close();
			}
			if (null != statement) {
				statement.close();
			}
			if (null != connect) {
				connect.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
