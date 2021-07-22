package org.openmrs.module.commonreports.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openmrs.Concept;
import org.openmrs.VisitType;
import org.openmrs.api.ConceptService;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.openmrs.module.commonreports.ActivatedReportManager;
import org.openmrs.module.commonreports.CommonReportsConstants;
import org.openmrs.module.initializer.api.InitializerService;
import org.openmrs.module.reporting.cohort.definition.CodedObsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.CompositionCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.GenderCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.NumericObsCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.cohort.definition.VisitCohortDefinition;
import org.openmrs.module.reporting.common.MessageUtil;
import org.openmrs.module.reporting.common.RangeComparator;
import org.openmrs.module.reporting.common.SetComparator;
import org.openmrs.module.reporting.dataset.definition.CohortCrossTabDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(CommonReportsConstants.COMPONENT_REPORTMANAGER_ANTENATAL)
public class AntenatalReportManager extends ActivatedReportManager {
	
	@Autowired
	private InitializerService inizService;
	
	@Override
	public boolean isActivated() {
		return inizService.getBooleanFromKey("report.antenatal.active", false);
	}
	
	@Override
	public String getVersion() {
		return "1.0.0-SNAPSHOT";
	}
	
	@Override
	public String getUuid() {
		return "b6b2a422-38d1-4fb1-acfc-46eb842bcf7f";
	}
	
	@Override
	public String getName() {
		return MessageUtil.translate("commonreports.report.antenatal.reportName");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("commonreports.report.antenatal.reportDescription");
	}
	
	private Parameter getStartDateParameter() {
		return new Parameter("startDate", "Start Date", Date.class);
	}
	
	private Parameter getEndDateParameter() {
		return new Parameter("endDate", "End Date", Date.class);
	}
	
	public String getGestationName() {
		return MessageUtil.translate("commonreports.report.antenatalGestation.reportName");
	}
	
	public String getRisksName() {
		return MessageUtil.translate("commonreports.report.antenatalRisks.reportName");
	}
	
	public static String col1 = "";
	
	public static String col2 = "";
	
	public static String col3 = "";
	
	public static String col4 = "";
	
	public static String col5 = "";
	
	public static String col6 = "";
	
	public static String risksCol1 = "";
	
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
		
		// Antenal general
		CohortCrossTabDataSetDefinition antenatalGestation = new CohortCrossTabDataSetDefinition();
		antenatalGestation.addParameters(getParameters());
		rd.addDataSetDefinition(getGestationName(), Mapped.mapStraightThrough(antenatalGestation));
		
		//Risks
		CohortCrossTabDataSetDefinition antenatalRisks = new CohortCrossTabDataSetDefinition();
		antenatalRisks.addParameters(getParameters());
		rd.addDataSetDefinition(getRisksName(), Mapped.mapStraightThrough(antenatalRisks));
		
		Map<String, Object> parameterMappings = new HashMap<String, Object>();
		parameterMappings.put("onOrAfter", "${startDate}");
		parameterMappings.put("onOrBefore", "${endDate}");
		
		String[] gestationDuration = inizService.getValueFromKey("report.antenatal.gestationDuration").split(",");
		for (String member : gestationDuration) {
			
			if (member.equals("Total")) {
				CodedObsCohortDefinition sqd = new CodedObsCohortDefinition();
				sqd.addParameter(new Parameter("onOrAfter", "On Or After", Date.class));
				sqd.addParameter(new Parameter("onOrBefore", "On Or Before", Date.class));
				sqd.setOperator(SetComparator.IN);
				sqd.setGroupingConcept(inizService.getConceptFromKey("report.antenatal.estimatedGestationalAge"));
				sqd.setQuestion(inizService.getConceptFromKey("report.antenatal.numberOfWeeks"));
				
				antenatalGestation.addRow(member, sqd, parameterMappings);
			} else {
				
				String[] bit = member.split("-");
				String firstNumber = bit[0];
				String lastNumber = bit[1];
				
				NumericObsCohortDefinition sqd = new NumericObsCohortDefinition();
				sqd.setGroupingConcept(inizService.getConceptFromKey("report.antenatal.estimatedGestationalAge"));
				sqd.setQuestion(inizService.getConceptFromKey("report.antenatal.numberOfWeeks"));
				sqd.addParameter(new Parameter("onOrAfter", "On Or After", Date.class));
				sqd.addParameter(new Parameter("onOrBefore", "On Or Before", Date.class));
				sqd.setValue1(Double.parseDouble(firstNumber));
				sqd.setValue2(Double.parseDouble(lastNumber));
				sqd.setOperator1(RangeComparator.GREATER_EQUAL);
				sqd.setOperator2(RangeComparator.LESS_EQUAL);
				
				antenatalGestation.addRow(
				    member + " " + MessageUtil.translate("commonreports.report.antenatalGestation.gestationWeeks"), sqd,
				    parameterMappings);
				
			}
			
		}
		
		//Risks related
		GenderCohortDefinition female = new GenderCohortDefinition();
		female.setFemaleIncluded(true);
		
		// Risky Pregnancies
		CodedObsCohortDefinition riskyPregnancy = new CodedObsCohortDefinition();
		riskyPregnancy.addParameter(new Parameter("onOrAfter", "On Or After", Date.class));
		riskyPregnancy.addParameter(new Parameter("onOrBefore", "On Or Before", Date.class));
		riskyPregnancy.setOperator(SetComparator.IN);
		
		riskyPregnancy.setQuestion(inizService.getConceptFromKey("report.antenatal.riskyPregnancy"));
		riskyPregnancy.setValueList(Arrays.asList(inizService.getConceptFromKey("report.antenatal.yes")));
		antenatalRisks.addRow(MessageUtil.translate("commonreports.report.antenatalRisks.riskyPregnancy"), riskyPregnancy,
		    parameterMappings);
		
		// Iron def ANC visit Pregnancies
		CodedObsCohortDefinition anemia = new CodedObsCohortDefinition();
		anemia.addParameter(new Parameter("onOrAfter", "On Or After", Date.class));
		anemia.addParameter(new Parameter("onOrBefore", "On Or Before", Date.class));
		anemia.setOperator(SetComparator.IN);
		anemia.setQuestion(inizService.getConceptFromKey("report.antenatal.codedDiagnosis"));
		anemia.setValueList(Arrays.asList(inizService.getConceptFromKey("report.antenatal.anemiaIronDeficiency")));
		
		VisitCohortDefinition _prenatal = new VisitCohortDefinition();
		_prenatal.setVisitTypeList(Arrays.asList(Context.getVisitService()
		        .getVisitTypeByUuid(inizService.getValueFromKey("report.antenatal.prenatalVisitType"))));
		CompositionCohortDefinition ccd = new CompositionCohortDefinition();
		ccd.initializeFromElements(_prenatal, anemia, female);
		antenatalRisks.addRow(MessageUtil.translate("commonreports.report.antenatalRisks.ironDefANC"), ccd,
		    parameterMappings);
		
		// Prenatal visit + Fer Folate Co prescribed 
		SqlCohortDefinition sqd = new SqlCohortDefinition(
		        "select patient_id from orders where concept_id=(select DISTINCT concept_id from concept where uuid = '"
		                + inizService.getValueFromKey("report.antenatal.ferrousFolate")
		                + "' and retired = 0) and order_type_id =(select order_type_id from order_type where uuid = '"
		                + inizService.getValueFromKey("report.antenatal.drugOrder") + "')");
		sqd.addParameter(new Parameter("onOrAfter", "On Or After", Date.class));
		sqd.addParameter(new Parameter("onOrBefore", "On Or Before", Date.class));
		CompositionCohortDefinition ccd1 = new CompositionCohortDefinition();
		ccd1.initializeFromElements(_prenatal, sqd, female);
		antenatalRisks.addRow(MessageUtil.translate("commonreports.report.antenatalRisks.prenatalIron"), ccd1,
		    parameterMappings);
		
		//Prenatal visit treated for Fe def (Same as above--> Prenatal visit + Fer Folate Co prescribed)
		antenatalRisks.addRow(MessageUtil.translate("commonreports.report.antenatalRisks.prenatalIronDef"), ccd1,
		    parameterMappings);
		
		//Mothers with a birth plan
		NumericObsCohortDefinition scd = new NumericObsCohortDefinition();
		scd.setGroupingConcept(inizService.getConceptFromKey("report.antenatal.estimatedGestationalAge"));
		scd.setQuestion(inizService.getConceptFromKey("report.antenatal.numberOfWeeks"));
		scd.addParameter(new Parameter("onOrAfter", "On Or After", Date.class));
		scd.addParameter(new Parameter("onOrBefore", "On Or Before", Date.class));
		
		antenatalRisks.addRow(MessageUtil.translate("commonreports.report.antenatalRisks.motherBirthPlan"), scd,
		    parameterMappings);
		
		//Prenatal visit + malaria test positive + chloroqine co prescribed 
		CodedObsCohortDefinition malaria = new CodedObsCohortDefinition();
		malaria.addParameter(new Parameter("onOrAfter", "On Or After", Date.class));
		malaria.addParameter(new Parameter("onOrBefore", "On Or Before", Date.class));
		malaria.setOperator(SetComparator.IN);
		
		malaria.setQuestion(inizService.getConceptFromKey("report.antenatal.malaria"));
		
		List<Concept> malariaPositiveConceptSet = new ArrayList<Concept>();
		malariaPositiveConceptSet.add(inizService.getConceptFromKey("report.antenatal.positive"));
		malariaPositiveConceptSet.add(inizService.getConceptFromKey("report.antenatal.onePlus"));
		malariaPositiveConceptSet.add(inizService.getConceptFromKey("report.antenatal.twoPlus"));
		malariaPositiveConceptSet.add(inizService.getConceptFromKey("report.antenatal.threePlus"));
		malariaPositiveConceptSet.add(inizService.getConceptFromKey("report.antenatal.fourPlus"));
		
		malaria.setValueList(malariaPositiveConceptSet);
		
		SqlCohortDefinition sql = new SqlCohortDefinition(
		        "select patient_id from orders where concept_id=(select DISTINCT concept_id from concept where uuid = '"
		                + inizService.getValueFromKey("report.antenatal.chloroquine")
		                + "' and retired = 0) and order_type_id =(select order_type_id from order_type where uuid = '"
		                + inizService.getValueFromKey("report.antenatal.drugOrder") + "')");
		sql.addParameter(new Parameter("onOrAfter", "On Or After", Date.class));
		sql.addParameter(new Parameter("onOrBefore", "On Or Before", Date.class));
		CompositionCohortDefinition ccd2 = new CompositionCohortDefinition();
		ccd2.initializeFromElements(_prenatal, sql, malaria, female);
		antenatalRisks.addRow(
		    MessageUtil.translate("commonreports.report.antenatalRisks.prenatalMalariaPositiveChloroquine"), ccd2,
		    parameterMappings);
		
		//Prental + MUAC =<21cm
		NumericObsCohortDefinition muac = new NumericObsCohortDefinition();
		muac.addParameter(new Parameter("onOrAfter", "On Or After", Date.class));
		muac.addParameter(new Parameter("onOrBefore", "On Or Before", Date.class));
		muac.setQuestion(inizService.getConceptFromKey("report.antenatal.midUpperArmCircumference"));
		muac.setOperator1(RangeComparator.GREATER_EQUAL);
		muac.setValue1(0.0);
		muac.setOperator2(RangeComparator.LESS_EQUAL);
		muac.setValue2(210.0);
		CompositionCohortDefinition ccd3 = new CompositionCohortDefinition();
		ccd3.initializeFromElements(_prenatal, muac, female);
		antenatalRisks.addRow(MessageUtil.translate("commonreports.report.antenatalRisks.prenatalMUAC=<21cm"), ccd3,
		    parameterMappings);
		
		//Women + fer folate co prescribed 
		VisitService vs = Context.getVisitService();
		List<VisitType> lVT = new ArrayList<VisitType>();
		String[] otherVisitTypes = inizService.getValueFromKey("report.antenatal.otherVisitTypes").split(",");
		
		for (String member : otherVisitTypes) {
			lVT.add(vs.getVisitTypeByUuid(member));
		}
		
		VisitCohortDefinition _other = new VisitCohortDefinition();
		_other.setVisitTypeList(lVT);
		
		SqlCohortDefinition sqdc = new SqlCohortDefinition(
		        "select patient_id from orders where concept_id=(select DISTINCT concept_id from concept where uuid = '"
		                + inizService.getValueFromKey("report.antenatal.ferrousFolate")
		                + "' and retired = 0) and order_type_id =(select order_type_id from order_type where uuid = '"
		                + inizService.getValueFromKey("report.antenatal.drugOrder") + "')");
		sqdc.addParameter(new Parameter("onOrAfter", "On Or After", Date.class));
		sqdc.addParameter(new Parameter("onOrBefore", "On Or Before", Date.class));
		CompositionCohortDefinition ccd5 = new CompositionCohortDefinition();
		ccd5.initializeFromElements(_other, sqdc, female);
		antenatalRisks.addRow(MessageUtil.translate("commonreports.report.antenatalRisks.womenIron"), ccd5,
		    parameterMappings);
		
		setColumnNames();
		
		// First Visit To column
		CodedObsCohortDefinition firstVisit = new CodedObsCohortDefinition();
		firstVisit.addParameter(new Parameter("onOrAfter", "On Or After", Date.class));
		firstVisit.addParameter(new Parameter("onOrBefore", "On Or Before", Date.class));
		firstVisit.setOperator(SetComparator.IN);
		
		firstVisit.setQuestion(inizService.getConceptFromKey("report.antenatal.visitNumber"));
		firstVisit.addValue(inizService.getConceptFromKey("report.antenatal.one"));
		antenatalGestation.addColumn(col1, createCohortComposition(firstVisit), null);
		
		// Second Visit column
		CodedObsCohortDefinition secondVisit = new CodedObsCohortDefinition();
		secondVisit.addParameter(new Parameter("onOrAfter", "On Or After", Date.class));
		secondVisit.addParameter(new Parameter("onOrBefore", "On Or Before", Date.class));
		secondVisit.setOperator(SetComparator.IN);
		secondVisit.setQuestion(inizService.getConceptFromKey("report.antenatal.visitNumber"));
		secondVisit.addValue(inizService.getConceptFromKey("report.antenatal.two"));
		antenatalGestation.addColumn(col2, createCohortComposition(secondVisit), null);
		
		// Third Visit column
		CodedObsCohortDefinition thirdVisit = new CodedObsCohortDefinition();
		thirdVisit.addParameter(new Parameter("onOrAfter", "On Or After", Date.class));
		thirdVisit.addParameter(new Parameter("onOrBefore", "On Or Before", Date.class));
		thirdVisit.setOperator(SetComparator.IN);
		thirdVisit.setQuestion(inizService.getConceptFromKey("report.antenatal.visitNumber"));
		thirdVisit.addValue(inizService.getConceptFromKey("report.antenatal.three"));
		antenatalGestation.addColumn(col3, createCohortComposition(thirdVisit), null);
		
		// Fourth Visit column
		CodedObsCohortDefinition fourthVisit = new CodedObsCohortDefinition();
		fourthVisit.addParameter(new Parameter("onOrAfter", "On Or After", Date.class));
		fourthVisit.addParameter(new Parameter("onOrBefore", "On Or Before", Date.class));
		fourthVisit.setOperator(SetComparator.IN);
		fourthVisit.setQuestion(inizService.getConceptFromKey("report.antenatal.visitNumber"));
		fourthVisit.addValue(inizService.getConceptFromKey("report.antenatal.four"));
		antenatalGestation.addColumn(col4, createCohortComposition(fourthVisit), null);
		
		// Fifth Visit column
		CodedObsCohortDefinition fifthVisit = new CodedObsCohortDefinition();
		fifthVisit.addParameter(new Parameter("onOrAfter", "On Or After", Date.class));
		fifthVisit.addParameter(new Parameter("onOrBefore", "On Or Before", Date.class));
		fifthVisit.setOperator(SetComparator.IN);
		fifthVisit.setQuestion(inizService.getConceptFromKey("report.antenatal.visitNumber"));
		fifthVisit.addValue(inizService.getConceptFromKey("report.antenatal.fivePlus"));
		antenatalGestation.addColumn(col5, createCohortComposition(fifthVisit), null);
		
		// Total Visit column
		CodedObsCohortDefinition totalVisit = new CodedObsCohortDefinition();
		antenatalGestation.addColumn(col6, createCohortComposition(totalVisit), null);
		
		//Risks related
		// All column
		GenderCohortDefinition allGender = new GenderCohortDefinition();
		allGender.setMaleIncluded(true);
		allGender.setFemaleIncluded(true);
		antenatalRisks.addColumn(risksCol1, createCohortComposition(allGender), null);
		
		return rd;
	}
	
	private void setColumnNames() {
		
		col1 = MessageUtil.translate("commonreports.report.antenatalGestation.firstVisit.label");
		col2 = MessageUtil.translate("commonreports.report.antenatalGestation.secondVisit.label");
		col3 = MessageUtil.translate("commonreports.report.antenatalGestation.thirdVisit.label");
		col4 = MessageUtil.translate("commonreports.report.antenatalGestation.fourthVisit.label");
		col5 = MessageUtil.translate("commonreports.report.antenatalGestation.fifthVisit.label");
		col6 = MessageUtil.translate("commonreports.report.antenatalGestation.total.label");
		risksCol1 = MessageUtil.translate("commonreports.report.antenatalRisks.all.label");
		
	}
	
	private CompositionCohortDefinition createCohortComposition(Object... elements) {
		CompositionCohortDefinition compCD = new CompositionCohortDefinition();
		compCD.initializeFromElements(elements);
		return compCD;
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		return Arrays
		        .asList(ReportManagerUtil.createCsvReportDesign("a0ebe3c1-d6a3-4761-bff0-6a03626d2c75", reportDefinition));
	}
}
