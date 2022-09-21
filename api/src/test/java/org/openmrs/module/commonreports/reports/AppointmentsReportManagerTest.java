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

public class AppointmentsReportManagerTest extends BaseModuleContextSensitiveMysqlBackedTest {
	
	public AppointmentsReportManagerTest() throws SQLException {
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
	@Qualifier("appointmentsReportManager")
	private ActivatedReportManager manager;
	
	@Before
	public void setUp() throws Exception {
		executeDataSet("org/openmrs/module/commonreports/include/appointmentsTestDataset.xml");
		
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
		Assert.assertNotNull(rs.getReportDesignByUuid("989b07a4-f532-4b82-8b56-3d3042fd9037"));
		
	}
	
	@Test
	public void testReport() throws Exception {
		
		EvaluationContext context = new EvaluationContext();
		context.addParameterValue("startDateTime", DateUtil.parseDate("1970-06-01", "yyyy-MM-dd"));
		
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
					assertEquals(row1columnValuePairs.get("patient_appointment_id"),
					    Integer.parseInt(row.getColumnValue("patient_appointment_id").toString()));
					assertEquals(row1columnValuePairs.get("patient_id"),
					    Integer.parseInt(row.getColumnValue("patient_id").toString()));
					assertEquals(row1columnValuePairs.get("appointment_number"), row.getColumnValue("appointment_number"));
					assertEquals(row1columnValuePairs.get("start_date_time"),
					    row.getColumnValue("start_date_time").toString());
					assertEquals(row1columnValuePairs.get("end_date_time"), row.getColumnValue("end_date_time").toString());
					assertEquals(row1columnValuePairs.get("location_id"), row.getColumnValue("location_id"));
					assertEquals(row1columnValuePairs.get("outcome_concept_id"), row.getColumnValue("outcome_concept_id"));
					assertEquals(row1columnValuePairs.get("creator"),
					    Integer.parseInt(row.getColumnValue("creator").toString()));
					assertEquals(row1columnValuePairs.get("date_created"), row.getColumnValue("date_created").toString());
					assertEquals(row1columnValuePairs.get("changed_by"), row.getColumnValue("changed_by"));
					assertEquals(row1columnValuePairs.get("date_changed"), row.getColumnValue("date_changed"));
					assertEquals(row1columnValuePairs.get("uuid"), row.getColumnValue("uuid"));
					assertEquals(row1columnValuePairs.get("appointment_service_id"),
					    Integer.parseInt(row.getColumnValue("appointment_service_id").toString()));
					assertEquals(row1columnValuePairs.get("appointment_service_type_id"),
					    row.getColumnValue("appointment_service_type_id"));
					assertEquals(row1columnValuePairs.get("appointment_kind"), row.getColumnValue("appointment_kind"));
					assertEquals(row1columnValuePairs.get("comments"), row.getColumnValue("comments"));
					assertEquals(row1columnValuePairs.get("related_appointment_id"),
					    row.getColumnValue("related_appointment_id"));
					assertEquals(row1columnValuePairs.get("appointment_service_name"),
					    row.getColumnValue("appointment_service_name").toString());
					assertEquals(row1columnValuePairs.get("appointment_service_description"),
					    row.getColumnValue("appointment_service_description"));
					assertEquals(row1columnValuePairs.get("appointment_service_voided"),
					    row.getColumnValue("appointment_service_voided"));
					assertEquals(row1columnValuePairs.get("appointment_service_uuid"),
					    row.getColumnValue("appointment_service_uuid"));
					assertEquals(row1columnValuePairs.get("appointment_service_color"),
					    row.getColumnValue("appointment_service_color"));
					assertEquals(row1columnValuePairs.get("appointment_service_start_time"),
					    row.getColumnValue("appointment_service_start_time").toString());
					assertEquals(row1columnValuePairs.get("appointment_service_end_time"),
					    row.getColumnValue("appointment_service_end_time").toString());
					assertEquals(row1columnValuePairs.get("appointment_service_speciality_id"),
					    Integer.parseInt(row.getColumnValue("appointment_service_speciality_id").toString()));
					assertEquals(row1columnValuePairs.get("appointment_service_duration_mins"),
					    row.getColumnValue("appointment_service_duration_mins"));
					assertEquals(row1columnValuePairs.get("appointment_service_initial_appointment_status"),
					    row.getColumnValue("appointment_service_initial_appointment_status"));
					assertEquals(row1columnValuePairs.get("appointment_service_max_appointments_limit"),
					    row.getColumnValue("appointment_service_max_appointments_limit"));
					assertEquals(row1columnValuePairs.get("appointment_service_type_name"),
					    row.getColumnValue("appointment_service_type_name"));
					assertEquals(row1columnValuePairs.get("date_created"), row.getColumnValue("date_created").toString());
					assertEquals(row1columnValuePairs.get("creator"), row.getColumnValue("creator"));
					assertEquals(row1columnValuePairs.get("appointment_service_type_duration_mins"),
					    row.getColumnValue("appointment_service_type_duration_mins"));
					assertEquals(row1columnValuePairs.get("appointment_service_type_voided"),
					    row.getColumnValue("appointment_service_type_voided"));
					assertEquals(row1columnValuePairs.get("appointment_service_type_uuid"),
					    row.getColumnValue("appointment_service_type_uuid"));
					assertEquals(row1columnValuePairs.get("patient_appointment_provider"),
					    Integer.parseInt(row.getColumnValue("patient_appointment_provider").toString()));
					assertEquals(row1columnValuePairs.get("patient_appointment_provider_response"),
					    row.getColumnValue("patient_appointment_provider_response"));
					
				}
				
				if (rowNumber == 4) {
					assertEquals(row4columnValuePairs.get("patient_appointment_id"),
					    Integer.parseInt(row.getColumnValue("patient_appointment_id").toString()));
					assertEquals(row4columnValuePairs.get("patient_id"),
					    Integer.parseInt(row.getColumnValue("patient_id").toString()));
					assertEquals(row4columnValuePairs.get("appointment_number"), row.getColumnValue("appointment_number"));
					assertEquals(row4columnValuePairs.get("start_date_time"),
					    row.getColumnValue("start_date_time").toString());
					assertEquals(row4columnValuePairs.get("end_date_time"), row.getColumnValue("end_date_time").toString());
					assertEquals(row4columnValuePairs.get("location_id"), row.getColumnValue("location_id"));
					assertEquals(row4columnValuePairs.get("outcome_concept_id"), row.getColumnValue("outcome_concept_id"));
					assertEquals(row4columnValuePairs.get("creator"),
					    Integer.parseInt(row.getColumnValue("creator").toString()));
					assertEquals(row4columnValuePairs.get("date_created"), row.getColumnValue("date_created").toString());
					assertEquals(row4columnValuePairs.get("changed_by"), row.getColumnValue("changed_by"));
					assertEquals(row4columnValuePairs.get("date_changed"), row.getColumnValue("date_changed"));
					assertEquals(row4columnValuePairs.get("uuid"), row.getColumnValue("uuid"));
					assertEquals(row4columnValuePairs.get("appointment_service_id"),
					    Integer.parseInt(row.getColumnValue("appointment_service_id").toString()));
					assertEquals(row4columnValuePairs.get("appointment_service_type_id"),
					    row.getColumnValue("appointment_service_type_id"));
					assertEquals(row4columnValuePairs.get("appointment_kind"), row.getColumnValue("appointment_kind"));
					assertEquals(row4columnValuePairs.get("comments"), row.getColumnValue("comments"));
					assertEquals(row4columnValuePairs.get("related_appointment_id"),
					    row.getColumnValue("related_appointment_id"));
					assertEquals(row4columnValuePairs.get("appointment_service_name"),
					    row.getColumnValue("appointment_service_name").toString());
					assertEquals(row4columnValuePairs.get("appointment_service_description"),
					    row.getColumnValue("appointment_service_description"));
					assertEquals(row4columnValuePairs.get("appointment_service_voided"),
					    row.getColumnValue("appointment_service_voided"));
					assertEquals(row4columnValuePairs.get("appointment_service_uuid"),
					    row.getColumnValue("appointment_service_uuid"));
					assertEquals(row4columnValuePairs.get("appointment_service_color"),
					    row.getColumnValue("appointment_service_color"));
					assertEquals(row4columnValuePairs.get("appointment_service_start_time"),
					    row.getColumnValue("appointment_service_start_time").toString());
					assertEquals(row4columnValuePairs.get("appointment_service_end_time"),
					    row.getColumnValue("appointment_service_end_time").toString());
					assertEquals(row4columnValuePairs.get("appointment_service_speciality_id"),
					    Integer.parseInt(row.getColumnValue("appointment_service_speciality_id").toString()));
					assertEquals(row4columnValuePairs.get("appointment_service_duration_mins"),
					    row.getColumnValue("appointment_service_duration_mins"));
					assertEquals(row4columnValuePairs.get("appointment_service_initial_appointment_status"),
					    row.getColumnValue("appointment_service_initial_appointment_status"));
					assertEquals(row4columnValuePairs.get("appointment_service_max_appointments_limit"),
					    row.getColumnValue("appointment_service_max_appointments_limit"));
					assertEquals(row4columnValuePairs.get("appointment_service_type_name"),
					    row.getColumnValue("appointment_service_type_name"));
					assertEquals(row4columnValuePairs.get("date_created"), row.getColumnValue("date_created").toString());
					assertEquals(row4columnValuePairs.get("creator"), row.getColumnValue("creator"));
					assertEquals(row4columnValuePairs.get("appointment_service_type_duration_mins"),
					    row.getColumnValue("appointment_service_type_duration_mins"));
					assertEquals(row4columnValuePairs.get("appointment_service_type_voided"),
					    row.getColumnValue("appointment_service_type_voided"));
					assertEquals(row4columnValuePairs.get("appointment_service_type_uuid"),
					    row.getColumnValue("appointment_service_type_uuid"));
					assertEquals(row4columnValuePairs.get("patient_appointment_provider"),
					    row.getColumnValue("patient_appointment_provider"));
					assertEquals(row4columnValuePairs.get("patient_appointment_provider_response"),
					    row.getColumnValue("patient_appointment_provider_response"));
				}
				
			}
			assertEquals(23, rowNumber);
		}
		
	}
	
	private Map<String, Object> getRow1ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("patient_appointment_id", 2);
		map.put("patient_id", 1);
		map.put("appointment_number", null);
		map.put("start_date_time", "2018-08-15 12:00:00.0");
		map.put("location_id", null);
		map.put("end_date_time", "2018-08-15 13:00:00.0");
		map.put("creator", 1);
		map.put("date_created", "2018-08-10 15:57:09.0");
		map.put("changed_by", null);
		map.put("date_changed", null);
		map.put("uuid", "75504r42-3ca8-11e3-bf2b-0800271c1111");
		map.put("appointment_service_id", 1);
		map.put("appointment_service_type_id", null);
		map.put("program_uuid", "Scheduled");
		map.put("appointment_kind", null);
		map.put("comments", null);
		map.put("related_appointment_id", null);
		map.put("appointment_service_name", "Consultation");
		map.put("appointment_service_description", "Consultation");
		map.put("appointment_service_voided", false);
		map.put("appointment_service_uuid", "c36006e5-9fbb-4f20-866b-0ece245615a6");
		map.put("appointment_service_color", null);
		map.put("appointment_service_start_time", "09:00:00");
		map.put("appointment_service_end_time", "17:00:00");
		map.put("appointment_service_speciality_id", 1);
		map.put("appointment_service_max_appointments_limit", 4);
		map.put("appointment_service_duration_mins", 30);
		map.put("appointment_service_initial_appointment_status", null);
		map.put("appointment_service_type_name", "serviceType1");
		map.put("appointment_service_type_duration_mins", 30);
		map.put("appointment_service_type_voided", false);
		map.put("appointment_service_type_uuid", "678906e5-9fbb-4f20-866b-0ece24564578");
		map.put("patient_appointment_provider", 2220);
		map.put("patient_appointment_provider_response", "ACCEPTED");
		
		return map;
	}
	
	private Map<String, Object> getRow4ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("patient_appointment_id", 3);
		map.put("patient_id", 1);
		map.put("appointment_number", null);
		map.put("start_date_time", "2018-08-15 13:00:00.0");
		map.put("location_id", null);
		map.put("end_date_time", "2018-08-15 14:00:00.0");
		map.put("creator", 1);
		map.put("date_created", "2018-08-10 15:57:09.0");
		map.put("changed_by", null);
		map.put("date_changed", null);
		map.put("uuid", "75504r42-3ca8-11e3-bf2b-0800271c12222");
		map.put("appointment_service_id", 1);
		map.put("appointment_service_type_id", null);
		map.put("program_uuid", "Scheduled");
		map.put("appointment_kind", null);
		map.put("comments", null);
		map.put("related_appointment_id", null);
		map.put("appointment_service_name", "Consultation");
		map.put("appointment_service_description", "Consultation");
		map.put("appointment_service_voided", false);
		map.put("appointment_service_uuid", "c36006e5-9fbb-4f20-866b-0ece245615a6");
		map.put("appointment_service_color", null);
		map.put("appointment_service_start_time", "09:00:00");
		map.put("appointment_service_end_time", "17:00:00");
		map.put("appointment_service_speciality_id", 1);
		map.put("appointment_service_max_appointments_limit", 4);
		map.put("appointment_service_duration_mins", 30);
		map.put("appointment_service_initial_appointment_status", null);
		map.put("appointment_service_type_name", "serviceType2");
		map.put("appointment_service_type_duration_mins", 30);
		map.put("appointment_service_type_voided", true);
		map.put("appointment_service_type_uuid", "678906e5-9fbb-4f20-866b-0ece24564878");
		map.put("patient_appointment_provider", null);
		map.put("patient_appointment_provider_response", null);
		
		return map;
	}
}
