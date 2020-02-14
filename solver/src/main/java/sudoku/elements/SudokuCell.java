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

	public void removeAllOtherCandidates(List<String> set) {
		if (!isSolved()) {
			String val = candidates.removeAllOthers(set);
			if (val != null) {
				value = val;
			}
		}

	}

	public void removeCandidate(String value) {
		if (!isSolved()) {
			String val = candidates.remove(value);
			if (val != null) {
				value = val;
			}
		}
	}

	public List<String> getCandidates() {
		return candidates.deepCopyList();
	}

	public boolean isSolved() {
		return value != null;
	}

	@Override
	public String toString() {
		return value;
	}

}
