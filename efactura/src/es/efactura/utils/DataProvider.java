package es.efactura.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import es.efactura.App;

public class DataProvider {

	public static Connection getConnection() {
		Connection conn = null;
		try {
			File f = new File(App.properties.getProperty("database"));
			conn = DriverManager.getConnection(
			        "jdbc:ucanaccess://" + f.getPath());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Map<String, Object>> getData(Connection conn, String query) throws SQLException {
		List<Map<String, Object>> data = new ArrayList<>();
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = conn.createStatement();
			 
			// Step 2.C: Executing SQL and 
			// retrieve data into ResultSet
			resultSet = statement.executeQuery(query);

			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			// processing returned data and printing into console
			while(resultSet.next()) {
				Map<String, Object> columns = new LinkedHashMap<String, Object>();

			    for (int i = 1; i <= columnCount; i++) {
			        columns.put(metaData.getColumnLabel(i), resultSet.getObject(i));
			    }
			    
			    data.add(columns);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}
		}
		return data;
	}
	
}
