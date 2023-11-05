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

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.module.reporting.common.Localized;
import org.openmrs.module.reporting.common.ObjectUtil;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetColumn;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.dataset.SimpleDataSet;
import org.openmrs.module.reporting.dataset.SimpleDataSetMetaData;
import org.openmrs.module.reporting.dataset.definition.CohortCrossTabDataSetDefinition;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.renderer.CsvReportRenderer;
import org.openmrs.module.reporting.report.renderer.RenderingException;

/**
 * ReportRenderer that renders to a csv form for CohortCrossTabDataSet reports
 */
@Handler
@Localized("reporting.CohortCrossTabDataSetCsvReportRenderer")
public class CohortCrossTabDataSetCsvReportRenderer extends CsvReportRenderer {
	
	@Override
	public void render(ReportData results, String argument, OutputStream out) throws IOException, RenderingException {
		results.getDefinition();
		EvaluationContext context = ObjectUtil.nvl(results.getContext(), new EvaluationContext());
		ReportData resultsToRender = new ReportData();
		resultsToRender.setContext(context);
		resultsToRender.setDefinition(results.getDefinition());
		
		Map<String, DataSet> dataSetsToRender = new HashMap<String, DataSet>();
		
		results.getDataSets().forEach((name, ds) -> {
			if (ds.getDefinition() instanceof CohortCrossTabDataSetDefinition) {
				CohortCrossTabDataSetDefinition dsd = (CohortCrossTabDataSetDefinition) ds.getDefinition();
				DataSetRow cohortCrossTabDataSetRow = ds.iterator().next();
				
				SimpleDataSet data = new SimpleDataSet(dsd, context);
				
				SimpleDataSetMetaData metaData = new SimpleDataSetMetaData();
				
				// Add first empty column since it's a place holder for the row indicators.
				metaData.addColumn(new DataSetColumn("", "", Object.class));
				
				Set<String> columns = dsd.getColumns().keySet();
				Set<String> rows = dsd.getRows().keySet();
				
				// Add rest of the columns
		        for (String column : columns) {
		            metaData.addColumn(new DataSetColumn(column, column, Object.class));
		        }
		        data.setMetaData(metaData);
		        
		        // Add column values
		        for (String row : rows) {
		            DataSetRow dsr = new DataSetRow();
		            for (DataSetColumn column : metaData.getColumns()) {
		            	if (column.getName().equals("")) {
		            		dsr.addColumnValue(column, row);
		            	} else {
		            		dsr.addColumnValue(column, ((Cohort) cohortCrossTabDataSetRow.getColumnValue(row + "." + column))
		            				.size());
		            	}
		            }
		            data.addRow(dsr);
		        }
		        dataSetsToRender.put(name, data);
			} else {
				dataSetsToRender.put(name, ds);
			}
		});
		resultsToRender.setDataSets(dataSetsToRender);
		
		super.render(resultsToRender, argument, out);
	}
}
