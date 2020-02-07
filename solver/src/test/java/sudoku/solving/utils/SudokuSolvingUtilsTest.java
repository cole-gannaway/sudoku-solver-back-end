package sudoku.solving.utils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
		File testFile = CommonTestUtils.getTestFile("/9by9/EasyPuzzle.csv");
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

}
