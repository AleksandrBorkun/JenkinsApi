package by.epam.kronos.jenkinsApi.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import by.epam.kronos.jenkinsApi.entity.JenkinsJobDetails;
import by.epam.kronos.jenkinsApi.entity.JenkinsJobList;
import by.epam.kronos.jenkinsApi.entity.TestCasesFromSuite;
import by.epam.kronos.jenkinsApi.entity.TestSuiteFromJenkins;

public class ExcelParser {

	private final static ExcelParser INSTANCE = new ExcelParser();

	private final static HSSFWorkbook workBook = new HSSFWorkbook();
	private HSSFSheet sheet;
	private HSSFRow row;
//	private String testSuiteName;
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

			writer = new FileOutputStream(createFile("src/main/resources/excelReport2.xls"));
			workBook.write(writer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		parserTearDown();

	}

	private void prepareForWriting() {

		for (JenkinsJobDetails jobDetail : JenkinsJobList.getInstance().getJenkinsJobList()) {

			createSheet(titleSheet);										//This code
			createRow(titleRow);											//Create title sheet
			titleRow++;														// and write there 
			createFirstCell().setCellValue(jobDetail.getJobName());			//Job Name & Count of
			createSecondCell().setCellValue(jobDetail.getCountOfFail());	// failed tests

			createSheet(jobDetail.getJobName());											//
			createRow(0);																	// THIS CODE
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
	}

	private HSSFCell createFirstCell() {
		return row.createCell(0);
	}

	private HSSFCell createSecondCell() {
		return row.createCell(1);
	}

	private HSSFRow createRow(int numOfRow) {
		row = sheet.createRow(numOfRow);
		return row;
	}

	private void createNextRow() {
		row = sheet.createRow(row.getRowNum() + 1);
	}

/*	private HSSFSheet getCurrentSheet(String jobName) {
		if (jobName.contains(sheet.getSheetName())) {
			return sheet;
		} else {
			createSheet(jobName);
			return sheet;

		}
	} */

	private void createSheet(String jobName) {
		if (jobName.equals("JobsReport") && sheet != null) {
			sheet = workBook.getSheet(jobName);
		}

		else {
			sheet = workBook.createSheet(jobName);
		}
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
