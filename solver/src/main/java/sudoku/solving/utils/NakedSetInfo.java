package sudoku.solving.utils;

import java.util.List;

import sudoku.elements.SudokuCoordinate;

public class NakedSetInfo {
	private List<String> candidates;
	private List<SudokuCoordinate> coordinates;

	public boolean containsCoordinate(SudokuCoordinate testCoord) {
		return coordinates.contains(testCoord);
	}

	public boolean containsCommonCandidate(List<String> pCandidates) {
		return SudokuSolvingUtils.containsSharedCandidates(candidates, pCandidates);
	}

	public List<String> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<String> candidates) {
		this.candidates = candidates;
	}

	public List<SudokuCoordinate> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<SudokuCoordinate> coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public String toString() {
		return "NakedSetInfo [candidates=" + candidates + ", coordinates=" + coordinates + "]";
	}

}
