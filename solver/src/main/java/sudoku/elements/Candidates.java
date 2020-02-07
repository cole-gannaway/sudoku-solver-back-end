package sudoku.elements;

import java.util.ArrayList;
import java.util.List;

public class Candidates {

	List<String> candidateVals;

	public Candidates(List<String> candidateVals) {
		this.candidateVals = candidateVals;
	}

	public String remove(String value) {
		candidateVals.remove(value);
		if (candidateVals.size() == 1) {
			return candidateVals.get(0);
		}
		return null;
	}

	public String removeAllExcept(String value) {
		if (candidateVals.contains(value)) {
			candidateVals.removeIf(val -> !(val.equals(value)));
			return candidateVals.get(0);
		} else {
			System.err.println("Candidates: (removeAllExcept) Value = " + value + " does not exist in candidates");
			return null;
		}
	}

	public List<String> deepCopyList() {
		return new ArrayList<String>(candidateVals);
	}

}
