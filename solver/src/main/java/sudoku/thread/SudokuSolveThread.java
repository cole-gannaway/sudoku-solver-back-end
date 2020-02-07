package sudoku.thread;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import sudoku.elements.SudokuCellDataBase;
import sudoku.elements.SudokuCoordinate;
import sudoku.enums.ESudokuSolveStep;
import sudoku.solving.utils.SudokuSolvingUtils;

public class SudokuSolveThread implements Callable<Boolean> {
	List<SudokuCoordinate> coords;
	SudokuCellDataBase db;
	long startTime = System.currentTimeMillis();
	long printInterval = TimeUnit.SECONDS.toMillis(1);

	public SudokuSolveThread(SudokuCellDataBase db, List<SudokuCoordinate> coords) {
		this.coords = coords;
		this.db = db;
	}

	@Override
	public Boolean call() {
		Boolean retVal = false;
		long lastPrintTime = startTime;
		while (coords.size() != 0) {
			ListIterator<SudokuCoordinate> it = coords.listIterator();
			while (it.hasNext()) {
				SudokuCoordinate coordinate = it.next();
				boolean isSolved = false;
				ESudokuSolveStep[] values = ESudokuSolveStep.values();
				for (int i = 0; i < values.length && isSolved == false; i++) {
					ESudokuSolveStep step = values[i];
					switch (step) {
					case SETCANDIDATES:
						SudokuSolvingUtils.setCandidates(db, coordinate);
						break;
					case UNIQUECANDIDATE:
						String uniqueCandidate = SudokuSolvingUtils.hasUniqueCandidate(db, coordinate);
						if (uniqueCandidate != null) {
							db.solveCell(coordinate, uniqueCandidate);
							isSolved = true;
						}
						break;
					default:
						break;

					}
				}
				if (isSolved) {
					System.out.println("removing " + coordinate + ", " + coords.size() + " left to go.");
					it.remove();
				} else {
					long currentTimeMillis = System.currentTimeMillis();
					if (currentTimeMillis - lastPrintTime > printInterval) {
						System.out.println(coords.size() + " left to go.");
						lastPrintTime = currentTimeMillis;
					}
				}
			}
		}
		retVal = true;
		return retVal;
	}

}
