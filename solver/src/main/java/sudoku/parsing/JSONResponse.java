package sudoku.parsing;

import java.util.List;

public class JSONResponse {
	private List<List<String>> rows;

	/* Default constructor for Jackson JSON Parser */
	public JSONResponse() {
	}
	
	public JSONResponse(String message, List<List<String>> rows) {
		this.rows = rows;
	}

	public List<List<String>> getRows() {
		return rows;
	}

	public void setRows(List<List<String>> rows) {
		this.rows = rows;
	}

}
