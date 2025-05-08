package org.openmrs.module.commonreports.reports;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Cohort;
import org.openmrs.module.commonreports.ActivatedReportManager;
import org.openmrs.module.commonreports.CommonReportsConstants;
import org.openmrs.module.commonreports.renderer.PatientIdStickerXmlReportRenderer;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
import org.openmrs.module.reporting.report.service.ReportService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class PatientIdStickerReportManagerTest extends BaseModuleContextSensitiveTest {
	
	@Autowired
	private ReportService reportService;
	
	@Autowired
	private ReportDefinitionService reportDefinitionService;
	
	@Autowired
	@Qualifier(CommonReportsConstants.COMPONENT_REPORTMANAGER_PATIENT_ID_STICKER)
	private ActivatedReportManager reportManager;
	
	@Before
	public void setUp() throws Exception {
		executeDataSet("org/openmrs/module/reporting/include/ReportTestDataset-openmrs-2.0.xml");
		executeDataSet("org/openmrs/module/commonreports/include/patientHistoryManagerTestDataset.xml");
	}
	
	private ReportDesign setupAndReturnReportDesign() {
		ReportManagerUtil.setupReport(this.reportManager);
		
		List<ReportDefinition> reportDefinitions = this.reportDefinitionService
		        .getDefinitions(PatientIdStickerReportManager.REPORT_DEFINITION_NAME, true);
		
		Assert.assertNotNull(reportDefinitions);
		MatcherAssert.assertThat(reportDefinitions, IsCollectionWithSize.hasSize(1));
		ReportDefinition reportDefinition = reportDefinitions.get(0);
		Assert.assertNotNull(reportDefinition);
		Assert.assertEquals(PatientIdStickerReportManager.REPORT_DEFINITION_NAME, reportDefinition.getName());
		Assert.assertNotNull(reportDefinition.getDataSetDefinitions());
		MatcherAssert.assertThat(reportDefinition.getDataSetDefinitions().keySet(),
		    Matchers.contains(PatientIdStickerReportManager.DATASET_KEY_STICKER_FIELDS));
		
		List<ReportDesign> reportDesigns = this.reportService.getReportDesigns(reportDefinition,
		    PatientIdStickerXmlReportRenderer.class, false);
		Assert.assertNotNull(reportDesigns);
		MatcherAssert.assertThat(reportDesigns, IsCollectionWithSize.hasSize(1));
		
		return reportDesigns.get(0);
	}
	
	@Test
	public void setupReport_shouldSetupPatientIdSticker() throws Exception {
		ReportDesign reportDesign = setupAndReturnReportDesign();
		Assert.assertEquals(PatientIdStickerReportManager.REPORT_DESIGN_UUID, reportDesign.getUuid());
	}
	
	@Test
	public void evaluate_shouldReturnAllFields() throws Exception {
		EvaluationContext context = new EvaluationContext();
		context.setBaseCohort(new Cohort("100"));
		ReportDefinition rd = this.reportManager.constructReportDefinition();
		ReportData reportData = this.reportDefinitionService.evaluate(rd, context);
		assertNotNull(reportData.getDataSets());
		DataSet dataSet = reportData.getDataSets().get(PatientIdStickerReportManager.DATASET_KEY_STICKER_FIELDS);
		assertNotNull(dataSet);
		assertNotNull(dataSet.getMetaData());
		assertNotNull(dataSet.getMetaData().getColumns());
		MatcherAssert.assertThat(dataSet.getMetaData().getColumns(), Matchers.hasSize(7));
		
		@SuppressWarnings("unchecked")
		Matcher<Iterable<? extends Object>> containsInAnyOrder = Matchers.containsInAnyOrder(
		    Matchers.hasProperty("name", is("Patient Identifier")), Matchers.hasProperty("name", is("First Name")),
		    Matchers.hasProperty("name", is("Last Name")), Matchers.hasProperty("name", is("Date of Birth")),
		    Matchers.hasProperty("name", is("Current Age")), Matchers.hasProperty("name", is("Gender")),
		    Matchers.hasProperty("name", is("Address")));
		
		MatcherAssert.assertThat(dataSet.getMetaData().getColumns(), containsInAnyOrder);
		
		DataSetRow dataSetRow = dataSet.iterator().next();
		
		assertEquals("6TS-4MZ", getStringValue(dataSetRow, "Patient Identifier"));
		assertEquals("Collet", getStringValue(dataSetRow, "First Name"));
		assertEquals("Chebaskwony", getStringValue(dataSetRow, "Last Name"));
		assertEquals("1976-08-25 00:00:00.0", getStringValue(dataSetRow, "Date of Birth"));
		String currentAge = Integer.toString(Period.between(LocalDate.of(1976, 8, 25), LocalDate.now()).getYears());
		assertEquals(currentAge, getStringValue(dataSetRow, "Current Age"));
		assertEquals("F", getStringValue(dataSetRow, "Gender"));
		assertEquals("Kapina", getStringValue(dataSetRow, "Address"));
	}
	
	private String getStringValue(DataSetRow row, String columnName) {
		Object value = row.getColumnValue(columnName);
		String strVal = StringUtils.EMPTY;
		if (value != null)
			return strVal = value.toString();
		return strVal;
	}
}
