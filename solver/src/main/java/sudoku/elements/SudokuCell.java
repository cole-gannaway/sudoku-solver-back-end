package sudoku.elements;

import java.util.List;

public class SudokuCell {
	private String value;
	private Candidates candidates;

	public SudokuCell(List<String> possibleCandidateValues) {
		candidates = new Candidates(possibleCandidateValues);
	}

	public String getValue() {
		return value;
	}

	public void solveCell(String value) {
		this.value = value;
		candidates.removeAllExcept(value);
	}

	public List<String> getCandidates() {
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
