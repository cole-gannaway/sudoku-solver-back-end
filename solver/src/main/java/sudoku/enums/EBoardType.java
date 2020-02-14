package sudoku.enums;

import java.util.ArrayList;
import java.util.List;

public enum EBoardType {
	SUDOKU("SUDOKU"), HEXADOKU("HEXADOKU");

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

	public static EBoardType getValueOf(String val) {
		EBoardType boardType = null;
		try {
			boardType = EBoardType.valueOf(val.toUpperCase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return boardType;
	}

}
