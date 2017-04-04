package by.epam.kronos.jenkinsApi.app;

import by.epam.kronos.jenkinsApi.job.JenkinsReportBuilder;
import by.epam.kronos.jenkinsApi.parser.ExcelParser;

public class Demo {

	public static void main(String[] args) {

		JenkinsReportBuilder jr = new JenkinsReportBuilder();

		String[] s = { "Flintstones_Attendance_DomainAPI_develop_soapui",
						"Flintstones_Attendance_ExceptionsActions_develop_soapui" };

		for (int i = 0; i < s.length; i++) {
			String[] a = null;

			if (s[i].contains("/")) {
				a = s[i].split("/");
			}

			if (a != null) {
				jr.makeReport(a[0], a[1]);

			} else {
				jr.makeReport(s[i]);
			}
		}
		ExcelParser.getInstance().writeReportToExcel();

	}

}
