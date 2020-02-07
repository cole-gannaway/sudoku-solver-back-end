package sudoku.enums;

public enum EConfigFileProperties {
	TESTCONFIGS("testConfigs"), ID("id"),PUZZLE("puzzle"),ANSWER("answer"),FILEPATH("filepath");

	private String value;

	EConfigFileProperties(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}
}
