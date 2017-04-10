package by.epam.kronos.jenkinsApi.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MakeFileFromInputStream {

	InputStream inputStream = null;
	OutputStream outputStream = null;
	String fullFileName = null;

	public String convert(InputStream file, String fileName) {

		try {
			String fullFileName = "src/main/resources/" + fileName + ".html";
			// read this file into InputStream
			inputStream = file;

			// write the inputStream to the File
			outputStream = new FileOutputStream(new File(fullFileName));

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			return fullFileName;

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					// outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		return null;
	}

}
