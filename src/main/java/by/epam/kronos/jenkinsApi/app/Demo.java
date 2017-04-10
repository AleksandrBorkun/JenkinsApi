package by.epam.kronos.jenkinsApi.app;

import by.epam.kronos.jenkinsApi.job.PrepareReportBuilder;
import by.epam.kronos.jenkinsApi.parser.ExcelParser;

public class Demo {

	public static void main(String[] args) {

		PrepareReportBuilder jr = new PrepareReportBuilder();

		String[] s = {  "Flintstones_Attendance_ExceptionsEventsCollaboration_develop_soapui",
						"Gremlins_Attendance_RegressionUI_develop_selenium",
						"Flintstones_DocMgmt_apiTests_restAssured"};

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
