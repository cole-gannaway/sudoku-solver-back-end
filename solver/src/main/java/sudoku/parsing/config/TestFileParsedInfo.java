package sudoku.parsing.config;

import sudoku.enums.EBoardType;
import sudoku.enums.EDifficulty;

public class TestFileParsedInfo {
	private String id;
	private FileInfo puzzle;
	private FileInfo answer;
	private EBoardType boardtype;
	private EDifficulty difficulty;

	TestFileParsedInfo() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FileInfo getPuzzle() {
		return puzzle;
	}

	public void setPuzzle(FileInfo puzzle) {
		this.puzzle = puzzle;
	}

	public FileInfo getAnswer() {
		return answer;
	}

	public void setAnswer(FileInfo answer) {
		this.answer = answer;
	}

	public EBoardType getBoardtype() {
		return boardtype;
	}

	public void setBoardtype(EBoardType boardType) {
		this.boardtype = boardType;
	}

	public EDifficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(EDifficulty difficulty) {
		this.difficulty = difficulty;
	}

}
