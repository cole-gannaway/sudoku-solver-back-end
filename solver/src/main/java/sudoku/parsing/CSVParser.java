package sudoku.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVParser {
	public static List<String[]> parseFile(File f) throws FileNotFoundException {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(f);
		List<String[]> rows = new ArrayList<String[]>();
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] split = line.split(",", -1);
			rows.add(split);
		}
		return rows;
	}
}
