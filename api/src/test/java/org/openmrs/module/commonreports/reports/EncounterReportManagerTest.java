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

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

public class EncounterReportManagerTest extends BaseModuleContextSensitiveMysqlBackedTest {
	
	public EncounterReportManagerTest() throws SQLException {
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
	@Qualifier("encounterReportManager")
	private ActivatedReportManager manager;
	
	@Before
	public void setUp() throws Exception {
		// executeDataSet("org/openmrs/module/commonreports/include/encounterTestDataset.xml");
		
		// executeDataSet("org/openmrs/module/reporting/include/ReportTestDataset-openmrs-2.0.xml");
		updateDatabase("org/openmrs/module/commonreports/liquibase/test-liquibase.xml");
		
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
		Assert.assertNotNull(rs.getReportDesignByUuid("7975c2c9-5055-42c3-8d90-fa7bcce79361"));
		
	}
	
	@Test
	public void testReport() throws Exception {
		
		EvaluationContext context = new EvaluationContext();
		context.addParameterValue("startDate", DateUtil.parseDate("1970-06-01", "yyyy-MM-dd"));
		context.addParameterValue("endDate", DateUtil.parseDate("2021-06-30", "yyyy-MM-dd"));
		
		ReportDefinition rd = manager.constructReportDefinition();
		ReportData data = rds.evaluate(rd, context);
		
		Map<String, Object> row1columnValuePairs = getRow1ColumnValues();
		Map<String, Object> row4columnValuePairs = getRow4ColumnValues();
		
		for (DataSet ds : data.getDataSets().values()) {
			int rowNumber = 0;
			for (Iterator<DataSetRow> itr = ds.iterator(); itr.hasNext();) {
				rowNumber++;
				DataSetRow row = itr.next();
				System.out.println(row);
				if (rowNumber == 1) {
					
					assertEquals(row1columnValuePairs.get("encounter_id"),
					    Integer.parseInt(row.getColumnValue("encounter_id").toString()));
					assertEquals(row1columnValuePairs.get("encounter_patient_id"),
					    Integer.parseInt(row.getColumnValue("encounter_patient_id").toString()));
					assertEquals(row1columnValuePairs.get("encounter_type_id"),
					    Integer.parseInt(row.getColumnValue("encounter_type_id").toString()));
					assertEquals(row1columnValuePairs.get("encounter_type_name"), row.getColumnValue("encounter_type_name"));
					assertEquals(row1columnValuePairs.get("encounter_type_uuid"), row.getColumnValue("encounter_type_uuid"));
					assertEquals(row1columnValuePairs.get("encounter_type_retired"),
					    row.getColumnValue("encounter_type_retired"));
					assertEquals(row1columnValuePairs.get("encounter_voided"), row.getColumnValue("encounter_voided"));
					assertEquals(row1columnValuePairs.get("encounter_visit_id"), row.getColumnValue("encounter_visit_id"));
					assertEquals(row1columnValuePairs.get("encounter_datetime"),
					    row.getColumnValue("encounter_datetime").toString());
					assertEquals(row1columnValuePairs.get("visit_type_name"), row.getColumnValue("visit_type_name"));
					assertEquals(row1columnValuePairs.get("encounter_form_id"),
					    Integer.parseInt(row.getColumnValue("encounter_form_id").toString()));
					assertEquals(row1columnValuePairs.get("form_name"), row.getColumnValue("form_name"));
					assertEquals(row1columnValuePairs.get("form_uuid"), row.getColumnValue("form_uuid"));
					assertEquals(row1columnValuePairs.get("form_version"),
					    Double.parseDouble(row.getColumnValue("form_version").toString()));
					assertEquals(row1columnValuePairs.get("form_published"), row.getColumnValue("form_published"));
					assertEquals(row1columnValuePairs.get("encounter_uuid"), row.getColumnValue("encounter_uuid"));
					assertEquals(row1columnValuePairs.get("encounter_date_created"),
					    row.getColumnValue("encounter_date_created").toString());
					assertEquals(row1columnValuePairs.get("location_name"), row.getColumnValue("location_name"));
					assertEquals(row1columnValuePairs.get("location_uuid"), row.getColumnValue("location_uuid"));
					assertEquals(row1columnValuePairs.get("encounter_type_description"),
					    row.getColumnValue("encounter_type_description"));
					assertEquals(row1columnValuePairs.get("form_description"), row.getColumnValue("form_description"));
					
				}
				
				if (rowNumber == 7) {
					
					assertEquals(row4columnValuePairs.get("encounter_id"),
					    Integer.parseInt(row.getColumnValue("encounter_id").toString()));
					assertEquals(row4columnValuePairs.get("encounter_patient_id"),
					    Integer.parseInt(row.getColumnValue("encounter_patient_id").toString()));
					assertEquals(row4columnValuePairs.get("encounter_type_id"),
					    Integer.parseInt(row.getColumnValue("encounter_type_id").toString()));
					assertEquals(row4columnValuePairs.get("encounter_type_name"), row.getColumnValue("encounter_type_name"));
					assertEquals(row4columnValuePairs.get("encounter_type_uuid"), row.getColumnValue("encounter_type_uuid"));
					assertEquals(row4columnValuePairs.get("encounter_type_retired"),
					    row.getColumnValue("encounter_type_retired"));
					assertEquals(row4columnValuePairs.get("encounter_voided"), row.getColumnValue("encounter_voided"));
					assertEquals(row4columnValuePairs.get("encounter_visit_id"), row.getColumnValue("encounter_visit_id"));
					assertEquals(row4columnValuePairs.get("encounter_datetime"),
					    row.getColumnValue("encounter_datetime").toString());
					assertEquals(row4columnValuePairs.get("visit_type_name"), row.getColumnValue("visit_type_name"));
					assertEquals(row4columnValuePairs.get("encounter_form_id"),
					    Integer.parseInt(row.getColumnValue("encounter_form_id").toString()));
					assertEquals(row4columnValuePairs.get("form_name"), row.getColumnValue("form_name"));
					assertEquals(row4columnValuePairs.get("form_uuid"), row.getColumnValue("form_uuid"));
					assertEquals(row4columnValuePairs.get("form_version"),
					    Double.parseDouble(row.getColumnValue("form_version").toString()));
					assertEquals(row4columnValuePairs.get("form_published"), row.getColumnValue("form_published"));
					assertEquals(row4columnValuePairs.get("encounter_uuid"), row.getColumnValue("encounter_uuid"));
					assertEquals(row4columnValuePairs.get("encounter_date_created"),
					    row.getColumnValue("encounter_date_created").toString());
					assertEquals(row4columnValuePairs.get("location_name"), row.getColumnValue("location_name"));
					assertEquals(row4columnValuePairs.get("location_uuid"), row.getColumnValue("location_uuid"));
					assertEquals(row4columnValuePairs.get("encounter_type_description"),
					    row.getColumnValue("encounter_type_description"));
					assertEquals(row4columnValuePairs.get("form_description"), row.getColumnValue("form_description"));
					
				}
				
			}
			assertEquals(4, rowNumber);
		}
		
	}
	
	private Map<String, Object> getRow1ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("encounter_id", 4);
		map.put("encounter_patient_id", 7);
		map.put("encounter_type_id", 1);
		map.put("encounter_type_name", "Scheduled");
		map.put("encounter_type_uuid", "61ae96f4-6afe-4351-b6f8-cd4fc383cce1");
		map.put("encounter_type_retired", false);
		map.put("encounter_datetime", "2008-08-15 00:00:00.0");
		map.put("birthdate_estimated", false);
		map.put("encounter_voided", false);
		map.put("encounter_visit_id", null);
		map.put("visit_type_name", null);
		map.put("encounter_form_id", 1);
		map.put("form_name", "Basic Form");
		map.put("form_uuid", "d9218f76-6c39-45f4-8efa-4c5c6c199f50");
		map.put("form_version", 0.1);
		map.put("form_published", false);
		map.put("encounter_uuid", "eec646cb-c847-45a7-98bc-91c8c4f70add");
		map.put("encounter_date_created", "2008-08-18 14:22:17.0");
		map.put("location_name", "Unknown Location");
		map.put("location_uuid", "8d6c993e-c2cc-11de-8d13-0010c6dffd0f");
		map.put("encounter_type_description", "Scheduled Visit");
		map.put("form_description", "Test form");
		return map;
	}
	
	private Map<String, Object> getRow4ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("encounter_id", 4);
		map.put("encounter_patient_id", 7);
		map.put("encounter_type_id", 1);
		map.put("encounter_type_name", "Scheduled");
		map.put("encounter_type_uuid", "61ae96f4-6afe-4351-b6f8-cd4fc383cce1");
		map.put("encounter_type_retired", false);
		map.put("encounter_datetime", "2008-08-15 00:00:00.0");
		map.put("birthdate_estimated", false);
		map.put("encounter_voided", false);
		map.put("encounter_visit_id", null);
		map.put("visit_type_name", null);
		map.put("encounter_form_id", 1);
		map.put("form_name", "Basic Form");
		map.put("form_uuid", "d9218f76-6c39-45f4-8efa-4c5c6c199f50");
		map.put("form_version", 0.1);
		map.put("form_published", false);
		map.put("encounter_uuid", "eec646cb-c847-45a7-98bc-91c8c4f70add");
		map.put("encounter_date_created", "2008-08-18 14:22:17.0");
		map.put("location_name", "Unknown Location");
		map.put("location_uuid", "8d6c993e-c2cc-11de-8d13-0010c6dffd0f");
		map.put("encounter_type_description", "Scheduled Visit");
		map.put("form_description", "Test form");
		
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
