package by.epam.kronos.jenkinsApi.entity;

import java.util.ArrayList;
import java.util.List;

public class JenkinsJobList {

	private void JenkinsJobList() {

	}

	private final static JenkinsJobList INSTANCE = new JenkinsJobList();

	public static JenkinsJobList getInstance() {
		return INSTANCE;
	}

	private List<JenkinsJobDetails> jenkinsJobList = new ArrayList();

	public List<JenkinsJobDetails> getJenkinsJobList() {
		return jenkinsJobList;
	}

	public void addJenkinsJobToList(JenkinsJobDetails jenkinsJobList) {
		this.jenkinsJobList.add(jenkinsJobList);
	}

}
