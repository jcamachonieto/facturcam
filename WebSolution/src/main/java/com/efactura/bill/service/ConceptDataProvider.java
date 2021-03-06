package com.efactura.bill.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.efactura.bill.model.ConceptEntity;
import com.efactura.utils.DataProvider;
import com.efactura.utils.FileUtils;

@Component
public class ConceptDataProvider {

	@Autowired
	DataProvider dataProvider;

	@Autowired
	FileUtils fileUtils;

	public List<ConceptEntity> getList(Integer idBill) {
		Connection conn = null;
		PreparedStatement statement = null;
		List<Map<String, Object>> data = null;
		try {
			conn = dataProvider.getConnection(fileUtils.getDatabaseFile());
			
			statement = conn.prepareStatement("SELECT * FROM Concept WHERE [id_Bill] = ?");
			statement.setInt(1, idBill);
			data = dataProvider.getData(conn, statement);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			dataProvider.closeConnection(conn);
		}
		return convertData(data);
	}
	
	

	public void insert(Integer idBill, List<ConceptEntity> dataList) {
		Connection conn = null;
		String query = null;
		try {
			conn = dataProvider.getConnection(fileUtils.getDatabaseFile());

			for (ConceptEntity data : dataList) {
				List<Object> params = new ArrayList<Object>();

				query = "INSERT INTO Concept ([id_bill], [description], [quantity], [tax_base]) VALUES (?, ?, ?, ?)";
				params.add(idBill);
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
				data.add(ConceptEntity.builder().id((int) d.get("Id")).idBill((Integer) d.get("id_bill"))
						.description((String) d.get("description")).quantity(BigDecimal.valueOf((Double) d.get("quantity")))
						.taxBase(BigDecimal.valueOf((Double) d.get("tax_base"))).build());
			}
		}
		return data;
	}
}
