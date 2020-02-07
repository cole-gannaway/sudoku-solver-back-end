package sudoku.common.test.utils;

import java.io.File;

public class TestFileInfo {
	private String id;
	private File puzzleFile;
	private File answerFile;

	public TestFileInfo(String id, String puzzleFilePath, String answerFilePath) {
		this.id = id;
		this.puzzleFile = CommonTestUtils.getTestFile(puzzleFilePath);
		this.answerFile = CommonTestUtils.getTestFile(answerFilePath);
	}

	public String getId() {
		return id;
	}

	public File getPuzzleFile() {
		return puzzleFile;
	}

	public File getAnswerFile() {
		return answerFile;
	}

}
