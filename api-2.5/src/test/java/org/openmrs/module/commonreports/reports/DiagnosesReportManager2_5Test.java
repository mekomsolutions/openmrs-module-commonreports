package org.openmrs.module.commonreports.reports;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.hibernate.cfg.Environment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.commonreports.reports.BaseModuleContextSensitiveMysqlBackedTest;
import org.openmrs.module.commonreports.ActivatedReportManager;
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
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class DiagnosesReportManager2_5Test extends BaseModuleContextSensitiveTest {
	
	@Autowired
	private InitializerService iniz;
	
	@Autowired
	private ReportService rs;
	
	@Autowired
	private ReportDefinitionService rds;
	
	@Autowired
	@Qualifier("diagnosesReportManager2_5")
	private ActivatedReportManager manager;
	
	@Before
	public void setUp() throws Exception {
		executeDataSet("org/openmrs/module/commonreports/include/diagnosesTestDataset2_5.xml");
		
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
		Assert.assertNotNull(rs.getReportDesignByUuid("cf69cbb4-5114-4fe9-8495-35c10b878157"));
		
	}
	
	@Test
	public void testReport() throws Exception {
		
		EvaluationContext context = new EvaluationContext();
		context.addParameterValue("startDate", DateUtil.parseDate("1970-06-01", "yyyy-MM-dd"));
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
					assertEquals(row1columnValuePairs.get("diagnosis_id"),
					    Integer.parseInt(row.getColumnValue("diagnosis_id").toString()));
					assertEquals(row1columnValuePairs.get("patient_id"),
					    Integer.parseInt(row.getColumnValue("patient_id").toString()));
					assertEquals(row1columnValuePairs.get("dx_rank"), row.getColumnValue("dx_rank"));
					assertEquals(row1columnValuePairs.get("encounter_id"),
					    Integer.parseInt(row.getColumnValue("encounter_id").toString()));
					assertEquals(row1columnValuePairs.get("certainty"), row.getColumnValue("certainty"));
					assertEquals(row1columnValuePairs.get("date_created"), row.getColumnValue("date_created").toString());
					assertEquals(row1columnValuePairs.get("creator"),
					    Integer.parseInt(row.getColumnValue("creator").toString()));
					assertEquals(row1columnValuePairs.get("uuid"), row.getColumnValue("uuid"));
					
				}
				
				if (rowNumber == 4) {
					assertEquals(row4columnValuePairs.get("diagnosis_id"),
					    Integer.parseInt(row.getColumnValue("diagnosis_id").toString()));
					assertEquals(row4columnValuePairs.get("patient_id"),
					    Integer.parseInt(row.getColumnValue("patient_id").toString()));
					assertEquals(row4columnValuePairs.get("dx_rank"), row.getColumnValue("dx_rank"));
					assertEquals(row4columnValuePairs.get("encounter_id"),
					    Integer.parseInt(row.getColumnValue("encounter_id").toString()));
					assertEquals(row4columnValuePairs.get("certainty"), row.getColumnValue("certainty"));
					assertEquals(row4columnValuePairs.get("date_created"), row.getColumnValue("date_created").toString());
					assertEquals(row4columnValuePairs.get("creator"),
					    Integer.parseInt(row.getColumnValue("creator").toString()));
					assertEquals(row4columnValuePairs.get("uuid"), row.getColumnValue("uuid"));
				}
				
			}
			assertEquals(4, rowNumber);
		}
		
	}
	
	private Map<String, Object> getRow1ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("diagnosis_id", 1);
		map.put("patient_id", 2);
		map.put("dx_rank", 1);
		map.put("encounter_id", 6);
		map.put("certainty", "CONFIRMED");
		map.put("uuid", "68802cce-6880-17e4-6880-a68804d22fb7");
		map.put("creator", 1);
		map.put("date_created", "2017-01-12 00:00:00.0");
		
		return map;
	}
	
	private Map<String, Object> getRow4ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("diagnosis_id", 4);
		map.put("patient_id", 2);
		map.put("dx_rank", 2);
		map.put("encounter_id", 5);
		map.put("certainty", "PROVISIONAL");
		map.put("uuid", "77009cce-8804-17e4-8804-a68804d22fb7");
		map.put("creator", 1);
		map.put("date_created", "2015-01-12 00:00:00.0");
		
		return map;
	}
}
