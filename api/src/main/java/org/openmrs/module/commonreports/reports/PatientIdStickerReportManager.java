package org.openmrs.module.commonreports.reports;

import static org.openmrs.module.commonreports.common.Helper.getStringFromResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.module.commonreports.ActivatedReportManager;
import org.openmrs.module.commonreports.CommonReportsConstants;
import org.openmrs.module.commonreports.library.BasePatientDataLibrary;
import org.openmrs.module.commonreports.renderer.PatientIdStickerXmlReportRenderer;
import org.openmrs.module.initializer.api.InitializerService;
import org.openmrs.module.reporting.common.MessageUtil;
import org.openmrs.module.reporting.data.patient.definition.PatientDataDefinition;
import org.openmrs.module.reporting.data.patient.library.BuiltInPatientDataLibrary;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(CommonReportsConstants.COMPONENT_REPORTMANAGER_PATIENT_ID_STICKER)
public class PatientIdStickerReportManager extends ActivatedReportManager {
	
	public static final String REPORT_DESIGN_UUID = "f0f27c39-2b3a-4254-b09f-29dad8adbc7b";
	
	public static final String REPORT_DEFINITION_NAME = "Patient Identifier Sticker";
	
	public static final String DATASET_KEY_STICKER_FIELDS = "fields";
	
	@Autowired
	private BuiltInPatientDataLibrary builtInPatientDataLibrary;
	
	@Autowired
	BasePatientDataLibrary basePatientDataLibrary;
	
	@Autowired
	private InitializerService initializerService;
	
	@Override
	public boolean isActivated() {
		return super.isActivated();
	}
	
	@Override
	public String getVersion() {
		return "1.1.0-SNAPSHOT";
	}
	
	@Override
	public String getUuid() {
		return "08e2d4eb-91f7-4067-a0c9-025af2122686";
	}
	
	@Override
	public String getName() {
		return REPORT_DEFINITION_NAME;
	}
	
	@Override
	public String getDescription() {
		return StringUtils.EMPTY;
	}
	
	private Parameter getPatientParameter() {
		return new Parameter("patientNameOrID", "Patient Name or ID", String.class, null, null);
	}
	
	@Override
	public List<Parameter> getParameters() {
		List<Parameter> params = new ArrayList<Parameter>();
		params.add(getPatientParameter());
		return params;
	}
	
	@Override
	public ReportDefinition constructReportDefinition() {
		ReportDefinition reportDef = new ReportDefinition();
		reportDef.setUuid(this.getUuid());
		reportDef.setName(REPORT_DEFINITION_NAME);
		reportDef.setDescription(this.getDescription());
		reportDef.setParameters(getParameters());
		
		// Add SQL dataset definition
		SqlDataSetDefinition sqlDsd = createSqlDataSetDefinition();
		Map<String, Object> parameterMappings = new HashMap<>();
		parameterMappings.put("patientNameOrID", "${patientNameOrID}");
		reportDef.addDataSetDefinition(getName(), sqlDsd, parameterMappings);
		
		// Add patient dataset definition
		PatientDataSetDefinition patientDataSetDef = createStickerFieldsDataSetDefinition();
		reportDef.addDataSetDefinition(DATASET_KEY_STICKER_FIELDS, patientDataSetDef, new HashMap<>());
		
		return reportDef;
	}
	
	private SqlDataSetDefinition createSqlDataSetDefinition() {
		SqlDataSetDefinition sqlDsd = new SqlDataSetDefinition();
		sqlDsd.setName(MessageUtil.translate("commonreports.report.patientIdSticker.datasetName"));
		sqlDsd.setDescription(MessageUtil.translate("commonreports.report.patientIdSticker.datasetDescription"));
		
		String sql = getStringFromResource("org/openmrs/module/commonreports/sql/searchPatient.sql");
		sqlDsd.setSqlQuery(sql);
		sqlDsd.addParameters(getParameters());
		
		return sqlDsd;
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		ReportDesign reportDesign = new ReportDesign();
		reportDesign.setName("Patient ID Sticker PDF");
		reportDesign.setUuid(REPORT_DESIGN_UUID);
		reportDesign.setReportDefinition(reportDefinition);
		reportDesign.setRendererType(PatientIdStickerXmlReportRenderer.class);
		return Arrays.asList(reportDesign);
	}
	
	/**
	 * Creates a patient sticker fields dataset definition with configurable columns.
	 * 
	 * @return the configured PatientDataSetDefinition
	 */
	private PatientDataSetDefinition createStickerFieldsDataSetDefinition() {
		PatientDataSetDefinition patientDataSetDef = new PatientDataSetDefinition();
		
		// Map of column definitions with their corresponding data definitions
		Map<String, PatientDataDefinition> columnDefinitions = createColumnDefinitions();
		
		// Add columns that should be included based on configuration
		for (Map.Entry<String, PatientDataDefinition> entry : columnDefinitions.entrySet()) {
			if (shouldIncludeColumn(entry.getKey())) {
				addColumn(patientDataSetDef, entry.getKey(), entry.getValue());
			}
		}
		
		return patientDataSetDef;
	}
	
	private Map<String, PatientDataDefinition> createColumnDefinitions() {
		Map<String, PatientDataDefinition> columnDefinitions = new LinkedHashMap<>();
		columnDefinitions.put("commonreports.patientIdSticker.fields.identifier",
		    builtInPatientDataLibrary.getPreferredIdentifierIdentifier());
		columnDefinitions.put("commonreports.patientIdSticker.fields.firstname",
		    builtInPatientDataLibrary.getPreferredGivenName());
		columnDefinitions.put("commonreports.patientIdSticker.fields.lastname",
		    builtInPatientDataLibrary.getPreferredFamilyName());
		columnDefinitions.put("commonreports.patientIdSticker.fields.dob", basePatientDataLibrary.getBirthdate());
		columnDefinitions.put("commonreports.patientIdSticker.fields.age", basePatientDataLibrary.getAgeAtEndInYears());
		columnDefinitions.put("commonreports.patientIdSticker.fields.gender", builtInPatientDataLibrary.getGender());
		columnDefinitions.put("commonreports.patientIdSticker.fields.fulladdress", basePatientDataLibrary.getAddressFull());
		return columnDefinitions;
	}
	
	/**
	 * Adds a column to the dataset definition.
	 * 
	 * @param dsd the dataset definition
	 * @param columnName the name of the column
	 * @param pdd the patient data definition
	 */
	private void addColumn(PatientDataSetDefinition dsd, String columnName, PatientDataDefinition pdd) {
		dsd.addColumn(columnName, pdd, Mapped.straightThroughMappings(pdd));
	}
	
	/**
	 * Determines if a column should be included based on configuration.
	 * 
	 * @param columnName the name of the column to check
	 * @return true if the column should be included, false otherwise
	 */
	private boolean shouldIncludeColumn(String columnName) {
		Map<String, String> configKeyMap = createConfigKeyMap();
		
		// Find the matching configuration key
		for (Map.Entry<String, String> entry : configKeyMap.entrySet()) {
			if (columnName.equals(entry.getKey())) {
				return Boolean.TRUE.equals(initializerService.getBooleanFromKey(entry.getValue()));
			}
		}
		
		return false;
	}
	
	private Map<String, String> createConfigKeyMap() {
		Map<String, String> configKeyMap = new HashMap<>();
		configKeyMap.put("commonreports.patientIdSticker.fields.identifier", "report.patientIdSticker.fields.identifier");
		configKeyMap.put("commonreports.patientIdSticker.fields.firstname", "report.patientIdSticker.fields.firstname");
		configKeyMap.put("commonreports.patientIdSticker.fields.lastname", "report.patientIdSticker.fields.lastname");
		configKeyMap.put("commonreports.patientIdSticker.fields.age", "report.patientIdSticker.fields.age");
		configKeyMap.put("commonreports.patientIdSticker.fields.dob", "report.patientIdSticker.fields.dob");
		configKeyMap.put("commonreports.patientIdSticker.fields.gender", "report.patientIdSticker.fields.gender");
		configKeyMap.put("commonreports.patientIdSticker.fields.fulladdress", "report.patientIdSticker.fields.fulladdress");
		return configKeyMap;
	}
}
