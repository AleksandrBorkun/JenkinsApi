package by.epam.kronos.jenkinsApi.job;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.offbytwo.jenkins.JenkinsServer;

import by.epam.kronos.jenkinsApi.parser.JSOUBParser;
import by.epam.kronos.jenkinsApi.property.PropertyProvider;

public class NoSOAPReportBuilder {
	public static final Logger log = LogManager.getLogger(PrepareReportBuilder.class);

	public void makeReportFromNotSOAPJob(String jobName, String buildNumber, JenkinsServer jenkins) {

		try {
			String url = jenkins.getJob(jobName).getBuildByNumber(Integer.parseInt(buildNumber)).getUrl();
			InputStream file = jenkins.getJob(jobName).getBuildByNumber(Integer.parseInt(buildNumber)).getClient()
					.getFile(URI.create(url + PropertyProvider.getProperty("ARTIFACT_URL")));
			log.info("Start to make a report for job: " + jobName);
			JSOUBParser.parseHTMLAndAddResultToJenkinsJobList(file, jobName);
		} catch (IOException e) {
			log.info("problem with connect:\n" + Arrays.toString(e.getStackTrace()));
		}
	}

	public void makeReportFromNotSOAPJob(String jobName, JenkinsServer jenkins) {

		try {
			String url = jenkins.getJob(jobName).getLastCompletedBuild().getUrl();
			InputStream file = jenkins.getJob(jobName).getLastBuild().getClient()
					.getFile(URI.create(url + PropertyProvider.getProperty("ARTIFACT_URL")));
			log.info("Start to make a report for job: " + jobName);
			JSOUBParser.parseHTMLAndAddResultToJenkinsJobList(file, jobName);
		} catch (IOException e) {
			log.info("problem with connect:\n" + Arrays.toString(e.getStackTrace()));
		}

	}

}
