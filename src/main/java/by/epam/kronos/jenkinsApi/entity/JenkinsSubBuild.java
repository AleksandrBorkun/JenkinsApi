package by.epam.kronos.jenkinsApi.entity;

public class JenkinsSubBuild {

	private Integer buildNumber;
	private String jobName;
	
	public Integer getBuildNumber() {
		return buildNumber;
	}
	
	public void setBuildNumber(Integer buildNumber) {
		this.buildNumber = buildNumber;
	}
	
	public String getJobName() {
		return jobName;
	}
	
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
}
