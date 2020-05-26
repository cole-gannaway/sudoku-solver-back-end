package sudoku.parsing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONParser {
	public static List<List<String>> parseFile(File f) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String fileAsStr = new String(Files.readAllBytes(Paths.get(f.getPath())));
		JSONBoardObject jsonBoardObject = mapper.readValue(fileAsStr,JSONBoardObject.class);
		return jsonBoardObject.getRows();
	}
}
