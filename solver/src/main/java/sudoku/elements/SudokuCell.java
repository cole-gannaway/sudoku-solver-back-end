package sudoku.elements;

import java.util.List;

import sudoku.enums.EBoardType;

public class SudokuCell {
	private String value;
	private Candidates candidates;

	SudokuCell(EBoardType boardType, int n) {
		candidates = new Candidates(boardType, n);
	}

	public String getValue() {
		return value;
	}

	public void solveCell(String value) {
		this.value = value;
	}
	
	public List<String> getCandidates(){
		return candidates.deepCopyList();
	}

	public void removeCandidate(String value) {
		if (!isSolved()) {
			String val = candidates.remove(value);
			if (val != null) {
				value = val;
			}
		}
	}

	public boolean isSolved() {
		return value != null;
	}

}
