package sudoku.parsing;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import sudoku.dev.utils.FileUtils;

public class FormatConverterUtils {
	public static void convertJSON2CSV(File f) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		List<List<String>> parsed = CSVParser.parseFile(f);
		JSONBoardObject outputObj = new JSONBoardObject(parsed, null);
		String outputJson = mapper.writeValueAsString(outputObj);
		File outputFile = new File(f.getParent() + File.separator + f.getName().replace(".csv", ".json"));
		FileUtils.writeStringToFile(outputFile, outputJson);
	}
}
