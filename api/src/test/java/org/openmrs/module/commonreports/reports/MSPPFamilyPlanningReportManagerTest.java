package org.openmrs.module.commonreports.reports;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
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

            // In CrossTabDataSet reports all rows and columns are in fact just columns of
            // one row
            System.out.println("Printing row values" + row.getColumnValues().toString());
            Cohort riskyPregnancy = (Cohort) row.getColumnValue("existentMicrolutFemaleLT25");
            assertNotNull(riskyPregnancy);
            assertEquals(1, riskyPregnancy.getSize());

//			Cohort prenatalIronDef = (Cohort) row
//					.getColumnValue("Iron Def and ANC Visit." + MSPPAntenatalReportManager.risksCol1);
//			assertNotNull(prenatalIronDef);
//			assertEquals(1, prenatalIronDef.getSize());
//
//			Cohort prenatalIronFolate = (Cohort) row
//					.getColumnValue("Prenatal visit + Fer Folate Co." + MSPPAntenatalReportManager.risksCol1);
//			assertNotNull(prenatalIronFolate);
//			assertEquals(1, prenatalIronFolate.getSize());
//
//			Cohort prenatalIronTreatment = (Cohort) row
//					.getColumnValue("Prenatal visit + treated for Fe def." + MSPPAntenatalReportManager.risksCol1);
//			assertNotNull(prenatalIronTreatment);
//			assertEquals(1, prenatalIronTreatment.getSize());
//
//			Cohort motherWithBirthPlan = (Cohort) row
//					.getColumnValue("Mothers with birth plan." + MSPPAntenatalReportManager.risksCol1);
//			assertNotNull(motherWithBirthPlan);
//			assertEquals(1, motherWithBirthPlan.getSize());
//
//			Cohort prenatalMalariaChloroquine = (Cohort) row.getColumnValue(
//					"Prenatal visit + malaria test positive + Chloroqine co." + MSPPAntenatalReportManager.risksCol1);
//			assertNotNull(prenatalMalariaChloroquine);
//			assertEquals(1, prenatalMalariaChloroquine.getSize());
//
//			Cohort prenatalMUAC = (Cohort) row
//					.getColumnValue("Prenatal + MUAC =<21cm." + MSPPAntenatalReportManager.risksCol1);
//			assertNotNull(prenatalMUAC);
//			assertEquals(1, prenatalMUAC.getSize());
//
//			Cohort otherWomenIronFolate = (Cohort) row
//					.getColumnValue("Women + fer folate co prescribed." + MSPPAntenatalReportManager.risksCol1);
//			assertNotNull(otherWomenIronFolate);
//			assertEquals(1, otherWomenIronFolate.getSize());
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
