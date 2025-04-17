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

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
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
import org.openmrs.api.AdministrationService;
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
import org.openmrs.util.OpenmrsClassLoader;
import org.openmrs.util.OpenmrsUtil;
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
	
	private static final Map<String, String> logoCache = new HashMap<>();
	
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
		
		final String ATTR_TYPE = "type";
		final String ATTR_LABEL = "label";
		final String ATTR_TIME = "time";
		final String ATTR_UUID = "uuid";
		final String ATTR_LOC = "location";
		final String ATTR_PROVIDER = "provider";
		
		final String DATETIME_FORMAT = "dd MMM yyyy @ HH:mm";
		final String TIME_FORMAT = "HH:mm:ss";
		
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
		
		// Set sticker height and width
		String stickerHeight = getInitializerService().getValueFromKey("report.patientIdSticker.size.height");
		String stickerWidth = getInitializerService().getValueFromKey("report.patientIdSticker.size.width");
		if (stickerHeight != null && stickerWidth != null) {
			rootElement.setAttribute("sticker-height", stickerHeight);
			rootElement.setAttribute("sticker-width", stickerWidth);
		} else {
			rootElement.setAttribute("sticker-height", "297mm");
			rootElement.setAttribute("sticker-width", "210mm");
		}
		
		Element templatePIDElement = doc.createElement("patientIdSticker");
		
		Element header = doc.createElement("header");
		Element headerText = doc.createElement("headerText");
		
		String headerStringId = String.join(".", MODULE_ARTIFACT_ID, PATIENT_ID_STICKER_ID.toLowerCase(), "requestedby");
		
		headerText.setTextContent(
		    getMessageSourceService().getMessage(headerStringId) + " " + Context.getAuthenticatedUser().getDisplayString());
		header.appendChild(headerText);
		
		AdministrationService adminService = Context.getAdministrationService();
		
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
		
		boolean useHeader = "true".equals(adminService.getGlobalProperty(MODULE_ARTIFACT_ID + ".enableHeader"));
		
		if (useHeader) {
			templatePIDElement.appendChild(header);
		}
		
		Element i18nStrings = doc.createElement("i18n");
		
		List<String> i18nIds = Arrays.asList("page", "of");
		
		for (String id : i18nIds) {
			String fqnId = String.join(".", MODULE_ARTIFACT_ID, PATIENT_ID_STICKER_ID.toLowerCase(), id);
			
			Element i18nChild = doc.createElement(id + "String");
			
			i18nChild.setTextContent(getMessageSourceService().getMessage(fqnId));
			
			i18nStrings.appendChild(i18nChild);
		}
		
		templatePIDElement.appendChild(i18nStrings);
		
		Element barcode = doc.createElement("barcode");
		templatePIDElement.appendChild(barcode);
		
		String dataSetKey = "";
		
		dataSetKey = DATASET_KEY_STICKER_FIELDS;
		if (results.getDataSets().containsKey(dataSetKey)) {
			MessageSourceService i18nTranslator = Context.getMessageSourceService();
			DataSet dataSet = results.getDataSets().get(dataSetKey);
			Element fields = doc.createElement("fields");
			templatePIDElement.appendChild(fields);
			
			for (DataSetRow row : dataSet) {
				for (DataSetColumn column : dataSet.getMetaData().getColumns()) {
					Element fieldData = doc.createElement("field");
					fields.appendChild(fieldData);
					fieldData.setAttribute(ATTR_LABEL, column.getLabel());
					String strValue = getStringValue(row, column);
					String patientIdColumnName = i18nTranslator
					        .getMessage("commonreports.patientIdSticker.fields.identifier");
					if (column.getLabel().equals(patientIdColumnName)) {
						barcode.setAttribute("barcodeValue", strValue);
					}
					
					fieldData.appendChild(doc.createTextNode(strValue));
				}
			}
		}
		
		String numOfIdStickersValue = getInitializerService().getValueFromKey("report.patientIdSticker.pages");
		int numOfIdStickers = Integer.parseInt(numOfIdStickersValue);
		for (int i = 1; i <= numOfIdStickers; i++) {
			Element clonedPidElement = (Element) templatePIDElement.cloneNode(true);
			
			// Update the ID to make it unique
			clonedPidElement.setAttribute("page", "Page-" + i);
			
			// Append to root
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
}
