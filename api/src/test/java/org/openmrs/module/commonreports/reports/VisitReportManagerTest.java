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

public class VisitReportManagerTest extends BaseModuleContextSensitiveMysqlBackedTest {
	
	public VisitReportManagerTest() throws SQLException {
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
	@Qualifier("visitReportManager")
	private ActivatedReportManager manager;
	
	@Before
	public void setUp() throws Exception {
		// executeDataSet("org/openmrs/module/commonreports/include/encounterTestDataset.xml");
		
		executeDataSet("org/openmrs/module/reporting/include/ReportTestDataset-openmrs-2.0.xml");
		
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
		Assert.assertNotNull(rs.getReportDesignByUuid("6d38c15d-7290-4ba9-bef1-0f44f04a8e8e"));
		
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
					assertEquals(row1columnValuePairs.get("visit_id"),
					    Integer.parseInt(row.getColumnValue("visit_id").toString()));
					assertEquals(row1columnValuePairs.get("patient_id"),
					    Integer.parseInt(row.getColumnValue("patient_id").toString()));
					assertEquals(row1columnValuePairs.get("visit_type_uuid"), row.getColumnValue("visit_type_uuid"));
					assertEquals(row1columnValuePairs.get("visit_type"), row.getColumnValue("visit_type"));
					assertEquals(row1columnValuePairs.get("date_started"), row.getColumnValue("date_started").toString());
					assertEquals(row1columnValuePairs.get("birthdate"), row.getColumnValue("birthdate").toString());
					assertEquals(row1columnValuePairs.get("date_stopped"), row.getColumnValue("date_stopped"));
					assertEquals(row1columnValuePairs.get("indication_concept_id"),
					    Integer.parseInt(row.getColumnValue("indication_concept_id").toString()));
					assertEquals(row1columnValuePairs.get("visit_uuid"), row.getColumnValue("visit_uuid"));
					assertEquals(row1columnValuePairs.get("age_at_visit_group_profile_1"),
					    row.getColumnValue("age_at_visit_group_profile_1"));
					assertEquals(row1columnValuePairs.get("age_at_visit"),
					    Double.parseDouble(row.getColumnValue("age_at_visit").toString()));
					assertEquals(row1columnValuePairs.get("person_uuid"), row.getColumnValue("person_uuid"));
				}
				
				if (rowNumber == 4) {
					assertEquals(row4columnValuePairs.get("visit_id"),
					    Integer.parseInt(row.getColumnValue("visit_id").toString()));
					assertEquals(row4columnValuePairs.get("patient_id"),
					    Integer.parseInt(row.getColumnValue("patient_id").toString()));
					assertEquals(row4columnValuePairs.get("visit_type_uuid"), row.getColumnValue("visit_type_uuid"));
					assertEquals(row4columnValuePairs.get("visit_type"), row.getColumnValue("visit_type"));
					assertEquals(row4columnValuePairs.get("date_started"), row.getColumnValue("date_started").toString());
					assertEquals(row4columnValuePairs.get("birthdate"), row.getColumnValue("birthdate").toString());
					assertEquals(row4columnValuePairs.get("date_stopped"), row.getColumnValue("date_stopped"));
					assertEquals(row4columnValuePairs.get("indication_concept_id"),
					    row.getColumnValue("indication_concept_id"));
					assertEquals(row4columnValuePairs.get("visit_uuid"), row.getColumnValue("visit_uuid"));
					assertEquals(row4columnValuePairs.get("age_at_visit_group_profile_1"),
					    row.getColumnValue("age_at_visit_group_profile_1"));
					assertEquals(row4columnValuePairs.get("age_at_visit"),
					    Double.parseDouble(row.getColumnValue("age_at_visit").toString()));
					assertEquals(row4columnValuePairs.get("person_uuid"), row.getColumnValue("person_uuid"));
					
				}
				
			}
			assertEquals(6, rowNumber);
		}
		
	}
	
	private Map<String, Object> getRow1ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("visit_id", 1);
		map.put("patient_id", 2);
		map.put("visit_type_uuid", "c0c579b0-8e59-401d-8a4a-976a0b183519");
		map.put("visit_type", "Initial HIV Clinic Visit");
		map.put("date_started", "2005-01-01 00:00:00.0");
		map.put("date_stopped", null);
		map.put("indication_concept_id", 5497);
		map.put("visit_uuid", "1e5d5d48-6b78-11e0-93c3-18a905e044dc");
		map.put("birthdate", "1975-04-08 00:00:00.0");
		map.put("age_at_visit_group_profile_1", "25+");
		map.put("age_at_visit", 29.7562);
		map.put("person_uuid", "da7f524f-27ce-4bb2-86d6-6d1d05312bd5");
		
		return map;
	}
	
	private Map<String, Object> getRow4ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("visit_id", 5);
		map.put("patient_id", 6);
		map.put("visit_type_uuid", "c0c579b0-8e59-401d-8a4a-976a0b183519");
		map.put("visit_type", "Initial HIV Clinic Visit");
		map.put("date_started", "2005-01-01 00:00:00.0");
		map.put("date_stopped", null);
		map.put("indication_concept_id", null);
		map.put("visit_uuid", "8cfda6ae-6b78-11e0-93c3-18a905e044dc");
		map.put("birthdate", "2007-05-27 00:00:00.0");
		map.put("age_at_visit_group_profile_1", "0 - 1");
		map.put("age_at_visit", -2.4000);
		map.put("person_uuid", "a7e04421-525f-442f-8138-05b619d16def");
		
		return map;
	}
}
