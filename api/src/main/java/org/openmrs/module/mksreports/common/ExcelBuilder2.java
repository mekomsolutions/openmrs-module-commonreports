package org.openmrs.module.mksreports.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openmrs.Cohort;
import org.openmrs.module.reporting.common.ExcelUtil;
import org.openmrs.module.reporting.common.ObjectUtil;
import org.openmrs.module.reporting.indicator.CohortIndicatorResult;
import org.openmrs.module.reporting.indicator.dimension.CohortIndicatorAndDimensionResult;

/**
 * Excel Helper class that facilitates creating rows and columns in a workbook
 */
public class ExcelBuilder2 {
	
	protected static Log log = LogFactory.getLog(ExcelBuilder2.class);
	
	private Workbook workbook;
	
	private Sheet currentSheet = null;
	
	private List<String> sheetNames = new ArrayList<String>();
	
	private Row currentRow = null;
	
	private int currentRowNum = 0;
	
	private int currentColNum = 0;
	
	private Map<String, CellStyle> styleCache = new HashMap<String, CellStyle>();
	
	public ExcelBuilder2() {
		workbook = new XSSFWorkbook();
	}
	
	/**
	 * Create a new sheet with a default name, and switch to this sheet
	 */
	public ExcelBuilder2 newSheet() {
		return newSheet(null);
	}
	
	/**
	 * Create a new sheet with the given name, and switch to this sheet
	 */
	public ExcelBuilder2 newSheet(String name) {
		name = ExcelUtil.formatSheetTitle(name, sheetNames);
		currentSheet = workbook.createSheet(name);
		sheetNames.add(name);
		currentRow = null;
		currentRowNum = 0;
		currentColNum = 0;
		return this;
	}
	
	public ExcelBuilder2 goToPosition(Properties properties) {
		this.currentSheet = workbook.getSheetAt(getDesginProperty(properties.getProperty("renderToTemplateSheet")));
		this.currentRowNum = getDesginProperty(properties.getProperty("renderToTemplateRow"));
		this.currentColNum = getDesginProperty(properties.getProperty("renderToTemplateColumn"));
		return this;
	}
	
	/**
	 * Adds the next cell with the given value, and no style.
	 */
	public ExcelBuilder2 addCell(Object cellValue) {
		return addCell(cellValue, null);
	}
	
	/**
	 * Adds the next cell with the given value, and style described by the String descriptor
	 */
	public ExcelBuilder2 addCell(Object cellValue, String style) {
		if (currentSheet == null) {
			newSheet();
		}
		if (currentRow == null) {
			currentRow = currentSheet.createRow(currentRowNum);
		}
		Cell cell;
		if (cellValue == null) {
			cell = currentRow.createCell(currentColNum, Cell.CELL_TYPE_BLANK);
		} else if (cellValue instanceof Number) {
			cell = currentRow.createCell(currentColNum, Cell.CELL_TYPE_NUMERIC);
		} else if (cellValue instanceof Date) {
			cell = currentRow.createCell(currentColNum, Cell.CELL_TYPE_NUMERIC);
		} else if (cellValue instanceof Boolean) {
			cell = currentRow.createCell(currentColNum, Cell.CELL_TYPE_BOOLEAN);
		} else if (cellValue instanceof Cohort) {
			cell = currentRow.createCell(currentColNum, Cell.CELL_TYPE_NUMERIC);
		} else if (cellValue instanceof CohortIndicatorResult) {
			cell = currentRow.createCell(currentColNum, Cell.CELL_TYPE_NUMERIC);
		} else if (cellValue instanceof CohortIndicatorAndDimensionResult) {
			cell = currentRow.createCell(currentColNum, Cell.CELL_TYPE_NUMERIC);
		} else {
			cell = currentRow.createCell(currentColNum, Cell.CELL_TYPE_STRING);
		}
		
		if (ObjectUtil.isNull(style) && cellValue instanceof Date) {
			style = "date";
		}
		if (ObjectUtil.notNull(style)) {
			CellStyle cellStyle = styleCache.get(style);
			if (cellStyle == null) {
				cellStyle = ExcelUtil.createCellStyle(workbook, style);
				styleCache.put(style, cellStyle);
			}
			cell.setCellStyle(cellStyle);
		}
		ExcelUtil.setCellContents(cell, cellValue);
		currentColNum++;
		return this;
	}
	
	/**
	 * Moves to the next row.
	 */
	public ExcelBuilder2 nextRow() {
		currentRow = null;
		currentRowNum++;
		currentColNum = 0;
		return this;
	}
	
	/**
	 * Outputs the Excel workbook to the specified output stream
	 */
	public void write(OutputStream out) throws IOException {
		write(out, null);
	}
	
	/**
	 * Outputs the Excel workbook to the specified output stream, first encrypting with a password if
	 * supplied See: http://poi.apache.org/encryption.html
	 */
	public void write(OutputStream out, String password) throws IOException {
		if (StringUtils.isBlank(password)) {
			workbook.write(out);
		} else {
			POIFSFileSystem fs = new POIFSFileSystem();
			EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
			Encryptor enc = info.getEncryptor();
			enc.confirmPassword(password);
			
			ByteArrayOutputStream baos = null;
			ByteArrayInputStream bais = null;
			
			try {
				baos = new ByteArrayOutputStream();
				workbook.write(baos);
				bais = new ByteArrayInputStream(baos.toByteArray());
				
				OPCPackage opc = OPCPackage.open(bais);
				OutputStream os = enc.getDataStream(fs);
				opc.save(os);
				opc.close();
			}
			catch (Exception e) {
				throw new IllegalStateException("Error writing encrypted Excel document", e);
			}
			finally {
				IOUtils.closeQuietly(baos);
				IOUtils.closeQuietly(bais);
			}
			
			fs.writeFilesystem(out);
		}
	}
	
	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}
	
	public Workbook getWorkbook() {
		return workbook;
	}
	
	public Sheet getCurrentSheet() {
		return currentSheet;
	}
	
	public Row getCurrentRow() {
		return currentRow;
	}
	
	public int getDesginProperty(String property) {
		try {
			return Integer.parseInt(property);
		}
		catch (NumberFormatException e) {
			log.error("Invalid value for property " + property + " : " + e);
		}
		return 0;
	}
}
