package com.efactura.bill.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.efactura.bill.model.ConceptEntity;
import com.efactura.client.model.ClientEntity;
import com.efactura.utils.DataProvider;
import com.efactura.utils.FileUtils;

@Component
public class ConceptDataProvider {

	@Autowired
	DataProvider dataProvider;

	@Autowired
	FileUtils fileUtils;

	public List<ConceptEntity> getList() {
		Connection conn = null;
		List<Map<String, Object>> data = null;
		try {
			conn = dataProvider.getConnection(fileUtils.getDatabaseFile());
			data = dataProvider.getData(conn, "SELECT * FROM Concept");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataProvider.closeConnection(conn);
		}
		return convertData(data);
	}

	public void insert(List<ConceptEntity> dataList) {
		Connection conn = null;
		String query = null;
		try {
			conn = dataProvider.getConnection(fileUtils.getDatabaseFile());

			for (ConceptEntity data : dataList) {
				List<Object> params = new ArrayList<Object>();

				query = "INSERT INTO Concept ([id_bill], [description], [quantity], [tax_base]) VALUES (?, ?, ?, ?)";
				params.add(data.getIdBill());
				params.add(data.getDescription());
				params.add(data.getQuantity());
				params.add(data.getTaxBase());

				dataProvider.execute(conn, query, params);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataProvider.closeConnection(conn);
		}
	}

	public void deleteByBill(int idBill) {
		Connection conn = null;
		try {
			conn = dataProvider.getConnection(fileUtils.getDatabaseFile());

			String query = "DELETE FROM Concept WHERE [id_bill] = ?";

			List<Object> params = new ArrayList<Object>();
			params.add(idBill);

			dataProvider.execute(conn, query, params);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataProvider.closeConnection(conn);
		}
	}

	private List<ConceptEntity> convertData(List<Map<String, Object>> sourcedata) {
		List<ConceptEntity> data = new ArrayList<>();
		if (sourcedata != null) {
			for (Map<String, Object> d : sourcedata) {
				data.add(ConceptEntity.builder().id((int) d.get("Id")).idBill((Integer) d.get("idBill"))
						.description((String) d.get("description")).quantity((Integer) d.get("quantity"))
						.taxBase((Integer) d.get("taxBase")).build());
			}
		}
		return data;
	}

	public boolean exists(ClientEntity client) {
		Connection conn = null;
		List<Map<String, Object>> data = null;
		try {
			conn = dataProvider.getConnection(fileUtils.getDatabaseFile());
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM Concept WHERE [id] = ?");
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
