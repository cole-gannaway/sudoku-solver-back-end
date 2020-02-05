package sudoku.elements;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

import sudoku.common.test.utils.CommonTestUtils;
import sudoku.parsing.CSVParser;

public class SudokuCellDataBaseTest {

	@Test
	public void test9By9() throws FileNotFoundException {
		File testFile = CommonTestUtils.getTestFile("/16by16/EasyPuzzle.csv");
		List<String[]> fields = CSVParser.parseFile(testFile);
		SudokuCellDataBase dataBase = SudokuCellDataBaseBuilder.buildDataBase(fields);
		assertEquals(256, dataBase.size());
		System.out.println(dataBase.toHTML());
	}

}
