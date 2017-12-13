package by.epam.kronos.jenkinsApi.job;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.TestChildReport;

import by.epam.kronos.jenkinsApi.entity.JenkinsBuild;
import by.epam.kronos.jenkinsApi.entity.JenkinsSubBuild;
import by.epam.kronos.jenkinsApi.property.PropertyProvider;

public class PrepareReportBuilder {

	public static final Logger log = LogManager.getLogger(PrepareReportBuilder.class);

	private JenkinsHttpClient client = null;
	private JenkinsServer jenkins = null;
	private SOAPReportBuilder soapReport;
	private NoSOAPReportBuilder noSoapReport;
	private static final String BASE_URL = PropertyProvider.getProperty("BASE_URL");

	public void startPrepearing(String jobName, int buildNumber) throws IOException{

		if (client == null) {
			log.info("Try to find Jenkins Server by BASE URL: " + BASE_URL);
			restartServer();
		}

			try {
				log.info("Try to find " + jobName);
				restartServer();
				buildNumber = buildNumber != 0 ? buildNumber : jenkins.getJob(jobName).getLastCompletedBuild().getNumber();
				JenkinsBuild jenkinsBuild = client.get(String.format("/job/%s/%s/",jobName, buildNumber),  JenkinsBuild.class);
				if(jenkinsBuild.getSubBuilds().size() != 0){
					log.info("This is a multi job '" + jobName + "' Go to find the result of each jobs");

					for(JenkinsSubBuild subBuild : jenkinsBuild.getSubBuilds()){
						if(!subBuild.getJobName().equals("KATE_Report_Builder") && !subBuild.getJobName().equals("Attendance_WFMTeam_Deployment") && !subBuild.getJobName().equals("TenantProvisioning"))
							startPrepearing(subBuild.getJobName(), subBuild.getBuildNumber());
					}	
				}
				else{
					if(buildNumber != 0){
						makeReport(jobName, buildNumber);
					}
					else{
						// deprecated method
						makeReport(jobName);
					}
				}
			} catch (IOException e) {
					log.info("Something was wrong. Can't find the job name: '" + jobName + "' try it again /n"+Arrays.toString(e.getStackTrace()));
			}
	}

	private void makeReport(String jobName, int buildNumber) throws IOException {
		
		try {
			restartServer();
			int duration = (int)jenkins.getJob(jobName).getBuildByNumber(buildNumber).details().getDuration();
			// here we launch SOAP report by build number
			log.info("try to find job by Name " + jobName + "#" + buildNumber);
			List<TestChildReport> testReportList = jenkins.getJob(jobName)
					.getBuildByNumber(buildNumber).getTestReport().getChildReports();
			log.info("Go to SOAP Report Builder to prepare report");
			soapReport = new SOAPReportBuilder();
			soapReport.makeReport(jobName, buildNumber, testReportList, duration);
		} catch (IOException e1) {
			restartServer();
			log.info("Go to NO_SOAP_UI Report Builder find job");					//
			noSoapReport = new NoSOAPReportBuilder();								// here we launch No SOAP Report
			noSoapReport.makeReportFromNotSOAPJob(jobName, buildNumber, jenkins);	//
		} catch (NullPointerException e1) {
			log.info("The Job Name: " + jobName + " Does Not exist:\n" + Arrays.toString(e1.getStackTrace()));
		}
	}
	
	private void makeReport(String jobName){
		
		try {
			int duration = (int)jenkins.getJob(jobName).getLastCompletedBuild().details().getDuration();
			// here we launch latest SOAP report
			log.info("try to find job by Name " + jobName);
			List<TestChildReport> testReportList = jenkins.getJob(jobName).getLastCompletedBuild().getTestReport()
					.getChildReports();
			log.info("Go to SOAP Report Builder to prepare report");
			soapReport = new SOAPReportBuilder();
			soapReport.makeReport(jobName, testReportList, duration);
		} catch (IOException e1) { 											
			log.info("Go to NO_SOAP_UI Report Builder find job");		//
			noSoapReport = new NoSOAPReportBuilder();					// here we launch No SOAP Report
			noSoapReport.makeReportFromNotSOAPJob(jobName, jenkins);	//
		} catch (NullPointerException e1) {
			log.info("The Job Name: " + jobName + " Does Not exist:\n" + Arrays.toString(e1.getStackTrace()));
		}
		
		
	}
	
	private void restartServer() throws IOException{
		if(jenkins != null){
			jenkins.quietDown();
			jenkins = null;
			client = null;
		}
		client = new JenkinsHttpClient(URI.create(BASE_URL));
		jenkins = new JenkinsServer(client);
		
	}
	
}
