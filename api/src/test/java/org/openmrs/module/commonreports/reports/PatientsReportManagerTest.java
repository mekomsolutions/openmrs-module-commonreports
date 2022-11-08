package org.openmrs.module.commonreports.reports;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.commonreports.ActivatedReportManager;
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

public class PatientsReportManagerTest extends BaseModuleContextSensitiveMysqlBackedTest {
	
	public PatientsReportManagerTest() throws SQLException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	private ReportService rs;
	
	@Autowired
	private ReportDefinitionService rds;
	
	@Autowired
	@Qualifier("patientsReportManager")
	private ActivatedReportManager manager;
	
	@Test
	public void setupReport_shouldSetUpReport() {
		
		// replay
		ReportManagerUtil.setupReport(manager);
		
		// verify
		Assert.assertNotNull(rs.getReportDesignByUuid("be80af44-ce58-489b-b015-0fd7b2409575"));
		
	}
	
	@Test
	public void testReport() throws Exception {
		
		EvaluationContext context = new EvaluationContext();
		
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
					assertEquals(row1columnValuePairs.get("patient_id"),
					    Integer.parseInt(row.getColumnValue("patient_id").toString()));
					assertEquals(row1columnValuePairs.get("given_name"), row.getColumnValue("given_name"));
					assertEquals(row1columnValuePairs.get("middle_name"), row.getColumnValue("middle_name"));
					assertEquals(row1columnValuePairs.get("family_name"), row.getColumnValue("family_name"));
					assertEquals(row1columnValuePairs.get("name"), row.getColumnValue("name"));
					assertEquals(row1columnValuePairs.get("birthdate"), row.getColumnValue("birthdate").toString());
					assertEquals(row1columnValuePairs.get("gender"), row.getColumnValue("gender"));
					assertEquals(row1columnValuePairs.get("birthdate_estimated"), row.getColumnValue("birthdate_estimated"));
					assertEquals(row1columnValuePairs.get("city"), row.getColumnValue("city"));
					assertEquals(row1columnValuePairs.get("date_created"), row.getColumnValue("date_created").toString());
					assertEquals(row1columnValuePairs.get("creator"), row.getColumnValue("creator"));
					assertEquals(row1columnValuePairs.get("cause_of_death"), row.getColumnValue("cause_of_death"));
					assertEquals(row1columnValuePairs.get("death_date"), row.getColumnValue("death_date"));
					assertEquals(row1columnValuePairs.get("dead"), row.getColumnValue("dead"));
					assertEquals(row1columnValuePairs.get("country"), row.getColumnValue("country"));
					assertEquals(row1columnValuePairs.get("state_province"), row.getColumnValue("state_province"));
					assertEquals(row1columnValuePairs.get("address2"), row.getColumnValue("address2"));
					assertEquals(row1columnValuePairs.get("person_voided"), row.getColumnValue("person_voided"));
					assertEquals(row1columnValuePairs.get("person_void_reason"), row.getColumnValue("person_void_reason"));
					assertEquals(row1columnValuePairs.get("address1"), row.getColumnValue("address1"));
					
				}
				
				if (rowNumber == 7) {
					
					assertEquals(row7columnValuePairs.get("patient_id"),
					    Integer.parseInt(row.getColumnValue("patient_id").toString()));
					assertEquals(row7columnValuePairs.get("given_name"), row.getColumnValue("given_name"));
					assertEquals(row7columnValuePairs.get("middle_name"), row.getColumnValue("middle_name"));
					assertEquals(row7columnValuePairs.get("family_name"), row.getColumnValue("family_name"));
					assertEquals(row7columnValuePairs.get("identifier"), row.getColumnValue("identifier"));
					assertEquals(row7columnValuePairs.get("birthdate"), row.getColumnValue("birthdate").toString());
					assertEquals(row7columnValuePairs.get("gender"), row.getColumnValue("gender"));
					assertEquals(row7columnValuePairs.get("birthdate_estimated"), row.getColumnValue("birthdate_estimated"));
					assertEquals(row7columnValuePairs.get("city"), row.getColumnValue("city"));
					assertEquals(row7columnValuePairs.get("date_created"), row.getColumnValue("date_created").toString());
					assertEquals(row7columnValuePairs.get("creator"), row.getColumnValue("creator"));
					assertEquals(row7columnValuePairs.get("cause_of_death"), row.getColumnValue("cause_of_death"));
					assertEquals(row7columnValuePairs.get("death_date"), row.getColumnValue("death_date"));
					assertEquals(row7columnValuePairs.get("dead"), row.getColumnValue("dead"));
					assertEquals(row7columnValuePairs.get("country"), row.getColumnValue("country"));
					assertEquals(row7columnValuePairs.get("state_province"), row.getColumnValue("state_province"));
					assertEquals(row7columnValuePairs.get("address2"), row.getColumnValue("address2"));
					assertEquals(row7columnValuePairs.get("person_voided"), row.getColumnValue("person_voided"));
					assertEquals(row7columnValuePairs.get("person_void_reason"), row.getColumnValue("person_void_reason"));
					assertEquals(row7columnValuePairs.get("address1"), row.getColumnValue("address1"));
					
				}
				
			}
			assertEquals(13, rowNumber);
		}
		
	}
	
	private Map<String, Object> getRow1ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("patient_id", 2);
		map.put("given_name", "Horatio");
		map.put("middle_name", "Test");
		map.put("family_name", "Hornblower");
		map.put("identifier", 101);
		map.put("birthdate", "1975-04-08 00:00:00.0");
		map.put("gender", "M");
		map.put("birthdate_estimated", false);
		map.put("city", "Indianapolis");
		map.put("address1", "1050 Wishard Blvd.");
		map.put("address2", "RG5");
		map.put("state_province", "IN");
		map.put("country", "USA");
		map.put("dead", false);
		map.put("death_date", null);
		map.put("cause_of_death", null);
		map.put("creator", 1);
		map.put("date_created", "2005-09-22 00:00:00.0");
		map.put("person_voided", false);
		map.put("person_void_reason", null);
		
		return map;
	}
	
	private Map<String, Object> getRow7ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("patient_id", 6);
		map.put("given_name", "Johnny");
		map.put("middle_name", "Test");
		map.put("family_name", "Doe");
		map.put("identifier", "12345K");
		map.put("birthdate", "2007-05-27 00:00:00.0");
		map.put("gender", "M");
		map.put("birthdate_estimated", false);
		map.put("city", "");
		map.put("address1", "");
		map.put("address2", "");
		map.put("state_province", "");
		map.put("country", "");
		map.put("dead", false);
		map.put("death_date", null);
		map.put("cause_of_death", null);
		map.put("creator", 1);
		map.put("date_created", "2006-01-18 00:00:00.0");
		map.put("person_voided", false);
		map.put("person_void_reason", null);
		
		return map;
	}
}
