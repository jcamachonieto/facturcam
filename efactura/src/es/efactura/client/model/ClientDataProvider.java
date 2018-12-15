package es.efactura.client.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import es.efactura.utils.DataProvider;

public class ClientDataProvider {

	Connection con;

	public ClientDataProvider() {
		
	}
	
	public List<ClientDto> getList() {
		Connection conn = null;
		List<Map<String, Object>> data = null;
		try {
			conn = DataProvider.getConnection();
			data = DataProvider.getData(conn, "SELECT * FROM Client");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataProvider.closeConnection(conn);
		}
		return convertData(data);
	}
	
	public void upsert(ClientDto data) {
		Connection conn = null;
		String query = null;
		try {
			conn = DataProvider.getConnection();
			
			List<Object> params = new ArrayList<Object>();
			
			if (data.getId() == 0) {
				query = "INSERT INTO Client ([name], [cif]) VALUES (?, ?)";
				params.add(data.getName());
				params.add(data.getCif());
			} else {
				query = "UPDATE Client SET [name] = ?, [CIF] = ?, [address] = ? WHERE [Id] = ?";
				params.add(data.getName());
				params.add(data.getCif());
				params.add(data.getAddress());
				params.add(data.getId());
			}
			
			DataProvider.execute(conn, query, params);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataProvider.closeConnection(conn);
		}
	}
	
	public void delete(int id) {
		Connection conn = null;
		try {
			conn = DataProvider.getConnection();
			
			String query = "DELETE FROM Client WHERE [Id] = ?";
			
			List<Object> params = new ArrayList<Object>();
			params.add(id);
			
			DataProvider.execute(conn, query, params);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataProvider.closeConnection(conn);
		}
	}
	
	private List<ClientDto> convertData(List<Map<String, Object>> sourcedata) {
		List<ClientDto> data = new ArrayList<>();
		if (sourcedata != null) {
			for (Map<String, Object> d : sourcedata) {
				data.add(ClientDto.builder()
						.id((int) d.get("Id"))
						.cif((String) d.get("cif"))
						.name((String) d.get("name"))
						.address((String) d.get("address"))
						.build());
			}
		}
		return data;
	}
}
