package org.openmrs.module.mksreports.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.mksreports.MKSReportManager;
import org.openmrs.module.mksreports.MKSReportsConstants;
import org.openmrs.module.mksreports.data.converter.ConceptDataTypeConverter;
import org.openmrs.module.mksreports.data.converter.ConceptNameConverter;
import org.openmrs.module.mksreports.data.converter.EncounterProviderFromIdConverter;
import org.openmrs.module.mksreports.data.converter.EncounterTypeUUIDFromEncounterIdConverter;
import org.openmrs.module.mksreports.data.converter.ObsProviderFromIdConverter;
import org.openmrs.module.mksreports.data.converter.ObsValueFromIdConverter;
import org.openmrs.module.mksreports.data.converter.VisitLocationFromIdConverter;
import org.openmrs.module.mksreports.data.converter.VisitTypeFromIdConverter;
import org.openmrs.module.mksreports.data.converter.VisitUUIDFromIdConverter;
import org.openmrs.module.mksreports.data.obs.definition.ObsDatetimeDataDefinition;
import org.openmrs.module.mksreports.library.BasePatientDataLibrary;
import org.openmrs.module.mksreports.library.EncounterDataLibrary;
import org.openmrs.module.mksreports.library.ObsDataLibrary;
import org.openmrs.module.mksreports.renderer.PatientHistoryXmlReportRenderer;
import org.openmrs.module.reporting.common.SortCriteria;
import org.openmrs.module.reporting.data.converter.DateConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDatetimeDataDefinition;
import org.openmrs.module.reporting.data.encounter.definition.EncounterIdDataDefinition;
import org.openmrs.module.reporting.data.encounter.definition.EncounterTypeDataDefinition;
import org.openmrs.module.reporting.data.obs.definition.ObsIdDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientDataDefinition;
import org.openmrs.module.reporting.data.patient.library.BuiltInPatientDataLibrary;
import org.openmrs.module.reporting.dataset.definition.EncounterDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.ObsDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(MKSReportsConstants.COMPONENT_REPORTMANAGER_PATIENTHISTORY)
public class PatientHistoryReportManager extends MKSReportManager {
	
	public final static String REPORT_DESIGN_NAME = "mksPatientHistory.xml_";
	
	protected final static String REPORT_DEFINITION_NAME = "Patient History";
	
	public final static String DATASET_KEY_DEMOGRAPHICS = "demographics";
	
	public final static String DATASET_KEY_OBS = "obs";
	
	public final static String DATASET_KEY_ENCOUNTERS = "encounters";
	
	public final static String VISIT_UUID_LABEL = "visit_uuid";
	
	public final static String VISIT_LOCATION_LABEL = "visit_location";
	
	public final static String VISIT_TYPE_LABEL = "visit_type";
	
	public final static String ENCOUNTER_UUID_LABEL = "encounter_uuid";
	
	public final static String ENCOUNTER_PROVIDER_LABEL = "provider_name";
	
	public final static String ENCOUNTER_TYPE_UUID_LABEL = "encounter_type_uuid";
	
	public final static String ENCOUNTERTYPE_NAME_LABEL = "encounter_type_name";
	
	public final static String ENCOUNTER_DATETIME_LABEL = "encounter_datetime";
	
	public final static String OBS_VALUE_LABEL = "obs_value";
	
	public final static String OBS_DATETIME_LABEL = "obs_datetime";
	
	public final static String OBS_DATATYPE_LABEL = "concept_datatype";
	
	public final static String OBS_NAME_LABEL = "concept_name";
	
	public final static String OBS_PROVIDER_LABEL = "provider_name";
	
	public final static String OBS_ID_LABEL = "obs_id";
	
	@Autowired
	private EncounterDataLibrary encounterDataLibrary;
	
	@Autowired
	private BuiltInPatientDataLibrary builtInPatientDataLibrary;
	
	@Autowired
	private ObsDataLibrary obsDataLibrary;
	
	@Autowired
	BasePatientDataLibrary basePatientDataLibrary;
	
	@Override
	public boolean isActive() {
		return super.isActive();
	}
	
	@Override
	public String getVersion() {
		return "1.1.0-SNAPSHOT";
	}
	
	@Override
	public String getUuid() {
		return "ae2152c1-1a91-4dc9-96d2-dfc38820648e";
	}
	
	@Override
	public String getName() {
		return "HC1 Patient History";
	}
	
	@Override
	public String getDescription() {
		return StringUtils.EMPTY;
	}
	
	private Parameter getLocationParameter() {
		return new Parameter("locationList", "Visit Location", Location.class, List.class, null);
	}
	
	@Override
	public List<Parameter> getParameters() {
		List<Parameter> params = new ArrayList<Parameter>();
		params.add(getLocationParameter());
		return params;
	}
	
	@Override
	public ReportDefinition constructReportDefinition() {
		ReportDefinition reportDef = new ReportDefinition();
		reportDef.setUuid(this.getUuid());
		reportDef.setName(REPORT_DEFINITION_NAME);
		reportDef.setDescription(this.getDescription());
		
		Map<String, Object> mappings = new HashMap<String, Object>();
		MessageSourceService i18nTranslator = Context.getMessageSourceService();
		
		// Create dataset definitions
		PatientDataSetDefinition patientDataSetDef = createDemographicsDataSetDefinition(i18nTranslator);
		ObsDataSetDefinition obsDataSetDef = createObsAndEncounterDataSetDefinition();
		EncounterDataSetDefinition encountersDatasetSetDef = createEncounterAndVisitDataSetDefinition();
		
		// Add datasets to the report
		reportDef.addDataSetDefinition(DATASET_KEY_DEMOGRAPHICS, patientDataSetDef, mappings);
		reportDef.addDataSetDefinition(DATASET_KEY_OBS, obsDataSetDef, mappings);
		reportDef.addDataSetDefinition(DATASET_KEY_ENCOUNTERS, encountersDatasetSetDef, new HashMap<String, Object>());
		
		return reportDef;
	}
	
	private EncounterDataSetDefinition createEncounterAndVisitDataSetDefinition() {
		EncounterDataSetDefinition encounterAndVistDatasetSetDef = new EncounterDataSetDefinition();
		encounterAndVistDatasetSetDef.addColumn(VISIT_UUID_LABEL, encounterDataLibrary.getVisitId(), StringUtils.EMPTY,
		    new VisitUUIDFromIdConverter());
		encounterAndVistDatasetSetDef.addColumn(VISIT_LOCATION_LABEL, encounterDataLibrary.getVisitId(), StringUtils.EMPTY,
		    new VisitLocationFromIdConverter());
		encounterAndVistDatasetSetDef.addColumn(VISIT_TYPE_LABEL, encounterDataLibrary.getVisitId(), StringUtils.EMPTY,
		    new VisitTypeFromIdConverter());
		encounterAndVistDatasetSetDef.addColumn(ENCOUNTER_UUID_LABEL, encounterDataLibrary.getUUID(), StringUtils.EMPTY,
		    new ObjectFormatter());
		encounterAndVistDatasetSetDef.addColumn(ENCOUNTER_PROVIDER_LABEL, new EncounterIdDataDefinition(), StringUtils.EMPTY,
		    new EncounterProviderFromIdConverter());
		encounterAndVistDatasetSetDef.addColumn(ENCOUNTER_TYPE_UUID_LABEL, new EncounterIdDataDefinition(),
		    StringUtils.EMPTY, new EncounterTypeUUIDFromEncounterIdConverter());
		encounterAndVistDatasetSetDef.addColumn(ENCOUNTERTYPE_NAME_LABEL, new EncounterTypeDataDefinition(),
		    StringUtils.EMPTY, new ObjectFormatter());
		encounterAndVistDatasetSetDef.addColumn(ENCOUNTER_DATETIME_LABEL, new EncounterDatetimeDataDefinition(),
		    StringUtils.EMPTY, new DateConverter());
		encounterAndVistDatasetSetDef.addSortCriteria(ENCOUNTER_DATETIME_LABEL, SortCriteria.SortDirection.DESC);
		return encounterAndVistDatasetSetDef;
	}
	
	/**
	 * @param i18nTranslator
	 * @return
	 */
	private PatientDataSetDefinition createDemographicsDataSetDefinition(MessageSourceService i18nTranslator) {
		PatientDataSetDefinition patientDataSetDef = new PatientDataSetDefinition();
		addColumn(patientDataSetDef, i18nTranslator.getMessage("mksreports.patienthistory.demographics.identifier"),
		    builtInPatientDataLibrary.getPreferredIdentifierIdentifier());
		addColumn(patientDataSetDef, i18nTranslator.getMessage("mksreports.patienthistory.demographics.firstname"),
		    builtInPatientDataLibrary.getPreferredGivenName());
		addColumn(patientDataSetDef, i18nTranslator.getMessage("mksreports.patienthistory.demographics.lastname"),
		    builtInPatientDataLibrary.getPreferredFamilyName());
		addColumn(patientDataSetDef, i18nTranslator.getMessage("mksreports.patienthistory.demographics.dob"),
		    basePatientDataLibrary.getBirthdate());
		addColumn(patientDataSetDef, i18nTranslator.getMessage("mksreports.patienthistory.demographics.age"),
		    basePatientDataLibrary.getAgeAtEndInYears());
		addColumn(patientDataSetDef, i18nTranslator.getMessage("mksreports.patienthistory.demographics.gender"),
		    builtInPatientDataLibrary.getGender());
		addColumn(patientDataSetDef, i18nTranslator.getMessage("mksreports.patienthistory.demographics.fulladdress"),
		    basePatientDataLibrary.getAddressFull());
		
		return patientDataSetDef;
	}
	
	/**
	 * @return
	 */
	private ObsDataSetDefinition createObsAndEncounterDataSetDefinition() {
		ObsDataSetDefinition obsDataSetDef = new ObsDataSetDefinition();
		obsDataSetDef.addColumn(ENCOUNTER_UUID_LABEL, encounterDataLibrary.getUUID(), StringUtils.EMPTY,
		    new ObjectFormatter());
		obsDataSetDef.addColumn(OBS_PROVIDER_LABEL, new ObsIdDataDefinition(), StringUtils.EMPTY,
		    new ObsProviderFromIdConverter());
		obsDataSetDef.addColumn(OBS_DATETIME_LABEL, new ObsDatetimeDataDefinition(), StringUtils.EMPTY, new DateConverter());
		obsDataSetDef.addColumn(OBS_DATATYPE_LABEL, obsDataLibrary.getConceptId(), StringUtils.EMPTY,
		    new ConceptDataTypeConverter());
		obsDataSetDef.addColumn(OBS_NAME_LABEL, obsDataLibrary.getConceptId(), StringUtils.EMPTY,
		    new ConceptNameConverter());
		obsDataSetDef.addColumn(OBS_ID_LABEL, new ObsIdDataDefinition(), null, null);
		obsDataSetDef.addColumn(OBS_VALUE_LABEL, new ObsIdDataDefinition(), StringUtils.EMPTY,
		    new ObsValueFromIdConverter());
		obsDataSetDef.addSortCriteria(OBS_DATETIME_LABEL, SortCriteria.SortDirection.DESC);
		return obsDataSetDef;
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		ReportDesign reportDesign = new ReportDesign();
		reportDesign.setName(REPORT_DESIGN_NAME);
		reportDesign.setReportDefinition(reportDefinition);
		reportDesign.setRendererType(PatientHistoryXmlReportRenderer.class);
		return Arrays.asList(reportDesign);
	}
	
	private void addColumn(PatientDataSetDefinition dsd, String columnName, PatientDataDefinition pdd) {
		dsd.addColumn(columnName, pdd, Mapped.straightThroughMappings(pdd));
	}
}
