package sudoku.parsing.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import sudoku.enums.EBoardType;
import sudoku.enums.EConfigFileProperties;
import sudoku.enums.EDifficulty;

public class ConfigFileReader {
	public static List<TestFileParsedInfo> readConfigFile(File configFile)
			throws FileNotFoundException, IOException, ParseException {
		// parsing file "JSONExample.json"
		Object obj = new JSONParser().parse(new FileReader(configFile));

		// typecasting obj to JSONObject
		JSONObject jo = (JSONObject) obj;

		// getting firstName and lastName
		JSONArray testConfigs = (JSONArray) jo.get(EConfigFileProperties.TESTCONFIGS.getValue());
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> it = testConfigs.iterator();
		List<TestFileParsedInfo> retVal = new ArrayList<TestFileParsedInfo>();
		while (it.hasNext()) {
			JSONObject testConfig = (JSONObject) it.next();
			String id = (String) testConfig.get(EConfigFileProperties.ID.getValue());
			String boardTypeStr = (String) testConfig.get(EConfigFileProperties.BOARDTYPE.getValue());
			String difficultStr = (String) testConfig.get(EConfigFileProperties.DIFFICULTY.getValue());
			String puzzleFilePath = getFilePath((JSONObject) testConfig.get(EConfigFileProperties.PUZZLE.getValue()));
			String answerFilePath = getFilePath((JSONObject) testConfig.get(EConfigFileProperties.ANSWER.getValue()));

			EBoardType boardType = EBoardType.getValueOf(boardTypeStr);
			EDifficulty difficulty = EDifficulty.getValueOf(difficultStr);
			TestFileParsedInfo testFileInfo = new TestFileParsedInfo(id, puzzleFilePath, answerFilePath, boardType,
					difficulty);
			retVal.add(testFileInfo);
		}

		return retVal;

	}

	private static String getFilePath(JSONObject file) {
		String filePath = (String) file.get(EConfigFileProperties.FILEPATH.getValue());
		return filePath;
	}
}
