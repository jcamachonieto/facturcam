package com.efactura.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DataProvider {

	@Value("${local.database.driverManager}")
	private String driverManager;

	public Connection getConnection(String databaseFile) {
		Connection conn = null;
		try {
			File f = new File(databaseFile);
			conn = DriverManager.getConnection(driverManager + f.getAbsolutePath());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void execute(Connection conn, String query, List<Object> params) throws SQLException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(query);

			if (params != null) {
				for (int i = 1; i <= params.size(); i++) {
					st.setObject(i, params.get(i - 1));
				}
			}

			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (st != null) {
				st.close();
			}
		}
	}

	public List<Map<String, Object>> getData(Connection conn, String query) throws SQLException {
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
			while (resultSet.next()) {
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
	
	public List<Map<String, Object>> getData(Connection conn, PreparedStatement statement) throws SQLException {
		List<Map<String, Object>> data = new ArrayList<>();
		ResultSet resultSet = null;
		try {
			// Step 2.C: Executing SQL and
			// retrieve data into ResultSet
			resultSet = statement.executeQuery();

			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();

			// processing returned data and printing into console
			while (resultSet.next()) {
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
