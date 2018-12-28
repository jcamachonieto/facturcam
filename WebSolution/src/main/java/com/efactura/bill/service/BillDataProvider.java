package com.efactura.bill.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.efactura.bill.model.BillEntity;
import com.efactura.client.service.ClientDataProvider;
import com.efactura.utils.DataProvider;
import com.efactura.utils.FileUtils;

@Component
public class BillDataProvider {

	@Autowired
	DataProvider dataProvider;
	
	@Autowired
	ClientDataProvider clientDataProvider;
	
	@Autowired
	FileUtils fileUtils;

	public List<BillEntity> getList() {
		Connection conn = null;
		List<Map<String, Object>> data = null;
		try {
			conn = dataProvider.getConnection(fileUtils.getDatabaseFile());
			data = dataProvider.getData(conn, "SELECT * FROM Bill");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataProvider.closeConnection(conn);
		}
		return convertData(data);
	}

	public void upsert(BillEntity data) {
		Connection conn = null;
		String query = null;
		try {
			conn = dataProvider.getConnection(fileUtils.getDatabaseFile());

			List<Object> params = new ArrayList<Object>();

			if (data.getId() == 0) {
				query = "INSERT INTO Bill ([id_client], [number], [broadcast_date], [expiration_date], [year], "
						+ "[tax]) VALUES (?, ?, ?, ?, ?, ?)";
				params.add(data.getIdClient());
				params.add(data.getNumber());
				params.add(data.getBroadCast());
				params.add(data.getExpiration());
				params.add(data.getYear());
				params.add(data.getTax());
			} else {
				query = "UPDATE Bill SET [id_client] = ?, [number] = ?, [broadcast_date] = ?,"
						+ " [expiration_date] = ?, [year] = ?, [tax] = ? WHERE [Id] = ?";
				params.add(data.getIdClient());
				params.add(data.getNumber());
				params.add(data.getBroadCast());
				params.add(data.getExpiration());
				params.add(data.getYear());
				params.add(data.getTax());
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

			String query = "DELETE FROM Bill WHERE [Id] = ?";

			List<Object> params = new ArrayList<Object>();
			params.add(id);

			dataProvider.execute(conn, query, params);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataProvider.closeConnection(conn);
		}
	}

	private List<BillEntity> convertData(List<Map<String, Object>> sourcedata) {
		List<BillEntity> data = new ArrayList<>();
		if (sourcedata != null) {
			for (Map<String, Object> d : sourcedata) {
				data.add(BillEntity.builder().id((int) d.get("Id")).idClient((Integer) d.get("idClient"))
						.number((Long) d.get("number")).broadCast((Date) d.get("broadCast"))
						.expiration((Date) d.get("expiration")).year((Integer) d.get("year"))
						.tax((Integer) d.get("tax")).build());
			}
		}
		return data;
	}

	public boolean exists(BillEntity bill) {
		Connection conn = null;
		List<Map<String, Object>> data = null;
		try {
			conn = dataProvider.getConnection(fileUtils.getDatabaseFile());
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM Bill WHERE [id] = ?");
			statement.setInt(1, bill.getId());
			data = dataProvider.getData(conn, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataProvider.closeConnection(conn);
		}
		return data != null && data.size() > 0;
	}
	
	public Long countByYear(int year) {
		Connection conn = null;
		Map<String, Object> data = null;
		try {
			conn = dataProvider.getConnection(fileUtils.getDatabaseFile());
			PreparedStatement statement = conn.prepareStatement("SELECT COUNT(1) FROM Bill WHERE [year] = ?");
			statement.setInt(1, year);
			data = dataProvider.getSingleData(conn, statement);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataProvider.closeConnection(conn);
		}
		return (Long) data.get("C1"); 
	}
}
