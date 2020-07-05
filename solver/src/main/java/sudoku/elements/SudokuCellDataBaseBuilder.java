package sudoku.elements;

import java.util.List;

public class SudokuCellDataBaseBuilder {
	public static SudokuCellDataBase buildDataBase(List<List<String>> fields, List<String> possibleCandidateValues) {
		SudokuCellDataBase db = new SudokuCellDataBase(possibleCandidateValues);
		int rowNum = 1;
		int colNum = 1;
		for (List<String> row : fields) {
			for (int i = 0; i < row.size(); i++) {
				SudokuCoordinate coordinate = new SudokuCoordinate(rowNum, colNum);
				db.addCell(coordinate);
				if (!row.get(i).contentEquals("")) {
					db.solveCell(coordinate, row.get(i));
				}
				colNum++;
			}
			colNum = 1;
			rowNum++;
		}
		return db;
	}

}
