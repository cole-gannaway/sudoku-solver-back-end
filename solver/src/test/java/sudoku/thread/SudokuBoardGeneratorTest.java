package sudoku.thread;


import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.junit.Test;

import sudoku.common.test.utils.CommonTestUtils;
import sudoku.elements.SudokuCellDataBase;
import sudoku.elements.SudokuCellDataBaseBuilder;
import sudoku.elements.SudokuCoordinate;

public class SudokuBoardGeneratorTest {
	List<String> possibleCandidates = Arrays.asList("1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P");
//	List<String> possibleCandidates = Arrays.asList("1","2","3","4","5","6","7","8","9");
//	List<String> possibleCandidates = Arrays.asList("1","2","3","4");
	String filePath = "generatedBoard.html";
	String csvFilePath = "generatedBoard.csv";


	@Test
	public void createABoard() throws IOException{
		long startTimeMillis = System.currentTimeMillis();
		new SudokuCellDataBaseBuilder();
		SudokuCellDataBase db = buildDataBase(possibleCandidates);
		Stack<CustomPair<SudokuCoordinate, String>> stack = new Stack<CustomPair<SudokuCoordinate, String>>();
		SudokuCellDataBase finalRes = recur(stack, db);
		long finalTimeMillis = System.currentTimeMillis();
		System.out.println("Raianne I'm sorry, I took " + ((finalTimeMillis - startTimeMillis) / 1000) + " seconds to create a puzzle :) ");

//		assertTrue(finalRes != null);
		CommonTestUtils.saveCSVFileAsOutput(csvFilePath, finalRes.toCSV());
		
//		CommonTestUtils.openHTMLFileInBrowser(filePath);
	}
	
	public SudokuCellDataBase recur(Stack<CustomPair<SudokuCoordinate, String>> stack, SudokuCellDataBase db) throws IOException {
		// base case
		if (db.getAllUnsolvedCoordinates().size() == 0) return db;
		List<SudokuCoordinate> unsolvedCoordinates = db.getAllUnsolvedCoordinates();
		for (SudokuCoordinate coordinate : unsolvedCoordinates) {
			// check if can place it
			List<String> candidates = db.getCandidatesForCell(coordinate);
			for (String candidate : candidates) {
				boolean isValidPlacement = db.isValidPlacement(coordinate, candidate);
				if (isValidPlacement) {
					db.solveCell(coordinate, candidate);
					stack.add(new CustomPair<SudokuCoordinate, String>(coordinate, candidate));
					SudokuCellDataBase resultingDb = recur(stack, db);
					if (resultingDb != null) return resultingDb;
					// can't move forward any
					if (resultingDb == null) {
						// need to go back
						stack.pop();
						SudokuCellDataBase newDb = createDatabaseFromStack(stack);
						db = newDb;
					}
				}
			}
			
		}
		return null;
	}
	
	public SudokuCellDataBase createDatabaseFromStack(Stack<CustomPair<SudokuCoordinate, String>> stack) {
		SudokuCellDataBase newDb = buildDataBase(possibleCandidates);
		Iterator<CustomPair<SudokuCoordinate, String>> it = stack.iterator();
		while(it.hasNext()) {
			CustomPair<SudokuCoordinate, String> next = it.next();
			newDb.solveCell(next.getFirst(), next.getSecond());
		}
		return newDb;
	}
	
	public static SudokuCellDataBase buildDataBase(List<String> possibleCandidateValues) {
		SudokuCellDataBase db = new SudokuCellDataBase(possibleCandidateValues);
		for (int i = 1; i <= possibleCandidateValues.size(); i++) {
			for (int j = 1; j <= possibleCandidateValues.size(); j++) {
				SudokuCoordinate coordinate = new SudokuCoordinate(i, j);
				db.addCell(coordinate);				
			}
		}
		return db;
	}

	public class CustomPair<T, U> {
	    private final T first;
	    private final U second;

	    public CustomPair(T first, U second) {
	        this.first = first;
	        this.second = second;
	    }

	    public T getFirst() {
	        return first;
	    }

	    public U getSecond() {
	        return second;
	    }
	}
	

}
