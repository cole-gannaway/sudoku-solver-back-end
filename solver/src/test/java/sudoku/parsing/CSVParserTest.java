package sudoku.parsing;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import sudoku.common.test.utils.CommonTestUtils;

public class CSVParserTest {

	@Test
	public void testParseFile() throws FileNotFoundException {
		File testFile = CommonTestUtils.getTestFile("/9by9/Puzzles/EasyPuzzle.csv");
		List<String[]> fields = CSVParser.parseFile(testFile);
		for (String[] row : fields) {
			assertEquals(9, row.length);
		}
	}

}
