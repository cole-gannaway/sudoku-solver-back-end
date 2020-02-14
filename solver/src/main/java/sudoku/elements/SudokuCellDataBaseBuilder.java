package sudoku.elements;

import java.util.List;

import sudoku.enums.EBoardType;

public class SudokuCellDataBaseBuilder {
	public static SudokuCellDataBase buildDataBase(List<List<String>> fields, EBoardType boardType) {
		int n = fields.get(0).size();
		SudokuCellDataBase db = new SudokuCellDataBase(boardType, n);
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
