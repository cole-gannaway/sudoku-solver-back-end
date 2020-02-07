package sudoku.solving.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import sudoku.elements.SudokuCellDataBase;
import sudoku.elements.SudokuCoordinate;
import sudoku.enums.ESudokuSection;

public class SudokuSolvingUtils {

	public static void setCandidates(SudokuCellDataBase db, SudokuCoordinate coordinate) {
		for (ESudokuSection section : ESudokuSection.values()) {
			List<SudokuCoordinate> otherCoordinates = db.generateCoordinatesForSegment(coordinate,
					Arrays.asList(section));
			setCandidates(db, coordinate, otherCoordinates);
		}
	}

	private static void setCandidates(SudokuCellDataBase db, SudokuCoordinate coordinate,
			List<SudokuCoordinate> otherCoordinates) {
		Iterator<SudokuCoordinate> it = otherCoordinates.iterator();
		while (it.hasNext()) {
			SudokuCoordinate lookUpCoord = it.next();
			String value = db.getCellValue(lookUpCoord);
			if (value != null) {
				db.removeCandidateFromCell(coordinate, value);
			}
		}
		return;
	}

	public static String hasUniqueCandidate(SudokuCellDataBase db, SudokuCoordinate coordinate) {
		String retVal = null;
		for (ESudokuSection section : ESudokuSection.values()) {
			List<SudokuCoordinate> otherCoordinates = db.generateCoordinatesForSegment(coordinate,
					Arrays.asList(section));
			retVal = hasUniqueCandidate(db, coordinate, otherCoordinates);
		}
		return retVal;
	}

	private static String hasUniqueCandidate(SudokuCellDataBase db, SudokuCoordinate coordinate,
			List<SudokuCoordinate> otherCoordinates) {
		String retVal = null;
		Iterator<String> testIt = db.getCandidatesForCell(coordinate).iterator();
		while (testIt.hasNext()) {
			String testCandiate = testIt.next();
			int matchCount = 0;
			Iterator<SudokuCoordinate> lookUpIt = otherCoordinates.iterator();
			while (lookUpIt.hasNext()) {
				SudokuCoordinate lookUpCoord = lookUpIt.next();
				List<String> lookUpCandidates = db.getCandidatesForCell(lookUpCoord);
				if (lookUpCandidates.contains(testCandiate)) {
					matchCount++;
				}
			}
			if (matchCount == 0) {
				retVal = testCandiate;
				break;
			}
		}
		return retVal;
	}

}
