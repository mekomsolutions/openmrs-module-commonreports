package org.openmrs.module.commonreports.reports;

import static java.math.BigDecimal.ONE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.hibernate.cfg.Environment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Cohort;
import org.openmrs.api.ConceptService;
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

public class DisbursementReportManagerTest extends BaseModuleContextSensitiveMysqlBackedTest {
	
	@Autowired
	private InitializerService iniz;
	
	@Autowired
	private ReportService rs;
	
	@Autowired
	private ReportDefinitionService rds;
	
	@Autowired
	@Qualifier("conceptService")
	private ConceptService cs;
	
	@Autowired
	private DisbursementReportManager manager;
	
	@Before
	public void setup() throws Exception {
		executeDataSet("org/openmrs/module/reporting/include/ReportTestDataset-openmrs-2.0.xml");
		executeDataSet("org/openmrs/module/commonreports/include/disbursementReportTestDataset.xml");
		
		String path = getClass().getClassLoader().getResource("testAppDataDir").getPath() + File.separator;
		System.setProperty("OPENMRS_APPLICATION_DATA_DIRECTORY", path);
		
		for (Loader loader : iniz.getLoaders()) {
			if (loader.getDomainName().equals(Domain.JSON_KEY_VALUES.getName())) {
				loader.load();
			}
		}
	}
	
	@Test
	public void setupReport_shouldSetupEmergencyReport() {
		
		// replay
		ReportManagerUtil.setupReport(manager);
		
		// verify
		Assert.assertNotNull(rs.getReportDesignByUuid("77b6c2cb-4b96-47d8-bcde-b1b7f16f5670"));
		
	}
	
	@Test
	public void testReport() throws Exception {
		// setup
		EvaluationContext context = new EvaluationContext();
		context.addParameterValue("startDate", DateUtil.parseDate("2023-12-01", "yyyy-MM-dd"));
		context.addParameterValue("endDate", DateUtil.parseDate("2024-04-28", "yyyy-MM-dd"));
		
		// replay
		ReportDefinition rd = manager.constructReportDefinition();
		ReportData data = rds.evaluate(rd, context);
		getConnection().commit();
		
		// verify
		boolean indicator1 = true;
		boolean indicator2 = true;
		boolean indicator3 = true;
		boolean indicator4 = true;
		for (DataSet ds : data.getDataSets().values()) {
			for (Iterator<DataSetRow> itr = ds.iterator(); itr.hasNext();) {
				DataSetRow row = itr.next();
				
				if (row.getColumnValue("Indicator").equals("Patients aged 40 and above with NCD form filled out for the first time")) {
					assertEquals("7", row.getColumnValue("Value"));
					indicator1 = true;
				}
				if (row.getColumnValue("Indicator").equals("Women aged 30 to 49 years with CCS form filled out for the first time")) {
					assertEquals("8", row.getColumnValue("Value"));
					indicator2 = true;
				}
				if (row.getColumnValue("Indicator").equals("80% (of women aged 30 to 49 years with CCS form filled out for the first time) had their first CCS screening, were VIA positive and referred")) {
					assertEquals("Yes", row.getColumnValue("Value"));
					indicator3 = true;
				}
				if (row.getColumnValue("Indicator").equals("80% have a Follow-up date and were given medication at least 3 weeks ago")) {
					assertEquals("No", row.getColumnValue("Value"));
					indicator4 = true;
				}
			}
			assertTrue(indicator1 && indicator2 && indicator3 && indicator4);
		}
	}
}
