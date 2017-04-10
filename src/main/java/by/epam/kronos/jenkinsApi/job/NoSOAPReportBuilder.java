package by.epam.kronos.jenkinsApi.job;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import com.offbytwo.jenkins.JenkinsServer;

import by.epam.kronos.jenkinsApi.parser.JSOUBParser;
import by.epam.kronos.jenkinsApi.property.PropertyProvider;
import by.epam.kronos.jenkinsApi.utils.MakeFileFromInputStream;

public class NoSOAPReportBuilder {

	private MakeFileFromInputStream converter = new MakeFileFromInputStream();

	public void makeReportFromNotSOAPJob(String jobName, String buildNumber, JenkinsServer jenkins) {

		try {
			String url = jenkins.getJob(jobName).getBuildByNumber(Integer.parseInt(buildNumber)).getUrl();

			InputStream file = jenkins.getJob(jobName).getBuildByNumber(Integer.parseInt(buildNumber)).getClient()
					.getFile(URI.create(url + PropertyProvider.getProperty("ARTIFACT_URL")));

			String fileName = converter.convert(file, jobName + "_#" + buildNumber);
			JSOUBParser.parseHTMLAndAddResultToJenkinsJobList(fileName, jobName);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void makeReportFromNotSOAPJob(String jobName, JenkinsServer jenkins) {

		try {
			String url = jenkins.getJob(jobName).getLastBuild().getUrl();
			InputStream file = jenkins.getJob(jobName).getLastBuild().getClient()
					.getFile(URI.create(url + PropertyProvider.getProperty("ARTIFACT_URL")));

			String fileName = converter.convert(file, jobName);
			JSOUBParser.parseHTMLAndAddResultToJenkinsJobList(fileName, jobName);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
