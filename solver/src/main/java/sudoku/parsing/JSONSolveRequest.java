package sudoku.parsing;

public class JSONSolveRequest {
	private JSONBoardInformation board;
	private JSONSolveConfigParameters config;

	/* Default constructor for Jackson JSON Parser */
	public JSONSolveRequest() {

	}

	public JSONBoardInformation getBoard() {
		return board;
	}

	public void setBoard(JSONBoardInformation board) {
		this.board = board;
	}

	public JSONSolveConfigParameters getConfig() {
		return config;
	}

	public void setConfig(JSONSolveConfigParameters config) {
		this.config = config;
	}

}
