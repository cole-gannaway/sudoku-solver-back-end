package sudoku.parsing;

import java.util.List;

public class JSONResponse {
	private List<List<String>> rows;
	private boolean solved;

	/* Default constructor for Jackson JSON Parser */
	public JSONResponse() {
	}

	public List<List<String>> getRows() {
		return rows;
	}

	public void setRows(List<List<String>> rows) {
		this.rows = rows;
	}

	public boolean isSolved() {
		return solved;
	}

	public void setSolved(boolean solved) {
		this.solved = solved;
	}
}
