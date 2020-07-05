package sudoku.elements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import sudoku.common.test.utils.CommonTestUtils;
import sudoku.enums.EBoardType;
import sudoku.enums.ESudokuSection;
import sudoku.parsing.CSVParser;

public class SudokuCellDataBaseTest {

	@Test
	public void testBuildDataBase() throws IOException, InterruptedException {
		File testFile = CommonTestUtils.getTestFile("/9by9/Puzzles/EasyPuzzle.csv");
		List<List<String>> fields = CSVParser.parseFile(testFile);
		List<String> possibleCandidateValues = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9");
		SudokuCellDataBase dataBase = SudokuCellDataBaseBuilder.buildDataBase(fields, possibleCandidateValues);
		assertEquals(81, dataBase.size());
	}

	@Test
	public void testGetListOfListOfCoordinates() throws FileNotFoundException {
		SudokuCellDataBase dataBase = CommonTestUtils.createFakeSudokuDataBase(9);
		int totalCount = 81;
		assertEquals(totalCount, dataBase.size());

		int singleThreadSize = dataBase.splitCoordinatesForNThreads(1).get(0).size();
		assertEquals(totalCount, singleThreadSize);

		List<List<SudokuCoordinate>> coordinates = dataBase.splitCoordinatesForNThreads(4);
		assertEquals(4, coordinates.size());

		for (int i = 0; i < coordinates.size() - 1; i++) {
			assertEquals(20, coordinates.get(i).size());
		}
		assertEquals(21, coordinates.get(coordinates.size() - 1).size());
	}

	@Test
	public void testGenerateCoordinatesForSegment() {
		int n = 9;
		SudokuCellDataBase dataBase = CommonTestUtils.createFakeSudokuDataBase(n);
		SudokuCoordinate testCoord = new SudokuCoordinate(1, 1);

		// create expected
		List<SudokuCoordinate> rowExpected = new ArrayList<SudokuCoordinate>();
		for (int i = 1; i <= n; i++) {
			rowExpected.add(new SudokuCoordinate(i, testCoord.getyCoordinate()));
		}
		rowExpected.remove(testCoord);
		// call function
		List<SudokuCoordinate> rowActual = dataBase.generateCoordinatesForSection(testCoord,
				Arrays.asList(ESudokuSection.ROW));

		// verify
		assertEquals(rowExpected, rowActual);

		// create expected
		List<SudokuCoordinate> colExpected = new ArrayList<SudokuCoordinate>();
		for (int i = 1; i <= n; i++) {
			colExpected.add(new SudokuCoordinate(testCoord.getxCoordinate(), i));
		}
		colExpected.remove(testCoord);

		// call function
		List<SudokuCoordinate> colActual = dataBase.generateCoordinatesForSection(testCoord,
				Arrays.asList(ESudokuSection.COLUMN));

		// verify
		assertEquals(colExpected, colActual);

		// create expected
		List<SudokuCoordinate> sqExpected = new ArrayList<SudokuCoordinate>();
		int sqSize = (int) Math.round(Math.sqrt(n));
		for (int x = 0; x < sqSize; x++) {
			for (int y = 0; y < sqSize; y++) {
				sqExpected.add(new SudokuCoordinate(testCoord.getxCoordinate() + x, testCoord.getyCoordinate() + y));
			}
		}
		sqExpected.remove(testCoord);

		// call function
		List<SudokuCoordinate> sqActual = dataBase.generateCoordinatesForSection(testCoord,
				Arrays.asList(ESudokuSection.SQUARE));

		// verify
		assertEquals(sqExpected, sqActual);
	}

	@Test
	public void testRemoveAllOtherCandidatesFromCell() {
		// set up
		int n = 9;
		List<String> possibleCandidateValues = EBoardType.getPossibleCandidateValues(EBoardType.SUDOKU, n);
		SudokuCellDataBase dataBase = CommonTestUtils.createFakeSudokuDataBase(n);
		SudokuCoordinate testCoord = new SudokuCoordinate(1, 1);

		// remove all values except one aka solve
		List<String> oneCandidate = possibleCandidateValues.subList(0, 1);

		// call function
		dataBase.removeAllOtherCandidatesFromCell(testCoord, oneCandidate);

		// verify it is solved
		assertEquals(1, dataBase.getCandidatesForCell(testCoord).size());
		assertNotNull(dataBase.getCellValue(testCoord));

		// set up
		testCoord = new SudokuCoordinate(1, 2);

		// remove only one value
		List<String> allButOne = possibleCandidateValues.subList(0, possibleCandidateValues.size() - 1);

		// call function
		dataBase.removeAllOtherCandidatesFromCell(testCoord, allButOne);

		// verify it is solved
		assertEquals(possibleCandidateValues.size() - 1, dataBase.getCandidatesForCell(testCoord).size());
		assertNull(dataBase.getCellValue(testCoord));
	}

}
