package org.openmrs.module.commonreports.reports;

import java.io.IOException;
import java.sql.Connection;
import java.util.stream.Stream;

import org.dbunit.DatabaseUnitException;
import org.dbunit.DatabaseUnitRuntimeException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.dialect.MySQLDialect;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

public abstract class BaseModuleContextSensitiveMysqlBackedTest extends BaseModuleContextSensitiveTest {
	
	private static MySQLContainer mysqlContainer = new MySQLContainer("mysql:5.6.51");
	
	private static String databaseUrl = "jdbc:mysql://localhost:DATABASE_PORT/openmrs?autoReconnect=true&sessionVariables=default_storage_engine%3DInnoDB&useUnicode=true&characterEncoding=UTF-8";
	
	private static String databaseUsername = "user";
	
	private static String databaseUserPasswword = "password";
	
	private static String databaseDialect = MySQLDialect.class.getName();
	
	private static String databaseDriver = "com.mysql.jdbc.Driver";
	
	@Override
	public void executeDataSet(IDataSet dataset) {
		try {
			Connection connection = getConnection();
			IDatabaseConnection dbUnitConn = setupDatabaseConnection(connection);
			DatabaseOperation.REFRESH.execute(dbUnitConn, dataset);
		}
		catch (Exception e) {
			throw new DatabaseUnitRuntimeException(e);
		}
	}
	
	private IDatabaseConnection setupDatabaseConnection(Connection connection) throws DatabaseUnitException {
		IDatabaseConnection dbUnitConn = new DatabaseConnection(connection);
		
		DatabaseConfig config = dbUnitConn.getConfig();
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());
		
		return dbUnitConn;
	}
	
	@BeforeClass
	public static void setupMySqlDb() throws IOException {
		
		mysqlContainer.withDatabaseName("openmrs");
		mysqlContainer.withUsername(databaseUsername);
		mysqlContainer.withPassword(databaseUserPasswword);
		Startables.deepStart(Stream.of(mysqlContainer)).join();
		
		System.setProperty("databaseUrl",
		    databaseUrl.replaceAll("DATABASE_PORT", String.valueOf(mysqlContainer.getMappedPort(3306))));
		System.setProperty("databaseUsername", databaseUsername);
		System.setProperty("databasePassword", databaseUserPasswword);
		System.setProperty("databaseDialect", databaseDialect);
		System.setProperty("databaseDriver", databaseDriver);
		System.setProperty("useInMemoryDatabase", "false");
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (null != mysqlContainer) {
			mysqlContainer.stop();
		}
	}
	
}
