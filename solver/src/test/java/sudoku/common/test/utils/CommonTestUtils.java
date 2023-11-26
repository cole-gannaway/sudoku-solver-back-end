package sudoku.common.test.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import sudoku.elements.SudokuCellDataBase;
import sudoku.elements.SudokuCoordinate;
import sudoku.enums.EBoardType;
import sudoku.parsing.config.TestFileParsedInfo;
import sudoku.solving.utils.SudokuSolvingUtils;

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
		writeStringToFile(outputFile, htmlString);
	}
	
	public static void saveCSVFileAsOutput(String filePath, List<List<String>> csvInfo) throws IOException {
		File outputFile = CommonTestUtils.getTestFile("HTML/" + filePath);
		writeStringToFile(outputFile, convertToCSV(csvInfo));
	}
	
	public static String convertToCSV(List<List<String>> csvInfo) {
        StringBuilder csvStringBuilder = new StringBuilder();

        for (List<String> row : csvInfo) {
            StringJoiner rowJoiner = new StringJoiner(",");
            for (String cell : row) {
                rowJoiner.add(cell);
            }
            csvStringBuilder.append(rowJoiner.toString()).append("\n");
        }

        return csvStringBuilder.toString();
    }

	public static String getCSVOutputString(List<List<String>> fields) {
		StringBuilder builder = new StringBuilder();
		for (List<String> row : fields) {
			for (int i = 0; i < row.size(); i++) {
				builder.append(row.get(i));
				if (i + 1 != row.size()) {
					builder.append(",");
				}
			}
			builder.append(System.lineSeparator());
		}
		return builder.toString();
	}

	public static String getTestValidityOutputString(List<List<String>> fields) {
		StringBuilder builder = new StringBuilder();
		for (List<String> row : fields) {
			for (int i = 0; i < row.size(); i++) {
				if (row.get(i).isBlank()) {
					builder.append(" ");
				}
				builder.append(row.get(i));
			}
		}
		return builder.toString();
	}

	public static void writeStringToFile(File outputFile, String outputStr) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(outputFile);
		byte[] bytes = outputStr.getBytes();
		outputStream.write(bytes);
		outputStream.close();
	}

	public static SudokuCellDataBase createFakeSudokuDataBase(int n) {
		List<String> possibleCandidateValues = EBoardType.getPossibleCandidateValues(EBoardType.SUDOKU, n);
		SudokuCellDataBase db = new SudokuCellDataBase(possibleCandidateValues);
		for (int x = 1; x <= n; x++) {
			for (int y = 1; y <= n; y++) {
				db.addCell(new SudokuCoordinate(x, y));
			}
		}
		return db;
	}

	public static void setCandidatesOnAllCells(SudokuCellDataBase database) {
		List<List<SudokuCoordinate>> allCoords = database.splitCoordinatesForNThreads(1);
		for (List<SudokuCoordinate> subset : allCoords) {
			for (SudokuCoordinate coord : subset) {
				if (database.getCellValue(coord) == null)
					SudokuSolvingUtils.setCandidates(database, coord);
			}
		}
	}

	public static boolean compareCSVOutputs(List<List<String>> actualCSV, List<List<String>> expectedCSV) {
		return actualCSV.equals(expectedCSV);
	}

	public static TestFileParsedInfo getFirstTestFileInfoById(String lookUpId,
			List<TestFileParsedInfo> configFileInfos) {
		Optional<TestFileParsedInfo> optFileInfo = configFileInfos.stream()
				.filter(info -> info.getId().equals(lookUpId)).findFirst();

		TestFileParsedInfo retVal = null;
		if (optFileInfo.isPresent()) {
			retVal = optFileInfo.get();
		}
		return retVal;
	}
}
