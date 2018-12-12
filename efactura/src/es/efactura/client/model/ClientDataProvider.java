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
		Connection con = DataProvider.getConnection();
		return new ArrayList<ClientDto>();
	}
}
