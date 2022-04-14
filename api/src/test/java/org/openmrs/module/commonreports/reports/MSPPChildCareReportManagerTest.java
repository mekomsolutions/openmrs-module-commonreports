package org.openmrs.module.commonreports.reports;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Cohort;
import org.openmrs.module.commonreports.ActivatedReportManager;
import org.openmrs.module.initializer.Domain;
import org.openmrs.module.initializer.api.InitializerService;
import org.openmrs.module.initializer.api.loaders.Loader;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetColumn;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
import org.openmrs.module.reporting.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class MSPPChildCareReportManagerTest extends BaseModuleContextSensitiveMysqlBackedTest {
	
	public MSPPChildCareReportManagerTest() throws SQLException {
		super();
	}
	
	@Autowired
	private InitializerService iniz;
	
	@Autowired
	private ReportService rs;
	
	@Autowired
	private ReportDefinitionService rds;
	
	@Autowired
	private MSPPChildCareReportManager manager;
	
	@Before
	public void setUp() throws Exception {
		executeDataSet("org/openmrs/module/reporting/include/ReportTestDataset-openmrs-2.0.xml");
		executeDataSet("org/openmrs/module/commonreports/include/MSPPchildCareTestDataset.xml");
		
		String path = getClass().getClassLoader().getResource("testAppDataDir").getPath() + File.separator;
		System.setProperty("OPENMRS_APPLICATION_DATA_DIRECTORY", path);
		
		for (Loader loader : iniz.getLoaders()) {
			if (loader.getDomainName().equals(Domain.JSON_KEY_VALUES.getName())) {
				loader.load();
			}
		}
	}
	
	@Test
	public void setupReport_shouldSetupChildCareReport() {
		
		// replay
		ReportManagerUtil.setupReport(manager);
		
		// verif
		Assert.assertNotNull(rs.getReportDesignByUuid("0623f477-542c-4b37-8ee6-2a1a4a1821b8"));
		
	}
	
	@Test
	public void testReport() throws Exception {
		
		EvaluationContext context = new EvaluationContext();
		context.addParameterValue("startDate", DateUtil.parseDate("2021-06-01", "yyyy-MM-dd"));
		context.addParameterValue("endDate", DateUtil.parseDate("2021-06-30", "yyyy-MM-dd"));
		
		ReportDefinition rd = manager.constructReportDefinition();
		ReportData data = rds.evaluate(rd, context);
		data.getDataSets();
		
		Map<String, Integer> columnValuePairs = getColumnValues();
		for (DataSet ds : data.getDataSets().values()) {
			for (Iterator<DataSetRow> itr = ds.iterator(); itr.hasNext();) {
				DataSetRow row = itr.next();
				for (DataSetColumn column : row.getColumnValues().keySet()) {
					assertThat(column.getName(), ((Cohort) row.getColumnValue(column)).getSize(),
					    is(columnValuePairs.get(column.getName())));
				}
			}
		}
	}
	
	private Map<String, Integer> getColumnValues() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		map.put("Total children seen.Children under 6 months - M", 2);
		map.put("Total children seen.Children under 6 months - F", 0);
		map.put("Total children seen.Children under 6 months - Total", 2);
		map.put("Total children seen.Children 6 months to 23 months - M", 1);
		map.put("Total children seen.Children 6 months to 23 months - F", 1);
		map.put("Total children seen.Children 6 months to 23 months - Total", 2);
		map.put("Total children seen.Children from 24 months to 59 months - M", 2);
		map.put("Total children seen.Children from 24 months to 59 months - F", 1);
		map.put("Total children seen.Children from 24 months to 59 months - Total", 3);
		map.put("Children seen for the first time.Children under 6 months - M", 2);
		map.put("Children seen for the first time.Children under 6 months - F", 0);
		map.put("Children seen for the first time.Children under 6 months - Total", 2);
		map.put("Children seen for the first time.Children 6 months to 23 months - M", 1);
		map.put("Children seen for the first time.Children 6 months to 23 months - F", 1);
		map.put("Children seen for the first time.Children 6 months to 23 months - Total", 2);
		map.put("Children seen for the first time.Children from 24 months to 59 months - M", 1);
		map.put("Children seen for the first time.Children from 24 months to 59 months - F", 1);
		map.put("Children seen for the first time.Children from 24 months to 59 months - Total", 2);
		map.put("Children seen for the first time with MUAC measured.Children under 6 months - M", 1);
		map.put("Children seen for the first time with MUAC measured.Children under 6 months - F", 0);
		map.put("Children seen for the first time with MUAC measured.Children under 6 months - Total", 1);
		map.put("Children seen for the first time with MUAC measured.Children 6 months to 23 months - M", 1);
		map.put("Children seen for the first time with MUAC measured.Children 6 months to 23 months - F", 0);
		map.put("Children seen for the first time with MUAC measured.Children 6 months to 23 months - Total", 1);
		map.put("Children seen for the first time with MUAC measured.Children from 24 months to 59 months - M", 1);
		map.put("Children seen for the first time with MUAC measured.Children from 24 months to 59 months - F", 1);
		map.put("Children seen for the first time with MUAC measured.Children from 24 months to 59 months - Total", 2);
		map.put("Children seen for the first time with weighed measured.Children under 6 months - M", 1);
		map.put("Children seen for the first time with weighed measured.Children under 6 months - F", 0);
		map.put("Children seen for the first time with weighed measured.Children under 6 months - Total", 1);
		map.put("Children seen for the first time with weighed measured.Children 6 months to 23 months - M", 1);
		map.put("Children seen for the first time with weighed measured.Children 6 months to 23 months - F", 0);
		map.put("Children seen for the first time with weighed measured.Children 6 months to 23 months - Total", 1);
		map.put("Children seen for the first time with weighed measured.Children from 24 months to 59 months - M", 0);
		map.put("Children seen for the first time with weighed measured.Children from 24 months to 59 months - F", 1);
		map.put("Children seen for the first time with weighed measured.Children from 24 months to 59 months - Total", 1);
		map.put("Children seen for the first time (115 < MUAC < 125).Children under 6 months - M", 0);
		map.put("Children seen for the first time (115 < MUAC < 125).Children under 6 months - F", 0);
		map.put("Children seen for the first time (115 < MUAC < 125).Children under 6 months - Total", 0);
		map.put("Children seen for the first time (115 < MUAC < 125).Children 6 months to 23 months - M", 0);
		map.put("Children seen for the first time (115 < MUAC < 125).Children 6 months to 23 months - F", 0);
		map.put("Children seen for the first time (115 < MUAC < 125).Children 6 months to 23 months - Total", 0);
		map.put("Children seen for the first time (115 < MUAC < 125).Children from 24 months to 59 months - M", 1);
		map.put("Children seen for the first time (115 < MUAC < 125).Children from 24 months to 59 months - F", 1);
		map.put("Children seen for the first time (115 < MUAC < 125).Children from 24 months to 59 months - Total", 2);
		map.put("Children seen for the first time (MUAC < 115).Children under 6 months - M", 1);
		map.put("Children seen for the first time (MUAC < 115).Children under 6 months - F", 0);
		map.put("Children seen for the first time (MUAC < 115).Children under 6 months - Total", 1);
		map.put("Children seen for the first time (MUAC < 115).Children 6 months to 23 months - M", 1);
		map.put("Children seen for the first time (MUAC < 115).Children 6 months to 23 months - F", 0);
		map.put("Children seen for the first time (MUAC < 115).Children 6 months to 23 months - Total", 1);
		map.put("Children seen for the first time (MUAC < 115).Children from 24 months to 59 months - M", 0);
		map.put("Children seen for the first time (MUAC < 115).Children from 24 months to 59 months - F", 0);
		map.put("Children seen for the first time (MUAC < 115).Children from 24 months to 59 months - Total", 0);
		map.put("First visit children.Children under 6 months", 1);
		map.put("First visit children.Children 6 months to 23 months", 1);
		map.put("First visit children.Children from 24 months to 59 months", 3);
		map.put("Cured.Children under 6 months", 0);
		map.put("Cured.Children 6 months to 23 months", 0);
		map.put("Cured.Children from 24 months to 59 months", 1);
		map.put("Withdrawal.Children under 6 months", 0);
		map.put("Withdrawal.Children 6 months to 23 months", 0);
		map.put("Withdrawal.Children from 24 months to 59 months", 0);
		map.put("Vitamin A.Children under 6 months - Dose 1", 0);
		map.put("Vitamin A.Children under 6 months - Dose 2", 0);
		map.put("Vitamin A.Children under 6 months - Dose 3", 0);
		map.put("Vitamin A.Children 6 months to 23 months - Dose 1", 0);
		map.put("Vitamin A.Children 6 months to 23 months - Dose 2", 1);
		map.put("Vitamin A.Children 6 months to 23 months - Dose 3", 0);
		map.put("Vitamin A.Children from 24 months to 59 months - Dose 1", 1);
		map.put("Vitamin A.Children from 24 months to 59 months - Dose 2", 1);
		map.put("Vitamin A.Children from 24 months to 59 months - Dose 3", 1);
		map.put("Albendazole.Children under 6 months - Dose 1", 0);
		map.put("Albendazole.Children under 6 months - Dose 2", 0);
		map.put("Albendazole.Children under 6 months - Dose 3", 1);
		map.put("Albendazole.Children 6 months to 23 months - Dose 1", 0);
		map.put("Albendazole.Children 6 months to 23 months - Dose 2", 1);
		map.put("Albendazole.Children 6 months to 23 months - Dose 3", 0);
		map.put("Albendazole.Children from 24 months to 59 months - Dose 1", 0);
		map.put("Albendazole.Children from 24 months to 59 months - Dose 2", 1);
		map.put("Albendazole.Children from 24 months to 59 months - Dose 3", 0);
		
		return map;
	}
}
