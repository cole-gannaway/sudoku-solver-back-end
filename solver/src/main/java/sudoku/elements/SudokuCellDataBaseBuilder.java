package sudoku.elements;

import java.util.List;

public class SudokuCellDataBaseBuilder {
	public static SudokuCellDataBase buildDataBase(List<String[]> rows) {
		int n = rows.get(0).length;
		SudokuCellDataBase db = new SudokuCellDataBase(n);
		int rowNum = 1;
		int colNum = 1;
		for (String[] row : rows) {
			for (int i = 0; i < row.length; i++) {
				SudokuCell cell = new SudokuCell();
				SudokuCoordinate coordinate = new SudokuCoordinate(rowNum, colNum);
				if (!row[i].contentEquals("")) {
					cell.solveCell(row[i]);
				}
				db.addCell(cell, coordinate);
				colNum++;
			}
			colNum = 1;
			rowNum++;
		}
		return db;
	}

}
