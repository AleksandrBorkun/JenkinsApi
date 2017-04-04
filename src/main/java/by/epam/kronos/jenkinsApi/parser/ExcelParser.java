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
	private String testSuiteName;
	private File fileWithReport;
	private FileOutputStream writer;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		parserTearDown();

	}

	private void prepareForWriting() {

		for (JenkinsJobDetails jobDetail : JenkinsJobList.getInstance().getJenkinsJobList()) {

			createSheet(jobDetail.getJobName());
			createRow(0);
			createFirstCell().setCellValue(jobDetail.getJobName());
			createSecondCell().setCellValue(jobDetail.getCountOfFail());

			for (TestSuiteFromJenkins testSuite : jobDetail.getTestSuiteList()) {
				createNextRow();
				createNextRow();
				createFirstCell().setCellValue(testSuite.getSuiteName());
				createSecondCell().setCellValue(testSuite.getCountOfFailedTests());

				for (TestCasesFromSuite testCase : testSuite.getTestCaseList()) {

					createNextRow();
					createFirstCell().setCellValue(testCase.getTestCaseName());
					createSecondCell().setCellValue(testCase.getErrorLog());
				}
			}
		}
	}

	private HSSFCell createFirstCell() {
		return row.createCell(1);
	}

	private HSSFCell createSecondCell() {
		return row.createCell(2);
	}

	private HSSFRow createRow(int numOfRow) {
		row = sheet.createRow(numOfRow);
		return row;
	}

	private void createNextRow() {
		row = sheet.createRow(row.getRowNum() + 1);
	}

	private HSSFSheet getCurrentSheet(String jobName) {
		if (jobName.contains(sheet.getSheetName())) {
			return sheet;
		} else {
			createSheet(jobName);
			return sheet;

		}
	}

	private void createSheet(String jobName) {
		sheet = workBook.createSheet(jobName);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
