package sudoku.parsing;

import java.util.List;

public class JSONBoardInformation {
	private List<List<String>> rows;
	private List<String> possibleValues;

	/* Default constructor for Jackson JSON Parser */
	public JSONBoardInformation() {

	}

	public List<List<String>> getRows() {
		return rows;
	}

	public void setRows(List<List<String>> rows) {
		this.rows = rows;
	}

	public List<String> getPossibleValues() {
		return possibleValues;
	}

	public void setPossibleValues(List<String> possibleValues) {
		this.possibleValues = possibleValues;
	}

}
