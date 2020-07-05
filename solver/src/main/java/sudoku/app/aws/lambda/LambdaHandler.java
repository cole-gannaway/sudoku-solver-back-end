package sudoku.app.aws.lambda;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sudoku.elements.SudokuCellDataBase;
import sudoku.elements.SudokuCellDataBaseBuilder;
import sudoku.parsing.JSONBoardObject;
import sudoku.thread.SudokuThreadManager;;

public class LambdaHandler implements RequestHandler<JSONBoardObject, String> {
	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Override
	public String handleRequest(JSONBoardObject request, Context context) {
		LambdaLogger logger = context.getLogger();
		// log execution details
		logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
		logger.log("CONTEXT: " + gson.toJson(context));
		// process event
		logger.log("EVENT: " + gson.toJson(request));
		List<List<String>> board = request.getRows();
		List<String> possibleCandidateValues = request.getPossibleValues();
		SudokuCellDataBase db = SudokuCellDataBaseBuilder.buildDataBase(board, possibleCandidateValues);
		SudokuThreadManager manager = new SudokuThreadManager(db, 1);
		manager.solve(20, TimeUnit.SECONDS);
		List<List<String>> result = db.toCSV();
		return result.toString();
	}

}