package sudoku.enums;

public enum EDifficulty {
	EASY("Easy"), MEDIUM("Medium"), HARD("Hard"), EVIL("Evil");

	private String value;

	private EDifficulty(String val) {
		value = val;
	}

	public String getValue() {
		return value;
	}

	public static EDifficulty getValueOf(String val) {
		EDifficulty difficulty = null;
		try {
			difficulty = EDifficulty.valueOf(val.toUpperCase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return difficulty;
	}

}
