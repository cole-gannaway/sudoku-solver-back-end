package sudoku.thread;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.json.simple.parser.ParseException;
import org.junit.Before;
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
	private Predicate<? super TestFileParsedInfo> filterByDifficulty = info -> !info.getDifficulty()
			.equals(EDifficulty.EVIL);
	Predicate<? super TestFileParsedInfo> filterBySize = info -> !info.getId().contains("16by16");

	@Before
	public void setUp() throws FileNotFoundException, IOException, ParseException {
		configFileInfos = ConfigFileReader.readConfigFile(CommonTestUtils.getTestFile("config.json"));
	}

	@Test
//	@Ignore
	public void testTimeout() throws ExecutionException, TimeoutException {
		int numOfThreads = 1;
		SudokuCellDataBase db = CommonTestUtils.createFakeSudokuDataBase(9);
		SudokuThreadManager threadManager = new SudokuThreadManager(db, numOfThreads);
		long startTimeMillis = System.currentTimeMillis();
		TimeUnit timeUnit = TimeUnit.SECONDS;
		int duration = 1;
		threadManager.solve(duration, timeUnit);
		Long executionTimeMillis = System.currentTimeMillis() - startTimeMillis;
		assertTrue(executionTimeMillis > timeUnit.toMillis(duration));
	}

	@Test
//	@Ignore
	public void testSolveSingleThread() throws IOException {
		// test all filtered
		List<TestFileParsedInfo> list = configFileInfos.stream()//
				.filter(filterByDifficulty)//
				.filter(filterBySize)//
				.collect(Collectors.toList());
		for (TestFileParsedInfo info : list) {
			solve(info.getId(), 1, "[Single Thread]");
		}
	}

	@Test
//	@Ignore
	public void testSolveMultiThread() throws IOException {
		// test all filtered
		List<TestFileParsedInfo> list = configFileInfos.stream()//
				.filter(filterByDifficulty)//
				.filter(filterBySize)//
				.collect(Collectors.toList());
		for (TestFileParsedInfo info : list) {
			solve(info.getId(), 4, "[Multi Thread]");
		}
	}

	private void solve(String lookUpId, int numOfThreads, String solveType) throws IOException {
		// lookUp
		TestFileParsedInfo fileInfo = CommonTestUtils.getFirstTestFileInfoById(lookUpId, configFileInfos);

		// get info from lookUp
		EBoardType boardType = fileInfo.getBoardType();
		File testFile = CommonTestUtils.getTestFile(fileInfo.getPuzzleFilePath());
		File answerFile = CommonTestUtils.getTestFile(fileInfo.getAnswerFilePath());

		// create database
		List<List<String>> fields = CSVParser.parseFile(testFile);
		SudokuCellDataBase dataBase = SudokuCellDataBaseBuilder.buildDataBase(fields, boardType);
		String beforeHTML = dataBase.toHTML(lookUpId, "");
		CommonTestUtils.setCandidatesOnAllCells(dataBase);

		// run threads
		SudokuThreadManager threadManager = new SudokuThreadManager(dataBase, numOfThreads);
		long startTimeMillis = System.currentTimeMillis();
		threadManager.solve(20, TimeUnit.SECONDS);
		Long executionTimeMillis = System.currentTimeMillis() - startTimeMillis;

		// verify output
		List<List<String>> expectedFields = CSVParser.parseFile(answerFile);
		System.out.println(solveType + " Checking " + lookUpId + " against answer after " + executionTimeMillis + "ms");
		List<List<String>> actualFields = dataBase.toCSV();
		boolean verified = CommonTestUtils.compareCSVOutputs(actualFields, expectedFields);
		if (!verified) {
			// show output in browser
			outputBeforeAndAfterHTML(beforeHTML, dataBase.toHTML(lookUpId, executionTimeMillis.toString()));

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
