package org.openmrs.module.commonreports.reports;

import static org.openmrs.module.commonreports.common.Helper.getStringFromResource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.openmrs.module.commonreports.ActivatedReportManager;
import org.openmrs.module.initializer.api.InitializerService;
import org.openmrs.module.reporting.common.MessageUtil;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
import org.openmrs.util.OpenmrsClassLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MSPPLabReportManager extends ActivatedReportManager {
	
	@Autowired
	private InitializerService inizService;
	
	@Override
	public boolean isActivated() {
		return inizService.getBooleanFromKey("report.MSPP.lab.active", false);
	}
	
	@Override
	public String getVersion() {
		return "1.0.0-SNAPSHOT";
	}
	
	@Override
	public String getUuid() {
		return "14f9b6a6-e176-467c-83ee-6281e743834d";
	}
	
	@Override
	public String getName() {
		return MessageUtil.translate("commonreports.report.MSPP.lab.reportName");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("commonreports.report.MSPP.lab.reportDescription");
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
		
		rd.setName(getName());
		rd.setDescription(getDescription());
		rd.setParameters(getParameters());
		rd.setUuid(getUuid());
		
		SqlDataSetDefinition sqlDsd = new SqlDataSetDefinition();
		sqlDsd.setName("Lab SQL Dataset");
		sqlDsd.setDescription("Lab SQL Dataset");
		
		String rawSql = getStringFromResource("org/openmrs/module/commonreports/sql/MSPPlab.sql");
		String sql = applyMetadataReplacements(rawSql);
		sqlDsd.setSqlQuery(sql);
		sqlDsd.addParameters(getParameters());
		
		Map<String, Object> parameterMappings = new HashMap<String, Object>();
		parameterMappings.put("startDate", "${startDate}");
		parameterMappings.put("endDate", "${endDate}");
		
		rd.addDataSetDefinition(getName(), sqlDsd, parameterMappings);
		
		return rd;
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		ReportDesign reportDesign = ReportManagerUtil.createExcelTemplateDesign("988d3408-9aac-4b6f-9cab-77c3f02beef5",
		    reportDefinition, "org/openmrs/module/commonreports/reportTemplates/labReportTemplate.xls");
		
		Properties designProperties = new Properties();
		
		reportDesign.setProperties(designProperties);
		return Arrays.asList(reportDesign);
	}
	
	private String applyMetadataReplacements(String rawSql) {
		String s = rawSql
		        .replace(":serialSputumBacilloscopy",
		            "'" + inizService.getValueFromKey("report.MSPP.lab.serialSputumBacilloscopy") + "'")
		        .replace(":positive", "'" + inizService.getValueFromKey("report.MSPP.lab.positive") + "'")
		        .replace(":negative", "'" + inizService.getValueFromKey("report.MSPP.lab.negative") + "'")
		        .replace(":indeterminate", "'" + inizService.getValueFromKey("report.MSPP.lab.indeterminate") + "'")
		        .replace(":zero", "'" + inizService.getValueFromKey("report.MSPP.lab.zero") + "'")
		        .replace(":onePlus", "'" + inizService.getValueFromKey("report.MSPP.lab.onePlus") + "'")
		        .replace(":twoPlus", "'" + inizService.getValueFromKey("report.MSPP.lab.twoPlus") + "'")
		        .replace(":threePlus", "'" + inizService.getValueFromKey("report.MSPP.lab.threePlus") + "'")
		        .replace(":fourPlus", "'" + inizService.getValueFromKey("report.MSPP.lab.fourPlus") + "'")
		        .replace(":malaria", "'" + inizService.getValueFromKey("report.MSPP.lab.malaria") + "'")
		        .replace(":completeBloodCount",
		            "'" + inizService.getValueFromKey("report.MSPP.lab.completeBloodCount") + "'")
		        .replace(":sicklingTest", "'" + inizService.getValueFromKey("report.MSPP.lab.sicklingTest") + "'")
		        .replace(":bloodGroup", "'" + inizService.getValueFromKey("report.MSPP.lab.bloodGroup") + "'")
		        .replace(":urinalysis", "'" + inizService.getValueFromKey("report.MSPP.lab.urinalysis") + "'")
		        .replace(":prenatalVisitType", "'" + inizService.getValueFromKey("report.MSPP.lab.prenatalVisitType") + "'");
		return s;
	}
	
}
