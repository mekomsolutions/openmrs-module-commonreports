package org.openmrs.module.commonreports.reports;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Cohort;
import org.openmrs.api.ConceptService;
import org.openmrs.module.initializer.Domain;
import org.openmrs.module.initializer.api.InitializerService;
import org.openmrs.module.initializer.api.loaders.Loader;
import org.openmrs.module.reporting.common.DateUtil;
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MSPPFamilyPlanningReportManagerTest extends BaseModuleContextSensitiveTest {

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
	private MSPPFamilyPlanningReportManager manager;


	@Before
	public void setup() throws Exception {
		executeDataSet("org/openmrs/module/reporting/include/ReportTestDataset-openmrs-2.0.xml");
		executeDataSet("org/openmrs/module/commonreports/include/MSPPfamilyPlanningTestDataset.xml");

		String path = getClass().getClassLoader().getResource("testAppDataDir").getPath() + File.separator;
		System.setProperty("OPENMRS_APPLICATION_DATA_DIRECTORY", path);

		for (Loader loader : iniz.getLoaders()) {
			if (loader.getDomainName().equals(Domain.JSON_KEY_VALUES.getName())) {
				loader.load();
			}
		}
	}

	@Test
	public void setupReport_shouldSetupFamilyPlanningReport() {

		// replay
		ReportManagerUtil.setupReport(manager);

		// verify
		Assert.assertNotNull(rs.getReportDesignByUuid("8e300676-75d7-48f8-82eb-4fe9971459fe"));

	}
	
	@Test
	public void testReport_shouldEvaluateReportWithCorrectData() throws Exception {
		// setup
		EvaluationContext context = new EvaluationContext();
		context.addParameterValue("startDate", DateUtil.parseDate("2021-07-01", "yyyy-MM-dd"));
		context.addParameterValue("endDate", DateUtil.parseDate("2021-07-30", "yyyy-MM-dd"));
		
		// replay
		ReportDefinition rd = manager.constructReportDefinition();
		ReportData data = rds.evaluate(rd, context);
		
		// verify
		assertNotNull("Report data should not be null", data);
		assertTrue("Should have data sets", data.getDataSets().size() > 0);

		for (Iterator<DataSetRow> itr = data.getDataSets().get(rd.getName()).iterator(); itr.hasNext();) {
			DataSetRow row = itr.next();
			Map<String, Integer> columnValuePairs = getColumnValues();
			for (String column : columnValuePairs.keySet()) {
				assertThat(column, ((Cohort) row.getColumnValue(column)).getSize(), is(columnValuePairs.get(column)));
			}
		}
	}

	private Map<String, Integer> getColumnValues() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("existentMicrolutFemaleLT25", 1);
		map.put("existentMicrolutFemaleGT25", 0);
		map.put("newMicrolutFemaleLT25", 1);
		map.put("newMicrolutFemaleGT25", 0);
		map.put("newJadelFemaleLT25", 1);
		map.put("newJadelFemaleGT25", 0);
		map.put("existentDepoFemaleGT25", 1);
		map.put("existentDepoFemaleLT25", 0);
		map.put("newCondomFemaleGT25", 1);
		map.put("newCondomFemaleLT25", 0);
		map.put("newCondomMaleGT25", 1);
		map.put("newCondomMaleLT25", 0);

		return map;
	}
}
