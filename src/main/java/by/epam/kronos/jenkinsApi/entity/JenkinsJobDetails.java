package by.epam.kronos.jenkinsApi.entity;

import java.util.ArrayList;
import java.util.List;

public class JenkinsJobDetails {
	
	private int countOfFail;
	private String jobName;
	private List<TestSuiteFromJenkins> testSuiteList = new ArrayList();

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public List<TestSuiteFromJenkins> getTestSuiteList() {
		return testSuiteList;
	}

	public void addTestSuiteToList(TestSuiteFromJenkins testSuiteList) {
		this.testSuiteList.add(testSuiteList);
	}

	public int getCountOfFail() {
		return countOfFail;
	}

	public void setCountOfFail(int countOfFail) {
		this.countOfFail = countOfFail;
	}

}
