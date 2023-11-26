package sudoku.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sudoku.enums.ESudokuSection;
import sudoku.parsing.CSVParser;
import sudoku.solving.utils.SudokuSolvingUtils;

public class SudokuCellDataBase {
	private final int n;
	/* Typically it is 1 - 9, but can be any unique identifiers */
	private final List<String> possibleCandidateValues;
	private Map<SudokuCoordinate, SudokuCell> cells = new HashMap<SudokuCoordinate, SudokuCell>();
	private List<List<String>> allCombosOfCandidates = null;

	public SudokuCellDataBase(List<String> possibleCandidateValues) {
		this.possibleCandidateValues = possibleCandidateValues;
		this.n = possibleCandidateValues.size();
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
		cells.put(coordinate, new SudokuCell(getPossibleCandidates()));
	}

	public List<String> getPossibleCandidates() {
		// return deep copy
		return new ArrayList<String>(possibleCandidateValues);
	}

	public List<List<String>> getAllCombosOfCandidates() {
		if (allCombosOfCandidates == null) {
			allCombosOfCandidates = SudokuSolvingUtils.generateAllCombosOfCandidates(getPossibleCandidates());
		}
		return allCombosOfCandidates;
	}

	public String getCellValue(SudokuCoordinate coordinate) {
		String retVal = null;
		SudokuCell cell = cells.get(coordinate);
		if (cell == null) {
			System.err.println("cell == null");
		}
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
	
	public List<SudokuCoordinate> getAllUnsolvedCoordinates() {
		List<SudokuCoordinate> retList = new ArrayList<>();
		Set<SudokuCoordinate> keys = cells.keySet();
		Iterator<SudokuCoordinate> it = keys.iterator();
		while (it.hasNext()) {
			SudokuCoordinate coord = it.next();
			SudokuCell cell = cells.get(coord);
			if (!cell.isSolved()) retList.add(coord);
		}
		return retList;
	}
	
	/**
	 * Check row column and square to see if the value already exists 
	 * 
	 * @param coord
	 * @param value
	 * @return
	 */
	public boolean isValidPlacement(SudokuCoordinate coord, String value) {
		List<ESudokuSection> listOfSegments = Arrays.asList(ESudokuSection.ROW, ESudokuSection.COLUMN, ESudokuSection.SQUARE);
		List<SudokuCoordinate> coordinatesForSections = generateCoordinatesForSection(coord, listOfSegments);
		for (SudokuCoordinate testCoord : coordinatesForSections) {
			if (cells.get(testCoord).getValue() == value) return false;
		}
		return true;
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
		return toHTML(null, null);
	}

	public String toHTML(String lookUpId, String executionTime) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("<html>");
		strBuilder.append("<head>");
		if (lookUpId != null) {
			strBuilder.append("<title>");
			strBuilder.append(lookUpId);
			strBuilder.append("</title>");
		}
		strBuilder.append("<style>");
		strBuilder.append("td {  height:30px;  width:30px;  border:1px solid;  text-align:center;}");
		strBuilder.append("td:first-child {  border-left:solid;}");
		strBuilder.append("td:nth-child(" + (int) Math.sqrt(n) + "n) {  border-right:solid ;}");
		strBuilder.append("tr:first-child {  border-top:solid;}");
		strBuilder.append("tr:nth-child(" + (int) Math.sqrt(n) + "n) td {  border-bottom:solid ;}");
		strBuilder.append("</style>");
		strBuilder.append("</head>");
		if (lookUpId != null) {
			strBuilder.append("<h1>");
			strBuilder.append(lookUpId);
			strBuilder.append("</h1>");
		}
		if (executionTime != null) {
			strBuilder.append("<p>");
			strBuilder.append("Execution Time: " + executionTime);
			strBuilder.append("</p>");
		}
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

	public List<List<String>> toCSV() {
		List<List<String>> retVal = new ArrayList<List<String>>();
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

	public boolean isSolved() {
		Iterator<SudokuCell> it = cells.values().iterator();
		while (it.hasNext()) {
			SudokuCell sudokuCell =  it.next();
			if (!sudokuCell.isSolved()) {
				return false;
			}
		}
		return true;
	}

}
