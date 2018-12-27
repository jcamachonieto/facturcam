package com.efactura.client.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.efactura.client.model.ClientEntity;
import com.efactura.utils.DataProvider;
import com.efactura.utils.FileUtils;

@Component
public class ClientDataProvider {

	@Autowired
	DataProvider dataProvider;
	
	@Autowired
	FileUtils fileUtils;

	public List<ClientEntity> getList() {
		Connection conn = null;
		List<Map<String, Object>> data = null;
		try {
			conn = dataProvider.getConnection(fileUtils.getDatabaseFile());
			data = dataProvider.getData(conn, "SELECT * FROM Client");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataProvider.closeConnection(conn);
		}
		return convertData(data);
	}

	public void upsert(ClientEntity data) {
		Connection conn = null;
		String query = null;
		try {
			conn = dataProvider.getConnection(fileUtils.getDatabaseFile());

			List<Object> params = new ArrayList<Object>();

			if (data.getId() == 0) {
				query = "INSERT INTO Client ([name], [cif], [address], [location], [province], "
						+ "[postalCode], [country], [telephone], [email]) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
				params.add(data.getName());
				params.add(data.getCif());
				params.add(data.getAddress());
				params.add(data.getLocation());
				params.add(data.getProvince());
				params.add(data.getPostalCode());
				params.add(data.getCountry());
				params.add(data.getTelephone());
				params.add(data.getEmail());
			} else {
				query = "UPDATE Client SET [name] = ?, [address] = ?, [location] = ?,"
						+ " [province] = ?, [postalCode] = ?, [country] = ?, [telephone] = ?,"
						+ " [email] = ? WHERE [Id] = ?";
				params.add(data.getName());
				params.add(data.getAddress());
				params.add(data.getLocation());
				params.add(data.getProvince());
				params.add(data.getPostalCode());
				params.add(data.getCountry());
				params.add(data.getTelephone());
				params.add(data.getEmail());
				params.add(data.getId());
			}

			dataProvider.execute(conn, query, params);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataProvider.closeConnection(conn);
		}
	}

	public void delete(int id) {
		Connection conn = null;
		try {
			conn = dataProvider.getConnection(fileUtils.getDatabaseFile());

			String query = "DELETE FROM Client WHERE [Id] = ?";

			List<Object> params = new ArrayList<Object>();
			params.add(id);

			dataProvider.execute(conn, query, params);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataProvider.closeConnection(conn);
		}
	}

	private List<ClientEntity> convertData(List<Map<String, Object>> sourcedata) {
		List<ClientEntity> data = new ArrayList<>();
		if (sourcedata != null) {
			for (Map<String, Object> d : sourcedata) {
				data.add(ClientEntity.builder().id((int) d.get("Id")).cif((String) d.get("cif"))
						.name((String) d.get("name")).address((String) d.get("address"))
						.location((String) d.get("location")).province((String) d.get("province"))
						.postalCode((String) d.get("postalCode")).country((String) d.get("country"))
						.telephone((String) d.get("telephone")).email((String) d.get("email")).build());
			}
		}
		return data;
	}

	public boolean exists(ClientEntity client) {
		Connection conn = null;
		List<Map<String, Object>> data = null;
		try {
			conn = dataProvider.getConnection(fileUtils.getDatabaseFile());
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM Client WHERE [cif] = ?");
			statement.setString(1, client.getCif());
			data = dataProvider.getData(conn, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataProvider.closeConnection(conn);
		}
		return data != null && data.size() > 0;
	}
}
