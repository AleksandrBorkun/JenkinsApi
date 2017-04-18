package by.epam.kronos.jenkinsApi.app;

import by.epam.kronos.jenkinsApi.entity.JenkinsJobList;
import by.epam.kronos.jenkinsApi.job.PrepareReportBuilder;
import by.epam.kronos.jenkinsApi.parser.ExcelParser;
import by.epam.kronos.jenkinsApi.utils.ReportNameMaker;

public class Demo {

	public static void main(String[] args) {

		PrepareReportBuilder jr = new PrepareReportBuilder();

		String[] s = {  "Flintstones_Attendance_FAPControl_develop_soapui",
						"Flintstones_Attendance_SetupATtests_develop_soapui", 
						"Flintstones_Attendance_EmploymentStatus_develop_soapui",
						"Flintstones_Attendance_DomainAPI_develop_soapui",
						"Flintstones_Attendance_PunchAndComment_develop_soapui",
						"Flintstones_Attendance_ExceptionsEventsCollaboration_develop_soapui",
						"Flintstones_Attendance_ExceptionsActions_develop_soapui",
						"Flintstones_Attendance_WorkedPayCodeAndComment_develop_soapui",
						"Flintstones_Attendance_ExceptionAndWorkedPayCode_develop_soapui",
						"Flintstones_Attendance_Patterns_develop_soapui",
						"Flintstones_Attendance_ScheduledAndWorkedPaycode_develop_soapui",
						"Flintstones_Attendance_ExceptionAndComment_develop_soapui",
						"Flintstones_Attendance_JTF-WAT-PARS_develop_soapui",
						"Flintstones_Attendance_WATJTFtests_develop_soapui",
						"Flintstones_Attendance_WATSmokeTest_develop_soapui",
						"Flintstones_Attendance_JTF-WAT-PP_develop_soapui"};

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
		if (JenkinsJobList.getInstance().getJenkinsJobList().isEmpty()) {
			jr.log.info("Application Error. Information About jobs is Empty. Can't Run Excel Parser.\nApplication Closed");
		} else {
			jr.log.info("Start writing the result to Excel file: " + ReportNameMaker.get());
			ExcelParser.getInstance().writeReportToExcel();
			jr.log.info("Application close");
		}
	}

}
