package org.openmrs.module.commonreports.reports;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.Cohort;
import org.openmrs.module.commonreports.reports.BaseModuleContextSensitiveMysqlBackedTest;
import org.openmrs.module.commonreports.ActivatedReportManager;
import org.openmrs.module.commonreports.CommonReportsConstants;
import org.openmrs.module.initializer.Domain;
import org.openmrs.module.initializer.api.InitializerService;
import org.openmrs.module.initializer.api.loaders.Loader;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
import org.openmrs.module.reporting.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

@RunWith(SpringJUnit4ClassRunner.class)
public class ConditionsReportManagerTest2_2 extends BaseModuleContextSensitiveMysqlBackedTest {
	
	public ConditionsReportManagerTest2_2() throws SQLException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	private InitializerService iniz;
	
	@Autowired
	private ReportService rs;
	
	@Autowired
	private ReportDefinitionService rds;
	
	@Autowired
	@Qualifier("conditionsReportManager2_2")
	private ActivatedReportManager manager;
	
	@Before
	public void setUp() throws Exception {
		updateDatabase("org/openmrs/module/commonreports/liquibase/test-liquibase.xml");
		executeDataSet("org/openmrs/module/commonreports/include/conditionTestDataset2_2.xml");
		
		String path = getClass().getClassLoader().getResource("testAppDataDir").getPath() + File.separator;
		System.setProperty("OPENMRS_APPLICATION_DATA_DIRECTORY", path);
		
		for (Loader loader : iniz.getLoaders()) {
			if (loader.getDomainName().equals(Domain.JSON_KEY_VALUES.getName())) {
				loader.load();
			}
		}
	}
	
	@Test
	public void setupReport_shouldSetUpReport() {
		
		// replay
		ReportManagerUtil.setupReport(manager);
		
		// verify
		Assert.assertNotNull(rs.getReportDesignByUuid("ffadf928-16a7-462e-ba59-49af495d9ca0"));
		
	}
	
	@Test
	public void testReport() throws Exception {
		
		EvaluationContext context = new EvaluationContext();
		context.addParameterValue("onsetDate", DateUtil.parseDate("1970-06-01", "yyyy-MM-dd"));
		context.addParameterValue("endDate", DateUtil.parseDate("2022-06-30", "yyyy-MM-dd"));
		
		ReportDefinition rd = manager.constructReportDefinition();
		ReportData data = rds.evaluate(rd, context);
		
		Map<String, Object> row1columnValuePairs = getRow1ColumnValues();
		Map<String, Object> row4columnValuePairs = getRow4ColumnValues();
		
		for (DataSet ds : data.getDataSets().values()) {
			int rowNumber = 0;
			for (Iterator<DataSetRow> itr = ds.iterator(); itr.hasNext();) {
				rowNumber++;
				DataSetRow row = itr.next();
				if (rowNumber == 1) {
					assertEquals(row1columnValuePairs.get("condition_id"),
					    Integer.parseInt(row.getColumnValue("condition_id").toString()));
					assertEquals(row1columnValuePairs.get("patient_id"),
					    Integer.parseInt(row.getColumnValue("patient_id").toString()));
					assertEquals(row1columnValuePairs.get("status"), row.getColumnValue("status"));
					assertEquals(row1columnValuePairs.get("concept_id"),
					    Integer.parseInt(row.getColumnValue("concept_id").toString()));
					assertEquals(row1columnValuePairs.get("condition_coded"),
					    Integer.parseInt(row.getColumnValue("condition_coded").toString()));
					assertEquals(row1columnValuePairs.get("onset_date"), row.getColumnValue("onset_date").toString());
					assertEquals(row1columnValuePairs.get("end_date"), row.getColumnValue("end_date").toString());
					assertEquals(row1columnValuePairs.get("condition_non_coded"), row.getColumnValue("condition_non_coded"));
					assertEquals(row1columnValuePairs.get("date_created"), row.getColumnValue("date_created").toString());
					assertEquals(row1columnValuePairs.get("creator"),
					    Integer.parseInt(row.getColumnValue("creator").toString()));
					assertEquals(row1columnValuePairs.get("uuid"), row.getColumnValue("uuid"));
					assertEquals(row1columnValuePairs.get("end_reason"), row.getColumnValue("end_reason"));
					
				}
				
				if (rowNumber == 4) {
					assertEquals(row4columnValuePairs.get("condition_id"),
					    Integer.parseInt(row.getColumnValue("condition_id").toString()));
					assertEquals(row4columnValuePairs.get("patient_id"),
					    Integer.parseInt(row.getColumnValue("patient_id").toString()));
					assertEquals(row4columnValuePairs.get("status"), row.getColumnValue("status"));
					assertEquals(row4columnValuePairs.get("concept_id"),
					    Integer.parseInt(row.getColumnValue("concept_id").toString()));
					assertEquals(row4columnValuePairs.get("condition_coded"),
					    Integer.parseInt(row.getColumnValue("condition_coded").toString()));
					assertEquals(row4columnValuePairs.get("onset_date"), row.getColumnValue("onset_date").toString());
					assertEquals(row4columnValuePairs.get("end_date"), row.getColumnValue("end_date").toString());
					assertEquals(row4columnValuePairs.get("condition_non_coded"), row.getColumnValue("condition_non_coded"));
					
					assertEquals(row4columnValuePairs.get("date_created"), row.getColumnValue("date_created").toString());
					assertEquals(row4columnValuePairs.get("creator"),
					    Integer.parseInt(row.getColumnValue("creator").toString()));
					assertEquals(row4columnValuePairs.get("uuid"), row.getColumnValue("uuid"));
					assertEquals(row4columnValuePairs.get("end_reason"), row.getColumnValue("end_reason"));
				}
				
			}
			assertEquals(5, rowNumber);
		}
		
	}
	
	private Map<String, Object> getRow1ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("condition_id", 1);
		map.put("patient_id", 1);
		map.put("status", "CONFIRMED");
		map.put("concept_id", 409);
		map.put("condition_coded", 409);
		map.put("onset_date", "2015-01-12 00:00:00.0");
		map.put("end_date", "2017-03-12 00:00:00.0");
		map.put("condition_non_coded", "NON-CODED-CONDITION2");
		map.put("uuid", "2cc6880e-2c46-11e4-9038-a6c5e4d22fb7");
		map.put("creator", 1);
		map.put("date_created", "2015-01-12 00:00:00.0");
		
		return map;
	}
	
	private Map<String, Object> getRow4ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("condition_id", 4);
		map.put("patient_id", 4);
		map.put("status", "CONFIRMED");
		map.put("concept_id", 409);
		map.put("condition_coded", 408);
		map.put("onset_date", "2014-01-12 00:00:00.0");
		map.put("end_date", "2016-03-12 00:00:00.0");
		map.put("condition_non_coded", "NON-CODED-CONDITION");
		map.put("uuid", "2ss6880e-2c46-11e4-5844-a6c5e4d22fb7");
		map.put("creator", 1);
		map.put("date_created", "2014-01-12 00:00:00.0");
		
		return map;
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
