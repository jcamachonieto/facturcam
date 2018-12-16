package es.efactura.license.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.efactura.utils.EfacturaProperties;

@Component
public class LicenseDataProvider {

	@Autowired
	EfacturaProperties efacturaProperties;

	public Connection getConnection() {
		Connection conn = null;
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			conn = DriverManager.getConnection("jdbc:mysql://" + efacturaProperties.getLicenseDatabaseHost() + "/"
					+ efacturaProperties.getLicenseDatabaseName() + "?" + "user="
					+ efacturaProperties.getLicenseDatabaseUsername() + "&password=LKFnSXknrF");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Map<String, Object> getData(Connection conn, PreparedStatement statement) throws SQLException {
		ResultSet resultSet = null;
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		try {
			// Step 2.C: Executing SQL and
			// retrieve data into ResultSet
			resultSet = statement.executeQuery();

			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();

			// processing returned data and printing into console
			if (resultSet.next()) {
				for (int i = 1; i <= columnCount; i++) {
					data.put(metaData.getColumnLabel(i), resultSet.getObject(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}
		}
		return data;
	}

	public boolean isValidLicense() {
		Connection conn = null;
		Map<String, Object> sourcedata = null;
		boolean valid = false;
		try {
			File file = new File(efacturaProperties.getLicenseKeyFile());
			if (file.exists()) {
				FileInputStream fileIn = new FileInputStream(file);
				Properties prop = new Properties();
				prop.load(fileIn);
				LicenseDto license = LicenseDto.builder().email(prop.getProperty("email"))
						.key(prop.getProperty("key") != null ? UUID.fromString(prop.getProperty("key")) : UUID.randomUUID()).build();
				if (license.getEmail() != null && license.getKey() != null) {
					conn = getConnection();
					PreparedStatement statement = conn.prepareStatement("SELECT * FROM licenses WHERE email = ?");
					statement.setString(1, license.getEmail());
					sourcedata = getData(conn, statement);

					LicenseDto data = convertData(sourcedata);

					if (data != null) {
						DefaultArtifactVersion actualVersion = new DefaultArtifactVersion(
								efacturaProperties.getVersion());
						DefaultArtifactVersion licenseVersion = new DefaultArtifactVersion(data.getMaxVersion());
						if (license.getKey().compareTo(data.getKey()) == 0 && data.getExpirationDate().compareTo(new Date()) >= 0
								&& licenseVersion.compareTo(actualVersion) >= 0) {
							valid = true;
						}
					}
				}
			}
		} catch (SQLException | ParseException | IOException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
		}
		return valid;
	}

	private LicenseDto convertData(Map<String, Object> sourcedata) throws ParseException {
		LicenseDto data = null;
		if (sourcedata != null) {
			data = LicenseDto.builder().build();
			data = (LicenseDto.builder().email((String) sourcedata.get("email"))
					.key(UUID.fromString((String) sourcedata.get("key")))
					.maxVersion((String) sourcedata.get("max_version"))
					.expirationDate(new Date(((java.sql.Date) sourcedata.get("expiration_date")).getTime())).build());
		}
		return data;
	}
}
