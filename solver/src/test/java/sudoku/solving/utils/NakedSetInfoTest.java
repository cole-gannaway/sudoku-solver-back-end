package sudoku.solving.utils;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import sudoku.elements.SudokuCoordinate;

public class NakedSetInfoTest {

	@Test
	public void testContainsCoordinate() {
		// set up
		NakedSetInfo nakedSet = new NakedSetInfo();
		List<SudokuCoordinate> list = new ArrayList<SudokuCoordinate>();
		for (int i = 1; i < 3; i++) {
			list.add(new SudokuCoordinate(1, i));
		}
		nakedSet.setCoordinates(list);

		// verify
		SudokuCoordinate testCoord = new SudokuCoordinate(1, 2);
		assertTrue(nakedSet.containsCoordinate(testCoord));
	}

}
