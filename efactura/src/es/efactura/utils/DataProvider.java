package es.efactura.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataProvider {

	public static Connection getConnection() {
		Connection conn = null;
		try {
			File f = new File(System.getProperty("user.dir"));
			conn = DriverManager.getConnection(
			        "jdbc:ucanaccess://efactura.accdb");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
}
