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
	private List<SudokuCoordinate> coords;
	private SudokuCellDataBase db;
	private long startTime = System.currentTimeMillis();
	private long printInterval = TimeUnit.SECONDS.toMillis(1);
	private final Integer threadId;

	public SudokuSolveThread(SudokuCellDataBase db, List<SudokuCoordinate> coords, int id) {
		this.coords = coords;
		this.db = db;
		threadId = id;
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
					case HIDDENSET:
						List<String> hiddenSet = SudokuSolvingUtils.findHiddenSet(db, coordinate);
						if (hiddenSet != null) {
							db.removeAllOtherCandidatesFromCell(coordinate, hiddenSet);
						}
						break;
					default:
						break;

					}
				}
				if (isSolved) {
					it.remove();
				} else {
					long currentTimeMillis = System.currentTimeMillis();
					if (currentTimeMillis - lastPrintTime > printInterval) {
						System.out.println(
								"Solving update: " + coords.size() + " unsolved cells left for threadId = " + threadId);
						lastPrintTime = currentTimeMillis;
					}
				}
			}
		}
		retVal = true;
		return retVal;
	}

}
