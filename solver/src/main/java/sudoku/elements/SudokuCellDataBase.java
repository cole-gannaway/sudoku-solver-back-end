package sudoku.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sudoku.enums.EBoardType;
import sudoku.enums.ESudokuSection;
import sudoku.parsing.CSVParser;

public class SudokuCellDataBase {
	private final int n;
	private final EBoardType boardType;
	private Map<SudokuCoordinate, SudokuCell> cells = new HashMap<SudokuCoordinate, SudokuCell>();

	public SudokuCellDataBase(EBoardType boardType, int n) {
		this.n = n;
		this.boardType = boardType;
	}

	public void removeCandidateFromCell(SudokuCoordinate coordinate, String value) {
		SudokuCell cell = cells.get(coordinate);
		cell.removeCandidate(value);
	}

	public void solveCell(SudokuCoordinate coordinate, String value) {
		SudokuCell cell = cells.get(coordinate);
		cell.solveCell(value);
	}

	public void removeAllOtherCandidatesFromCell(SudokuCoordinate coordinate, List<String> set) {
		SudokuCell cell = cells.get(coordinate);
		cell.removeAllOtherCandidates(set);
	}

	public void addCell(SudokuCoordinate coordinate) {
		cells.put(coordinate, new SudokuCell(EBoardType.getPossibleCandidateValues(boardType, n)));
	}

	public String getCellValue(SudokuCoordinate coordinate) {
		String retVal = null;
		SudokuCell cell = cells.get(coordinate);
		if (cell.isSolved()) {
			retVal = cell.getValue();
		}
		return retVal;
	}

	public List<String> getCandidatesForCell(SudokuCoordinate coordinate) {
		return cells.get(coordinate).getCandidates();
	}

	public int size() {
		return cells.size();
	}

	public List<List<SudokuCoordinate>> splitCoordinatesForNThreads(int n) {
		ArrayList<List<SudokuCoordinate>> bigList = new ArrayList<List<SudokuCoordinate>>();
		List<SudokuCoordinate> smallList = new ArrayList<SudokuCoordinate>();

		int coordsPer = cells.size() / n;
		Iterator<SudokuCoordinate> it = cells.keySet().iterator();
		while (it.hasNext()) {
			SudokuCoordinate objToClone = it.next();
			smallList.add(new SudokuCoordinate(objToClone));
			if (smallList.size() == coordsPer) {
				bigList.add(smallList);
				smallList = new ArrayList<SudokuCoordinate>();
			}
		}
		// add it to the last list
		if (smallList.size() != 0) {
			bigList.get(bigList.size() - 1).addAll(smallList);
		}

		return bigList;
	}

	public List<SudokuCoordinate> generateCoordinatesForSection(SudokuCoordinate coord,
			List<ESudokuSection> listOfSegments) {
		List<SudokuCoordinate> retList = new ArrayList<SudokuCoordinate>();
		for (ESudokuSection segment : listOfSegments) {
			switch (segment) {
			case ROW:
				for (int i = 1; i <= n; i++) {
					if (i != coord.getxCoordinate())
						retList.add(new SudokuCoordinate(i, coord.getyCoordinate()));
				}
				break;
			case COLUMN:
				for (int i = 1; i <= n; i++) {
					if (i != coord.getyCoordinate())
						retList.add(new SudokuCoordinate(coord.getxCoordinate(), i));
				}
				break;
			case SQUARE:
				int sqSize = (int) Math.round(Math.sqrt(n));
				SudokuCoordinate topLeft = SudokuCoordinate.getTopLeftOfSquare(coord, sqSize);
				for (int x = 0; x < sqSize; x++) {
					for (int y = 0; y < sqSize; y++) {
						int xCoord = topLeft.getxCoordinate() + x;
						int yCoord = topLeft.getyCoordinate() + y;
						if (xCoord != coord.getxCoordinate() || yCoord != coord.getyCoordinate()) {
							retList.add(new SudokuCoordinate(xCoord, yCoord));
						}
					}
				}
				break;
			default:
				break;
			}
		}
		return retList;
	}

	public String toHTML() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("<html>");
		strBuilder.append("<head>");
		strBuilder.append("<style>");
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

	public List<String[]> toCSV() {
		List<String[]> retVal = new ArrayList<String[]>();
		for (int x = 1; x <= n; x++) {
			StringBuilder strBuilder = new StringBuilder();
			for (int y = 1; y <= n; y++) {
				String value = getCellValue(new SudokuCoordinate(x, y));
				if (value != null) {
					strBuilder.append(value);
				}
				// don't add a comma after the last one
				if (y != n) {
					strBuilder.append(",");
				}
			}
			String line = strBuilder.toString();
			retVal.add(CSVParser.splitLine(line));
		}
		return retVal;

	}

}
