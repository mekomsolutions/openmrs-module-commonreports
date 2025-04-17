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
package org.openmrs.module.commonreports.web.controller;

import static org.openmrs.module.commonreports.CommonReportsConstants.PATIENT_ID_STICKER_ID;
import static org.openmrs.module.commonreports.CommonReportsConstants.ROOT_URL;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.module.commonreports.reports.PatientIdStickerPdfReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PatientIdStickerDataPdfExportController {
	
	private PatientIdStickerPdfReport pdfReport;
	
	private PatientService ps;
	
	@Autowired
	public PatientIdStickerDataPdfExportController(@Qualifier("patientService") PatientService ps,
	    PatientIdStickerPdfReport pdfReport) {
		this.ps = ps;
		this.pdfReport = pdfReport;
	}
	
	private void writeReponse(Patient patient, String contentDisposition, HttpServletResponse response) {
		response.setContentType("application/pdf");
		
		if (StringUtils.isBlank(contentDisposition)) {
			response.addHeader("Content-Disposition", "attachment;filename=" + PATIENT_ID_STICKER_ID + ".pdf");
		} else {
			response.addHeader("Content-Disposition", contentDisposition);
		}
		
		try {
			byte[] pdfBytes = pdfReport.getBytes(patient, null);
			response.setContentLength(pdfBytes.length);
			response.getOutputStream().write(pdfBytes);
			response.getOutputStream().flush();
		}
		catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = ROOT_URL + "/" + PATIENT_ID_STICKER_ID)
	public void getPatientIdSticker(ModelMap model, HttpServletRequest request, HttpServletResponse response,
	        @RequestParam(value = "patientUuid") String patientUuid,
	        @RequestParam(value = "contentDisposition", required = false) String contentDisposition) {
		
		Patient patient = ps.getPatientByUuid(patientUuid);
		writeReponse(patient, contentDisposition, response);
	}
	
}
