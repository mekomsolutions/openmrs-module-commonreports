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

public class OrdersReportManagerTest extends BaseModuleContextSensitiveMysqlBackedTest {
	
	public OrdersReportManagerTest() throws SQLException {
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
	@Qualifier("ordersReportManager")
	private ActivatedReportManager manager;
	
	@Before
	public void setUp() throws Exception {
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
		Assert.assertNotNull(rs.getReportDesignByUuid("b7382e3f-7314-4cf7-9a6b-a4053c6519f5"));
		
	}
	
	@Test
	public void testReport() throws Exception {
		
		EvaluationContext context = new EvaluationContext();
		context.addParameterValue("dateActivated", DateUtil.parseDate("1970-06-01", "yyyy-MM-dd"));
		context.addParameterValue("dateStopped", DateUtil.parseDate("2021-06-30", "yyyy-MM-dd"));
		
		ReportDefinition rd = manager.constructReportDefinition();
		ReportData data = rds.evaluate(rd, context);
		
		Map<String, Object> row1columnValuePairs = getRow1ColumnValues();
		Map<String, Object> row7columnValuePairs = getRow7ColumnValues();
		
		for (DataSet ds : data.getDataSets().values()) {
			System.out.println(ds.getDefinition());
			System.out.println(ds.toString());
			
			int rowNumber = 0;
			for (Iterator<DataSetRow> itr = ds.iterator(); itr.hasNext();) {
				rowNumber++;
				DataSetRow row = itr.next();
				if (rowNumber == 1) {
					assertEquals(row1columnValuePairs.get("order_id"),
					    Integer.parseInt(row.getColumnValue("order_id").toString()));
					assertEquals(row1columnValuePairs.get("patient_id"),
					    Integer.parseInt(row.getColumnValue("patient_id").toString()));
					assertEquals(row1columnValuePairs.get("order_type_id"),
					    Integer.parseInt(row.getColumnValue("order_type_id").toString()));
					assertEquals(row1columnValuePairs.get("order_type_name"), row.getColumnValue("order_type_name"));
					assertEquals(row1columnValuePairs.get("order_type_uuid"), row.getColumnValue("order_type_uuid"));
					assertEquals(row1columnValuePairs.get("order_type_java_class_name"),
					    row.getColumnValue("order_type_java_class_name"));
					assertEquals(row1columnValuePairs.get("concept_id"), row.getColumnValue("concept_id"));
					assertEquals(row1columnValuePairs.get("orderer"), row.getColumnValue("orderer"));
					assertEquals(row1columnValuePairs.get("order_type_name"), row.getColumnValue("order_type_name"));
					assertEquals(row1columnValuePairs.get("care_setting_name"), row.getColumnValue("care_setting_name"));
					assertEquals(row1columnValuePairs.get("care_setting_type"), row.getColumnValue("care_setting_type"));
					assertEquals(row1columnValuePairs.get("care_setting_uuid"), row.getColumnValue("care_setting_uuid"));
					assertEquals(row1columnValuePairs.get("instructions"), row.getColumnValue("instructions"));
					assertEquals(row1columnValuePairs.get("date_activated"),
					    row.getColumnValue("date_activated").toString());
					assertEquals(row1columnValuePairs.get("uuid"), row.getColumnValue("uuid"));
					
				}
				
				if (rowNumber == 7) {
					assertEquals(row7columnValuePairs.get("order_id"),
					    Integer.parseInt(row.getColumnValue("order_id").toString()));
					assertEquals(row7columnValuePairs.get("patient_id"),
					    Integer.parseInt(row.getColumnValue("patient_id").toString()));
					assertEquals(row7columnValuePairs.get("order_type_id"),
					    Integer.parseInt(row.getColumnValue("order_type_id").toString()));
					assertEquals(row7columnValuePairs.get("order_type_name"), row.getColumnValue("order_type_name"));
					assertEquals(row7columnValuePairs.get("order_type_uuid"), row.getColumnValue("order_type_uuid"));
					assertEquals(row7columnValuePairs.get("order_type_java_class_name"),
					    row.getColumnValue("order_type_java_class_name"));
					assertEquals(row7columnValuePairs.get("concept_id"), row.getColumnValue("concept_id"));
					assertEquals(row7columnValuePairs.get("orderer"), row.getColumnValue("orderer"));
					assertEquals(row7columnValuePairs.get("order_type_name"), row.getColumnValue("order_type_name"));
					assertEquals(row7columnValuePairs.get("care_setting_name"), row.getColumnValue("care_setting_name"));
					assertEquals(row7columnValuePairs.get("care_setting_type"), row.getColumnValue("care_setting_type"));
					assertEquals(row7columnValuePairs.get("care_setting_uuid"), row.getColumnValue("care_setting_uuid"));
					assertEquals(row7columnValuePairs.get("instructions"), row.getColumnValue("instructions"));
					assertEquals(row7columnValuePairs.get("date_activated"),
					    row.getColumnValue("date_activated").toString());
					assertEquals(row7columnValuePairs.get("uuid"), row.getColumnValue("uuid"));
					
				}
				
			}
			assertEquals(14, rowNumber);
		}
		
	}
	
	private Map<String, Object> getRow1ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("order_id", 1);
		map.put("patient_id", 7);
		map.put("order_type_id", 1);
		map.put("order_type_name", "Drug order");
		map.put("order_type_uuid", "131168f4-15f5-102d-96e4-000c29c2a5d7");
		map.put("order_type_java_class_name", "org.openmrs.DrugOrder");
		map.put("concept_id", 88);
		map.put("orderer", 1);
		map.put("encounter_datetime", "2008-08-01 00:00:00.0");
		map.put("care_setting_name", "OUTPATIENT");
		map.put("care_setting_type", "OUTPATIENT");
		map.put("care_setting_uuid", "6f0c9a92-6f24-11e3-af88-005056821db0");
		map.put("instructions", "2x daily");
		map.put("date_activated", "2008-08-08 00:00:00.0");
		map.put("uuid", "921de0a3-05c4-444a-be03-e01b4c4b9142");
		
		return map;
	}
	
	private Map<String, Object> getRow7ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("order_id", 7);
		map.put("patient_id", 2);
		map.put("order_type_id", 2);
		map.put("order_type_name", "Test order");
		map.put("order_type_uuid", "52a447d3-a64a-11e3-9aeb-50e549534c5e");
		map.put("order_type_java_class_name", "org.openmrs.TestOrder");
		map.put("concept_id", 5497);
		map.put("orderer", 1);
		map.put("encounter_datetime", "2008-08-19 00:00:00.0");
		map.put("care_setting_name", "OUTPATIENT");
		map.put("care_setting_type", "OUTPATIENT");
		map.put("care_setting_uuid", "6f0c9a92-6f24-11e3-af88-005056821db0");
		map.put("instructions", null);
		map.put("date_activated", "2008-10-19 09:24:10.0");
		map.put("uuid", "2c96f25c-4949-4f72-9931-d808fbc226df");
		
		return map;
	}
}
