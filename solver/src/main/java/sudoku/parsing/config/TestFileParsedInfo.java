package sudoku.parsing.config;

public class TestFileParsedInfo {
	private String id;
	private String puzzleFilePath;
	private String answerFilePath;

	public TestFileParsedInfo(String id, String puzzleFilePath, String answerFilePath) {
		this.id = id;
		this.puzzleFilePath = puzzleFilePath;
		this.answerFilePath = answerFilePath;
	}

	public String getId() {
		return id;
	}

	public String getPuzzleFilePath() {
		return puzzleFilePath;
	}

	public String getAnswerFilePath() {
		return answerFilePath;
	}

	@Override
	public String toString() {
		return "TestFileParsedInfo [id=" + id + ", puzzleFilePath=" + puzzleFilePath + ", answerFilePath="
				+ answerFilePath + "]";
	}

}
