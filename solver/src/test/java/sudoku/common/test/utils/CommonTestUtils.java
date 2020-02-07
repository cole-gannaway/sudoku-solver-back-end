package sudoku.common.test.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sudoku.elements.SudokuCellDataBase;
import sudoku.elements.SudokuCoordinate;
import sudoku.enums.EBoardType;

public class CommonTestUtils {
	private static String basePath = "src/test/resources";

	public static File getTestFile(String filePath) {
		return new File(basePath, filePath);
	}

	public static void openHTMLFileInBrowser(String filePath) throws IOException {
		File htmlFile = CommonTestUtils.getTestFile("HTML/" + filePath);
		Desktop.getDesktop().browse(htmlFile.toURI());
	}

	public static void saveHTMLFileAsOutput(String filePath, String htmlString) throws IOException {
		File outputFile = CommonTestUtils.getTestFile("HTML/" + filePath);
		FileOutputStream outputStream = new FileOutputStream(outputFile);
		byte[] bytes = htmlString.getBytes();
		outputStream.write(bytes);
		outputStream.close();
	}

	public static SudokuCellDataBase createFakeSudokuDataBase(int n) {
		SudokuCellDataBase db = new SudokuCellDataBase(EBoardType.SUDOKU, n);
		for (int x = 1; x <= n; x++) {
			for (int y = 1; y <= n; y++) {
				db.addCell(new SudokuCoordinate(x, y));
			}
		}
		return db;
	}
}
