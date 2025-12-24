package org.openmrs.module.commonreports.reports;

import java.io.File;
import java.sql.SQLException;
import java.util.Iterator;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Cohort;
import org.openmrs.module.commonreports.ActivatedReportManager;
import org.openmrs.module.commonreports.CommonReportsConstants;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MSPPFamilyPlanningReportManagerTest extends BaseModuleContextSensitiveMysqlBackedTest {
	
	public MSPPFamilyPlanningReportManagerTest() throws SQLException {
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
	@Qualifier(CommonReportsConstants.COMPONENT_REPORTMANAGER_FAMILY_PLANNING)
	private ActivatedReportManager manager;
	
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

		for (Iterator<DataSetRow> itr = data.getDataSets().get("MSPP Family Planning").iterator(); itr.hasNext(); ) {
			DataSetRow row = itr.next();
			Cohort existentMicrolutFemaleLT25 = (Cohort) row.getColumnValue("existentMicrolutFemaleLT25 - Total");
			assertNotNull(existentMicrolutFemaleLT25);
			assertEquals(1, existentMicrolutFemaleLT25.getSize());

			Cohort existentMicrolutFemaleGT25 = (Cohort) row.getColumnValue("existentMicrolutFemaleGT25 - Total");
			assertNotNull(existentMicrolutFemaleGT25);
			assertEquals(0, existentMicrolutFemaleGT25.getSize());

			Cohort newMicrolutFemaleLT25 = (Cohort) row.getColumnValue("newMicrolutFemaleLT25 - Total");
			assertNotNull(newMicrolutFemaleLT25);
			assertEquals(1, newMicrolutFemaleLT25.getSize());

			Cohort newMicrolutFemaleGT25 = (Cohort) row.getColumnValue("newMicrolutFemaleGT25 - Total");
			assertNotNull(newMicrolutFemaleGT25);
			assertEquals(0, newMicrolutFemaleGT25.getSize());

			Cohort newJadelFemaleLT25 = (Cohort) row.getColumnValue("newJadelFemaleLT25 - Total");
			assertNotNull(newJadelFemaleLT25);
			assertEquals(1, newJadelFemaleLT25.getSize());

			Cohort newJadelFemaleGT25 = (Cohort) row.getColumnValue("newJadelFemaleGT25 - Total");
			assertNotNull(newJadelFemaleGT25);
			assertEquals(0, newJadelFemaleGT25.getSize());

			Cohort existentDepoFemaleGT25 = (Cohort) row.getColumnValue("existentDepoFemaleGT25 - Total");
			assertNotNull(existentDepoFemaleGT25);
			assertEquals(1, existentDepoFemaleGT25.getSize());

			Cohort existentDepoFemaleLT25 = (Cohort) row.getColumnValue("existentDepoFemaleLT25 - Total");
			assertNotNull(existentDepoFemaleLT25);
			assertEquals(0, existentDepoFemaleLT25.getSize());

			Cohort newCondomFemaleGT25 = (Cohort) row.getColumnValue("newCondomFemaleGT25 - Total");
			assertNotNull(newCondomFemaleGT25);
			assertEquals(1, newCondomFemaleGT25.getSize());

			Cohort newCondomFemaleLT25 = (Cohort) row.getColumnValue("newCondomFemaleLT25 - Total");
			assertNotNull(newCondomFemaleLT25);
			assertEquals(0, newCondomFemaleLT25.getSize());

			Cohort newCondomMaleGT25 = (Cohort) row.getColumnValue("newCondomMaleGT25 - Total");
			assertNotNull(newCondomMaleGT25);
			assertEquals(1, newCondomMaleGT25.getSize());

			Cohort newCondomMaleLT25 = (Cohort) row.getColumnValue("newCondomMaleLT25 - Total");
			assertNotNull(newCondomMaleLT25);
			assertEquals(0, newCondomMaleLT25.getSize());
		}
	}
}
