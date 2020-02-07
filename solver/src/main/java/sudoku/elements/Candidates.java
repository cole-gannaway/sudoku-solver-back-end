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

	public List<String> deepCopyList() {
		return new ArrayList<String>(candidateVals);
	}

}
