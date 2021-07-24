package org.openmrs.module.commonreports.reports;

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
public class FamilyPlanningReportManager extends ActivatedReportManager {
	
	@Autowired
	private InitializerService inizService;
	
	@Override
	public boolean isActivated() {
		return inizService.getBooleanFromKey("report.familyPlanning.active", false);
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
		return MessageUtil.translate("commonreports.report.familyPlanning.reportName");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("commonreports.report.familyPlanning.reportDescription");
	}
	
	private Parameter getStartDateParameter() {
		return new Parameter("startDate", "Start Date", Date.class);
	}
	
	private Parameter getEndDateParameter() {
		return new Parameter("endDate", "End Date", Date.class);
	}
	
	private String getSqlString(String resourceName) {
		
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
		sqlDsd.setName("Family Planning SQL Dataset");
		sqlDsd.setDescription("Family Planning SQL Dataset");
		
		String rawSql = getSqlString("org/openmrs/module/commonreports/sql/familyPlanning.sql");
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
		
		reportDesign.setProperties(designProperties);
		return Arrays.asList(reportDesign);
	}
	
	private String applyMetadataReplacements(String rawSql) {
		String s = rawSql
		        .replace(":FPAdministred",
		            inizService.getConceptFromKey("report.familyPlanning.FPAdministred").getConceptId() + "")
		        .replace(":familyPlanning",
		            inizService.getConceptFromKey("report.familyPlanning.familyPlanning").getConceptId() + "")
		        .replace(":femaleLT25",
		            "person.gender = 'F' AND round(DATEDIFF(obs.obs_datetime, person.birthdate)/365.25, 1) < 25")
		        .replace(":femaleGT25",
		            "person.gender = 'F' AND round(DATEDIFF(obs.obs_datetime, person.birthdate)/365.25, 1) >= 25")
		        .replace(":maleLT25",
		            "person.gender = 'M' AND round(DATEDIFF(obs.obs_datetime, person.birthdate)/365.25, 1) < 25")
		        .replace(":maleGT25",
		            "person.gender = 'M' AND round(DATEDIFF(obs.obs_datetime, person.birthdate)/365.25, 1) >= 25")
		        
		        .replace(":typeOfUser",
		            inizService.getConceptFromKey("report.familyPlanning.typeOfUser").getConceptId() + "")
		        .replace(":new", inizService.getConceptFromKey("report.familyPlanning.new").getConceptId() + "")
		        .replace(":existent", inizService.getConceptFromKey("report.familyPlanning.existent").getConceptId() + "")
		        
		        .replace(":microgynon",
		            inizService.getConceptFromKey("report.familyPlanning.microgynon").getConceptId() + "")
		        
		        .replace(":microlut", inizService.getConceptFromKey("report.familyPlanning.microlut").getConceptId() + "")
		        
		        .replace(":depoProveraInjection",
		            inizService.getConceptFromKey("report.familyPlanning.depoProveraInjection").getConceptId() + "")
		        .replace(":jadel", inizService.getConceptFromKey("report.familyPlanning.jadel").getConceptId() + "")
		        .replace(":condom", inizService.getConceptFromKey("report.familyPlanning.condom").getConceptId() + "")
		        .replace(":FPObsGroupId",
		            "obs_group_id IN (select obs_group_id from obs where obs_group_id IN (SELECT obs_id FROM obs");
		return s;
	}
	
}
