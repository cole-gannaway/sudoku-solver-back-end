package sudoku.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import sudoku.common.test.utils.CommonTestUtils;
import sudoku.elements.SudokuCellDataBase;

public class SudokuThreadManagerTest {

	@Test
	public void test() throws ExecutionException, TimeoutException {
		int numOfThreads = 3;
		SudokuCellDataBase db = CommonTestUtils.createFakeSudokuDataBase(9);
		SudokuThreadManager threadManager = new SudokuThreadManager(db, numOfThreads);
		threadManager.solve(2, TimeUnit.SECONDS);
	}

}
