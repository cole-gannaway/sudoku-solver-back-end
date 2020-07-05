package sudoku.parsing;

import java.util.List;

public class JSONResponse {
	private String message;
	private List<List<String>> rows;

	/* Default constructor for Jackson JSON Parser */
	public JSONResponse() {
	}
	
	public JSONResponse(String message, List<List<String>> rows) {
		this.message = message;
		this.rows = rows;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<List<String>> getRows() {
		return rows;
	}

	public void setRows(List<List<String>> rows) {
		this.rows = rows;
	}

}
