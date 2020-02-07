package sudoku.elements;

import java.util.List;

import sudoku.enums.EBoardType;

public class SudokuCellDataBaseBuilder {
	public static SudokuCellDataBase buildDataBase(List<String[]> rows, EBoardType boardType) {
		int n = rows.get(0).length;
		SudokuCellDataBase db = new SudokuCellDataBase(boardType, n);
		int rowNum = 1;
		int colNum = 1;
		for (String[] row : rows) {
			for (int i = 0; i < row.length; i++) {
				SudokuCoordinate coordinate = new SudokuCoordinate(rowNum, colNum);
				db.addCell(coordinate);
				if (!row[i].contentEquals("")) {
					db.solveCell(coordinate, row[i]);
				}
				colNum++;
			}
			colNum = 1;
			rowNum++;
		}
		return db;
	}

}
