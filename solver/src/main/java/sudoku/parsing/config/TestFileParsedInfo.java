package sudoku.parsing.config;

import sudoku.enums.EBoardType;

public class TestFileParsedInfo {
	private String id;
	private String puzzleFilePath;
	private String answerFilePath;
	private EBoardType boardType;

	public TestFileParsedInfo(String id, String puzzleFilePath, String answerFilePath, EBoardType boardType) {
		this.id = id;
		this.puzzleFilePath = puzzleFilePath;
		this.answerFilePath = answerFilePath;
		this.boardType = boardType;
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

}
