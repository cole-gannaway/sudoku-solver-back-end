package sudoku.parsing.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigFileReader {

	public static String getFileContents(File f) throws FileNotFoundException {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(f);
		StringBuilder builder = new StringBuilder();
		while (sc.hasNextLine()) {
			builder.append(sc.nextLine());
		}
		return replaceAllWhiteSpace(builder.toString());
	}

	private static String replaceAllWhiteSpace(String str) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char charAt = str.charAt(i);
			if (!Character.isWhitespace(charAt)) {
				builder.append(charAt);
			}
		}
		return builder.toString();
	}

	public static List<TestFileParsedInfo> readConfigFile(File configFile)
			throws FileNotFoundException, IOException, ParseException {
		String jsonStr = getFileContents(configFile);
		TestFileParsedInfoList testFileParsedInfo = new ObjectMapper().readValue(jsonStr, TestFileParsedInfoList.class);

		return testFileParsedInfo.getTestConfigs();

	}

}
