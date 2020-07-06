package sudoku.app.aws.lambda;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sudoku.elements.SudokuCellDataBase;
import sudoku.elements.SudokuCellDataBaseBuilder;
import sudoku.parsing.JSONBoardInformation;
import sudoku.parsing.JSONResponse;
import sudoku.parsing.JSONSolveConfigParameters;
import sudoku.parsing.JSONSolveRequest;
import sudoku.thread.SudokuThreadManager;;

public class LambdaHandler implements RequestHandler<JSONSolveRequest, String> {
	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Override
	public String handleRequest(JSONSolveRequest request, Context context) {
		LambdaLogger logger = context.getLogger();
		
		// log execution details
		logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
		logger.log("CONTEXT: " + gson.toJson(context));
		
		// process event
		logger.log("EVENT: " + gson.toJson(request));
		
		// get parameters
		JSONBoardInformation board = request.getBoard();
		List<List<String>> boardValues = board.getRows();
		List<String> possibleCandidateValues = board.getPossibleValues();

		JSONSolveConfigParameters config = request.getConfig();
		Integer numberOfThreads = config.getNumberOfThreads();
		Integer timeoutSeconds = config.getTimeoutSeconds();
		
		// solve
		SudokuCellDataBase db = SudokuCellDataBaseBuilder.buildDataBase(boardValues, possibleCandidateValues);
		SudokuThreadManager manager = new SudokuThreadManager(db, numberOfThreads);
		manager.solve(timeoutSeconds, TimeUnit.SECONDS);
		List<List<String>> resultingBoard = db.toCSV();
		
		// write out response
		JSONResponse response = new JSONResponse("Success", resultingBoard);
		ObjectMapper mapper = new ObjectMapper();
		String retVal = null;
		try {
			retVal = mapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return retVal.toString();
	}

}