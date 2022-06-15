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
public class MSPPFamilyPlanningReportManager extends ActivatedReportManager {
	
	@Autowired
	private InitializerService inizService;
	
	@Override
	public boolean isActivated() {
		return inizService.getBooleanFromKey("report.MSPP.familyPlanning.active", false);
	}
	
	@Override
	public String getVersion() {
		return "1.0.0-SNAPSHOT";
	}
	
	@Override
	public String getUuid() {
		return "efd7ba26-7888-45a8-9184-423833ab79d3";
	}
	
	@Override
	public String getName() {
		return MessageUtil.translate("commonreports.report.MSPP.familyPlanning.reportName");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("commonreports.report.MSPP.familyPlanning.reportDescription");
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
		sqlDsd.setName(MessageUtil.translate("commonreports.report.MSPP.familyPlanning.datasetName"));
		sqlDsd.setDescription(MessageUtil.translate("commonreports.report.MSPP.familyPlanning.datasetDescription"));
		
		String rawSql = getStringFromResource("org/openmrs/module/commonreports/sql/MSPPfamilyPlanning.sql");
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
		ReportDesign reportDesign = ReportManagerUtil.createExcelTemplateDesign("c51fc24f-50ba-48f8-9678-90462f7cff80",
		    reportDefinition, "org/openmrs/module/commonreports/reportTemplates/familyPlanningReportTemplate.xls");
		
		Properties designProperties = new Properties();
		
		designProperties.put("newUser.label",
		    MessageUtil.translate("commonreports.report.MSPP.familyPlanning.newUser.label"));
		designProperties.put("existentUser.label",
		    MessageUtil.translate("commonreports.report.MSPP.familyPlanning.existentUser.label"));
		designProperties.put("LT25years.label",
		    MessageUtil.translate("commonreports.report.MSPP.familyPlanning.LT25years.label"));
		designProperties.put("GT25years.label",
		    MessageUtil.translate("commonreports.report.MSPP.familyPlanning.GT25years.label"));
		designProperties.put("method.label", MessageUtil.translate("commonreports.report.MSPP.familyPlanning.method.label"));
		designProperties.put("females.label",
		    MessageUtil.translate("commonreports.report.MSPP.familyPlanning.females.label"));
		designProperties.put("males.label", MessageUtil.translate("commonreports.report.MSPP.familyPlanning.males.label"));
		designProperties.put("PC.label", MessageUtil.translate("commonreports.report.MSPP.familyPlanning.PC.label"));
		designProperties.put("PP.label", MessageUtil.translate("commonreports.report.MSPP.familyPlanning.PP.label"));
		designProperties.put("depo.label", MessageUtil.translate("commonreports.report.MSPP.familyPlanning.depo.label"));
		designProperties.put("implant.label",
		    MessageUtil.translate("commonreports.report.MSPP.familyPlanning.implant.label"));
		designProperties.put("condoms.label",
		    MessageUtil.translate("commonreports.report.MSPP.familyPlanning.condoms.label"));
		designProperties.put("total.label", MessageUtil.translate("commonreports.report.MSPP.familyPlanning.total.label"));
		
		reportDesign.setProperties(designProperties);
		return Arrays.asList(reportDesign);
	}
	
	private String applyMetadataReplacements(String rawSql) {
		String s = rawSql
		        .replace(":FPAdministred",
		            inizService.getConceptFromKey("report.MSPP.familyPlanning.FPAdministred").getConceptId() + "")
		        .replace(":familyPlanning",
		            inizService.getConceptFromKey("report.MSPP.familyPlanning.familyPlanning").getConceptId() + "")
		        .replace(":femaleLT25",
		            "person.gender = 'F' AND round(DATEDIFF(obs.obs_datetime, person.birthdate)/365.25, 1) < 25")
		        .replace(":femaleGT25",
		            "person.gender = 'F' AND round(DATEDIFF(obs.obs_datetime, person.birthdate)/365.25, 1) >= 25")
		        .replace(":maleLT25",
		            "person.gender = 'M' AND round(DATEDIFF(obs.obs_datetime, person.birthdate)/365.25, 1) < 25")
		        .replace(":maleGT25",
		            "person.gender = 'M' AND round(DATEDIFF(obs.obs_datetime, person.birthdate)/365.25, 1) >= 25")
		        
		        .replace(":typeOfUser",
		            inizService.getConceptFromKey("report.MSPP.familyPlanning.typeOfUser").getConceptId() + "")
		        .replace(":new", inizService.getConceptFromKey("report.MSPP.familyPlanning.new").getConceptId() + "")
		        .replace(":existent",
		            inizService.getConceptFromKey("report.MSPP.familyPlanning.existent").getConceptId() + "")
		        
		        .replace(":microgynon",
		            inizService.getConceptFromKey("report.MSPP.familyPlanning.microgynon").getConceptId() + "")
		        
		        .replace(":microlut",
		            inizService.getConceptFromKey("report.MSPP.familyPlanning.microlut").getConceptId() + "")
		        
		        .replace(":depoProveraInjection",
		            inizService.getConceptFromKey("report.MSPP.familyPlanning.depoProveraInjection").getConceptId() + "")
		        .replace(":jadel", inizService.getConceptFromKey("report.MSPP.familyPlanning.jadel").getConceptId() + "")
		        .replace(":condom", inizService.getConceptFromKey("report.MSPP.familyPlanning.condom").getConceptId() + "");
		return s;
	}
	
}
