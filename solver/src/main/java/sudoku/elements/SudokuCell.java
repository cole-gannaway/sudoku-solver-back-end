package sudoku.elements;

public class SudokuCell {
	private String value;

	public String getValue() {
		return value;
	}

	public void solveCell(String value) {
		this.value = value;
	}

}
