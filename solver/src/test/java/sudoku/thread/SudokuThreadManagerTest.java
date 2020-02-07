package sudoku.thread;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Ignore;
import org.junit.Test;

import sudoku.common.test.utils.CommonTestUtils;
import sudoku.elements.SudokuCellDataBase;
import sudoku.elements.SudokuCellDataBaseBuilder;
import sudoku.enums.EBoardType;
import sudoku.parsing.CSVParser;

public class SudokuThreadManagerTest {

	@Test
	@Ignore
	public void testTimeout() throws ExecutionException, TimeoutException {
		int numOfThreads = 1;
		SudokuCellDataBase db = CommonTestUtils.createFakeSudokuDataBase(9);
		SudokuThreadManager threadManager = new SudokuThreadManager(db, numOfThreads);
		threadManager.solve(2, TimeUnit.SECONDS);
	}

	@Test
	public void testSolveSingleThread() throws IOException {
		// set up
		File testFile = CommonTestUtils.getTestFile("9by9/Puzzles/EasyPuzzle.csv");
		List<String[]> fields = CSVParser.parseFile(testFile);
		EBoardType boardType = EBoardType.SUDOKU;
		SudokuCellDataBase dataBase = SudokuCellDataBaseBuilder.buildDataBase(fields, boardType);
		CommonTestUtils.setCandidatesOnAllCells(dataBase);

		int numOfThreads = 1;
		SudokuThreadManager threadManager = new SudokuThreadManager(dataBase, numOfThreads);
		threadManager.solve(30, TimeUnit.SECONDS);

		String htmlFileName = "after.html";
		CommonTestUtils.saveHTMLFileAsOutput(htmlFileName, dataBase.toHTML());
		CommonTestUtils.openHTMLFileInBrowser(htmlFileName);

		// test output
		File answerFile = CommonTestUtils.getTestFile("9by9/Answers/EasyPuzzleAnswer.csv");
		List<String[]> expectedFields = CSVParser.parseFile(answerFile);
		SudokuCellDataBase expectedDataBase = SudokuCellDataBaseBuilder.buildDataBase(expectedFields, boardType);
		assertTrue(CommonTestUtils.compareCSVOutputs(dataBase, expectedDataBase));
	}

}
