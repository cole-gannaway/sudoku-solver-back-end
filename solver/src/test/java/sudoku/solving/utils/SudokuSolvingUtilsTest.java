package sudoku.solving.utils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import sudoku.common.test.utils.CommonTestUtils;
import sudoku.elements.SudokuCellDataBase;
import sudoku.elements.SudokuCellDataBaseBuilder;
import sudoku.elements.SudokuCoordinate;
import sudoku.enums.EBoardType;
import sudoku.parsing.CSVParser;

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
		File testFile = CommonTestUtils.getTestFile("/9by9/Puzzles/EasyPuzzle.csv");
		List<String[]> fields = CSVParser.parseFile(testFile);
		SudokuCellDataBase dataBase = SudokuCellDataBaseBuilder.buildDataBase(fields, EBoardType.SUDOKU);
		assertEquals(81, dataBase.size());
		CommonTestUtils.setCandidatesOnAllCells(dataBase);

		// call function
		SudokuCoordinate testCoord = new SudokuCoordinate(5, 1);
		String actual = SudokuSolvingUtils.hasUniqueCandidate(dataBase, testCoord);

		// verify
		assertEquals("9", actual);
	}

	@Test
	public void testGenerateCombos() {
		int n = 4;
		List<Integer> expectedSizeList = Arrays.asList(4, 6, 4, 1);
		List<Integer> actualSizeList = new ArrayList<Integer>();
		for (int r = 1; r <= n; r++) {
			List<List<Integer>> combos = SudokuSolvingUtils.generateCombos(n, r);
			System.out.println(combos);
			actualSizeList.add(combos.size());
		}
		assertEquals(expectedSizeList, actualSizeList);
	}

	@Test
	public void testIsInHiddenSet() throws FileNotFoundException {
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

}
