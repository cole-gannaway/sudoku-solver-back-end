# SudokuSolver

SudokuSolver solves a sudoku puzzle of any size and difficulty in parallel. It then creates an html page and displays the final results.

## Parallel
All threads have read capabilities on all cells.
Each thread is given write responsiblity for a set of cells and only those cells. 

## Configurator
The following fields should be filled out for the system to read in .csv files and also know the possible candidate values (1-9, A-F).
```json
{
    "testConfigs": [
		{
			"id": "exampleId",
			"boardtype": "Sudoku/Hexadoku",
			"difficulty" : "Easy/Medium/Hard/Evil",
			"puzzle": {
				"filepath": "exampleFilePath/Puzzle.csv"
			},
			"answer": {
				"filepath": "exampleFilePath/Answer.csv"
			}
        },
    ]
}
```
