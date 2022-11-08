package org.openmrs.module.commonreports.reports;

import static org.junit.Assert.assertEquals;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.commonreports.reports.BaseModuleContextSensitiveMysqlBackedTest;
import org.openmrs.module.commonreports.ActivatedReportManager;
import org.openmrs.module.initializer.api.InitializerService;
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

public class ConceptsReportManagerTest extends BaseModuleContextSensitiveMysqlBackedTest {
	
	public ConceptsReportManagerTest() throws SQLException {
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
	@Qualifier("conceptsReportManager")
	private ActivatedReportManager manager;
	
	@Test
	public void setupReport_shouldSetUpReport() {
		
		// replay
		ReportManagerUtil.setupReport(manager);
		
		// verify
		Assert.assertNotNull(rs.getReportDesignByUuid("2658a450-fc0a-40db-a9e3-2849666ebe9d"));
		
	}
	
	@Test
	public void testReport() throws Exception {
		
		EvaluationContext context = new EvaluationContext();
		
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
					assertEquals(row1columnValuePairs.get("concept_id"),
					    Integer.parseInt(row.getColumnValue("concept_id").toString()));
					assertEquals(row1columnValuePairs.get("Concept Mapping Source"),
					    row.getColumnValue("Concept Mapping Source"));
					assertEquals(row1columnValuePairs.get("Concept Mapping Code"),
					    row.getColumnValue("Concept Mapping Code"));
					assertEquals(row1columnValuePairs.get("Concept Mapping Name"),
					    row.getColumnValue("Concept Mapping Name"));
					assertEquals(row1columnValuePairs.get("name"), row.getColumnValue("name"));
					assertEquals(row1columnValuePairs.get("locale"), row.getColumnValue("locale"));
					assertEquals(row1columnValuePairs.get("locale_preferred"), row.getColumnValue("locale_preferred"));
					assertEquals(row1columnValuePairs.get("retired"), row.getColumnValue("retired"));
					assertEquals(row1columnValuePairs.get("uuid"), row.getColumnValue("uuid"));
					
				}
				
				if (rowNumber == 7) {
					assertEquals(row7columnValuePairs.get("concept_id"),
					    Integer.parseInt(row.getColumnValue("concept_id").toString()));
					assertEquals(row7columnValuePairs.get("Concept Mapping Source"),
					    row.getColumnValue("Concept Mapping Source"));
					assertEquals(row7columnValuePairs.get("Concept Mapping Code"),
					    row.getColumnValue("Concept Mapping Code"));
					assertEquals(row7columnValuePairs.get("Concept Mapping Name"),
					    row.getColumnValue("Concept Mapping Name"));
					assertEquals(row7columnValuePairs.get("name"), row.getColumnValue("name"));
					assertEquals(row7columnValuePairs.get("locale"), row.getColumnValue("locale"));
					assertEquals(row7columnValuePairs.get("locale_preferred"), row.getColumnValue("locale_preferred"));
					assertEquals(row7columnValuePairs.get("retired"), row.getColumnValue("retired"));
					assertEquals(row7columnValuePairs.get("uuid"), row.getColumnValue("uuid"));
					
				}
				
			}
			assertEquals(55, rowNumber);
		}
		
	}
	
	private Map<String, Object> getRow1ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("concept_id", 6);
		map.put("Concept Mapping Source", "Some Standardized Terminology");
		map.put("Concept Mapping Code", "127689");
		map.put("Concept Mapping Name", "married term");
		map.put("name", "MARRIED");
		map.put("locale", "en_GB");
		map.put("locale_preferred", true);
		map.put("retired", false);
		map.put("uuid", "92afda7c-78c9-47bd-a841-0de0817027d4");
		
		return map;
	}
	
	private Map<String, Object> getRow7ColumnValues() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("concept_id", 5089);
		map.put("Concept Mapping Source", "Some Standardized Terminology");
		map.put("Concept Mapping Code", "WGT234");
		map.put("Concept Mapping Name", "weight term");
		map.put("name", "WEIGHT (KG)");
		map.put("locale", "en_GB");
		map.put("locale_preferred", true);
		map.put("retired", false);
		map.put("uuid", "c607c80f-1ea9-4da3-bb88-6276ce8868dd");
		return map;
	}
}
