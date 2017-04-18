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

	private JenkinsJobDetails jobDetails = new JenkinsJobDetails();
	private TestCasesFromSuite testCaseDetail;
	private TestSuiteFromJenkins testSuiteDetail;
	private int allTestsFailedInJob = 0;
	private int totalJobTests = 0;
	private int countOfSkiped = 0;
	private int countOfPassedTests;
	private boolean isSkipSuitePresent = false;
	
	public void makeReport(String jobName, List<TestChildReport> testReportList) {

		for (TestChildReport testReport : testReportList) {

			getCountOfAllTestsInJob(testReport); //count of all and count of skiped adding to JobDetails
			
			if (testReport.getResult().getFailCount() == 0) {
				
				continue;
			}
			for (TestSuites testSuite : testReport.getResult().getSuites()) {
			
				for(String checkName:PropertyProvider.getProperty("TESTS_FOR_SKIP").split("/")){
					
					if(testSuite.getName().toUpperCase().contains(checkName.toUpperCase())){	//
						isSkipSuitePresent = true;                                              //
						break;																	// This code	
					}																			//	check the suite name 
				}																				//	and skip it
																								//	if this name
				if (isSkipSuitePresent) {														//	contains in property file
					isSkipSuitePresent = false;													//
					continue;																	//			
				}
				
				int count = 0;
				testSuiteDetail = new TestSuiteFromJenkins();

				for (TestCase testCase : testSuite.getCases()) {
					testCaseDetail = null;

					for(String checkName:PropertyProvider.getProperty("TESTS_FOR_SKIP").split("/")){
						
						if(testCase.getName().toUpperCase().contains(checkName.toUpperCase())){	//
							isSkipSuitePresent = true;                                              //
							break;																	// This code	
						}																			//	check the testCase name 
					}																				//	and skip it
																									//	if this name
					if (isSkipSuitePresent) {														//	contains in property file
						isSkipSuitePresent = false;													//
						continue;																	//			
					}
					
					if (testCase.getErrorDetails() == null) {
						
						continue;
					} else {
						
						testCaseDetail = new TestCasesFromSuite();
						testCaseDetail.setTestCaseName(testCase.getName());
						testCaseDetail.setErrorLog(testCase.getErrorStackTrace());
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
		//jobDetails.setTotalTestsCount(totalJobTests);
		JenkinsJobList.getInstance().addJenkinsJobToList(jobDetails);

	}

	public void makeReport(String jobName, String buildNumber, List<TestChildReport> testReportList) {

		for (TestChildReport testReport : testReportList) {
			
			getCountOfAllTestsInJob(testReport); //count of all and count of skiped adding to JobDetails

			if (testReport.getResult().getFailCount() == 0) {
				continue;
			}
			
			
			for (TestSuites testSuite : testReport.getResult().getSuites()) {

				for(String checkName:PropertyProvider.getProperty("TESTS_FOR_SKIP").split("/")){
					
					if(testSuite.getName().toUpperCase().contains(checkName.toUpperCase())){	//
						isSkipSuitePresent = true;                                              //
						break;																	// This code	
					}																			//	check the suite name 
				}																				//	and skip it
																								//	if this name
				if (isSkipSuitePresent) {														//	contains in property file
					isSkipSuitePresent = false;													//
					continue;																	//			
				}
				

				int count = 0;
				testSuiteDetail = new TestSuiteFromJenkins();

				for (TestCase testCase : testSuite.getCases()) {
					testCaseDetail = null;

					for(String checkName:PropertyProvider.getProperty("TESTS_FOR_SKIP").split("/")){
						
						if(testCase.getName().toUpperCase().contains(checkName.toUpperCase())){	//
							isSkipSuitePresent = true;                                              //
							break;																	// This code	
						}																			//	check the testCase name 
					}																				//	and skip it
																									//	if this name
					if (isSkipSuitePresent) {														//	contains in property file
						isSkipSuitePresent = false;													//
						continue;																	//			
					}
					
					
					if (testCase.getErrorDetails() == null) {
						continue;
					} else {
						testCaseDetail = new TestCasesFromSuite();
						testCaseDetail.setTestCaseName(testCase.getName());
						testCaseDetail.setErrorLog(testCase.getErrorStackTrace());
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

		JenkinsJobList.getInstance().addJenkinsJobToList(jobDetails);


	}
	
	private void getCountOfAllTestsInJob(TestChildReport testReport){

		for(TestSuites testSuite :testReport.getResult().getSuites()){
			
			if (testSuite.getName().contains("DSL") || testSuite.getName().contains("SetUp")) {
				continue;
				}
			
			for(TestCase caseCount: testSuite.getCases()){
				
				if(caseCount.getName().toUpperCase().contains("DSL") || caseCount.getName().toUpperCase().contains("SETUP"))
				continue;
				if(caseCount.isSkipped()){
					countOfSkiped++;
				}
				if(caseCount.getStatus().equals("PASSED")){
					countOfPassedTests++;
				}
				totalJobTests++;
			}
			}
		
		jobDetails.setCountOfPass(countOfPassedTests);
		jobDetails.setCountOfSkip(countOfSkiped);
		jobDetails.setTotalTestsCount(totalJobTests);
	}
	
}
