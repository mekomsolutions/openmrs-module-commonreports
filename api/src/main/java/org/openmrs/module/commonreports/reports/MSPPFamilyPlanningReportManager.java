package org.openmrs.module.commonreports.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openmrs.module.commonreports.ActivatedReportManager;
import org.openmrs.module.commonreports.CommonReportsConstants;
import org.openmrs.module.initializer.api.InitializerService;
import org.openmrs.module.reporting.common.MessageUtil;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.GenderCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortCrossTabDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(CommonReportsConstants.COMPONENT_REPORTMANAGER_FAMILYPLANNING)
public class MSPPFamilyPlanningReportManager extends ActivatedReportManager {
	
	@Autowired
	private InitializerService inizService;
	
	@Override
	public boolean isActivated() {
		return inizService.getBooleanFromKey("report.MSPP.familyPlanning.active", false);
	}
	
	@Override
	public String getVersion() {
		return "1.0.0-SNAPSHOT";
	}
	
	@Override
	public String getUuid() {
		return "efd7ba26-7888-45a8-9184-423833ab79d3";
	}
	
	@Override
	public String getName() {
		return MessageUtil.translate("commonreports.report.MSPP.familyPlanning.reportName");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("commonreports.report.MSPP.familyPlanning.reportDescription");
	}
	
	private Parameter getStartDateParameter() {
		return new Parameter("startDate", "Start Date", Date.class);
	}
	
	private Parameter getEndDateParameter() {
		return new Parameter("endDate", "End Date", Date.class);
	}
	
	@Override
	public List<Parameter> getParameters() {
		List<Parameter> params = new ArrayList<Parameter>();
		params.add(getStartDateParameter());
		params.add(getEndDateParameter());
		return params;
	}
	
	@Override
	public ReportDefinition constructReportDefinition() {
		
		ReportDefinition rd = new ReportDefinition();
		rd.setUuid(getUuid());
		rd.setName(getName());
		rd.setDescription(getDescription());
		rd.setParameters(getParameters());
		
		CohortCrossTabDataSetDefinition familyPlanning = new CohortCrossTabDataSetDefinition();
		familyPlanning.addParameters(getParameters());
		rd.addDataSetDefinition(getName(), Mapped.mapStraightThrough(familyPlanning));
		
		Map<String, Object> parameterMappings = new HashMap<String, Object>();
		parameterMappings.put("onOrAfter", "${startDate}");
		parameterMappings.put("onOrBefore", "${endDate}");

		int fpAdministredConceptId = inizService.getConceptFromKey("report.MSPP.familyPlanning.FPAdministred")
		        .getConceptId();
		int familyPlanningConceptId = inizService.getConceptFromKey("report.MSPP.familyPlanning.familyPlanning")
		        .getConceptId();
		int typeOfUserConceptId = inizService.getConceptFromKey("report.MSPP.familyPlanning.typeOfUser").getConceptId();
		int newConceptId = inizService.getConceptFromKey("report.MSPP.familyPlanning.new").getConceptId();
		int existentConceptId = inizService.getConceptFromKey("report.MSPP.familyPlanning.existent").getConceptId();
		int microgynonConceptId = inizService.getConceptFromKey("report.MSPP.familyPlanning.microgynon").getConceptId();
		int microlutConceptId = inizService.getConceptFromKey("report.MSPP.familyPlanning.microlut").getConceptId();
		int depoProveraInjectionConceptId = inizService.getConceptFromKey("report.MSPP.familyPlanning.depoProveraInjection")
		        .getConceptId();
		int jadelConceptId = inizService.getConceptFromKey("report.MSPP.familyPlanning.jadel").getConceptId();
		int condomConceptId = inizService.getConceptFromKey("report.MSPP.familyPlanning.condom").getConceptId();
		
		// Add rows for each method and user type combination
		// Microgynon - New Users
		familyPlanning.addRow("newMycogynonFemaleLT25", createFamilyPlanningCohort(fpAdministredConceptId,
		    microgynonConceptId, familyPlanningConceptId, typeOfUserConceptId, newConceptId, "F", 25, false, 1),
		    parameterMappings);
		familyPlanning.addRow("newMycogynonFemaleGT25", createFamilyPlanningCohort(fpAdministredConceptId,
		    microgynonConceptId, familyPlanningConceptId, typeOfUserConceptId, newConceptId, "F", 25, true, 1),
		    parameterMappings);
		familyPlanning
		        .addRow(
		            "existentMycogynonFemaleLT25", createFamilyPlanningCohort(fpAdministredConceptId, microgynonConceptId,
		                familyPlanningConceptId, typeOfUserConceptId, existentConceptId, "F", 25, false, 1),
		            parameterMappings);
		familyPlanning
		        .addRow(
		            "existentMycogynonFemaleGT25", createFamilyPlanningCohort(fpAdministredConceptId, microgynonConceptId,
		                familyPlanningConceptId, typeOfUserConceptId, existentConceptId, "F", 25, true, 1),
		            parameterMappings);
		
		// Microlut - New Users
		familyPlanning.addRow("newMicrolutFemaleLT25", createFamilyPlanningCohort(fpAdministredConceptId, microlutConceptId,
		    familyPlanningConceptId, typeOfUserConceptId, newConceptId, "F", 25, false, 1), parameterMappings);
		familyPlanning.addRow("newMicrolutFemaleGT25", createFamilyPlanningCohort(fpAdministredConceptId, microlutConceptId,
		    familyPlanningConceptId, typeOfUserConceptId, newConceptId, "F", 25, true, 1), parameterMappings);
		familyPlanning
		        .addRow(
		            "existentMicrolutFemaleLT25", createFamilyPlanningCohort(fpAdministredConceptId, microlutConceptId,
		                familyPlanningConceptId, typeOfUserConceptId, existentConceptId, "F", 25, false, 1),
		            parameterMappings);
		familyPlanning
		        .addRow(
		            "existentMicrolutFemaleGT25", createFamilyPlanningCohort(fpAdministredConceptId, microlutConceptId,
		                familyPlanningConceptId, typeOfUserConceptId, existentConceptId, "F", 25, true, 1),
		            parameterMappings);
		
		// Depo Provera - New Users (3 months interval)
		familyPlanning.addRow("newDepoFemaleLT25", createFamilyPlanningCohort(fpAdministredConceptId,
		    depoProveraInjectionConceptId, familyPlanningConceptId, typeOfUserConceptId, newConceptId, "F", 25, false, 3),
		    parameterMappings);
		familyPlanning.addRow("newDepoFemaleGT25", createFamilyPlanningCohort(fpAdministredConceptId,
		    depoProveraInjectionConceptId, familyPlanningConceptId, typeOfUserConceptId, newConceptId, "F", 25, true, 3),
		    parameterMappings);
		familyPlanning
		        .addRow("existentDepoFemaleLT25",
		            createFamilyPlanningCohort(fpAdministredConceptId, depoProveraInjectionConceptId,
		                familyPlanningConceptId, typeOfUserConceptId, existentConceptId, "F", 25, false, 3),
		            parameterMappings);
		familyPlanning
		        .addRow("existentDepoFemaleGT25",
		            createFamilyPlanningCohort(fpAdministredConceptId, depoProveraInjectionConceptId,
		                familyPlanningConceptId, typeOfUserConceptId, existentConceptId, "F", 25, true, 3),
		            parameterMappings);
		
		// Jadel - New Users (5 years interval)
		familyPlanning.addRow("newJadelFemaleLT25", createFamilyPlanningCohort(fpAdministredConceptId, jadelConceptId,
		    familyPlanningConceptId, typeOfUserConceptId, newConceptId, "F", 25, false, 60), parameterMappings);
		familyPlanning.addRow("newJadelFemaleGT25", createFamilyPlanningCohort(fpAdministredConceptId, jadelConceptId,
		    familyPlanningConceptId, typeOfUserConceptId, newConceptId, "F", 25, true, 60), parameterMappings);
		familyPlanning.addRow("existentJadelFemaleLT25", createFamilyPlanningCohort(fpAdministredConceptId, jadelConceptId,
		    familyPlanningConceptId, typeOfUserConceptId, existentConceptId, "F", 25, false, 60), parameterMappings);
		familyPlanning.addRow("existentJadelFemaleGT25", createFamilyPlanningCohort(fpAdministredConceptId, jadelConceptId,
		    familyPlanningConceptId, typeOfUserConceptId, existentConceptId, "F", 25, true, 60), parameterMappings);
		
		// Condom - New Users (no interval)
		familyPlanning.addRow("newCondomFemaleLT25", createFamilyPlanningCohort(fpAdministredConceptId, condomConceptId,
		    familyPlanningConceptId, typeOfUserConceptId, newConceptId, "F", 25, false, 0), parameterMappings);
		familyPlanning.addRow("newCondomFemaleGT25", createFamilyPlanningCohort(fpAdministredConceptId, condomConceptId,
		    familyPlanningConceptId, typeOfUserConceptId, newConceptId, "F", 25, true, 0), parameterMappings);
		familyPlanning.addRow("existentCondomFemaleLT25", createFamilyPlanningCohort(fpAdministredConceptId, condomConceptId,
		    familyPlanningConceptId, typeOfUserConceptId, existentConceptId, "F", 25, false, 0), parameterMappings);
		familyPlanning.addRow("existentCondomFemaleGT25", createFamilyPlanningCohort(fpAdministredConceptId, condomConceptId,
		    familyPlanningConceptId, typeOfUserConceptId, existentConceptId, "F", 25, true, 0), parameterMappings);
		
		// Condom - Males
		familyPlanning.addRow("newCondomMaleLT25", createFamilyPlanningCohort(fpAdministredConceptId, condomConceptId,
		    familyPlanningConceptId, typeOfUserConceptId, newConceptId, "M", 25, false, 0), parameterMappings);
		familyPlanning.addRow("newCondomMaleGT25", createFamilyPlanningCohort(fpAdministredConceptId, condomConceptId,
		    familyPlanningConceptId, typeOfUserConceptId, newConceptId, "M", 25, true, 0), parameterMappings);
		familyPlanning.addRow("existentCondomMaleLT25", createFamilyPlanningCohort(fpAdministredConceptId, condomConceptId,
		    familyPlanningConceptId, typeOfUserConceptId, existentConceptId, "M", 25, false, 0), parameterMappings);
		familyPlanning.addRow("existentCondomMaleGT25", createFamilyPlanningCohort(fpAdministredConceptId, condomConceptId,
		    familyPlanningConceptId, typeOfUserConceptId, existentConceptId, "M", 25, true, 0), parameterMappings);
		
		// Add a single column for "Total" (all patients)
		GenderCohortDefinition allGender = new GenderCohortDefinition();
		allGender.setMaleIncluded(true);
		allGender.setFemaleIncluded(true);
		familyPlanning.addColumn("Total", createCohortComposition(allGender), null);
		
		return rd;
	}
	
	/**
	 * Creates a SqlCohortDefinition for family planning based on the provided parameters
	 * 
	 * @param fpAdministredConceptId The concept ID for FP Administered
	 * @param methodConceptId The concept ID for the method (microgynon, microlut, etc.)
	 * @param familyPlanningConceptId The concept ID for family planning
	 * @param typeOfUserConceptId The concept ID for type of user
	 * @param userTypeConceptId The concept ID for new or existent user
	 * @param gender Gender filter: "F" for female, "M" for male
	 * @param ageThreshold Age threshold (e.g., 25)
	 * @param ageGreaterOrEqual true for >= ageThreshold, false for < ageThreshold
	 * @param intervalMonths Number of months to subtract from startDate (0 for no interval, 1 for 1
	 *            month, 3 for 3 months, 60 for 5 years)
	 * @return SqlCohortDefinition
	 */
	private SqlCohortDefinition createFamilyPlanningCohort(int fpAdministredConceptId, int methodConceptId,
	        int familyPlanningConceptId, int typeOfUserConceptId, int userTypeConceptId, String gender, int ageThreshold,
	        boolean ageGreaterOrEqual, int intervalMonths) {
		
		String intervalClause = "";
		if (intervalMonths > 0) {
			if (intervalMonths == 60) {
				intervalClause = "DATE_SUB(:onOrAfter, INTERVAL 5 YEAR)";
			} else {
				intervalClause = "DATE_SUB(:onOrAfter, INTERVAL " + intervalMonths + " MONTH)";
			}
		} else {
			intervalClause = ":onOrAfter";
		}
		
		String ageCondition;
		if (ageGreaterOrEqual) {
			ageCondition = "round(DATEDIFF(obs.obs_datetime, person.birthdate)/365.25, 1) >= " + ageThreshold;
		} else {
			ageCondition = "round(DATEDIFF(obs.obs_datetime, person.birthdate)/365.25, 1) < " + ageThreshold;
		}
		
		String sql = "SELECT DISTINCT obs.person_id " + "FROM obs "
		        + "INNER JOIN person ON obs.person_id = person.person_id " + "WHERE obs.voided = 0 "
		        + "AND obs.concept_id = " + fpAdministredConceptId + " AND obs.value_coded = " + methodConceptId + " "
		        + "AND obs.obs_group_id IN (" + "  SELECT obs_group_id FROM obs WHERE " + "obs.obs_group_id IN ("
		        + " SELECT obs_id FROM obs " + "WHERE obs_datetime >= " + intervalClause
		        + " AND obs_datetime <= :onOrBefore " + "AND concept_id = " + familyPlanningConceptId
		        + " ) AND person.gender = '" + gender + "' " + "AND " + ageCondition + " AND obs.concept_id = "
		        + typeOfUserConceptId + " AND obs.value_coded = " + userTypeConceptId + ")";
		
		SqlCohortDefinition cohort = new SqlCohortDefinition(sql);
		cohort.addParameter(new Parameter("onOrAfter", "On Or After", Date.class));
		cohort.addParameter(new Parameter("onOrBefore", "On Or Before", Date.class));
		
		return cohort;
	}
	
	private CompositionCohortDefinition createCohortComposition(Object... elements) {
		CompositionCohortDefinition compCD = new CompositionCohortDefinition();
		compCD.initializeFromElements(elements);
		return compCD;
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		return Arrays
		        .asList(ReportManagerUtil.createCsvReportDesign("8e300676-75d7-48f8-82eb-4fe9971459fe", reportDefinition));
	}
	
}
