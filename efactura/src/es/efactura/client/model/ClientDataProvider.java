package es.efactura.client.model;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
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
	
	private List<ClientDto> convertData(List<Map<String, Object>> sourcedata) {
		List<ClientDto> data = new ArrayList<>();
		if (sourcedata != null) {
			for (Map<String, Object> d : sourcedata) {
				data.add(ClientDto.builder()
						.id((int) d.get("Id"))
						.cif((String) d.get("cif"))
						.name((String) d.get("name"))
						.build());
			}
		}
		return data;
	}
}
