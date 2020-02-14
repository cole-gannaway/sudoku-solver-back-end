package sudoku.thread;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import sudoku.common.test.utils.CommonTestUtils;
import sudoku.elements.SudokuCellDataBase;
import sudoku.elements.SudokuCellDataBaseBuilder;
import sudoku.enums.EBoardType;
import sudoku.enums.EDifficulty;
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
	@Ignore
	public void testTimeout() throws ExecutionException, TimeoutException {
		int numOfThreads = 1;
		SudokuCellDataBase db = CommonTestUtils.createFakeSudokuDataBase(9);
		SudokuThreadManager threadManager = new SudokuThreadManager(db, numOfThreads);
		threadManager.solve(2, TimeUnit.SECONDS);
	}

	@Test
	public void testSolveSingleThread() throws IOException {
		// single test
//		String lookUpId = "ImpossiblePuzzle";
//		solveSingleThread(lookUpId);

		// loop through
		List<TestFileParsedInfo> list = configFileInfos.stream()
				.filter(info -> !info.getDifficulty().equals(EDifficulty.EVIL))//
				.collect(Collectors.toList());
		for (TestFileParsedInfo info : list) {
			solveSingleThread(info.getId());
		}
	}

	@Test
	@Ignore
	public void testSolveMultiThread() throws IOException {
		// single test
//		String lookUpId = "HardPuzzle";
//		solveMultiThread(lookUpId, 3);

		// loop through
		List<TestFileParsedInfo> list = configFileInfos.stream()
				.filter(info -> !info.getDifficulty().equals(EDifficulty.EVIL))//
				.collect(Collectors.toList());
		for (TestFileParsedInfo info : list) {
			solveMultiThread(info.getId(), 2);
		}
	}

	private void solveSingleThread(String lookUpId) throws IOException {
		// lookUp
		TestFileParsedInfo fileInfo = CommonTestUtils.getFirstTestFileInfoById(lookUpId, configFileInfos);

		// get info from lookUp
		EBoardType boardType = fileInfo.getBoardType();
		File testFile = CommonTestUtils.getTestFile(fileInfo.getPuzzleFilePath());
		File answerFile = CommonTestUtils.getTestFile(fileInfo.getAnswerFilePath());

		// create database
		List<List<String>> fields = CSVParser.parseFile(testFile);
		SudokuCellDataBase dataBase = SudokuCellDataBaseBuilder.buildDataBase(fields, boardType);
		String beforeHTML = dataBase.toHTML();
		CommonTestUtils.setCandidatesOnAllCells(dataBase);

		// run threads
		int numOfThreads = 1;
		SudokuThreadManager threadManager = new SudokuThreadManager(dataBase, numOfThreads);
		long startTimeMillis = System.currentTimeMillis();
		threadManager.solve(3, TimeUnit.SECONDS);
		Long executionTimeMillis = System.currentTimeMillis() - startTimeMillis;

		// verify output
		List<List<String>> expectedFields = CSVParser.parseFile(answerFile);
		System.out.println("Checking " + lookUpId + " against answer after " + executionTimeMillis + "ms");
		List<List<String>> actualFields = dataBase.toCSV();
		boolean verified = CommonTestUtils.compareCSVOutputs(actualFields, expectedFields);
		if (!verified) {
			// show output in browser
			outputBeforeAndAfterHTML(beforeHTML, dataBase.toHTML());

			System.out.println(CommonTestUtils.getTestValidityOutputString(actualFields));
		}
		assertTrue(verified);
	}

	private void solveMultiThread(String lookUpId, int numOfThreads) throws IOException {
		// lookUp
		TestFileParsedInfo fileInfo = CommonTestUtils.getFirstTestFileInfoById(lookUpId, configFileInfos);

		// get info from lookUp
		EBoardType boardType = fileInfo.getBoardType();
		File testFile = CommonTestUtils.getTestFile(fileInfo.getPuzzleFilePath());
		File answerFile = CommonTestUtils.getTestFile(fileInfo.getAnswerFilePath());

		// create database
		List<List<String>> fields = CSVParser.parseFile(testFile);
		SudokuCellDataBase dataBase = SudokuCellDataBaseBuilder.buildDataBase(fields, boardType);
		String beforeHTML = dataBase.toHTML();
		CommonTestUtils.setCandidatesOnAllCells(dataBase);

		// run threads
		SudokuThreadManager threadManager = new SudokuThreadManager(dataBase, numOfThreads);
		long startTimeMillis = System.currentTimeMillis();
		threadManager.solve(3, TimeUnit.SECONDS);
		Long executionTimeMillis = System.currentTimeMillis() - startTimeMillis;

		// verify output
		List<List<String>> expectedFields = CSVParser.parseFile(answerFile);
		System.out
				.println("[Multithread] Checking " + lookUpId + " against answer after " + executionTimeMillis + "ms");
		List<List<String>> actualFields = dataBase.toCSV();
		boolean verified = CommonTestUtils.compareCSVOutputs(actualFields, expectedFields);
		if (!verified) {
			// show output in browser
			outputBeforeAndAfterHTML(beforeHTML, dataBase.toHTML());

			System.out.println(CommonTestUtils.getTestValidityOutputString(actualFields));
		}
		assertTrue(verified);

	}

	private void outputBeforeAndAfterHTML(String beforeHTML, String afterHTML) throws IOException {
		CommonTestUtils.saveHTMLFileAsOutput("before.html", beforeHTML);
		CommonTestUtils.openHTMLFileInBrowser("before.html");

		CommonTestUtils.saveHTMLFileAsOutput("after.html", afterHTML);
		CommonTestUtils.openHTMLFileInBrowser("after.html");
	}

}
