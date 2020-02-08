package sudoku.enums;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class EBoardTypeTest {

	@Test
	public void testGetPossibleCandidateValuesSudoku() {
		int n = 9;
		List<String> candidateValues = EBoardType.getPossibleCandidateValues(EBoardType.SUDOKU, n);
		List<String> expected = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9).stream().map(num -> num.toString())
				.collect(Collectors.toList());

		assertEquals(expected, candidateValues);
	}

	@Test
	public void testGetPossibleCandidateValuesHexadoku() {
		int n = 16;
		List<String> candidateValues = EBoardType.getPossibleCandidateValues(EBoardType.HEXADOKU, n);

		List<String> expected = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).stream().map(num -> num.toString())
				.collect(Collectors.toList());
		expected.addAll(Arrays.asList("A", "B", "C", "D", "E", "F"));

		assertEquals(expected, candidateValues);
	}

}
