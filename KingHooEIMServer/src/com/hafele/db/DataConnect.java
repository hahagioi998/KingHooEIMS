package com.hafele.db;

import java.sql.Connection;
import java.sql.DriverManager;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version ����ʱ�䣺2017��10��19�� ����11:57:39
* ���ݿ�������
*/
public class DataConnect {
	private static Connection conn;

	private DataConnect() {

	}

	public static Connection getConnect() {
		try {
			if (null == conn) {
//				String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//				String url = "jdbc:sqlserver://127.0.0.1:1433;databaseName=db_KingHooEIM";
				String driver = "com.mysql.jdbc.Driver";
				String url = "jdbc:mysql://localhost:3306/db_kinghooeim";
				Class.forName(driver);
				conn = DriverManager.getConnection(url, "root", "hafele");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
