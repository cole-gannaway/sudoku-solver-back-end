package sudoku.elements;

public class SudokuCoordinate implements Comparable<SudokuCoordinate> {
	private int xCoordinate;
	private int yCoordinate;

	public SudokuCoordinate(int xCoordinate, int yCoordinate) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}
	public SudokuCoordinate(SudokuCoordinate objToClone) {
		this.xCoordinate = objToClone.xCoordinate;
		this.yCoordinate = objToClone.yCoordinate;
	}

	public int compareTo(SudokuCoordinate b) {
		int result = 0;
		int xCoordinateCompare = Integer.compare(xCoordinate, b.xCoordinate);
		result = xCoordinateCompare;
		if (result == 0) {
			int yCoordinateCompare = Integer.compare(yCoordinate, b.yCoordinate);
			result = yCoordinateCompare;
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + xCoordinate;
		result = prime * result + yCoordinate;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SudokuCoordinate other = (SudokuCoordinate) obj;
		if (xCoordinate != other.xCoordinate)
			return false;
		if (yCoordinate != other.yCoordinate)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "{" + xCoordinate + "," + yCoordinate + "}";
	}

}
