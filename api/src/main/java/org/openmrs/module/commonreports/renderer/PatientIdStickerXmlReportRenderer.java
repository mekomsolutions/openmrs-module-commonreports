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
package org.openmrs.module.commonreports.renderer;

import static org.openmrs.module.commonreports.CommonReportsConstants.MODULE_ARTIFACT_ID;
import static org.openmrs.module.commonreports.CommonReportsConstants.PATIENT_ID_STICKER_ID;
import static org.openmrs.module.commonreports.reports.PatientIdStickerReportManager.DATASET_KEY_STICKER_FIELDS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.initializer.api.InitializerService;
import org.openmrs.module.reporting.common.Localized;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetColumn;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.ReportRequest;
import org.openmrs.module.reporting.report.renderer.RenderingException;
import org.openmrs.module.reporting.report.renderer.ReportDesignRenderer;
import org.openmrs.module.reporting.report.renderer.ReportRenderer;
import org.openmrs.module.reporting.serializer.ReportingSerializer;
import org.openmrs.serialization.SerializationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.thoughtworks.xstream.XStream;

/**
 * ReportRenderer that renders to a default XML format
 */
@Handler
@Localized("reporting.XmlReportRenderer")
public class PatientIdStickerXmlReportRenderer extends ReportDesignRenderer {
	
	// @Autowired, immediate, static, and ctor based initialization
	// of this reference all fail or cause the server to freeze
	// when this module is loaded
	
	// using "class local singleton"/Flyweight reference
	private MessageSourceService mss;
	
	private InitializerService inzService;
	
	private MessageSourceService getMessageSourceService() {
		
		if (mss == null) {
			mss = Context.getMessageSourceService();
		}
		
		return mss;
	}
	
	private InitializerService getInitializerService() {
		
		if (inzService == null) {
			inzService = Context.getService(InitializerService.class);
		}
		
		return inzService;
	}
	
	/**
	 * @see ReportRenderer#getFilename(org.openmrs.module.reporting.report.ReportRequest)
	 */
	@Override
	public String getFilename(ReportRequest request) {
		return getFilenameBase(request) + ".xml";
	}
	
	/**
	 * @see ReportRenderer#getRenderedContentType(org.openmrs.module.reporting.report.ReportRequest)
	 */
	@Override
	public String getRenderedContentType(ReportRequest request) {
		return "text/xml";
	}
	
	protected String getStringValue(Object obj) {
		return obj == null ? "" : getMessageSourceService().getMessage(obj.toString());
	}
	
	protected String getStringValue(DataSetRow row, String columnName) {
		Object obj = row.getColumnValue(columnName);
		return getStringValue(obj);
	}
	
	protected String getStringValue(DataSetRow row, DataSetColumn column) {
		return getStringValue(row, column.getName());
	}
	
	@Override
	public void render(ReportData results, String argument, OutputStream out) throws IOException, RenderingException {
		
		// - - - - - - - - - - - - - - - - - - - - - - - -
		// TODO This should go eventually.
		// - - - - - - - - - - - - - - - - - - - - - - - -
		if (false == StringUtils.equals(argument, "in_tests")) {
			
			// Marhsalling using Xstream directly
			try {
				File xmlFile = File.createTempFile("sampleReportData_Xstream_", ".xml");
				BufferedWriter outWriter = new BufferedWriter(new FileWriter(xmlFile));
				XStream xstream = new XStream();
				xstream.toXML(results, outWriter);
			}
			catch (IOException e) {
				System.out.println("IOException Occured" + e.getMessage());
			}
			
			// Marhsalling using ReportingSerializer
			try {
				File xmlFile = File.createTempFile("sampleReportData_ReportingSerializer_", ".xml");
				ReportingSerializer serializer = new ReportingSerializer();
				serializer.serializeToStream(results, new FileOutputStream(xmlFile));
				
			}
			catch (SerializationException e) {
				System.out.println("SerializationException Occured" + e.getMessage());
			}
		}
		// - - - - - - - - - - - - - - - - - - - - - - - -
		//
		// - - - - - - - - - - - - - - - - - - - - - - - -
		
		final String ATTR_LABEL = "label";
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e) {
			throw new RenderingException(e.getLocalizedMessage());
		}
		
		// Root element
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("patientIdStickers");
		doc.appendChild(rootElement);
		
		String stickerHeight = getInitializerService().getValueFromKey("report.patientIdSticker.size.height");
		String stickerWidth = getInitializerService().getValueFromKey("report.patientIdSticker.size.width");
		if (isNotNullOrEmpty(stickerHeight) && isNotNullOrEmpty(stickerWidth)) {
			rootElement.setAttribute("sticker-height", stickerHeight);
			rootElement.setAttribute("sticker-width", stickerWidth);
		} else {
			rootElement.setAttribute("sticker-height", "297mm");
			rootElement.setAttribute("sticker-width", "210mm");
		}
		
		String stickerLayoutType = getInitializerService().getValueFromKey("report.patientIdSticker.layout");
		if (isNotNullOrEmpty(stickerLayoutType)) {
			rootElement.setAttribute("layout-type", stickerLayoutType);
		}
		
		String labelFontSize = getInitializerService().getValueFromKey("report.patientIdSticker.fields.label.font.size");
		System.out.println("labelFontSize: " + labelFontSize);
		if (isNotNullOrEmpty(labelFontSize)) {
			rootElement.setAttribute("label-font-size", labelFontSize);
		}
		
		String labelFontFamily = getInitializerService().getValueFromKey("report.patientIdSticker.fields.label.font.family");
		if (isNotNullOrEmpty(labelFontFamily)) {
			rootElement.setAttribute("label-font-family", labelFontFamily);
		}
		
		String valueFontSize = getInitializerService()
		        .getValueFromKey("report.patientIdSticker.fields.label.value.font.size");
		System.out.println("valueFontSize: " + valueFontSize);
		if (isNotNullOrEmpty(valueFontSize)) {
			System.out.println("valueFontSize: " + valueFontSize);
			rootElement.setAttribute("value-font-size", valueFontSize);
		}
		
		String valueFontfamily = getInitializerService()
		        .getValueFromKey("report.patientIdSticker.fields.label.value.font.family");
		if (isNotNullOrEmpty(valueFontfamily)) {
			rootElement.setAttribute("value-font-family", valueFontfamily);
		}
		
		String fieldVerticalGap = getInitializerService().getValueFromKey("report.patientIdSticker.fields.label.gap");
		if (isNotNullOrEmpty(fieldVerticalGap)) {
			rootElement.setAttribute("field-vertical-gap", fieldVerticalGap);
		}
		
		// Create the sticker template element
		Element templatePIDElement = doc.createElement("patientIdSticker");
		
		// Set Label names to use in template layouts
		MessageSourceService i18nTranslator = Context.getMessageSourceService();
		String patientIdKey = i18nTranslator.getMessage("commonreports.patientIdSticker.fields.identifier");
		String patientNameKey = i18nTranslator.getMessage("commonreports.patientIdSticker.fields.patientname");
		String genderKey = i18nTranslator.getMessage("commonreports.patientIdSticker.fields.gender");
		String dobKey = i18nTranslator.getMessage("commonreports.patientIdSticker.fields.dob");
		String ageKey = i18nTranslator.getMessage("commonreports.patientIdSticker.fields.age");
		String addressKey = i18nTranslator.getMessage("commonreports.patientIdSticker.fields.fulladdress");
		String firstNameKey = i18nTranslator.getMessage("commonreports.patientIdSticker.fields.firstname");
		String lastNameKey = i18nTranslator.getMessage("commonreports.patientIdSticker.fields.lastname");
		
		templatePIDElement.setAttribute("addressKey", addressKey);
		templatePIDElement.setAttribute("patientIdKey", patientIdKey);
		templatePIDElement.setAttribute("patientNameKey", patientNameKey);
		templatePIDElement.setAttribute("genderKey", genderKey);
		templatePIDElement.setAttribute("dobKey", dobKey);
		templatePIDElement.setAttribute("ageKey", ageKey);
		
		// Handle header configuration
		Element header = doc.createElement("header");
		Element headerText = doc.createElement("headerText");
		
		String headerStringId = String.join(".", MODULE_ARTIFACT_ID, PATIENT_ID_STICKER_ID.toLowerCase(), "requestedby");
		headerText.setTextContent(
		    getMessageSourceService().getMessage(headerStringId) + " " + Context.getAuthenticatedUser().getDisplayString());
		header.appendChild(headerText);
		
		// Handle logo if configured		
		String logoUrlPath = getInitializerService().getValueFromKey("report.patientIdSticker.logourl");
		if (!StringUtils.isBlank(logoUrlPath) && logoUrlPath.startsWith("http")) {
			String logoPath = "";
			try {
				URL url = new URL(logoUrlPath);
				logoPath = url.getPath();
				File logoFile = new File(logoPath);
				
				// Check if file already exists
				if (!(logoFile.exists() && logoFile.canRead() && logoFile.isAbsolute())) {
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					InputStream is = connection.getInputStream();
					File tempFile = File.createTempFile("logo", ".png");
					Files.copy(is, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					logoPath = tempFile.getAbsolutePath();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
			Element branding = doc.createElement("branding");
			Element image = doc.createElement("logo");
			image.setTextContent(logoPath);
			branding.appendChild(image);
			header.appendChild(branding);
		}
		
		boolean useHeader = Boolean.TRUE.equals(getInitializerService().getBooleanFromKey("report.patientIdSticker.header"));
		
		if (useHeader) {
			templatePIDElement.appendChild(header);
		}
		
		// Include i18n strings
		Element i18nStrings = doc.createElement("i18n");
		
		List<String> i18nIds = Arrays.asList("page", "of");
		
		for (String id : i18nIds) {
			String fqnId = String.join(".", MODULE_ARTIFACT_ID, PATIENT_ID_STICKER_ID.toLowerCase(), id);
			
			Element i18nChild = doc.createElement(id + "String");
			i18nChild.setTextContent(getMessageSourceService().getMessage(fqnId));
			i18nStrings.appendChild(i18nChild);
		}
		
		templatePIDElement.appendChild(i18nStrings);
		
		// Create barcode element
		Element barcode = doc.createElement("barcode");
		
		// Process data set fields
		String dataSetKey = DATASET_KEY_STICKER_FIELDS;
		if (results.getDataSets().containsKey(dataSetKey)) {
			DataSet dataSet = results.getDataSets().get(dataSetKey);
			Element fields = doc.createElement("fields");
			templatePIDElement.appendChild(fields);
			
			String barcodeValue = null;
			
			String firstName = null;
			String lastName = null;
			
			for (DataSetRow row : dataSet) {
				for (DataSetColumn column : dataSet.getMetaData().getColumns()) {
					String columnLabel = i18nTranslator.getMessage(column.getLabel());
					String strValue = getStringValue(row, column);
					
					if (columnLabel.equals(firstNameKey)) {
						firstName = strValue;
						continue;
					} else if (columnLabel.equals(lastNameKey)) {
						lastName = strValue;
						continue;
					}
					
					if (columnLabel.equals(patientIdKey)) {
						barcodeValue = strValue;
					}
					
					Element fieldData = doc.createElement("field");
					fields.appendChild(fieldData);
					fieldData.setAttribute(ATTR_LABEL, columnLabel);
					fieldData.appendChild(doc.createTextNode(strValue));
				}
				
				// Create merged Patient Name field if either first name or last name exists
				if (isNotNullOrEmpty(firstName) || isNotNullOrEmpty(lastName)) {
					Element patientNameField = doc.createElement("field");
					fields.appendChild(patientNameField);
					patientNameField.setAttribute(ATTR_LABEL, patientNameKey);
					
					StringBuilder fullName = new StringBuilder();
					fullName.append(firstName.trim());
					
					if (fullName.length() > 0) {
						fullName.append(" ");
					}
					fullName.append(lastName.trim());
					
					patientNameField.appendChild(doc.createTextNode(fullName.toString()));
				}
			}
			
			// Only add the barcode if a value was found
			if (isNotNullOrEmpty(barcodeValue)) {
				barcode.setAttribute("barcodeValue", barcodeValue);
				templatePIDElement.appendChild(barcode);
			}
		}
		
		// Create multiple stickers as needed
		String numOfIdStickersValue = getInitializerService().getValueFromKey("report.patientIdSticker.pages");
		int numOfIdStickers = Integer.parseInt(isNotNullOrEmpty(numOfIdStickersValue) ? numOfIdStickersValue : "1");
		for (int i = 1; i <= numOfIdStickers; i++) {
			Element clonedPidElement = (Element) templatePIDElement.cloneNode(true);
			clonedPidElement.setAttribute("page", "Page-" + i);
			rootElement.appendChild(clonedPidElement);
		}
		// Write the content to the output stream
		Transformer transformer = null;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		}
		catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
			throw new RenderingException(e.getLocalizedMessage());
		}
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		DOMSource source = new DOMSource(doc);
		try {
			transformer.transform(source, new StreamResult(out));
		}
		catch (TransformerException e) {
			throw new RenderingException(e.getLocalizedMessage());
		}
		
		{
			System.out.println(out);
			"".toString();
		}
		
	}
	
	private boolean isNotNullOrEmpty(String str) {
		return !StringUtils.isBlank(str);
	}
}
