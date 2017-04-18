package by.epam.kronos.jenkinsApi.parser;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import by.epam.kronos.jenkinsApi.entity.JenkinsJobDetails;
import by.epam.kronos.jenkinsApi.entity.JenkinsJobList;
import by.epam.kronos.jenkinsApi.entity.TestCasesFromSuite;
import by.epam.kronos.jenkinsApi.entity.TestSuiteFromJenkins;

public class JSOUBParser {

	public static void parseHTMLAndAddResultToJenkinsJobList(String fileName, String jobName) {

		JenkinsJobDetails jobDet = null;
		TestCasesFromSuite testCase = null;
		TestSuiteFromJenkins testSuite = null;
		String suiteNameTemp = null;
		File file = new File(fileName);
		Document doc;
		int countOfFailedTests = 0;
		int countOfFailedTestsInSuite = 0;

		try {
			doc = Jsoup.parse(file, "UTF-8");

			int totalTestsCount = doc.getElementsByAttributeValue("class", "test-name").size(); // total
			int countOfSkipedTests = doc.getElementsByAttributeValue("class", "test-status label right outline capitalize skip").size();	//count of skiped																			// tests

			Elements links = doc.getElementsByTag("td");

			jobDet = new JenkinsJobDetails();
			jobDet.setJobName(jobName);
			jobDet.setTotalTestsCount(totalTestsCount);

			for (Element el : links) {

				for (Element errorLog : el.getElementsByAttributeValue("class", "step-details")) {
					for (int i = 0; i < errorLog.getElementsContainingText("with failure").size(); i++) {

						Element last = errorLog.getElementsContainingText("with failure").get(i);

						String suiteName;
						suiteName = last.parent().parent().parent().parent().parent()
								.getElementsByAttributeValue("class", "test-desc").get(0).child(1).text().substring(16); // suiteName(Package)

						if (testSuite == null || !suiteName.equals(suiteNameTemp)) {
							if (testSuite != null) {
								testSuite.setCountOfFailedTests(countOfFailedTestsInSuite);
								jobDet.addTestSuiteToList(testSuite);
							}

							testSuite = new TestSuiteFromJenkins();
							testSuite.setSuiteName(suiteName);
							suiteNameTemp = suiteName;
							countOfFailedTestsInSuite = 0;

						}

						String testCaseName = last.parent().parent().parent().parent().parent().parent()
								.getElementsByAttributeValue("class", "test-head").get(0).child(0).text(); // failed
																											// test
																											// name

						testCase = new TestCasesFromSuite();
						testCase.setTestCaseName(testCaseName);

						testCase.setErrorLog(last.text()); // Error Log
						testSuite.addTestCaseToList(testCase);
						countOfFailedTests++;
						countOfFailedTestsInSuite++;

					}

				}
			}

			testSuite.setCountOfFailedTests(countOfFailedTestsInSuite);
			jobDet.setCountOfFail(countOfFailedTests);
			jobDet.addTestSuiteToList(testSuite);
			jobDet.setCountOfSkip(countOfSkipedTests);
			JenkinsJobList.getInstance().addJenkinsJobToList(jobDet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
