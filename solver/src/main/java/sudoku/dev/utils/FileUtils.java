package sudoku.dev.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
	public static void writeStringToFile (File f, String outputStr) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		bw.write(outputStr);
		bw.close();
	}
}
