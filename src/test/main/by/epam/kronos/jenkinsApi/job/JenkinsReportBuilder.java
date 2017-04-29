package by.epam.kronos.jenkinsApi.job;

import java.io.IOException;
import java.net.URI;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.TestCase;
import com.offbytwo.jenkins.model.TestChildReport;
import com.offbytwo.jenkins.model.TestSuites;

import by.epam.kronos.jenkinsApi.entity.JenkinsJobDetails;
import by.epam.kronos.jenkinsApi.entity.JenkinsJobList;
import by.epam.kronos.jenkinsApi.entity.TestCasesFromSuite;
import by.epam.kronos.jenkinsApi.entity.TestSuiteFromJenkins;

public class JenkinsReportBuilder {

	public void makeReport(String jobName) {

		JenkinsServer jenkins = new JenkinsServer(URI.create("http://kvs-us-kate02.int.kronos.com:8080"));
		JenkinsJobDetails jobDetails = new JenkinsJobDetails();
		TestCasesFromSuite testCaseDetail;
		TestSuiteFromJenkins testSuiteDetail;
		int allTestsFailedInJob = 0;

		try {
			for (TestChildReport testReport : jenkins.getJob(jobName).getLastBuild().getTestReport()
					.getChildReports()) {

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
		} catch (IOException e) {
			///////////////// Here must be a msg "wrong job name"
			e.printStackTrace();
		}

		JenkinsJobList.getInstance().addJenkinsJobToList(jobDetails);

		try {
			jenkins.cancelQuietDown();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void makeReport(String jobName, String buildNumber) {

		JenkinsServer jenkins = new JenkinsServer(URI.create("http://kvs-us-kate02.int.kronos.com:8080"));
		JenkinsJobDetails jobDetails = new JenkinsJobDetails();
		TestCasesFromSuite testCaseDetail;
		TestSuiteFromJenkins testSuiteDetail;
		int allTestsFailedInJob = 0;

		try {
			for (TestChildReport testReport : jenkins.getJob(jobName).getBuildByNumber(Integer.parseInt(buildNumber))
					.getTestReport().getChildReports()) {

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
		} catch (IOException e) {
			///////////////// Here must be a msg "wrong job name"
			e.printStackTrace();
		}

		JenkinsJobList.getInstance().addJenkinsJobToList(jobDetails);

		try {
			jenkins.cancelQuietDown();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
