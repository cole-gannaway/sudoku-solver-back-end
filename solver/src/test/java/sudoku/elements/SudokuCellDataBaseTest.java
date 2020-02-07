package sudoku.elements;

import static org.junit.Assert.assertEquals;

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
	public void test9By9() throws IOException, InterruptedException {
		File testFile = CommonTestUtils.getTestFile("/9by9/EasyPuzzle.csv");
		List<String[]> fields = CSVParser.parseFile(testFile);
		SudokuCellDataBase dataBase = SudokuCellDataBaseBuilder.buildDataBase(fields, EBoardType.SUDOKU);
		assertEquals(81, dataBase.size());
		String htmlFileName = "before.html";
		CommonTestUtils.saveHTMLFileAsOutput(htmlFileName, dataBase.toHTML());
		CommonTestUtils.openHTMLFileInBrowser(htmlFileName);

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
		List<SudokuCoordinate> rowActual = dataBase.generateCoordinatesForSegment(testCoord,
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
		List<SudokuCoordinate> colActual = dataBase.generateCoordinatesForSegment(testCoord,
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
		List<SudokuCoordinate> sqActual = dataBase.generateCoordinatesForSegment(testCoord,
				Arrays.asList(ESudokuSection.SQUARE));

		// verify
		assertEquals(sqExpected, sqActual);
	}

}
