package sudoku.app.aws.lambda;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import sudoku.parsing.JSONResponse;

public class LambdaHandlerTest {

	@Test
	public void shouldCreateFakeJsonResponse() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
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
		String expectedJSON = "{ \"message\": \"Success\" ,\"rows\":[[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"]]}";

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

}
