package sudoku.elements;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import sudoku.common.test.utils.CommonTestUtils;
import sudoku.enums.EBoardType;
import sudoku.parsing.CSVParser;

public class SudokuCellDataBaseTest {

	@Test
	public void test9By9() throws IOException, InterruptedException {
		File testFile = CommonTestUtils.getTestFile("/9by9/EasyPuzzle.csv");
		List<String[]> fields = CSVParser.parseFile(testFile);
		SudokuCellDataBase dataBase = SudokuCellDataBaseBuilder.buildDataBase(fields, EBoardType.SUDOKU);
		assertEquals(81, dataBase.size());
		String htmlFileName = "before.html";
		CommonTestUtils.saveHTMLFileAsOutput(htmlFileName, dataBase.toHTML());
		CommonTestUtils.openHTMLFileInBrowser(htmlFileName);

	}

}
