package sudoku.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import sudoku.elements.SudokuCellDataBase;
import sudoku.elements.SudokuCoordinate;

public class SudokuThreadManager {
	int numOfThreads = 1;
	SudokuCellDataBase db;

	SudokuThreadManager(SudokuCellDataBase db, int numOfThreads) {
		this.numOfThreads = numOfThreads;
		this.db = db;
	}

	public void solve(int duration, TimeUnit timeOutUnit) {
		ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);
		List<SudokuSolveThread> threads = createThreads();
		List<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
		for (SudokuSolveThread thread : threads) {
			Future<Boolean> future = executorService.submit(thread);
			futures.add(future);
		}
		executorService.shutdown();
		try {
			if (!executorService.awaitTermination(duration, timeOutUnit)) {
				System.err.println("TIMEOUT occured after " + duration + " " + timeOutUnit);
				executorService.shutdownNow();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			executorService.shutdownNow();
		}

	}

	private List<SudokuSolveThread> createThreads() {
		ArrayList<SudokuSolveThread> retList = new ArrayList<SudokuSolveThread>();
		List<List<SudokuCoordinate>> coords = db.getListOfListOfCoordinates(numOfThreads);
		for (int i = 0; i < numOfThreads; i++) {
			retList.add(new SudokuSolveThread(db, coords.get(i)));
		}
		return retList;
	}

}
