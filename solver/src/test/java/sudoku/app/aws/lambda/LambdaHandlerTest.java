package sudoku.app.aws.lambda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import sudoku.parsing.JSONResponse;
import sudoku.parsing.JSONSolveRequest;

public class LambdaHandlerTest {
	private ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void shouldCreateFakeJsonResponse() throws JsonProcessingException {
		List<List<String>> fakeResponse = new ArrayList<List<String>>();
		for (int x = 0; x < 9; x++) {
			fakeResponse.add(new ArrayList<String>());
			List<String> row = fakeResponse.get(x);
			for (int y = 0; y < 9; y++) {
				row.add(Integer.toString(y + 1));
			}
		}
		JSONResponse boardObject = new JSONResponse("Success", fakeResponse);
		String response = mapper.writeValueAsString(boardObject);
		String expectedJSON = "{ \"rows\":[[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"]]}";

		String responseNoWhiteSpace = removeWhiteSpace(response);
		String expectedNoWhiteSpace = removeWhiteSpace(expectedJSON);

		assertEquals(expectedNoWhiteSpace, responseNoWhiteSpace);

	}

	private String removeWhiteSpace(String input) {
		StringBuilder strBuilder = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			Character c = input.charAt(i);
			// only include non-white space
			if (!Character.isWhitespace(c)) {
				strBuilder.append(c);
			}
		}
		return strBuilder.toString();
	}
	
	@Test
	public void shouldSolveEasy() throws IOException {
		File inputFile = new File("src/test/resources/APIRequests/AWS_Lambda/HexadokuEasySolveRequest.json");
		String fileAsStr = new String(Files.readAllBytes(Paths.get(inputFile.getPath())));
		JSONSolveRequest request = mapper.readValue(fileAsStr, JSONSolveRequest.class);
		assertNotNull(request);
		
		// internals
		assertNotNull(request.getBoard());
		
		

	}

}
