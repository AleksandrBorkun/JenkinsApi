package by.epam.kronos.jenkinsApi.job;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.TestChildReport;

import by.epam.kronos.jenkinsApi.property.PropertyProvider;

public class PrepareReportBuilder {

	public static final Logger log = LogManager.getRootLogger();
	
	private JenkinsServer jenkins = null;
	private SOAPReportBuilder soapReport;
	private NoSOAPReportBuilder noSoapReport;

	public void makeReport(String jobName) {

		
		if(jenkins == null){
		log.info("Try to find Jenkins Server by BASE URL");
		jenkins = new JenkinsServer(URI.create(PropertyProvider.getProperty("BASE_URL")));
		}
		
		try {

			// here we launch SOAP report
			log.info("try to find job by Name " + jobName);
			List<TestChildReport> testReportList = jenkins.getJob(jobName).getLastCompletedBuild().getTestReport()
					.getChildReports();
			
			log.info("Go to SOAP Report Builder to prepare report");
			soapReport = new SOAPReportBuilder();
			soapReport.makeReport(jobName, testReportList);

		} catch (IOException e1) { // here we launch No SOAP Report
			log.info("Go to NO_SOAP_UA Report Builder to prepare report");
			noSoapReport = new NoSOAPReportBuilder();
			noSoapReport.makeReportFromNotSOAPJob(jobName, jenkins);
		} catch (NullPointerException e1) {
			log.info("The Job Name: " + jobName + " Does Not exist:\n" + Arrays.toString(e1.getStackTrace()));
		}

	}

	public void makeReport(String jobName, String buildNumber) {
		log.info("Try to find Jenkins Server by BASE URL");
		JenkinsServer jenkins = new JenkinsServer(URI.create(PropertyProvider.getProperty("BASE_URL")));

		try {
			// here we launch SOAP report
			log.info("try to find job by Name " + jobName + "#" + buildNumber);
			List<TestChildReport> testReportList = jenkins.getJob(jobName)
					.getBuildByNumber(Integer.parseInt(buildNumber)).getTestReport().getChildReports();
			
			log.info("Go to SOAP Report Builder to prepare report");
			soapReport = new SOAPReportBuilder();
			soapReport.makeReport(jobName, buildNumber, testReportList);

		} catch (IOException e1) { // here we launch No SOAP Report

			log.info("Go to NO_SOAP_UI Report Builder to prepare report");
			noSoapReport = new NoSOAPReportBuilder();
			noSoapReport.makeReportFromNotSOAPJob(jobName, buildNumber, jenkins);
		} catch (NullPointerException e1) {
			log.info("The Job Name: " + jobName + " Does Not exist:\n" + Arrays.toString(e1.getStackTrace()));
		}

	}
}
