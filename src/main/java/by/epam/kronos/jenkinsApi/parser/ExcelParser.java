package by.epam.kronos.jenkinsApi.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;

import by.epam.kronos.jenkinsApi.entity.JenkinsJobDetails;
import by.epam.kronos.jenkinsApi.entity.JenkinsJobList;
import by.epam.kronos.jenkinsApi.entity.TestCasesFromSuite;
import by.epam.kronos.jenkinsApi.entity.TestSuiteFromJenkins;
import by.epam.kronos.jenkinsApi.utils.ReportNameMaker;

public class ExcelParser {

	private final static ExcelParser INSTANCE = new ExcelParser();

	private final static HSSFWorkbook workBook = new HSSFWorkbook();
	
	private HSSFSheet sheet;
	private HSSFRow row;
	private HSSFCellStyle colorStyle = null;
	private HSSFCellStyle rowStyle = null;
	private HSSFCellStyle persentageStyle = null;
	private File fileWithReport;
	private FileOutputStream writer;
	private String titleSheet = "JobsReport";
	private int titleRow = 0;

	public static ExcelParser getInstance() {
		return INSTANCE;
	}

	private ExcelParser() {

	}

	public void writeReportToExcel() {
		
		prepareForWriting();

		try {

			writer = new FileOutputStream(createFile(ReportNameMaker.get()));
			workBook.write(writer);
		} catch (FileNotFoundException e) {
			parserTearDown();
			e.printStackTrace();
		} catch (IOException e) {
			parserTearDown();
			e.printStackTrace();
		}
		finally{
			parserTearDown();
		}
		

	}

	private void prepareForWriting() {
	
		
		for (JenkinsJobDetails jobDetail : JenkinsJobList.getInstance().getJenkinsJobList()) {
			
			createSheet(titleSheet);
			
			createTitleHeader();
																				//This code
			createRow(titleRow);												//Create title sheet
			titleRow++;															// and write there 
			createFirstCell().setCellValue(jobDetail.getJobName());				//Job Name & Count of
			createSecondCell().setCellValue(jobDetail.getCountOfFail());		// failed tests
			createThirdCell().setCellValue(jobDetail.getTotalTestsCount());		//& total tests
			
			createSheet(jobDetail.getJobName());											//
			createHeaderForJodDetailsSheet();												//
			createNextRow();																// THIS CODE
			createFirstCell().setCellValue(jobDetail.getJobName());							// CREATE A NEW SHEET
			createSecondCell().setCellValue(jobDetail.getCountOfFail());					//

			for (TestSuiteFromJenkins testSuite : jobDetail.getTestSuiteList()) {			//
				createNextRow();															// AND PREPARE 
				createNextRow();															// FULL INFORMATION
				createFirstCell().setCellValue(testSuite.getSuiteName());					// ABOUT JENKINS JOB
				createSecondCell().setCellValue(testSuite.getCountOfFailedTests());			//
																									
				for (TestCasesFromSuite testCase : testSuite.getTestCaseList()) {			//

					createNextRow();														//  FOR WRITE IT
					createFirstCell().setCellValue(testCase.getTestCaseName());				//	TO EXCEL FILE
					createSecondCell().setCellValue(testCase.getErrorLog());				//
				}
			}
		}
		
		
		createTitleFooter();
		
	}

	private HSSFCell createFirstCell() {
		return row.createCell(0);
	}
	
	private HSSFCell createThirdCell() {
		return row.createCell(2);
	}

	private HSSFCell createSecondCell() {
		return row.createCell(1);
	}

	private HSSFCell getFirstCell() {
		return row.getCell(0);
	}
	private HSSFCell getSecondCell() {
		return row.getCell(1);
	}
	private HSSFCell getThirdCell() {
		return row.getCell(2);
	}
	private HSSFRow createRow(int numOfRow) {
		row = sheet.createRow(numOfRow);
		return row;
	}

	private void createNextRow() {
		row = sheet.createRow(row.getRowNum() + 1);
	}

	private void createSheet(String jobName) {
		if (jobName.equals("JobsReport") && sheet != null) {
			sheet = workBook.getSheet(jobName);
			sheet.setColumnWidth(0, 19200); // 19200/256 = 75
		}

		else if(jobName.equals("JobsReport")){
			sheet = workBook.createSheet(jobName);
			sheet.setColumnWidth(0, 19200); // 19200/256 = 75
		}
		else{
			sheet = workBook.createSheet(jobName);
			sheet.setColumnWidth(0, 19200); // 19200/256 = 75
			sheet.setColumnWidth(1, 19200); // 19200/256 = 75
		}
	}

	private void createTitleHeader(){
		
		if(titleRow == 0){									
			createRow(titleRow);							
			createFirstCell().setCellValue("Job Name");			//	THIS CODE 
			getFirstCell().setCellStyle(getBoldFont());			//	CREATE A HEADER
			createSecondCell().setCellValue("Failed");			//	FOR OUR TITLE
			getSecondCell().setCellStyle(getBoldFont());		//	SHEET
			createThirdCell().setCellValue("Total");
			getThirdCell().setCellStyle(getBoldFont());		
			titleRow++;
		}		
	}
	
	private void createTitleFooter(){
		
		createSheet(titleSheet);
				
		createRow(titleRow);																//
		
		createFirstCell().setCellValue("Total Result");										//	Make a Total
		getFirstCell().setCellStyle(getBoldFont()); 										//	 Result report	
		
		createSecondCell().setCellFormula("SUM(B2:B"+row.getRowNum()+")");;	
		getSecondCell().setCellStyle(getColorStyle("red"));		
		
		createThirdCell().setCellFormula("SUM(C2:C"+row.getRowNum()+")");	
		
		titleRow++; 																		//
		createRow(titleRow);
		
		createFirstCell().setCellValue("Result in Percent");
		getFirstCell().setCellStyle(getBoldFont());											//	 	
		
		createSecondCell().setCellFormula("(B"+row.getRowNum()+"/C"+row.getRowNum()+")");	// Make a Total
		getSecondCell().setCellStyle(getPercentStyle());									// Result report
		
		createThirdCell().setCellValue(1);													// in percent
		getThirdCell().setCellStyle(getPercentStyle());										//					
	
		
	}
	
	private void createHeaderForJodDetailsSheet(){
		
		createRow(0);
		createFirstCell().setCellValue("JobName/SuiteName/TestCaseName");
		getFirstCell().setCellStyle(getBoldFont());
		createSecondCell().setCellValue("Failed/Error log");
		getSecondCell().setCellStyle(getBoldFont());
		
		
		
	}
	
 	private HSSFCellStyle getBoldFont(){
		
		if(rowStyle == null){
		rowStyle = workBook.createCellStyle();
		HSSFFont font = workBook.createFont();
		font.setBold(true);
		rowStyle.setFont(font);
			}
		
		return rowStyle;
	}
	
	private HSSFCellStyle getColorStyle(String color){
		if(colorStyle == null){
			colorStyle = workBook.createCellStyle();
		}
		HSSFFont font = workBook.createFont();
		if(color.toUpperCase().equals("RED"))
		font.setColor(font.COLOR_RED);
		else if(color.toUpperCase().equals("GREEN"))
			font.setColor((short)3);
		else if(color.toUpperCase().equals("BLUE")){
			font.setColor((short)4);
		}
		else{
			font.setColor(font.COLOR_NORMAL);
		}
		colorStyle.setFont(font);
		return colorStyle;
	}
	
	private HSSFCellStyle getPercentStyle(){
		
		if(persentageStyle == null){
		persentageStyle = workBook.createCellStyle();
		persentageStyle.setDataFormat(workBook.createDataFormat().getFormat("##.##%"));
			}
		
		return persentageStyle;
	}
	
	private File createFile(String fileName) {

		if (fileWithReport != null) {
			return fileWithReport;
		}
		fileWithReport = new File(fileName);
		return fileWithReport;

	}

	private void parserTearDown() {

		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (workBook != null) {
			try {
				workBook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
