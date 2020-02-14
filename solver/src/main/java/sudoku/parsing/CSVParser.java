package sudoku.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CSVParser {
	public static List<List<String>> parseFile(File f) throws FileNotFoundException {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(f);
		List<List<String>> rows = new ArrayList<List<String>>();
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			List<String> split = splitLine(line);
			rows.add(split);
		}
		return rows;
	}

	public static List<String> splitLine(String line) {
		return Arrays.asList(line.split(",", -1));
	}
}
