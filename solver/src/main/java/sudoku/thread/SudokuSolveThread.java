package sudoku.thread;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;

import sudoku.elements.SudokuCellDataBase;
import sudoku.elements.SudokuCoordinate;
import sudoku.solving.utils.SudokuSolvingUtils;

public class SudokuSolveThread implements Callable<Boolean> {
	List<SudokuCoordinate> coords;
	SudokuCellDataBase db;

	public SudokuSolveThread(SudokuCellDataBase db, List<SudokuCoordinate> coords) {
		this.coords = coords;
		this.db = db;
	}

	@Override
	public Boolean call() throws Exception {
		Boolean retVal = false;
		while (coords.size() != 0) {
			ListIterator<SudokuCoordinate> it = coords.listIterator();
			while (it.hasNext()) {
				SudokuCoordinate coordinate = it.next();
				List<SudokuSolveStep> steps = Arrays.asList(SudokuSolveStep.values());
				for (SudokuSolveStep step : steps) {
					switch (step) {
					case UNIQUECANDIDATE:
						SudokuSolvingUtils.isUniqueCandidate(db, coordinate);
						break;

					default:
						break;
					}
				}
			}

		}
		retVal = true;
		return retVal;
	}

}
