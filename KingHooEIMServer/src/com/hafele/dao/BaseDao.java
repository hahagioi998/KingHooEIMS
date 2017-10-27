package com.hafele.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.hafele.db.DataConnect;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version ����ʱ�䣺2017��10��19�� ����11:55:28
* ���ݿ��������
*/
public class BaseDao {
	private static Connection connect;
	private static Statement statement;
	private static ResultSet resultSet;

	/** ��ѯ */
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

	/** �������������޸ġ�ɾ���� */
	public int operate(String sql) {
		int number = 0;
		try {
			System.out.println(sql);
			connect = DataConnect.getConnect();
			statement = connect.createStatement();
			number = statement.executeUpdate(sql);
			// ��������Ϊ�ֶ�������ع�
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

	/** �ر����� */
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
