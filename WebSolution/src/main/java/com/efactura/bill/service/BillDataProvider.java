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
import com.efactura.bill.model.ConceptEntity;
import com.efactura.client.service.ClientDataProvider;
import com.efactura.utils.DataProvider;
import com.efactura.utils.FileUtils;

@Component
public class BillDataProvider {

	@Autowired
	DataProvider dataProvider;
	
	@Autowired
	ConceptDataProvider conceptDataProvider;
	
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

	public void upsert(BillEntity data, List<ConceptEntity> concepts) {
		Connection conn = null;
		String query = null;
		try {
			conn = dataProvider.getConnection(fileUtils.getDatabaseFile());

			List<Object> params = new ArrayList<Object>();
			Integer idBill = null;
			if (data.getId() == 0) {
				query = "INSERT INTO Bill ([id_client], [number], [broadcast_date], [expiration_date], [year], "
						+ "[tax]) VALUES (?, ?, ?, ?, ?, ?)";
				params.add(data.getIdClient());
				params.add(data.getNumber());
				params.add(data.getBroadCast());
				params.add(data.getExpiration());
				params.add(data.getYear());
				params.add(data.getTax());
				idBill = dataProvider.execute(conn, query, params);
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
				idBill = data.getId();
				dataProvider.execute(conn, query, params);
			}
			
			if (data.getId() != 0) {
				conceptDataProvider.deleteByBill(data.getId());
			}
			
			conceptDataProvider.insert(idBill, concepts);
			
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
	
	private BillEntity convertSimpleData(Map<String, Object> d) {
		return BillEntity.builder().id((int) d.get("Id")).idClient((Integer) d.get("id_client"))
				.number((Long) d.get("number")).broadCast((Date) d.get("broadcast_date"))
				.expiration((Date) d.get("expiration_date")).year((Integer) d.get("year"))
				.tax((Integer) d.get("tax")).clientName(clientDataProvider.load((Integer) d.get("id_client")).getName()).build();
	}

	private List<BillEntity> convertData(List<Map<String, Object>> sourcedata) {
		List<BillEntity> data = new ArrayList<>();
		if (sourcedata != null) {
			for (Map<String, Object> d : sourcedata) {
				data.add(convertSimpleData(d));
			}
		}
		return data;
	}

	public BillEntity load(int idbill) {
		Connection conn = null;
		List<Map<String, Object>> data = null;
		PreparedStatement statement = null;
		BillEntity retorno = null;
		try {
			conn = dataProvider.getConnection(fileUtils.getDatabaseFile());
			statement = conn.prepareStatement("SELECT * FROM Bill WHERE [id] = ?");
			statement.setInt(1, idbill);
			data = dataProvider.getData(conn, statement);
			if (data != null && data.size() > 0) {
				retorno = convertSimpleData(data.get(0));
			}
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
		return retorno;
	}
	
	public Long countByYear(int year) {
		Connection conn = null;
		Map<String, Object> data = null;
		PreparedStatement statement = null;
		try {
			conn = dataProvider.getConnection(fileUtils.getDatabaseFile());
			statement = conn.prepareStatement("SELECT COUNT(1) FROM Bill WHERE [year] = ?");
			statement.setInt(1, year);
			data = dataProvider.getSingleData(conn, statement);
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
		return (Long) data.get("C1"); 
	}
}
