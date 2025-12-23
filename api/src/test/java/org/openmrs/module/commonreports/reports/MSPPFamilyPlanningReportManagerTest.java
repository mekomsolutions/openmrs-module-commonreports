package org.openmrs.module.commonreports.reports;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.dbunit.DatabaseUnitException;
import org.dbunit.DatabaseUnitRuntimeException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.ConceptService;
import org.openmrs.module.initializer.Domain;
import org.openmrs.module.initializer.api.InitializerService;
import org.openmrs.module.initializer.api.loaders.Loader;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.dataset.definition.CohortCrossTabDataSetDefinition;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
import org.openmrs.module.reporting.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MSPPFamilyPlanningReportManagerTest extends BaseModuleContextSensitiveMysqlBackedTest {
	
	public MSPPFamilyPlanningReportManagerTest() throws SQLException {
		super();
	}
	
	@Autowired
	private InitializerService iniz;
	
	@Autowired
	private ReportService rs;
	
	@Autowired
	private ReportDefinitionService rds;
	
	@Autowired
	@Qualifier("conceptService")
	private ConceptService cs;
	
	@Autowired
	private MSPPFamilyPlanningReportManager manager;
	
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
	
	@Before
	public void setUp() throws Exception {
		updateDatabase("org/openmrs/module/commonreports/liquibase/test-liquibase.xml");
		executeDataSet("org/openmrs/module/reporting/include/ReportTestDataset-openmrs-2.0.xml");
		executeDataSet("org/openmrs/module/commonreports/include/MSPPfamilyPlanningTestDataset.xml");
		
		String path = getClass().getClassLoader().getResource("testAppDataDir").getPath() + File.separator;
		System.setProperty("OPENMRS_APPLICATION_DATA_DIRECTORY", path);
		
		for (Loader loader : iniz.getLoaders()) {
			if (loader.getDomainName().equals(Domain.JSON_KEY_VALUES.getName())) {
				loader.load();
			}
		}
	}
	
	@Test
	public void constructReportDesigns_shouldCreateCsvReportDesign() {
		// replay
		ReportDefinition rd = manager.constructReportDefinition();
		List<ReportDesign> designs = manager.constructReportDesigns(rd);
		
		// verify
		assertNotNull("Report designs should not be null", designs);
		assertEquals("Should have 1 report design", 1, designs.size());
		
		ReportDesign design = designs.get(0);
		assertEquals("8e300676-75d7-48f8-82eb-4fe9971459fe", design.getUuid());
		assertEquals(rd, design.getReportDefinition());
	}
	
	@Test
	public void setupReport_shouldCreateCsvReportDesign() throws Exception {
		// replay
		ReportManagerUtil.setupReport(manager);
		
		// verify
		ReportDesign design = rs.getReportDesignByUuid("8e300676-75d7-48f8-82eb-4fe9971459fe");
		assertNotNull("Report design should exist", design);
		
		ReportDefinition def = design.getReportDefinition();
		assertEquals("efd7ba26-7888-45a8-9184-423833ab79d3", def.getUuid());
	}
	
	@Test
	public void testReport_shouldEvaluateReportWithCorrectData() throws Exception {
		// setup
		EvaluationContext context = new EvaluationContext();
		context.addParameterValue("startDate", DateUtil.parseDate("2021-07-01", "yyyy-MM-dd"));
		context.addParameterValue("endDate", DateUtil.parseDate("2021-07-30", "yyyy-MM-dd"));
		
		// replay
		ReportDefinition rd = manager.constructReportDefinition();
		ReportData data = rds.evaluate(rd, context);
		
		// verify
		assertNotNull("Report data should not be null", data);
		assertTrue("Should have data sets", data.getDataSets().size() > 0);
		
		for (Iterator<DataSetRow> itr = data.getDataSets().get(rd.getName()).iterator(); itr.hasNext();) {
			DataSetRow row = itr.next();
			
			// Verify Microlut data
			assertEquals(new Long(1), row.getColumnValue("existentMicrolutFemaleLT25"));
			assertEquals(new Long(0), row.getColumnValue("existentMicrolutFemaleGT25"));
			assertEquals(new Long(1), row.getColumnValue("newMicrolutFemaleLT25"));
			assertEquals(new Long(0), row.getColumnValue("newMicrolutFemaleGT25"));
			
			// Verify Jadel data
			assertEquals(new Long(1), row.getColumnValue("newJadelFemaleLT25"));
			assertEquals(new Long(0), row.getColumnValue("newJadelFemaleGT25"));
			
			// Verify Depo Provera data
			assertEquals(new Long(1), row.getColumnValue("existentDepoFemaleGT25"));
			assertEquals(new Long(0), row.getColumnValue("existentDepoFemaleLT25"));
			
			// Verify Condom Female data
			assertEquals(new Long(1), row.getColumnValue("newCondomFemaleGT25"));
			assertEquals(new Long(0), row.getColumnValue("newCondomFemaleLT25"));
			
			// Verify Condom Male data
			assertEquals(new Long(1), row.getColumnValue("newCondomMaleGT25"));
			assertEquals(new Long(0), row.getColumnValue("newCondomMaleLT25"));
			
			// Verify Total column exists
			assertNotNull("Total column should exist", row.getColumnValue("Total"));
		}
	}

	
	@Test
	public void testReport_shouldIncludeAllMethodRows() throws Exception {
		// setup
		EvaluationContext context = new EvaluationContext();
		context.addParameterValue("startDate", DateUtil.parseDate("2021-07-01", "yyyy-MM-dd"));
		context.addParameterValue("endDate", DateUtil.parseDate("2021-07-30", "yyyy-MM-dd"));
		
		// replay
		ReportDefinition rd = manager.constructReportDefinition();
		ReportData data = rds.evaluate(rd, context);
		
		// verify - check that all method rows are present in the results
		for (Iterator<DataSetRow> itr = data.getDataSets().get(rd.getName()).iterator(); itr.hasNext();) {
			DataSetRow row = itr.next();
			
			// Verify Microgynon rows exist
			assertNotNull("newMycogynonFemaleLT25 should exist", row.getColumnValue("newMycogynonFemaleLT25"));
			assertNotNull("newMycogynonFemaleGT25 should exist", row.getColumnValue("newMycogynonFemaleGT25"));
			assertNotNull("existentMycogynonFemaleLT25 should exist", row.getColumnValue("existentMycogynonFemaleLT25"));
			assertNotNull("existentMycogynonFemaleGT25 should exist", row.getColumnValue("existentMycogynonFemaleGT25"));
			
			// Verify Microlut rows exist
			assertNotNull("newMicrolutFemaleLT25 should exist", row.getColumnValue("newMicrolutFemaleLT25"));
			assertNotNull("newMicrolutFemaleGT25 should exist", row.getColumnValue("newMicrolutFemaleGT25"));
			assertNotNull("existentMicrolutFemaleLT25 should exist", row.getColumnValue("existentMicrolutFemaleLT25"));
			assertNotNull("existentMicrolutFemaleGT25 should exist", row.getColumnValue("existentMicrolutFemaleGT25"));
			
			// Verify Depo Provera rows exist
			assertNotNull("newDepoFemaleLT25 should exist", row.getColumnValue("newDepoFemaleLT25"));
			assertNotNull("newDepoFemaleGT25 should exist", row.getColumnValue("newDepoFemaleGT25"));
			assertNotNull("existentDepoFemaleLT25 should exist", row.getColumnValue("existentDepoFemaleLT25"));
			assertNotNull("existentDepoFemaleGT25 should exist", row.getColumnValue("existentDepoFemaleGT25"));
			
			// Verify Jadel rows exist
			assertNotNull("newJadelFemaleLT25 should exist", row.getColumnValue("newJadelFemaleLT25"));
			assertNotNull("newJadelFemaleGT25 should exist", row.getColumnValue("newJadelFemaleGT25"));
			assertNotNull("existentJadelFemaleLT25 should exist", row.getColumnValue("existentJadelFemaleLT25"));
			assertNotNull("existentJadelFemaleGT25 should exist", row.getColumnValue("existentJadelFemaleGT25"));
			
			// Verify Condom Female rows exist
			assertNotNull("newCondomFemaleLT25 should exist", row.getColumnValue("newCondomFemaleLT25"));
			assertNotNull("newCondomFemaleGT25 should exist", row.getColumnValue("newCondomFemaleGT25"));
			assertNotNull("existentCondomFemaleLT25 should exist", row.getColumnValue("existentCondomFemaleLT25"));
			assertNotNull("existentCondomFemaleGT25 should exist", row.getColumnValue("existentCondomFemaleGT25"));
			
			// Verify Condom Male rows exist
			assertNotNull("newCondomMaleLT25 should exist", row.getColumnValue("newCondomMaleLT25"));
			assertNotNull("newCondomMaleGT25 should exist", row.getColumnValue("newCondomMaleGT25"));
			assertNotNull("existentCondomMaleLT25 should exist", row.getColumnValue("existentCondomMaleLT25"));
			assertNotNull("existentCondomMaleGT25 should exist", row.getColumnValue("existentCondomMaleGT25"));
		}
	}
	
	private void updateDatabase(String filename) throws Exception {
		Liquibase liquibase = getLiquibase(filename);
		liquibase.update("Modify column datatype to longblob on reporting_report_design_resource table");
		liquibase.getDatabase().getConnection().commit();
	}
	
	private Liquibase getLiquibase(String filename) throws Exception {
		Database liquibaseConnection = DatabaseFactory.getInstance()
		        .findCorrectDatabaseImplementation(new JdbcConnection(getConnection()));
		
		liquibaseConnection.setDatabaseChangeLogTableName("LIQUIBASECHANGELOG");
		liquibaseConnection.setDatabaseChangeLogLockTableName("LIQUIBASECHANGELOGLOCK");
		
		return new Liquibase(filename, new ClassLoaderResourceAccessor(getClass().getClassLoader()), liquibaseConnection);
	}
}
