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
				data.add(convertData(d));
			}
		}
		return data;
	}

	@SuppressWarnings("unused")
	private ClientEntity convertData(Map<String, Object> sourcedata) {
		if (sourcedata != null) {
				return ClientEntity.builder().id((int) sourcedata.get("Id")).cif((String) sourcedata.get("cif"))
						.name((String) sourcedata.get("name")).address((String) sourcedata.get("address"))
						.location((String) sourcedata.get("location")).province((String) sourcedata.get("province"))
						.postalCode((String) sourcedata.get("postalCode")).country((String) sourcedata.get("country"))
						.telephone((String) sourcedata.get("telephone")).email((String) sourcedata.get("email")).build();
		}
		return null;
	}

	public ClientEntity load(Integer id) {
		Connection conn = null;
		try {
			conn = dataProvider.getConnection(fileUtils.getDatabaseFile());
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM Client WHERE [id] = ?");
			statement.setInt(1, id);
			Map<String, Object> data = dataProvider.getSingleData(conn, statement);
			return convertData(data);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataProvider.closeConnection(conn);
		}
		return null;
	}
}
