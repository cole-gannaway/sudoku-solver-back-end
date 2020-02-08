package sudoku.enums;

import java.util.ArrayList;
import java.util.List;

public enum EBoardType {
	SUDOKU("Sudoku"), HEXADOKU("Hexadoku");

	private String value;

	private EBoardType(String value) {
		this.value = value;
	}

	public static List<String> getPossibleCandidateValues(final EBoardType type, final int n) {
		List<String> retVal = new ArrayList<String>();
		switch (type) {
		case SUDOKU:
			for (Integer i = 1; i <= n; i++) {
				retVal.add(i.toString());
			}
			break;
		case HEXADOKU:
			for (Integer i = 0; i <= n && i <= 9; i++) {
				retVal.add(i.toString());
			}
			Character start = 'A';
			while (retVal.size() < n && retVal.size() < 9 + 26) {
				retVal.add(start.toString());
				start++;
			}
			break;
		default:
			break;
		}
		return retVal;
	}

	public String getValue() {
		return value;
	}

}
