/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.mksreports.patienthistory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.mksreports.common.Helper;
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
import org.openmrs.module.mksreports.dataset.definition.PatientHistoryEncounterAndVisitDataSetDefinition;
import org.openmrs.module.mksreports.dataset.definition.PatientHistoryObsAndEncounterDataSetDefinition;
import org.openmrs.module.mksreports.library.BasePatientDataLibrary;
import org.openmrs.module.mksreports.library.DataFactory;
import org.openmrs.module.mksreports.library.EncounterDataLibrary;
import org.openmrs.module.mksreports.library.ObsDataLibrary;
import org.openmrs.module.mksreports.patienthistory.MKSReportsReportManager;
import org.openmrs.module.reporting.common.SortCriteria;
import org.openmrs.module.reporting.data.converter.DateConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDatetimeDataDefinition;
import org.openmrs.module.reporting.data.encounter.definition.EncounterIdDataDefinition;
import org.openmrs.module.reporting.data.encounter.definition.EncounterTypeDataDefinition;
import org.openmrs.module.reporting.data.obs.definition.ObsIdDataDefinition;
import org.openmrs.module.reporting.data.patient.library.BuiltInPatientDataLibrary;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.service.ReportService;
import org.springframework.stereotype.Component;

@Component
public class PatientHistoryReportManager extends MKSReportsReportManager {
	
	public final static String REPORT_DESIGN_NAME = "mksPatientHistory.xml_";
	
	protected final static String REPORT_DEFINITION_NAME = "Patient History";
	
	public final static String DATASET_KEY_DEMOGRAPHICS = "demographics";
	
	public final static String DATASET_KEY_OBS = "obs";
	
	public final static String DATASET_KEY_ENCOUNTERS = "encounters";
	
	// @Autowired TODO Reconfigure this annotation after
	private DataFactory dataFactory = new DataFactory();
	
	// @Autowired TODO Reconfigure this annotation after
	private BuiltInPatientDataLibrary builtInPatientDataLibrary = new BuiltInPatientDataLibrary();
	
	// @Autowired TODO Reconfigure this annotation after
	private EncounterDataLibrary encounterDataLibrary = new EncounterDataLibrary();
	
	// @Autowired TODO Reconfigure this annotation after
	private ObsDataLibrary obsDataLibrary = new ObsDataLibrary();
	
	// @Autowired TODO Reconfigure this annotation after
	private BasePatientDataLibrary basePatientDataLibrary = new BasePatientDataLibrary();
	
	public void setup() throws Exception {
		
		ReportDefinition reportDef = constructReportDefinition();
		ReportDesign reportDesign = Helper.createXMLReportDesign(reportDef, REPORT_DESIGN_NAME);
		Helper.saveReportDesign(reportDesign);
	}
	
	public ReportDefinition constructReportDefinition() {
		ReportDefinition reportDef = new ReportDefinition();
		reportDef.setName(REPORT_DEFINITION_NAME);
		
		Map<String, Object> mappings = new HashMap<String, Object>();
		MessageSourceService i18nTranslator = Context.getMessageSourceService();
		Locale locale = Context.getLocale(); // TODO Figure out how to use a 'locale' param when getting msgs
		
		// Create dataset definitions
		PatientHistoryEncounterAndVisitDataSetDefinition encountersDatasetSetDef = createEncounterAndVisitDataSetDefinition();
		PatientDataSetDefinition patientDataSetDef = createDemographicsDataSetDefinition(i18nTranslator);
		PatientHistoryObsAndEncounterDataSetDefinition obsDataSetDef = createObsAndEncounterDataSetDefinition();
		
		// Add datasets to the report
		reportDef.addDataSetDefinition(DATASET_KEY_DEMOGRAPHICS, patientDataSetDef, mappings);
		reportDef.addDataSetDefinition(DATASET_KEY_OBS, obsDataSetDef, mappings);
		reportDef.addDataSetDefinition(DATASET_KEY_ENCOUNTERS, encountersDatasetSetDef, new HashMap<String, Object>());
		
		// Save the report definition
		Helper.saveReportDefinition(reportDef);
		
		return reportDef;
	}
	
	/**
	 * @param i18nTranslator
	 * @return
	 */
	public PatientDataSetDefinition createDemographicsDataSetDefinition(MessageSourceService i18nTranslator) {
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
	
	public final static String VISIT_UUID_LABEL = "visit_uuid";
	
	public final static String VISIT_LOCATION_LABEL = "visit_location";
	
	public final static String VISIT_TYPE_LABEL = "visit_type";
	
	public final static String ENCOUNTER_UUID_LABEL = "encounter_uuid";
	
	public final static String ENCOUNTER_PROVIDER_LABEL = "provider_name";
	
	public final static String ENCOUNTER_TYPE_UUID_LABEL = "encounter_type_uuid";
	
	public final static String ENCOUNTERTYPE_NAME_LABEL = "encounter_type_name";
	
	public final static String ENCOUNTER_DATETIME_LABEL = "encounter_datetime";
	
	/**
	 * @return
	 */
	public PatientHistoryEncounterAndVisitDataSetDefinition createEncounterAndVisitDataSetDefinition() {
		PatientHistoryEncounterAndVisitDataSetDefinition encounterAndVistDatasetSetDef = new PatientHistoryEncounterAndVisitDataSetDefinition();
		encounterAndVistDatasetSetDef.addColumn(VISIT_UUID_LABEL, encounterDataLibrary.getVisitId(), "",
		    new VisitUUIDFromIdConverter());
		encounterAndVistDatasetSetDef.addColumn(VISIT_LOCATION_LABEL, encounterDataLibrary.getVisitId(), "",
		    new VisitLocationFromIdConverter());
		encounterAndVistDatasetSetDef.addColumn(VISIT_TYPE_LABEL, encounterDataLibrary.getVisitId(), "",
		    new VisitTypeFromIdConverter());
		encounterAndVistDatasetSetDef.addColumn(ENCOUNTER_UUID_LABEL, encounterDataLibrary.getUUID(), "",
		    new ObjectFormatter());
		encounterAndVistDatasetSetDef.addColumn(ENCOUNTER_PROVIDER_LABEL, new EncounterIdDataDefinition(), "",
		    new EncounterProviderFromIdConverter());
		encounterAndVistDatasetSetDef.addColumn(ENCOUNTER_TYPE_UUID_LABEL, new EncounterIdDataDefinition(), "",
		    new EncounterTypeUUIDFromEncounterIdConverter());
		encounterAndVistDatasetSetDef.addColumn(ENCOUNTERTYPE_NAME_LABEL, new EncounterTypeDataDefinition(), "",
		    new ObjectFormatter());
		encounterAndVistDatasetSetDef.addColumn(ENCOUNTER_DATETIME_LABEL, new EncounterDatetimeDataDefinition(), "",
		    new DateConverter());
		encounterAndVistDatasetSetDef.addSortCriteria(ENCOUNTER_DATETIME_LABEL, SortCriteria.SortDirection.DESC);
		return encounterAndVistDatasetSetDef;
	}
	
	public final static String OBS_VALUE_LABEL = "obs_value";
	
	public final static String OBS_DATETIME_LABEL = "obs_datetime";
	
	public final static String OBS_DATATYPE_LABEL = "concept_datatype";
	
	public final static String OBS_NAME_LABEL = "concept_name";
	
	public final static String OBS_PROVIDER_LABEL = "provider_name";
	
	/**
	 * @return
	 */
	public PatientHistoryObsAndEncounterDataSetDefinition createObsAndEncounterDataSetDefinition() {
		PatientHistoryObsAndEncounterDataSetDefinition obsDataSetDef = new PatientHistoryObsAndEncounterDataSetDefinition();
		obsDataSetDef.addColumn(ENCOUNTER_UUID_LABEL, encounterDataLibrary.getUUID(), "", new ObjectFormatter());
		obsDataSetDef.addColumn(OBS_PROVIDER_LABEL, new ObsIdDataDefinition(), "", new ObsProviderFromIdConverter());
		obsDataSetDef.addColumn(OBS_DATETIME_LABEL, new ObsDatetimeDataDefinition(), "", new DateConverter());
		obsDataSetDef.addColumn(OBS_DATATYPE_LABEL, obsDataLibrary.getConceptId(), "", new ConceptDataTypeConverter());
		obsDataSetDef.addColumn(OBS_NAME_LABEL, obsDataLibrary.getConceptId(), "", new ConceptNameConverter());
		obsDataSetDef.addColumn(OBS_VALUE_LABEL, new ObsIdDataDefinition(), "", new ObsValueFromIdConverter());
		obsDataSetDef.addSortCriteria(OBS_DATETIME_LABEL, SortCriteria.SortDirection.DESC);
		return obsDataSetDef;
	}
	
	public void delete() {
		ReportService rs = Context.getService(ReportService.class);
		for (ReportDesign rd : rs.getAllReportDesigns(false)) {
			if (rd.getName().equals(PatientHistoryReportManager.REPORT_DESIGN_NAME)) {
				rs.purgeReportDesign(rd);
			}
		}
		Helper.purgeReportDefinition(REPORT_DEFINITION_NAME);
	}
}
