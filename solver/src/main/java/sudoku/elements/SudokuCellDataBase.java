package sudoku.elements;

import java.util.HashMap;
import java.util.Map;

public class SudokuCellDataBase {
	private final Integer n;
	private Map<SudokuCoordinate, SudokuCell> cells;

	public SudokuCellDataBase(Integer n) {
		this.n = n;
		cells = new HashMap<SudokuCoordinate, SudokuCell>();
	}

	public void addCell(SudokuCell newCell, SudokuCoordinate coordinate) {
		cells.put(coordinate, newCell);
	}

	public SudokuCell getCell(SudokuCoordinate coordinate) {
		return cells.get(coordinate);
	}

	public int size() {
		return cells.size();
	}

	public String toHTML() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("<html>");
		strBuilder.append("<head>");
		strBuilder.append("<style>");
//		+ (int) Math.sqrt(n) + 
		strBuilder.append("td {  height:30px;  width:30px;  border:1px solid;  text-align:center;}");
		strBuilder.append("td:first-child {  border-left:solid;}");
		strBuilder.append("td:nth-child(" + (int) Math.sqrt(n) + "n) {  border-right:solid ;}");
		strBuilder.append("tr:first-child {  border-top:solid;}");
		strBuilder.append("tr:nth-child(" + (int) Math.sqrt(n) + "n) td {  border-bottom:solid ;}");
		strBuilder.append("</style>");
		strBuilder.append("<table border=\"1|0\">");

		for (int x = 1; x <= n; x++) {
			strBuilder.append("<tr>");
			for (int y = 1; y <= n; y++) {
				strBuilder.append("<td>");
				SudokuCell cell = cells.get(new SudokuCoordinate(x, y));
				String value = cell.getValue();
				if (value != null) {
					strBuilder.append(value);
				} else {
					strBuilder.append("");
				}
				strBuilder.append("</td>");
			}
			strBuilder.append("</tr>");
		}
		strBuilder.append("</table>");
		strBuilder.append("</html>");
		return strBuilder.toString();

	}

}
