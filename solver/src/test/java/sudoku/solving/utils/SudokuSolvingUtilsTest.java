package sudoku.solving.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import sudoku.common.test.utils.CommonTestUtils;
import sudoku.elements.SudokuCellDataBase;
import sudoku.elements.SudokuCoordinate;
import sudoku.enums.EBoardType;
import sudoku.enums.ESudokuSection;

public class SudokuSolvingUtilsTest {

	@Test
	public void testSetCandidates() {
		// set up
		int n = 9;
		SudokuCellDataBase dataBase = CommonTestUtils.createFakeSudokuDataBase(n);
		dataBase.solveCell(new SudokuCoordinate(1, 1), "1");

		// call function
		SudokuCoordinate testCoord = new SudokuCoordinate(1, 2);
		SudokuSolvingUtils.setCandidates(dataBase, testCoord);

		// verify
		assertEquals(n - 1, dataBase.getCandidatesForCell(testCoord).size());
	}

	@Test
	public void testHasUniqueCandidate() throws IOException {
		// set up
		int n = 9;
		SudokuCellDataBase dataBase = CommonTestUtils.createFakeSudokuDataBase(n);
		assertEquals(81, dataBase.size());

		SudokuCoordinate testCoord = new SudokuCoordinate(5, 1);
		CommonTestUtils.setCandidatesOnAllCells(dataBase);

		String candidate = "9";
		List<SudokuCoordinate> coordinates = dataBase.generateCoordinatesForSection(testCoord,
				Arrays.asList(ESudokuSection.SQUARE));
		for (SudokuCoordinate coordinate : coordinates) {
			dataBase.removeCandidateFromCell(coordinate, candidate);
		}

		// call function
		String actual = SudokuSolvingUtils.hasUniqueCandidate(dataBase, testCoord);

		// verify
		assertEquals(candidate, actual);
	}

	@Test
	public void testGenerateCombos() {
		int n = 4;
		List<Integer> expectedSizeList = Arrays.asList(4, 6, 4, 1);
		List<Integer> actualSizeList = new ArrayList<Integer>();
		for (int r = 1; r <= n; r++) {
			List<List<Integer>> combos = SudokuSolvingUtils.generateCombos(n, r);
			actualSizeList.add(combos.size());
		}
		assertEquals(expectedSizeList, actualSizeList);
	}

	@Test
	public void testFindHiddenSet() throws FileNotFoundException {
		// set up
		int n = 9;
		SudokuCellDataBase dataBase = CommonTestUtils.createFakeSudokuDataBase(n);
		List<SudokuCoordinate> hiddenSetCoords = Arrays.asList(new SudokuCoordinate(1, 1), new SudokuCoordinate(1, 3));
		List<SudokuCoordinate> otherCoords = dataBase.splitCoordinatesForNThreads(1).stream()
				.<SudokuCoordinate>flatMap(list -> list.stream().map(str -> str)).collect(Collectors.toList());
		otherCoords.removeAll(hiddenSetCoords);
		List<String> allPossibleCandidates = EBoardType.getPossibleCandidateValues(EBoardType.SUDOKU, n);

		// hiddenSet and otherCoords share one value that they should remove
		int index = 2;
		List<String> hiddenSetCandidates = allPossibleCandidates.subList(0, index + 1);
		List<String> otherCoordsCandidates = allPossibleCandidates.subList(index, n);
		List<String> expectedHiddenSet = allPossibleCandidates.subList(0, index);

		// remove expectedHiddenSet canidates from other cells
		for (SudokuCoordinate othercoord : otherCoords) {
			dataBase.removeAllOtherCandidatesFromCell(othercoord, otherCoordsCandidates);
		}

		// remove all except hiddenSet + 1
		for (SudokuCoordinate hiddenSetCoord : hiddenSetCoords) {
			dataBase.removeAllOtherCandidatesFromCell(hiddenSetCoord, hiddenSetCandidates);
		}

		// call function
		for (SudokuCoordinate hiddenSetCoord : hiddenSetCoords) {
			List<String> hiddenSet = SudokuSolvingUtils.findHiddenSet(dataBase, hiddenSetCoord);
			assertEquals(expectedHiddenSet, hiddenSet);
			dataBase.removeAllOtherCandidatesFromCell(hiddenSetCoord, hiddenSet);
			assertEquals(expectedHiddenSet, dataBase.getCandidatesForCell(hiddenSetCoord));
		}
	}

	@Test
	public void testFindNakedSet() throws FileNotFoundException {
		// set up
		int n = 9;
		SudokuCellDataBase dataBase = CommonTestUtils.createFakeSudokuDataBase(n);
		SudokuCoordinate aCoordinate = new SudokuCoordinate(1, 1);
		List<SudokuCoordinate> squareCoordinates = new ArrayList<SudokuCoordinate>();
		squareCoordinates.add(aCoordinate);
		squareCoordinates
				.addAll(dataBase.generateCoordinatesForSection(aCoordinate, Arrays.asList(ESudokuSection.SQUARE)));
		assertEquals(n, squareCoordinates.size());

		// set up env
		List<String> nakedSetCandidates = Arrays.asList("1", "3", "7", "9");
		int four = 4;
		List<SudokuCoordinate> nakedSetCoordinates = squareCoordinates.subList(0, four);
		for (SudokuCoordinate nakedSetCoordinate : nakedSetCoordinates) {
			dataBase.removeAllOtherCandidatesFromCell(nakedSetCoordinate, nakedSetCandidates);
		}
		List<SudokuCoordinate> otherCoordinates = squareCoordinates.subList(four, n);
		// should contain at list one of the nakedSetCandidates
		List<String> otherCandidates = Arrays.asList("1", "4", "5", "6", "7");
		for (SudokuCoordinate otherCoordinate : otherCoordinates) {
			dataBase.removeAllOtherCandidatesFromCell(otherCoordinate, otherCandidates);
		}

		// set up function call
		List<String> candidates = dataBase.getPossibleCandidates();
		List<List<String>> subsets = new ArrayList<List<String>>();
		for (int r = 1; r <= n; r++) {
			List<List<Integer>> combos = SudokuSolvingUtils.generateCombos(n, r);
			List<List<String>> subset = combos.stream()
					.map(list -> list.stream().map(index -> candidates.get(index)).collect(Collectors.toList()))
					.collect(Collectors.toList());
			subsets.addAll(subset);
		}

		// call function
		NakedSetInfo nakedSet = SudokuSolvingUtils.findNakedSet(dataBase, aCoordinate);
		assertNotEquals(0, nakedSet.getCoordinates().size());
		assertEquals(nakedSetCoordinates, nakedSet.getCoordinates());
		assertEquals(nakedSetCandidates, nakedSet.getCandidates());

	}
}
