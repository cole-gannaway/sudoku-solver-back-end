package sudoku.elements;

import java.util.ArrayList;
import java.util.List;

public class CommonSudokuUtils {
	public static List<Integer> generateCandidateValuesInclusive(int low, int hi){
		List<Integer> arrayList = new ArrayList<Integer>();
		for (int i = low; i <= hi; i++) {
			arrayList.add(i);
		}
		return arrayList;
	}
}
