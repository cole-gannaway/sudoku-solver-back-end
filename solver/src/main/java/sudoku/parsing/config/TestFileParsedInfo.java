package sudoku.parsing.config;

import sudoku.enums.EBoardType;
import sudoku.enums.EDifficulty;

public class TestFileParsedInfo {
	private String id;
	private String puzzleFilePath;
	private String answerFilePath;
	private EBoardType boardType;
	private EDifficulty difficulty;

	public TestFileParsedInfo(String id, String puzzleFilePath, String answerFilePath, EBoardType boardType,
			EDifficulty difficulty) {
		this.id = id;
		this.puzzleFilePath = puzzleFilePath;
		this.answerFilePath = answerFilePath;
		this.boardType = boardType;
		this.difficulty = difficulty;
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

	public EBoardType getBoardType() {
		return boardType;
	}

	public EDifficulty getDifficulty() {
		return difficulty;
	}

}
