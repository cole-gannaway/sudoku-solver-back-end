package sudoku.thread;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import sudoku.common.test.utils.CommonTestUtils;
import sudoku.elements.SudokuCellDataBase;
import sudoku.elements.SudokuCellDataBaseBuilder;
import sudoku.enums.EBoardType;
import sudoku.parsing.CSVParser;
import sudoku.parsing.config.ConfigFileReader;
import sudoku.parsing.config.TestFileParsedInfo;

public class SudokuThreadManagerTest {
	private List<TestFileParsedInfo> configFileInfos;

	@Before
	public void setUp() throws FileNotFoundException, IOException, ParseException {
		configFileInfos = ConfigFileReader.readConfigFile(CommonTestUtils.getTestFile("config.json"));
	}

	@Test
	public void testTimeout() throws ExecutionException, TimeoutException {
		int numOfThreads = 1;
		SudokuCellDataBase db = CommonTestUtils.createFakeSudokuDataBase(9);
		SudokuThreadManager threadManager = new SudokuThreadManager(db, numOfThreads);
		threadManager.solve(2, TimeUnit.SECONDS);
	}

	@Test
	public void testSolveSingleThread() throws IOException {
		// lookUp
		String lookUpId = "EasyPuzzle16by16";
		Optional<TestFileParsedInfo> optFileInfo = configFileInfos.stream()
				.filter(info -> info.getId().equals(lookUpId)).findFirst();
		assertTrue(optFileInfo.isPresent());
		TestFileParsedInfo fileInfo = optFileInfo.get();

		// get info from lookUp
		EBoardType boardType = fileInfo.getBoardType();
		File testFile = CommonTestUtils.getTestFile(fileInfo.getPuzzleFilePath());
		File answerFile = CommonTestUtils.getTestFile(fileInfo.getAnswerFilePath());

		// create database
		List<String[]> fields = CSVParser.parseFile(testFile);
		SudokuCellDataBase dataBase = SudokuCellDataBaseBuilder.buildDataBase(fields, boardType);
		CommonTestUtils.setCandidatesOnAllCells(dataBase);

		// run threads
		int numOfThreads = 1;
		SudokuThreadManager threadManager = new SudokuThreadManager(dataBase, numOfThreads);
		long startTimeMillis = System.currentTimeMillis();
		threadManager.solve(3, TimeUnit.SECONDS);
		Long executionTimeMillis = System.currentTimeMillis() - startTimeMillis;

		// show results
		String htmlFileName = "after.html";
		String html = dataBase.toHTML(lookUpId, executionTimeMillis.toString() + " ms");
		CommonTestUtils.saveHTMLFileAsOutput(htmlFileName, html);
		CommonTestUtils.openHTMLFileInBrowser(htmlFileName);

		// verify output
		List<String[]> expectedFields = CSVParser.parseFile(answerFile);
		SudokuCellDataBase expectedDataBase = SudokuCellDataBaseBuilder.buildDataBase(expectedFields, boardType);
		assertTrue(CommonTestUtils.compareCSVOutputs(dataBase, expectedDataBase));
	}

	@Test
	public void testSolveMultiThread() throws IOException {
		// lookUp
		String lookUpId = "EasyPuzzle16by16";
		Optional<TestFileParsedInfo> optFileInfo = configFileInfos.stream()
				.filter(info -> info.getId().equals(lookUpId)).findFirst();
		assertTrue(optFileInfo.isPresent());
		TestFileParsedInfo fileInfo = optFileInfo.get();

		// get info from lookUp
		EBoardType boardType = fileInfo.getBoardType();
		File testFile = CommonTestUtils.getTestFile(fileInfo.getPuzzleFilePath());
		File answerFile = CommonTestUtils.getTestFile(fileInfo.getAnswerFilePath());

		// create database
		List<String[]> fields = CSVParser.parseFile(testFile);
		SudokuCellDataBase dataBase = SudokuCellDataBaseBuilder.buildDataBase(fields, boardType);
		CommonTestUtils.setCandidatesOnAllCells(dataBase);

		// run threads
		int numOfThreads = 4;
		SudokuThreadManager threadManager = new SudokuThreadManager(dataBase, numOfThreads);
		threadManager.solve(3, TimeUnit.SECONDS);

		// verify output
		List<String[]> expectedFields = CSVParser.parseFile(answerFile);
		SudokuCellDataBase expectedDataBase = SudokuCellDataBaseBuilder.buildDataBase(expectedFields, boardType);
		assertTrue(CommonTestUtils.compareCSVOutputs(dataBase, expectedDataBase));
	}

}
