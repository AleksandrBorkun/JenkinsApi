package by.epam.kronos.jenkinsApi.job;

import java.util.List;

import com.offbytwo.jenkins.model.TestCase;
import com.offbytwo.jenkins.model.TestChildReport;
import com.offbytwo.jenkins.model.TestSuites;

import by.epam.kronos.jenkinsApi.entity.JenkinsJobDetails;
import by.epam.kronos.jenkinsApi.entity.JenkinsJobList;
import by.epam.kronos.jenkinsApi.entity.TestCasesFromSuite;
import by.epam.kronos.jenkinsApi.entity.TestSuiteFromJenkins;
import by.epam.kronos.jenkinsApi.property.PropertyProvider;

public class SOAPReportBuilder {

	public void makeReport(String jobName, List<TestChildReport> testReportList) {

		JenkinsJobDetails jobDetails = new JenkinsJobDetails();
		TestCasesFromSuite testCaseDetail;
		TestSuiteFromJenkins testSuiteDetail;
		int allTestsFailedInJob = 0;
		int totalJobTests = 0;

		for (TestChildReport testReport : testReportList) {

			for(TestSuites testSuite :testReport.getResult().getSuites()){
	
				if (testSuite.getName().contains("DSL") || testSuite.getName().contains("SetUp")) {
					continue;
					}
				
				for(TestCase caseCount: testSuite.getCases()){
					
					if(caseCount.getName().toUpperCase().contains("DSL") || caseCount.getName().toUpperCase().contains("SETUP"))
					continue;
						totalJobTests++;
				}
				
				
				}
			
			if (testReport.getResult().getFailCount() == 0) {
				
				continue;
			}
			for (TestSuites testSuite : testReport.getResult().getSuites()) {
				if (testSuite.getName().contains("DSL") || testSuite.getName().contains("SetUp")) {
					continue;
				}
				
				int count = 0;
				testSuiteDetail = new TestSuiteFromJenkins();

				for (TestCase testCase : testSuite.getCases()) {
					testCaseDetail = null;

					if (testCase.getErrorDetails() == null) {
						
						continue;
					} else {
						
						testCaseDetail = new TestCasesFromSuite();
						testCaseDetail.setTestCaseName(testCase.getName());
						testCaseDetail.setErrorLog(testCase.getErrorDetails());
						// System.out.println();
						count++;
						allTestsFailedInJob++;
						testSuiteDetail.addTestCaseToList(testCaseDetail);
					}
				}

				if (count != 0) {
					testSuiteDetail.setSuiteName(testSuite.getName());
					testSuiteDetail.setCountOfFailedTests(count);
					jobDetails.addTestSuiteToList(testSuiteDetail);

				}

			}

			jobDetails.setJobName(jobName);
			jobDetails.setCountOfFail(allTestsFailedInJob);

		}

		if (allTestsFailedInJob == 0) {
			jobDetails.setJobName(jobName);
		}
		jobDetails.setTotalTestsCount(totalJobTests);
		JenkinsJobList.getInstance().addJenkinsJobToList(jobDetails);

	}

	public void makeReport(String jobName, String buildNumber, List<TestChildReport> testReportList) {

		JenkinsJobDetails jobDetails = new JenkinsJobDetails();
		TestCasesFromSuite testCaseDetail;
		TestSuiteFromJenkins testSuiteDetail;
		int allTestsFailedInJob = 0;
		int totalJobTests = 0;

		for (TestChildReport testReport : testReportList) {

			if (testReport.getResult().getFailCount() == 0) {
				continue;
			}
			for (TestSuites testSuite : testReport.getResult().getSuites()) {
				if (testSuite.getName().contains("DSL") || testSuite.getName().contains("SetUp")) {
					continue;
				}
				for (TestCase tstCase : testSuite.getCases()) {
					if (tstCase.getName().toUpperCase().contains("DSL")
							|| tstCase.getName().toUpperCase().contains("SETUP")) {
						continue;
					}
					totalJobTests++;
				}
				int count = 0;
				testSuiteDetail = new TestSuiteFromJenkins();

				for (TestCase testCase : testSuite.getCases()) {
					testCaseDetail = null;

					if (testCase.getErrorDetails() == null) {
						continue;
					} else {
						testCaseDetail = new TestCasesFromSuite();
						testCaseDetail.setTestCaseName(testCase.getName());
						testCaseDetail.setErrorLog(testCase.getErrorDetails());
						count++;
						allTestsFailedInJob++;
						testSuiteDetail.addTestCaseToList(testCaseDetail);
					}
				}

				if (count != 0) {
					testSuiteDetail.setSuiteName(testSuite.getName());
					testSuiteDetail.setCountOfFailedTests(count);
					jobDetails.addTestSuiteToList(testSuiteDetail);

				}

			}

			jobDetails.setJobName(jobName + " #" + buildNumber);
			jobDetails.setCountOfFail(allTestsFailedInJob);

		}

		if (allTestsFailedInJob == 0) {
			jobDetails.setJobName(jobName + " #" + buildNumber);
		}
		jobDetails.setTotalTestsCount(totalJobTests);
		JenkinsJobList.getInstance().addJenkinsJobToList(jobDetails);


	}
}
