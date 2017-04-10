package by.epam.kronos.jenkinsApi.job;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.TestChildReport;

import by.epam.kronos.jenkinsApi.property.PropertyProvider;

public class PrepareReportBuilder {

	SOAPReportBuilder soapReport;
	NoSOAPReportBuilder noSoapReport;

	public void makeReport(String jobName) {

		JenkinsServer jenkins = new JenkinsServer(URI.create(PropertyProvider.getProperty("BASE_URL")));

		try {
			 	// here we launch SOAP report
			List<TestChildReport> testReportList = jenkins.getJob(jobName).getLastBuild().getTestReport().getChildReports();
			
			soapReport = new SOAPReportBuilder();
			soapReport.makeReport(jobName, testReportList);
			
		} catch (IOException e1) { // here we launch No SOAP Report
			
			noSoapReport = new NoSOAPReportBuilder();
			noSoapReport.makeReportFromNotSOAPJob(jobName, jenkins);
		}

	}

	public void makeReport(String jobName, String buildNumber) {

		JenkinsServer jenkins = new JenkinsServer(URI.create(PropertyProvider.getProperty("BASE_URL")));

		try { 
				// here we launch SOAP report
			List<TestChildReport> testReportList = jenkins.getJob(jobName)
					.getBuildByNumber(Integer.parseInt(buildNumber)).getTestReport().getChildReports();

			soapReport = new SOAPReportBuilder();
			soapReport.makeReport(jobName, buildNumber, testReportList);
			
		} catch (IOException e1) { // here we launch No SOAP Report

			noSoapReport = new NoSOAPReportBuilder();
			noSoapReport.makeReportFromNotSOAPJob(jobName, buildNumber, jenkins);
		}

	}
}


