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

public class ProgramReportManagerTest extends BaseModuleContextSensitiveMysqlBackedTest {
	
	public ProgramReportManagerTest() throws SQLException {
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
	@Qualifier("programsReportManager")
	private ActivatedReportManager manager;
	
	@Before
	public void setUp() throws Exception {
		// executeDataSet("org/openmrs/module/commonreports/include/conditionTestDataset.xml");
		
		// executeDataSet("org/openmrs/module/reporting/include/ReportTestDataset-openmrs-2.0.xml");
		
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
		Assert.assertNotNull(rs.getReportDesignByUuid("ee701139-5126-435a-86f9-8b35afb066cd"));
		
	}
	
	@Test
	public void testReport() throws Exception {
		
		EvaluationContext context = new EvaluationContext();
		context.addParameterValue("dateEnrolled", DateUtil.parseDate("1970-06-01", "yyyy-MM-dd"));
		
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
					assertEquals(row1columnValuePairs.get("patient_program_id"),
					    Integer.parseInt(row.getColumnValue("patient_program_id").toString()));
					assertEquals(row1columnValuePairs.get("patient_id"),
					    Integer.parseInt(row.getColumnValue("patient_id").toString()));
					assertEquals(row1columnValuePairs.get("program_id"),
					    Integer.parseInt(row.getColumnValue("program_id").toString()));
					assertEquals(row1columnValuePairs.get("date_enrolled"), row.getColumnValue("date_enrolled").toString());
					assertEquals(row1columnValuePairs.get("date_completed"), row.getColumnValue("date_completed"));
					assertEquals(row1columnValuePairs.get("location_id"), row.getColumnValue("location_id"));
					assertEquals(row1columnValuePairs.get("outcome_concept_id"), row.getColumnValue("outcome_concept_id"));
					assertEquals(row1columnValuePairs.get("creator"),
					    Integer.parseInt(row.getColumnValue("creator").toString()));
					assertEquals(row1columnValuePairs.get("date_created"), row.getColumnValue("date_created").toString());
					
					assertEquals(row1columnValuePairs.get("changed_by"),
					    Integer.parseInt(row.getColumnValue("changed_by").toString()));
					assertEquals(row1columnValuePairs.get("date_changed"), row.getColumnValue("date_changed").toString());
					assertEquals(row1columnValuePairs.get("uuid"), row.getColumnValue("uuid"));
					assertEquals(row1columnValuePairs.get("program_name"), row.getColumnValue("program_name"));
					assertEquals(row1columnValuePairs.get("program_description"), row.getColumnValue("program_description"));
					assertEquals(row1columnValuePairs.get("program_uuid"), row.getColumnValue("program_uuid"));
					
					assertEquals(row1columnValuePairs.get("program_concept_id"),
					    Integer.parseInt(row.getColumnValue("program_concept_id").toString()));
					
				}
				
				if (rowNumber == 4) {
					assertEquals(row4columnValuePairs.get("patient_program_id"),
					    Integer.parseInt(row.getColumnValue("patient_program_id").toString()));
					assertEquals(row4columnValuePairs.get("patient_id"),
					    Integer.parseInt(row.getColumnValue("patient_id").toString()));
					assertEquals(row4columnValuePairs.get("program_id"),
					    Integer.parseInt(row.getColumnValue("program_id").toString()));
					assertEquals(row4columnValuePairs.get("date_enrolled"), row.getColumnValue("date_enrolled").toString());
					assertEquals(row4columnValuePairs.get("date_completed"), row.getColumnValue("date_completed"));
					assertEquals(row4columnValuePairs.get("location_id"), row.getColumnValue("location_id"));
					assertEquals(row4columnValuePairs.get("outcome_concept_id"), row.getColumnValue("outcome_concept_id"));
					assertEquals(row4columnValuePairs.get("creator"),
					    Integer.parseInt(row.getColumnValue("creator").toString()));
					assertEquals(row4columnValuePairs.get("date_created"), row.getColumnValue("date_created").toString());
					
					assertEquals(row4columnValuePairs.get("changed_by"), (row.getColumnValue("changed_by")));
					assertEquals(row4columnValuePairs.get("date_changed"), row.getColumnValue("date_changed"));
					assertEquals(row4columnValuePairs.get("uuid"), row.getColumnValue("uuid"));
					assertEquals(row4columnValuePairs.get("program_name"), row.getColumnValue("program_name"));
					assertEquals(row4columnValuePairs.get("program_description"), row.getColumnValue("program_description"));
					assertEquals(row4columnValuePairs.get("program_uuid"), row.getColumnValue("program_uuid"));
					
					assertEquals(row4columnValuePairs.get("program_concept_id"),
					    Integer.parseInt(row.getColumnValue("program_concept_id").toString()));
					
				}
				
			}
			assertEquals(4, rowNumber);
		}
		
	}
	
	private Map<String, Object> getRow1ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("patient_program_id", 1);
		map.put("patient_id", 2);
		map.put("program_id", 1);
		map.put("date_enrolled", "2008-08-01 00:00:00.0");
		map.put("date_completed", null);
		map.put("location_id", null);
		map.put("outcome_concept_id", null);
		map.put("creator", 1);
		map.put("date_created", "2008-08-18 11:40:48.0");
		map.put("changed_by", 1);
		map.put("date_changed", "2008-08-18 12:29:29.0");
		map.put("uuid", "b75462a0-4c92-451e-b8bc-e98b38b76534");
		map.put("program_name", "HIV PROGRAM");
		map.put("program_description", "hiv program");
		map.put("program_uuid", "da4a0391-ba62-4fad-ad66-1e3722d16380");
		map.put("program_concept_id", 9);
		
		return map;
	}
	
	private Map<String, Object> getRow4ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("patient_program_id", 4);
		map.put("patient_id", 7);
		map.put("program_id", 2);
		map.put("date_enrolled", "2008-08-01 00:00:00.0");
		map.put("date_completed", null);
		map.put("location_id", null);
		map.put("outcome_concept_id", null);
		map.put("creator", 1);
		map.put("date_created", "2008-08-18 11:53:00.0");
		map.put("changed_by", null);
		map.put("date_changed", null);
		map.put("uuid", "deff305f-48f5-4b84-a083-b18c80954fed");
		map.put("program_name", "MDR-TB PROGRAM");
		map.put("program_description", "MDR-TB program");
		map.put("program_uuid", "71779c39-d289-4dfe-91b5-e7cfaa27c78b");
		map.put("program_concept_id", 10);
		
		return map;
	}
}
