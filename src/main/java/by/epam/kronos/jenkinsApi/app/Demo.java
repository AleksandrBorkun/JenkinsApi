package by.epam.kronos.jenkinsApi.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import by.epam.kronos.jenkinsApi.entity.JenkinsJobList;
import by.epam.kronos.jenkinsApi.job.PrepareReportBuilder;
import by.epam.kronos.jenkinsApi.parser.ExcelParser;
import by.epam.kronos.jenkinsApi.utils.ReportNameMaker;

public class Demo {

	private static final String FILE_NAME = "src/main/resources/JobNames.txt";

	public static void main(String[] args) throws IOException {
		
		PrepareReportBuilder jr = new PrepareReportBuilder();
		List<String> lines = Files.readAllLines(Paths.get(FILE_NAME));
		
		for (String jobName: lines) {
			String[] a = null;
			if (jobName.contains("/")) {
				a = jobName.split("/");
			}
			if (a != null) {
				jr.makeReport(a[0], a[1]);
			} else {
				jr.makeReport(jobName);
			}
		}
		if (JenkinsJobList.getInstance().getJenkinsJobList().isEmpty()) {
			jr.log.info("Application Error. Information About jobs is Empty. Can't Run Excel Parser.\nApplication Closed");
		} else {
			jr.log.info("Start writing the result to Excel file: " + ReportNameMaker.get());
			ExcelParser.getInstance().writeReportToExcel();
			jr.log.info("Application close");
		}
	}

}
