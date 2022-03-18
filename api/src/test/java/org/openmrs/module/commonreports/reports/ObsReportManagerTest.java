package org.openmrs.module.commonreports.reports;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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

public class ObsReportManagerTest extends BaseModuleContextSensitiveMysqlBackedTest {
	
	public ObsReportManagerTest() throws SQLException {
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
	@Qualifier("obsReportManager")
	private ActivatedReportManager manager;
	
	@Before
	public void setUp() throws Exception {
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
		Assert.assertNotNull(rs.getReportDesignByUuid("6357f383-1a10-4501-937f-49816a4add18"));
		
	}
	
	@Test
	public void testReport() throws Exception {
		
		EvaluationContext context = new EvaluationContext();
		context.addParameterValue("startDate", DateUtil.parseDate("1970-06-01", "yyyy-MM-dd"));
		context.addParameterValue("endDate", DateUtil.parseDate("2021-06-30", "yyyy-MM-dd"));
		
		ReportDefinition rd = manager.constructReportDefinition();
		ReportData data = rds.evaluate(rd, context);
		
		Map<String, Object> row1columnValuePairs = getRow1ColumnValues();
		Map<String, Object> row7columnValuePairs = getRow7ColumnValues();
		
		for (DataSet ds : data.getDataSets().values()) {
			int rowNumber = 0;
			for (Iterator<DataSetRow> itr = ds.iterator(); itr.hasNext();) {
				rowNumber++;
				DataSetRow row = itr.next();
				if (rowNumber == 1) {
					assertEquals(row1columnValuePairs.get("obs_id"),
					    Integer.parseInt(row.getColumnValue("obs_id").toString()));
					assertEquals(row1columnValuePairs.get("person_id"),
					    Integer.parseInt(row.getColumnValue("person_id").toString()));
					assertEquals(row1columnValuePairs.get("concept_id"),
					    Integer.parseInt(row.getColumnValue("concept_id").toString()));
					assertEquals(row1columnValuePairs.get("concept_name"), row.getColumnValue("concept_name"));
					assertEquals(row1columnValuePairs.get("concept_uuid"), row.getColumnValue("concept_uuid"));
					assertEquals(row1columnValuePairs.get("obs_group_id"), row.getColumnValue("obs_group_id"));
					assertEquals(row1columnValuePairs.get("accession_number"), row.getColumnValue("accession_number"));
					assertEquals(row1columnValuePairs.get("form_namespace_and_path"),
					    row.getColumnValue("form_namespace_and_path"));
					assertEquals(row1columnValuePairs.get("value_coded"),
					    Integer.parseInt(row.getColumnValue("value_coded").toString()));
					assertEquals(row1columnValuePairs.get("value_coded_name"), row.getColumnValue("value_coded_name"));
					assertEquals(row1columnValuePairs.get("value_coded_uuid"), row.getColumnValue("value_coded_uuid"));
					assertEquals(row1columnValuePairs.get("value_coded_name_id"), row.getColumnValue("value_coded_name_id"));
					assertEquals(row1columnValuePairs.get("value_drug"), row.getColumnValue("value_drug"));
					assertEquals(row1columnValuePairs.get("value_datetime"), row.getColumnValue("value_datetime"));
					assertEquals(row1columnValuePairs.get("value_numeric"), row.getColumnValue("value_numeric"));
					assertEquals(row1columnValuePairs.get("value_modifier"), row.getColumnValue("value_modifier"));
					assertEquals(row1columnValuePairs.get("value_text"), row.getColumnValue("value_text"));
					assertEquals(row1columnValuePairs.get("value_complex"), row.getColumnValue("value_complex"));
					assertEquals(row1columnValuePairs.get("comments"), row.getColumnValue("comments"));
					assertEquals(row1columnValuePairs.get("creator"), row.getColumnValue("creator"));
					assertEquals(row1columnValuePairs.get("date_created"), row.getColumnValue("date_created").toString());
					assertEquals(row1columnValuePairs.get("obs_voided"), row.getColumnValue("obs_voided"));
					assertEquals(row1columnValuePairs.get("obs_void_reason"), row.getColumnValue("obs_void_reason"));
					assertEquals(row1columnValuePairs.get("previous_version"), row.getColumnValue("previous_version"));
					assertEquals(row1columnValuePairs.get("encounter_id"),
					    Integer.parseInt(row.getColumnValue("encounter_id").toString()));
					assertEquals(row1columnValuePairs.get("encounter_voided"), row.getColumnValue("encounter_voided"));
					assertEquals(row1columnValuePairs.get("encounter_type_name"), row.getColumnValue("encounter_type_name"));
					assertEquals(row1columnValuePairs.get("encounter_type_description"),
					    row.getColumnValue("encounter_type_description"));
					assertEquals(row1columnValuePairs.get("encounter_type_uuid"), row.getColumnValue("encounter_type_uuid"));
					assertEquals(row1columnValuePairs.get("encounter_type_retired"),
					    row.getColumnValue("encounter_type_retired"));
					assertEquals(row1columnValuePairs.get("visit_id"), row.getColumnValue("visit_id"));
					assertEquals(row1columnValuePairs.get("visit_date_started"), row.getColumnValue("visit_date_started"));
					assertEquals(row1columnValuePairs.get("visit_date_stopped"), row.getColumnValue("visit_date_stopped"));
					assertEquals(row1columnValuePairs.get("visit_type_name"), row.getColumnValue("visit_type_name"));
					assertEquals(row1columnValuePairs.get("visit_type_uuid"), row.getColumnValue("visit_type_uuid"));
					assertEquals(row1columnValuePairs.get("visit_type_retired"), row.getColumnValue("visit_type_retired"));
					assertEquals(row1columnValuePairs.get("location_id"), row.getColumnValue("location_id"));
					assertEquals(row1columnValuePairs.get("location_name"), row.getColumnValue("location_name"));
					assertEquals(row1columnValuePairs.get("location_address1"), row.getColumnValue("location_address1"));
					assertEquals(row1columnValuePairs.get("location_uuid"), row.getColumnValue("location_uuid"));
					
				}
				
				if (rowNumber == 7) {
					
					assertEquals(row7columnValuePairs.get("obs_id"),
					    Integer.parseInt(row.getColumnValue("obs_id").toString()));
					assertEquals(row7columnValuePairs.get("person_id"),
					    Integer.parseInt(row.getColumnValue("person_id").toString()));
					assertEquals(row7columnValuePairs.get("concept_id"),
					    Integer.parseInt(row.getColumnValue("concept_id").toString()));
					assertEquals(row7columnValuePairs.get("concept_name"), row.getColumnValue("concept_name"));
					assertEquals(row7columnValuePairs.get("concept_uuid"), row.getColumnValue("concept_uuid"));
					assertEquals(row7columnValuePairs.get("obs_group_id"), row.getColumnValue("obs_group_id"));
					assertEquals(row7columnValuePairs.get("accession_number"), row.getColumnValue("accession_number"));
					assertEquals(row7columnValuePairs.get("form_namespace_and_path"),
					    row.getColumnValue("form_namespace_and_path"));
					assertEquals(row7columnValuePairs.get("value_coded"), row.getColumnValue("value_coded"));
					assertEquals(row7columnValuePairs.get("value_coded_name"), row.getColumnValue("value_coded_name"));
					assertEquals(row7columnValuePairs.get("value_coded_uuid"), row.getColumnValue("value_coded_uuid"));
					assertEquals(row7columnValuePairs.get("value_coded_name_id"), row.getColumnValue("value_coded_name_id"));
					assertEquals(row7columnValuePairs.get("value_drug"), row.getColumnValue("value_drug"));
					assertEquals(row7columnValuePairs.get("value_datetime"), row.getColumnValue("value_datetime"));
					assertEquals(row7columnValuePairs.get("value_numeric"), row.getColumnValue("value_numeric"));
					assertEquals(row7columnValuePairs.get("value_modifier"), row.getColumnValue("value_modifier"));
					assertEquals(row7columnValuePairs.get("value_text"), row.getColumnValue("value_text"));
					assertEquals(row7columnValuePairs.get("value_complex"), row.getColumnValue("value_complex"));
					assertEquals(row7columnValuePairs.get("comments"), row.getColumnValue("comments"));
					assertEquals(row7columnValuePairs.get("creator"), row.getColumnValue("creator"));
					assertEquals(row7columnValuePairs.get("date_created"), row.getColumnValue("date_created").toString());
					assertEquals(row7columnValuePairs.get("obs_voided"), row.getColumnValue("obs_voided"));
					assertEquals(row7columnValuePairs.get("obs_void_reason"), row.getColumnValue("obs_void_reason"));
					assertEquals(row7columnValuePairs.get("previous_version"), row.getColumnValue("previous_version"));
					assertEquals(row7columnValuePairs.get("encounter_id"),
					    Integer.parseInt(row.getColumnValue("encounter_id").toString()));
					assertEquals(row7columnValuePairs.get("encounter_voided"), row.getColumnValue("encounter_voided"));
					assertEquals(row7columnValuePairs.get("encounter_type_name"), row.getColumnValue("encounter_type_name"));
					assertEquals(row7columnValuePairs.get("encounter_type_description"),
					    row.getColumnValue("encounter_type_description"));
					assertEquals(row7columnValuePairs.get("encounter_type_uuid"), row.getColumnValue("encounter_type_uuid"));
					assertEquals(row7columnValuePairs.get("encounter_type_retired"),
					    row.getColumnValue("encounter_type_retired"));
					assertEquals(row7columnValuePairs.get("visit_id"), row.getColumnValue("visit_id"));
					assertEquals(row7columnValuePairs.get("visit_date_started"), row.getColumnValue("visit_date_started"));
					assertEquals(row7columnValuePairs.get("visit_date_stopped"), row.getColumnValue("visit_date_stopped"));
					assertEquals(row7columnValuePairs.get("visit_type_name"), row.getColumnValue("visit_type_name"));
					assertEquals(row7columnValuePairs.get("visit_type_uuid"), row.getColumnValue("visit_type_uuid"));
					assertEquals(row7columnValuePairs.get("visit_type_retired"), row.getColumnValue("visit_type_retired"));
					assertEquals(row7columnValuePairs.get("location_id"), row.getColumnValue("location_id"));
					assertEquals(row7columnValuePairs.get("location_name"), row.getColumnValue("location_name"));
					assertEquals(row7columnValuePairs.get("location_address1"), row.getColumnValue("location_address1"));
					assertEquals(row7columnValuePairs.get("location_uuid"), row.getColumnValue("location_uuid"));
					
				}
				
			}
			assertEquals(48, rowNumber);
		}
		
	}
	
	private Map<String, Object> getRow1ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("obs_id", 6);
		map.put("person_id", 7);
		map.put("concept_id", 21);
		map.put("concept_name", "FOOD ASSISTANCE FOR ENTIRE FAMILY");
		map.put("concept_uuid", "325391a8-db12-4e24-863f-5d66f7a4d713");
		map.put("obs_group_id", null);
		map.put("accession_number", null);
		map.put("form_namespace_and_path", null);
		map.put("value_coded", 8);
		map.put("value_coded_name", "NO");
		map.put("value_coded_uuid", "b98a6ed4-77e7-4cee-aae2-81957fcd7f48");
		map.put("value_coded_name_id", null);
		map.put("value_drug", null);
		map.put("value_datetime", null);
		map.put("value_numeric", null);
		map.put("value_modifier", null);
		map.put("value_text", null);
		map.put("value_complex", null);
		map.put("comments", "");
		map.put("creator", 1);
		map.put("date_created", "2008-08-19 12:32:59.0");
		map.put("obs_voided", false);
		map.put("obs_void_reason", null);
		map.put("previous_version", null);
		map.put("encounter_id", 3);
		map.put("encounter_voided", false);
		map.put("encounter_type_name", "Emergency");
		map.put("encounter_type_description", "Emergency visit");
		map.put("encounter_type_uuid", "07000be2-26b6-4cce-8b40-866d8435b613");
		map.put("encounter_type_retired", false);
		map.put("visit_id", null);
		map.put("visit_date_started", null);
		map.put("visit_date_stopped", null);
		map.put("visit_type_name", null);
		map.put("visit_type_uuid", null);
		map.put("visit_type_retired", null);
		map.put("location_id", null);
		map.put("location_name", "Unknown Location");
		map.put("location_address1", "");
		map.put("location_uuid", "dc5c1fcc-0459-4201-bf70-0b90535ba362");
		
		return map;
	}
	
	private Map<String, Object> getRow7ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("obs_id", 9);
		map.put("person_id", 7);
		map.put("concept_id", 5497);
		map.put("concept_name", "CD3+CD4+ABS CNT");
		map.put("concept_uuid", "8230adbf-30a9-4e18-b6d7-fc57e0c23cab");
		map.put("obs_group_id", null);
		map.put("accession_number", null);
		map.put("form_namespace_and_path", null);
		map.put("value_coded", null);
		map.put("value_coded_name", null);
		map.put("value_coded_uuid", null);
		map.put("value_coded_name_id", null);
		map.put("value_drug", null);
		map.put("value_datetime", null);
		map.put("value_numeric", 150.0);
		map.put("value_modifier", null);
		map.put("value_text", null);
		map.put("value_complex", null);
		map.put("comments", "");
		map.put("creator", 1);
		map.put("date_created", "2008-08-18 14:11:13.0");
		map.put("obs_voided", false);
		map.put("obs_void_reason", null);
		map.put("previous_version", null);
		map.put("encounter_id", 3);
		map.put("encounter_voided", false);
		map.put("encounter_type_name", "Emergency");
		map.put("encounter_type_description", "Emergency visit");
		map.put("encounter_type_uuid", "07000be2-26b6-4cce-8b40-866d8435b613");
		map.put("encounter_type_retired", false);
		map.put("visit_id", null);
		map.put("visit_date_started", null);
		map.put("visit_date_stopped", null);
		map.put("visit_type_name", null);
		map.put("visit_type_uuid", null);
		map.put("visit_type_retired", null);
		map.put("location_id", null);
		map.put("location_name", "Unknown Location");
		map.put("location_address1", "");
		map.put("location_uuid", "dc5c1fcc-0459-4201-bf70-0b90535ba362");
		
		return map;
	}
}
