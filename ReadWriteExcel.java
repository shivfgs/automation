package com.ca.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadWriteExcel {
	XSSFWorkbook workbook = null;
	XSSFSheet sheet = null;
	FileInputStream inFile = null;
	FileOutputStream outFile = null;
	String excelFilePath;

	public static void main(String[] args) {
		String testXls = "\\src\\testdata\\outoptafeeds.xlsx";
		XLSWriter xls = new XLSWriter(testXls);
		// xls.set_CellValue("feeds", "123456", 1, 5); // 0,0
		// xls.set_CellValue("feeds", "hello world", 1, 6); // 0,0
		int rows = xls.getRowCount("feeds");
		for (int i = 1; i <= rows; i++) {
			String xmls = xls.getCellData("feeds", i, 3);
			// xmls = xmls.slice(1,-1);
			System.out.println(xmls);
			// xls.set_CellValue("feeds", xmls, i, 3); // 0,0
		}
		System.out.println("conversion done.");
	}

	public ReadWriteExcel(String strExcelPath) {
		try {
			excelFilePath = System.getProperty("user.dir") + strExcelPath;
			inFile = new FileInputStream(new File(excelFilePath));
			// Get the workbook instance for XLS file
			workbook = new XSSFWorkbook(inFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void set_CellData(String strSheetName, String strValue, int intRowNum, int intColNum) {
		try {
			// Get the sheet from the workbook
			sheet = workbook.getSheet(strSheetName);
			Cell cell = null;
			Row row = null;
			row = sheet.getRow(intRowNum);
			if (row == null) {
				sheet.createRow(intRowNum);
			}
			cell = sheet.getRow(intRowNum).getCell(intColNum);
			if (cell == null) {
				sheet.getRow(intRowNum).createCell(intColNum).setCellValue(strValue);
			} else if (sheet.getRow(intRowNum).getCell(intColNum) == null) {
				sheet.getRow(intRowNum).createCell(intColNum).setCellValue(strValue);
			} else if (cell.getCellTypeEnum() == CellType.STRING) {
				cell.setCellValue(strValue);

			} else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
				cell.setCellValue(Double.valueOf(strValue));

			} else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {

				cell.setCellValue(Boolean.valueOf(strValue));
			} else if (cell.getCellTypeEnum() == CellType.BLANK) {
				cell.setBlank();

			}

			inFile.close();
			outFile = new FileOutputStream(new File(excelFilePath));
			workbook.write(outFile);
			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setCellData(String strSheetName, String strValue, int intRowNum, int intColNum) {
		try {
			// Get the sheet from the workbook
			sheet = workbook.getSheet(strSheetName);
			Cell cell = null;
			CellType cellType;
			Row row = null;
			row = sheet.getRow(intRowNum);
			if (row == null) {
				sheet.createRow(intRowNum);
			}
			cell = sheet.getRow(intRowNum).getCell(intColNum);

			if (cell == null) {
				sheet.getRow(intRowNum).createCell(intColNum).setCellValue(strValue);
			} else {
				cellType = cell.getCellType();
				if (cellType == CellType.STRING) {
					cell.setCellValue(strValue);
				} else if (cellType == CellType.NUMERIC) {
					cell.setCellValue(Double.valueOf(strValue));
				} else if (cellType == CellType.BOOLEAN) {
					cell.setCellValue(Boolean.valueOf(strValue));
				} else if (cellType == CellType.BLANK) {
					cell.setBlank();
				}
			}

			inFile.close();
			outFile = new FileOutputStream(new File(excelFilePath));
			workbook.write(outFile);
			outFile.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setCellData(String strSheetName, String strValue, String strValueType, int intRowNum, int intColNum) {
		try {
			// Get the sheet from the workbook
			sheet = workbook.getSheet(strSheetName);
			Cell cell = null;			
			Row row = null;
			row = sheet.getRow(intRowNum);
			// Create a row in sheet if not exists
			if (row == null) {
				sheet.createRow(intRowNum);
			}
			cell = sheet.getRow(intRowNum).getCell(intColNum);
			// Create a cell in sheet if not exists
			if (cell == null) {
				sheet.getRow(intRowNum).createCell(intColNum);
				cell = sheet.getRow(intRowNum).getCell(intColNum);
			}
			// Write to excel sheet as the cell type value
			if (strValueType.equalsIgnoreCase("STRING")) {
				cell.setCellValue(strValue);
			} else if (strValueType.equalsIgnoreCase("NUMERIC")) {
				cell.setCellValue(Double.valueOf(strValue));
			} else if (strValueType.equalsIgnoreCase("BOOLEAN")) {
				cell.setCellValue(Boolean.valueOf(strValue));
			}
			// Save the excel file
			inFile.close();
			outFile = new FileOutputStream(new File(excelFilePath));
			workbook.write(outFile);
			outFile.close();
		} catch (NumberFormatException | IOException e) {
			System.out.println(e.toString());
		} 

	}

	public String getCellData(String sheetName, int intRow, int intCol) {
		String strCellValue = "";
		try {
			// Get the sheet from the workbook
			sheet = workbook.getSheet(sheetName);
			Cell cell = null;
			CellType cellType;
			// Update the value of cell
			cell = sheet.getRow(intRow).getCell(intCol);
			// Update the value of cell
			cellType = cell.getCellType();
			if (cellType == CellType.STRING) {
				strCellValue = cell.getStringCellValue();

			} else if (cellType == CellType.NUMERIC) {
				double dData = cell.getNumericCellValue();
				strCellValue = String.valueOf((int) dData);
			} else if (cellType == CellType.BOOLEAN) {
				boolean bData = cell.getBooleanCellValue();
				strCellValue = Boolean.toString(bData);
			} else if (cellType == CellType.BLANK) {
				strCellValue = "";
			}

			/*
			 * if (cell.getCellTypeEnum() == CellType.STRING) { strCellValue =
			 * cell.getStringCellValue();
			 * 
			 * } else if (cell.getCellTypeEnum() == CellType.NUMERIC) { //strCellValue =
			 * cell.getNumericCellValue() + ""; double dData=cell.getNumericCellValue();
			 * strCellValue=String.valueOf(dData);
			 * 
			 * } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
			 * 
			 * strCellValue = cell.getBooleanCellValue() + ""; } else if
			 * (cell.getCellTypeEnum() == CellType._NONE) { strCellValue = "";
			 * 
			 * } else if (cell.getCellTypeEnum() == CellType.BLANK) { strCellValue = "";
			 * 
			 * } else { strCellValue = "";
			 * 
			 * }
			 */

			inFile.close();
			return strCellValue;
		} catch (IOException e) {
			return strCellValue;
		}

	}

	public int getRowCount(String sheetName) {
		int intRowCount = 0;
		try {
			sheet = workbook.getSheet(sheetName);
			intRowCount = (sheet.getLastRowNum() - sheet.getFirstRowNum());
			return intRowCount;
		} catch (Exception e) {
			return intRowCount;
		}
	}

	public int getColCount(String sheetName) {
		int intColCount = 0;
		try {
			sheet = workbook.getSheet(sheetName);
			intColCount = (sheet.getRow(0).getLastCellNum() - sheet.getRow(0).getFirstCellNum());
			return intColCount;
		} catch (Exception e) {
			return intColCount;
		}
	}
}
