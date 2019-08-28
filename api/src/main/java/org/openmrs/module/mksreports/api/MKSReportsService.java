/**
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
package org.openmrs.module.mksreports.api;

import java.util.Map;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.mksreports.patientsummary.patienthistory.PatientSummaryResult;
import org.openmrs.module.mksreports.patientsummary.patienthistory.PatientSummaryTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured
 * in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(MekomSolutionsReportsService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional(readOnly = true)
public interface MKSReportsService extends OpenmrsService {
	
	/*
	 * Add service methods here
	 * 
	 */
	
	public PatientSummaryTemplate getPatientSummaryTemplate(Integer id);
	
	/**
	 * @return the resulting patient summary result from evaluating the passed patient summary template
	 *         for the given patient and parameters
	 */
	public PatientSummaryResult evaluatePatientSummaryTemplate(PatientSummaryTemplate patientSummaryTemplate,
	        Integer patientId, Map<String, Object> parameters);
}
