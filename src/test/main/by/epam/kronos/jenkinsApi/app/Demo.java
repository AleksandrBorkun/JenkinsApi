package by.epam.kronos.jenkinsApi.app;

import java.io.File;
import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
import java.io.Writer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.io.Files;

import by.epam.kronos.jenkinsApi.entity.JenkinsJobList;
import by.epam.kronos.jenkinsApi.job.PrepareReportBuilder;
import by.epam.kronos.jenkinsApi.parser.ExcelParser;
import by.epam.kronos.jenkinsApi.property.PropertyProvider;
import by.epam.kronos.jenkinsApi.utils.ReportNameMaker;

public class Demo {
	public static final Logger log = LogManager.getLogger(PrepareReportBuilder.class);
//	private static final String FILE_NAME = "src/main/resources/JobNames.txt";
	private static PrepareReportBuilder jr = new PrepareReportBuilder();
	private static final String JOB_NAMES = PropertyProvider.getProperty("JOB_NAMES");
	
	public static void main(String[] args) throws IOException {
		
		String [] lines = JOB_NAMES.split(" "); //Files.readAllLines(Paths.get(FILE_NAME));
		for(String s: lines){
			File f = new File("D:/log.txt");
			Files.copy((new File("src/main/resources/JobNames.txt")), f);
			System.out.println(s);
			log.info(s);
		}
		
	/*	for (String jobName: lines) {
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
			log.info("Application Error. Information About jobs is Empty. Can't Run Excel Parser.\nApplication Closed");
		} else {
			log.info("Start writing the result to Excel file: " + ReportNameMaker.get());
			ExcelParser.getInstance().writeReportToExcel();
			log.info("Application close");
		} */
	}

}
