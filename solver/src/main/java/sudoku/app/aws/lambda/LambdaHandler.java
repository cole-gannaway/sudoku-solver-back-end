package sudoku.app.aws.lambda;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sudoku.elements.SudokuCellDataBase;
import sudoku.elements.SudokuCellDataBaseBuilder;
import sudoku.parsing.JSONBoardInformation;
import sudoku.parsing.JSONResponse;
import sudoku.parsing.JSONSolveConfigParameters;
import sudoku.parsing.JSONSolveRequest;
import sudoku.thread.SudokuThreadManager;
public class LambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, JSONResponse> {
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	ObjectMapper mapper = new ObjectMapper();


	@Override
	public JSONResponse handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
		LambdaLogger logger = context.getLogger();
		
		// log execution details
		logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
		logger.log("CONTEXT: " + gson.toJson(context));
		
		// process event
		logger.log("EVENT: " + gson.toJson(requestEvent));
		
		// handle the request
		String bodyStr = requestEvent.getBody();
		JSONSolveRequest request = null;
		try {
			request = mapper.readValue(bodyStr,JSONSolveRequest.class);
		} catch (IOException e1) {
			logger.log(e1.toString());
		}
		
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
		
		// make JSON response
		JSONResponse jsonResponse = new JSONResponse();
		jsonResponse.setRows(db.toCSV());
		jsonResponse.setSolved(db.isSolved());
		return  jsonResponse;
	}

}