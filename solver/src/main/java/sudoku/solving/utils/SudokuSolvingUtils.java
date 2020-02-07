package sudoku.solving.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import sudoku.elements.SudokuCellDataBase;
import sudoku.elements.SudokuCoordinate;
import sudoku.enums.ESudokuSection;

public class SudokuSolvingUtils {

	public static void setCandidates(SudokuCellDataBase db, SudokuCoordinate coordinate) {
		for (ESudokuSection section : ESudokuSection.values()) {
			List<SudokuCoordinate> otherCoordinates = db.generateCoordinatesForSection(coordinate,
					Arrays.asList(section));
			setCandidates(db, coordinate, otherCoordinates);
		}
	}

	private static void setCandidates(SudokuCellDataBase db, SudokuCoordinate coordinate,
			List<SudokuCoordinate> otherCoordinates) {
		Iterator<SudokuCoordinate> it = otherCoordinates.iterator();
		while (it.hasNext()) {
			SudokuCoordinate lookUpCoord = it.next();
			String value = db.getCellValue(lookUpCoord);
			if (value != null) {
				db.removeCandidateFromCell(coordinate, value);
			}
		}
		return;
	}

	public static String hasUniqueCandidate(SudokuCellDataBase db, SudokuCoordinate coordinate) {
		String retVal = null;
		ESudokuSection[] values = ESudokuSection.values();
		for (int i = 0; i < values.length && retVal == null; i++) {
			ESudokuSection section = values[i];
			List<SudokuCoordinate> otherCoordinates = db.generateCoordinatesForSection(coordinate,
					Arrays.asList(section));
			retVal = hasUniqueCandidate(db, coordinate, otherCoordinates);
		}
		return retVal;
	}

	private static String hasUniqueCandidate(SudokuCellDataBase db, SudokuCoordinate coordinate,
			List<SudokuCoordinate> otherCoordinates) {
		String retVal = null;
		Iterator<String> testIt = db.getCandidatesForCell(coordinate).iterator();
		while (testIt.hasNext()) {
			String testCandiate = testIt.next();
			int matchCount = 0;
			Iterator<SudokuCoordinate> lookUpIt = otherCoordinates.iterator();
			while (lookUpIt.hasNext()) {
				SudokuCoordinate lookUpCoord = lookUpIt.next();
				List<String> lookUpCandidates = db.getCandidatesForCell(lookUpCoord);
				if (lookUpCandidates.contains(testCandiate)) {
					matchCount++;
				}
			}
			if (matchCount == 0) {
				retVal = testCandiate;
				break;
			}
		}
		return retVal;
	}

	/* package for testing */
	static List<List<Integer>> generateCombos(int n, int r) {
		List<List<Integer>> combinations = new ArrayList<List<Integer>>();
		List<Integer> combination = new ArrayList<Integer>(r);

		// initialize with lowest lexicographic combination
		for (int i = 0; i < r; i++) {
			combination.add(i);
		}

		while (combination.get(r - 1) < n) {
			combinations.add(cloneList(combination));

			// generate next combination in lexicographic order
			int t = r - 1;
			while (t != 0 && combination.get(t) == n - r + t) {
				t--;
			}
			// increment
			combination.set(t, combination.get(t) + 1);
			for (int i = t + 1; i < r; i++) {
				combination.set(i, combination.get(i - 1) + 1);
			}
		}

		return combinations;
	}

	private static List<Integer> cloneList(List<Integer> listToClone) {
		List<Integer> list = new ArrayList<Integer>();
		for (int val : listToClone) {
			list.add(val);
		}
		return list;
	}

	public static List<String> findHiddenSet(SudokuCellDataBase db, SudokuCoordinate coordinate) {
		// generate all possible subsets of candidates
		List<String> candidates = db.getCandidatesForCell(coordinate);
		List<List<String>> subsets = new ArrayList<List<String>>();
		int n = candidates.size();
		for (int r = 1; r <= n; r++) {
			List<List<Integer>> combos = SudokuSolvingUtils.generateCombos(n, r);
			List<List<String>> subset = combos.stream()
					.map(list -> list.stream().map(index -> candidates.get(index)).collect(Collectors.toList()))
					.collect(Collectors.toList());
			subsets.addAll(subset);
		}
		subsets.removeIf(subset-> subset.size() < 2);

		// check all sections
		List<String> retVal = null;
		ESudokuSection[] values = ESudokuSection.values();
		for (int i = 0; i < values.length && retVal == null; i++) {
			ESudokuSection section = values[i];
			List<SudokuCoordinate> otherCoordinates = db.generateCoordinatesForSection(coordinate,
					Arrays.asList(section));
			retVal = findHiddenSet(db, coordinate, subsets, otherCoordinates);
		}
		return retVal;
	}

	private static List<String> findHiddenSet(SudokuCellDataBase db, SudokuCoordinate coordinate,
			List<List<String>> subsets, List<SudokuCoordinate> otherCoordinates) {
		// iterate through each subset
		// in each subset, iterate through other coordinates
		// add to matchset if contains a portion of the subset
		// after all check the matchset size
		// num of matches = subset size - 1

		Iterator<List<String>> subsetsIt = subsets.iterator();
		while (subsetsIt.hasNext()) {
			List<String> subset = subsetsIt.next();
			Iterator<SudokuCoordinate> coordsIt = otherCoordinates.iterator();
			int matches = 0;
			while (coordsIt.hasNext()) {
				SudokuCoordinate lookUpCoord = coordsIt.next();
				List<String> lookUpCandidates = db.getCandidatesForCell(lookUpCoord);
				if (containsHiddenSet(subset, lookUpCandidates)) {
					// add to match set
					matches++;
				}
			}
			if (matches == subset.size() - 1) {
				return subset;
			}
		}
		return null;

	}

	private static boolean containsHiddenSet(List<String> subset, List<String> lookUpCandidates) {
		// just has to contain 1
		for (String val : subset) {
			if (lookUpCandidates.contains(val))
				return true;
		}
		return false;

	}
}
