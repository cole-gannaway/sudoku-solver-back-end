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

	private static List<List<String>> generateAllCombosOfCandidates(List<String> candidates) {
		ArrayList<List<String>> allCombos = new ArrayList<List<String>>();
		int n = candidates.size();
		for (int r = 1; r <= n; r++) {
			List<List<Integer>> combos = SudokuSolvingUtils.generateCombos(n, r);
			List<List<String>> subset = combos.stream()
					.map(list -> list.stream().map(index -> candidates.get(index)).collect(Collectors.toList()))
					.collect(Collectors.toList());
			allCombos.addAll(subset);
		}
		return allCombos;
	}

	public static List<String> findHiddenSet(SudokuCellDataBase db, SudokuCoordinate coordinate) {
		// generate all possible subsets of candidates
		List<String> candidates = db.getCandidatesForCell(coordinate);
		List<List<String>> subsets = generateAllCombosOfCandidates(candidates);
		subsets.removeIf(subset -> subset.size() < 2);

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

	/*
	 * Find a naked set with the following: (1) contains at least one common
	 * candidate (2) does not contain the look up coordinate
	 */
	public static NakedSetInfo findNakedSetForCoordinate(SudokuCellDataBase dataBase, SudokuCoordinate coordinate) {
		NakedSetInfo nakedSet = null;
		List<String> candidates = dataBase.getCandidatesForCell(coordinate);
		// get all possible candidate combinations
		List<List<String>> allPossibleCandidateCombos = generateAllCombosOfCandidates(dataBase.getPossibleCandidates());
		// filter combinations
		List<List<String>> filteredCandidateCombos = filterPossibleNakedSetCandidates(allPossibleCandidateCombos,
				candidates);
		Iterator<List<String>> candidateCombosIt = filteredCandidateCombos.iterator();
		ESudokuSection[] values = ESudokuSection.values();

		while (candidateCombosIt.hasNext()) {
			List<String> candidateCombo = candidateCombosIt.next();
			for (int i = 0; i < values.length && (!isValidNakedSet(nakedSet, coordinate, candidates)); i++) {
				ESudokuSection section = values[i];
				List<SudokuCoordinate> coordinates = new ArrayList<SudokuCoordinate>();
				// include coordinate in this one
				coordinates.add(coordinate);
				coordinates.addAll(dataBase.generateCoordinatesForSection(coordinate, Arrays.asList(section)));
				nakedSet = findNakedSet(dataBase, coordinates, candidateCombo);
				if (isValidNakedSet(nakedSet, coordinate, candidates)) {
					return nakedSet;
				}
			}
		}
		return null;

	}

	private static boolean isValidNakedSet(NakedSetInfo nakedSet, SudokuCoordinate testCoord, List<String> candidates) {
		if (nakedSet == null)
			return false;
		if (nakedSet.getCoordinates().size() < 2 || nakedSet.getCandidates().size() == 2)
			return false;
		if (nakedSet.containsCoordinate(testCoord))
			return false;
		if (!nakedSet.containsCommonCandidate(candidates))
			return false;
		return true;
	}

	static NakedSetInfo findNakedSet(SudokuCellDataBase dataBase, List<SudokuCoordinate> coordinates,
			List<String> candidates) {
		Iterator<SudokuCoordinate> it = coordinates.iterator();
		List<SudokuCoordinate> nakedSetCoords = new ArrayList<SudokuCoordinate>();
		while (it.hasNext()) {
			SudokuCoordinate coordinate = it.next();
			List<String> coordinateCandidates = dataBase.getCandidatesForCell(coordinate);
			// coordinateCandidates must be a subset of candidates
			if (isSubset(candidates, coordinateCandidates)) {
				nakedSetCoords.add(coordinate);
			}
		}
		if (nakedSetCoords.size() == candidates.size()) {
			NakedSetInfo nakedSet = new NakedSetInfo();
			nakedSet.setCandidates(candidates);
			nakedSet.setCoordinates(nakedSetCoords);
			return nakedSet;
		}
		return null;
	}

	// test candidates contain any coordinate that is not in candidates
	private static boolean isSubset(List<String> superSet, List<String> subSet) {
		// every value of subset must be contained in superSet
		for (String candidate : subSet) {
			if (!superSet.contains(candidate)) {
				return false;
			}
		}
		return true;
	}

	public static List<List<String>> filterPossibleNakedSetCandidates(List<List<String>> subsets,
			List<String> origCandidates) {
		return subsets.stream()//
				.filter(candidates -> candidates.size() > 1)//
				.filter(candidates -> containsSharedCandidates(origCandidates, candidates))//
				.collect(Collectors.toList());

	}

	public static boolean containsSharedCandidates(List<String> origCandidates, List<String> testCandidates) {
		for (String candidate : testCandidates) {
			if (origCandidates.contains(candidate)) {
				return true;
			}
		}
		return false;
	}

	public static List<String> getSharedCandidates(List<String> a, List<String> b) {
		List<String> aInB = a.stream().filter(candidate -> b.contains(candidate)).collect(Collectors.toList());
		return aInB;
	}
}
