package sudoku.parsing;

public class JSONSolveConfigParameters {
	private Integer numberOfThreads;
	private Integer timeoutSeconds;

	/* Default constructor for Jackson JSON Parser */
	public JSONSolveConfigParameters() {

	}

	public Integer getNumberOfThreads() {
		return numberOfThreads;
	}

	public void setNumberOfThreads(Integer numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}

	public Integer getTimeoutSeconds() {
		return timeoutSeconds;
	}

	public void setTimeoutSeconds(Integer timeoutSeconds) {
		this.timeoutSeconds = timeoutSeconds;
	}

}
