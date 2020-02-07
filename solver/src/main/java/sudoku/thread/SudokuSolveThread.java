package sudoku.thread;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;

import sudoku.elements.SudokuCellDataBase;
import sudoku.elements.SudokuCoordinate;
import sudoku.enums.ESudokuSolveStep;
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
				List<ESudokuSolveStep> steps = Arrays.asList(ESudokuSolveStep.values());
				for (ESudokuSolveStep step : steps) {
					switch (step) {
					case SETCANDIDATES:
						SudokuSolvingUtils.setCandidates(db, coordinate);
						break;
					case UNIQUECANDIDATE:
						String uniqueCandidate = SudokuSolvingUtils.hasUniqueCandidate(db, coordinate);
						if (uniqueCandidate != null) {
							db.solveCell(coordinate, uniqueCandidate);
						}
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
