# Sudoku Solver Back End

The Sudoku Solver exhibits dynamic capabilities by efficiently solving up to **64 x 64** Sudoku puzzles from **EASY** up to **EXPERT**. Furthermore, it extends its functionality to encompass **Hexadoku** puzzles, which employ letters instead of numbers. The solver can execute in **parallel** using multiple threads on a single computer for maxium effeciency. Currently, it is hosted on the AWS Lambda platform. Test it out [here](https://www.colegannaway.com/sudoku-solver-front-end/). Finally, the solver can be easily tested locally by following the provided instructions, resulting in the generation of an HTML output for thorough evaluation.

## Table of Contents

- [Features](#features)
- [Demo](#demo)
- [Installation](#installation)
- [Technologies Used](#technologies-used)
- [Contributing](#contributing)
- [License](#license)

## Features

- Interactive Interface: Allows users to input Sudoku puzzles and view the solving process step-by-step.
- Difficulty Levels: Supports different levels of Sudoku puzzles, ranging from easy to expert.
- Massive Scale: Supports up to up to **64 x 64** puzzles yielding 4096 boxes!
- Multiple Types: Supports **Sudoku** or **Hexadoku** puzzles
- Clear and Reset: Enables users to clear the puzzle or reset it to its original state.

## Demo

You can access the live website [here](https://www.colegannaway.com/sudoku-solver-front-end/).

## Installation

As this is a live website, there is no installation required. Simply open the provided link in your web browser to access the Sudoku Solver Front-End website.

However, if you'd like to install and test locally:

1. Clone the repository: `git clone https://github.com/cole-gannaway/sudoku-solver-back-end`
2. Navigate to the project directory: `cd sudoku-solver-back-end`
3. Run : `mvn clean install` to build the project
4. Run : `mvn test` to run the tests

#### Configurator
The following fields should be filled out for the system to read in .csv or .json files.
```json
{
    "testConfigs": [
		{
			"id": "exampleId",
			"boardtype": "SUDOKU | HEXADOKU",
			"difficulty" : "EASY | MEDIUM | HARD | EVIL",
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

## Technologies Used

The Sudoku Solver Front-End website utilizes the following technologies:

- Java
- Maven
- AWS Lambda

## Contributing

I appreciate your interest in contributing to my personal portfolio website. If you find any issues or have suggestions for improvement, please feel free to open an issue or submit a pull request.

## License

The contents of this repository are licensed under the [MIT License](LICENSE).

