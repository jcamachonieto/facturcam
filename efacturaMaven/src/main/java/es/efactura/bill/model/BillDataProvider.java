package es.efactura.bill.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.efactura.utils.data.DataProvider;

@Component
public class BillDataProvider {

	@Autowired
	DataProvider dataProvider;

	public List<BillDto> getList(String filter) {
		Connection conn = null;
		List<Map<String, Object>> data = null;
		try {
			conn = dataProvider.getConnection();
			data = dataProvider.getData(conn, "SELECT * FROM Bill");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dataProvider.closeConnection(conn);
		}
		return convertData(data, filter);
	}

	public void upsert(BillDto data) {
		Connection conn = null;
		String query = null;
		try {
			conn = dataProvider.getConnection();

			List<Object> params = new ArrayList<Object>();

			if (data.getId() == 0) {
				query = "INSERT INTO Bill ([name], [cif], [address], [location], [province], "
						+ "[postalCode], [country], [telephone], [email]) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			} else {
				query = "UPDATE Client SET [name] = ?, [address] = ?, [location] = ?,"
						+ " [province] = ?, [postalCode] = ?, [country] = ?, [telephone] = ?,"
						+ " [email] = ? WHERE [Id] = ?";
				
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
			conn = dataProvider.getConnection();

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

	private List<BillDto> convertData(List<Map<String, Object>> sourcedata, String filter) {
		List<BillDto> data = new ArrayList<>();
		if (sourcedata != null) {
			for (Map<String, Object> d : sourcedata) {
				boolean add = false;
				if (filter.isEmpty()) {
					add = true;
				} else {
					if ( ( d.get("name") != null && ((String) d.get("name")).contains(filter))
							|| ( d.get("cif") != null && ((String) d.get("cif")).contains(filter))
							|| ( d.get("address") != null && ((String) d.get("address")).contains(filter))
							|| ( d.get("location") != null && ((String) d.get("location")).contains(filter))
							|| ( d.get("province") != null && ((String) d.get("province")).contains(filter))
							|| ( d.get("postalCode") != null && ((String) d.get("postalCode")).contains(filter))
							|| ( d.get("country") != null && ((String) d.get("country")).contains(filter))
							|| ( d.get("telephone") != null && ((String) d.get("telephone")).contains(filter))
							|| ( d.get("email") != null && ((String) d.get("email")).contains(filter))) {
						add = true;
					}
				}
				if (add) {
					data.add(BillDto.builder()
							.id((int) d.get("Id"))
							.number((int) d.get("number"))
							.build());
				}
			}
		}
		return data;
	}
}
