package org.openmrs.module.mksreports.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.mksreports.renderer.PatientHistoryExcelTemplateRenderer;
import org.openmrs.module.mksreports.renderer.PatientHistoryXmlReportRenderer;
import org.openmrs.module.reporting.definition.service.SerializedDefinitionService;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.ReportDesignResource;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.openmrs.module.reporting.report.renderer.CsvReportRenderer;
import org.openmrs.module.reporting.report.renderer.XlsReportRenderer;
import org.openmrs.module.reporting.report.service.ReportService;
import org.openmrs.util.OpenmrsClassLoader;

/**
 * This class contains the logic necessary to set-up and tear down a report definitions
 */
public class Helper {
	
	public static void purgeReportDefinition(String name) {
		ReportDefinitionService rds = Context.getService(ReportDefinitionService.class);
		try {
			ReportDefinition findDefinition = findReportDefinition(name);
			if (findDefinition != null) {
				rds.purgeDefinition(findDefinition);
			}
		}
		catch (RuntimeException e) {
			// intentional empty as the author is too long out of business...
		}
	}
	
	public static ReportDefinition findReportDefinition(String name) {
		ReportDefinitionService s = (ReportDefinitionService) Context.getService(ReportDefinitionService.class);
		List<ReportDefinition> defs = s.getDefinitions(name, true);
		for (ReportDefinition def : defs) {
			return def;
		}
		throw new RuntimeException("Couldn't find Definition " + name);
	}
	
	public static void saveReportDefinition(ReportDefinition rd) {
		ReportDefinitionService rds = (ReportDefinitionService) Context.getService(ReportDefinitionService.class);
		
		// try to find existing report definitions to replace
		List<ReportDefinition> definitions = rds.getDefinitions(rd.getName(), true);
		if (definitions.size() > 0) {
			ReportDefinition existingDef = definitions.get(0);
			rd.setId(existingDef.getId());
			rd.setUuid(existingDef.getUuid());
		}
		try {
			rds.saveDefinition(rd);
		}
		catch (Exception e) {
			SerializedDefinitionService s = (SerializedDefinitionService) Context
			        .getService(SerializedDefinitionService.class);
			s.saveDefinition(rd);
		}
	}
	
	public static ReportDesign createRowPerPatientXlsOverviewReportDesign(ReportDefinition rd, String resourceName,
	        String name, Map<? extends Object, ? extends Object> properties) throws IOException {
		
		ReportService rs = Context.getService(ReportService.class);
		for (ReportDesign rdd : rs.getAllReportDesigns(false)) {
			if (name.equals(rdd.getName())) {
				rs.purgeReportDesign(rdd);
			}
		}
		
		ReportDesignResource resource = new ReportDesignResource();
		resource.setName(resourceName);
		resource.setExtension("xls");
		InputStream is = OpenmrsClassLoader.getInstance().getResourceAsStream(resourceName);
		resource.setContents(IOUtils.toByteArray(is));
		final ReportDesign design = new ReportDesign();
		design.setName(name);
		design.setReportDefinition(rd);
		design.setRendererType(PatientHistoryExcelTemplateRenderer.class);
		design.addResource(resource);
		if (properties != null) {
			design.getProperties().putAll(properties);
		}
		resource.setReportDesign(design);
		
		return design;
	}
	
	public static ReportDesign xlsReportDesign(ReportDefinition reportDefinition, byte[] excelTemplate,
	        Properties designProperties, String name) {
		ReportDesign design = new ReportDesign();
		design.setName(name);
		design.setReportDefinition(reportDefinition);
		design.setRendererType(PatientHistoryExcelTemplateRenderer.class);
		if (excelTemplate != null) {
			ReportDesignResource resource = new ReportDesignResource();
			resource.setName("template");
			resource.setExtension("xls");
			resource.setContentType("application/vnd.ms-excel");
			resource.setContents(excelTemplate);
			resource.setReportDesign(design);
			design.addResource(resource);
			if (designProperties != null) {
				design.setProperties(designProperties);
			}
		}
		return design;
	}
	
	/**
	 * @return a new ReportDesign for a standard Excel output
	 */
	public static ReportDesign createExcelDesign(ReportDefinition reportDefinition, String reportDesignName,
	        boolean includeParameters) {
		ReportDesign design = new ReportDesign();
		design.setName(reportDesignName);
		design.setReportDefinition(reportDefinition);
		
		design.setRendererType(XlsReportRenderer.class);
		if (includeParameters)
			design.addPropertyValue(XlsReportRenderer.INCLUDE_DATASET_NAME_AND_PARAMETERS_PROPERTY, "true");
		return design;
	}
	
	/**
	 * @return a new ReportDesign for a standard CSV output
	 */
	public static ReportDesign createCsvReportDesign(ReportDefinition reportDefinition, String reportDesignName) {
		ReportDesign design = new ReportDesign();
		design.setName(reportDesignName);
		design.setReportDefinition(reportDefinition);
		design.setRendererType(CsvReportRenderer.class);
		return design;
	}
	
	/**
	 * @return a new ReportDesign for a standard XML output
	 */
	public static ReportDesign createXMLReportDesign(ReportDefinition reportDefinition, String reportDesignName) {
		ReportDesign design = new ReportDesign();
		design.setName(reportDesignName);
		design.setReportDefinition(reportDefinition);
		design.setRendererType(PatientHistoryXmlReportRenderer.class);
		return design;
	}
	
	public static void saveReportDesign(ReportDesign design) {
		ReportService rs = Context.getService(ReportService.class);
		rs.saveReportDesign(design);
	}
	
	/**
	 * Given a location on the classpath, return the contents of this resource as a String
	 */
	public static String getStringFromResource(String resourceName) {
		InputStream is = null;
		try {
			is = OpenmrsClassLoader.getInstance().getResourceAsStream(resourceName);
			return IOUtils.toString(is, "UTF-8");
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Unable to load resource: " + resourceName, e);
		}
		finally {
			IOUtils.closeQuietly(is);
		}
	}
	
}
