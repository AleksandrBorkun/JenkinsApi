package by.epam.kronos.jenkinsApi.entity;

import java.util.ArrayList;
import java.util.List;

import com.offbytwo.jenkins.model.BaseModel;

public class JenkinsBuild extends BaseModel {
	
	private Integer id;
	private String url;
	private List<JenkinsSubBuild> subBuilds;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public List<JenkinsSubBuild> getSubBuilds() {
		return subBuilds != null ? subBuilds : new ArrayList<JenkinsSubBuild>();
	}
	
	public void setSubBuilds(List<JenkinsSubBuild> subBuilds) {
		this.subBuilds = subBuilds;
	}

}
