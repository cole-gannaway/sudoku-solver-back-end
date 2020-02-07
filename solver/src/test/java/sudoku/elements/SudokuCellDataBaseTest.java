package sudoku.elements;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import sudoku.common.test.utils.CommonTestUtils;
import sudoku.enums.EBoardType;
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
		File testFile = CommonTestUtils.getTestFile("/9by9/EasyPuzzle.csv");
		List<String[]> fields = CSVParser.parseFile(testFile);
		SudokuCellDataBase dataBase = SudokuCellDataBaseBuilder.buildDataBase(fields, EBoardType.SUDOKU);
		int totalCount = 81;
		assertEquals(totalCount, dataBase.size());

		int singleThreadSize = dataBase.getListOfListOfCoordinates(1).get(0).size();
		assertEquals(totalCount, singleThreadSize);

		List<List<SudokuCoordinate>> coordinates = dataBase.getListOfListOfCoordinates(4);
		assertEquals(4, coordinates.size());

		for (int i = 0; i < coordinates.size() - 1; i++) {
			assertEquals(20, coordinates.get(i).size());
		}
		assertEquals(21, coordinates.get(coordinates.size() - 1).size());
	}

}
