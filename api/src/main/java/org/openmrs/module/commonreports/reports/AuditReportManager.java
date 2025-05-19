package org.openmrs.module.commonreports.reports;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openmrs.module.commonreports.ActivatedReportManager;
import static org.openmrs.module.commonreports.common.Helper.getStringFromResource;
import org.openmrs.module.initializer.api.InitializerService;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.common.MessageUtil;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuditReportManager extends ActivatedReportManager {
	
	@Autowired
	private InitializerService inizService;
	
	@Override
	public boolean isActivated() {
		return inizService.getBooleanFromKey("report.audits.active", true);
	}
	
	@Override
	public String getVersion() {
		return "1.0.0-SNAPSHOT";
	}
	
	@Override
	public String getUuid() {
		return "b00d482d-3e66-473a-bd21-8bac5da2fa32";
	}
	
	@Override
	public String getName() {
		return MessageUtil.translate("commonreports.report.audits.reportName");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("commonreports.report.audits.reportDescription");
	}
	
	private Parameter getStartDateParameter() {
		return new Parameter("startDateTime", "Start Date", Date.class, null,
		        DateUtil.parseDate("1970-01-01", "yyyy-MM-dd"));
	}
	
	private Parameter getEndDateParameter() {
		long oneDayTimestamp = 24 * 60 * 60 * 1000;
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() + oneDayTimestamp));
		return new Parameter("endDate", "End Date", Date.class, null, DateUtil.parseDate(today, "yyyy-MM-dd"));
	}
	
	private Parameter getUserParameter() {
		return new Parameter("username", "Username", String.class, null, "jdoe", null, false);
	}
	
	private Parameter getPatientIdentifierParameter() {
		return new Parameter("patientIdentifier", "Patient Identifier", String.class, null, null, null, false);
	}
	
	@Override
	public List<Parameter> getParameters() {
		List<Parameter> params = new ArrayList<Parameter>();
		params.add(getStartDateParameter());
		params.add(getEndDateParameter());
		params.add(getUserParameter());
		params.add(getPatientIdentifierParameter());
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
		sqlDsd.setName(MessageUtil.translate("commonreports.report.audits.datasetName"));
		sqlDsd.setDescription(MessageUtil.translate("commonreports.report.audits.datasetDescription"));
		
		String sql = getStringFromResource("org/openmrs/module/commonreports/sql/audit.sql");
		
		sqlDsd.setSqlQuery(sql);
		sqlDsd.addParameters(getParameters());
		
		Map<String, Object> parameterMappings = new HashMap<String, Object>();
		parameterMappings.put("startDateTime", "${startDateTime}");
		parameterMappings.put("endDate", "${endDate}");
		parameterMappings.put("username", "${username}");
		parameterMappings.put("patientIdentifier", "${patientIdentifier}");
		
		rd.addDataSetDefinition(getName(), sqlDsd, parameterMappings);
		
		return rd;
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		ReportDesign reportDesign = ReportManagerUtil.createCsvReportDesign("f80f225b-719a-4573-8555-bb13a3fc2f49",
		    reportDefinition);
		return Arrays.asList(reportDesign);
	}
	
}
