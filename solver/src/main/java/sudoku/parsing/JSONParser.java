package sudoku.parsing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONParser {
	public static JSONSolveRequest parseJSONSolveRequest(File f) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String fileAsStr = new String(Files.readAllBytes(Paths.get(f.getPath())));
		JSONSolveRequest retVal = mapper.readValue(fileAsStr, JSONSolveRequest.class);
		return retVal;
	}
}
