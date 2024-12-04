package edu.unca.csci202;
import java.util.Random;
import java.util.Stack;
import java.util.Scanner;
public class Gameboard {

	private boolean first=true;
	private static final int size=8;
	Cell[][] board = new Cell[size][size];
	Cell[][] peekBoard= new Cell[size][size];
	private Random random;
	private int mines=10;
	Scanner scan = new Scanner(System.in);
	/**
	 * run method that contains all game logic, output and input.
	 */
	public void run() {

		int minesGuessed=0;
		this.random=new Random();
		startBoard();
		String playgame="";
		if(first) {
			System.out.println("Welcome to Minesweeper!");
			System.out.print("Would you like to play a game? (y/n): ");
			playgame=scan.nextLine();

		}
		else {
			System.out.println("Thank you for playing Minesweeper.");
			System.out.print("Would you like to play again? (y/n): ");
			playgame=scan.nextLine();
		}
		while(!playgame.equals("y") && !playgame.equals("n")) {
			System.out.println("Invalid input, please try again."); 
			System.out.print("Would you like to play a game? (y/n): ");
			playgame=scan.nextLine();
		}

		// not playing	
		if(playgame.equals("n")) {
			System.out.print("Goodbye!");
			System.out.println("");
			System.exit(0);
		}
		// playing game
		if (playgame.equals("y")) {
			while	(playgame.equals("y")) {
				displayBoard();
				System.out.print("Would you like to peek? (y/n): ");
				String ynPeek = scan.nextLine();
				//peek yes
				while (!ynPeek.equals("y") && !ynPeek.equals("n")) {
					System.out.println("Invalid input, please try again."); 

					System.out.print("Would you like to peek? (y/n): ");
					ynPeek = scan.nextLine();
				}
				if (ynPeek.equals("y")) {
					peekBoard();
				}

				// after peek
				// row guess
				String rowStrGuess="";
				int rowGuess=-1;
				System.out.print("Please enter a row number: ");
				rowStrGuess=scan.nextLine();
				// parse into an int for proper guess format
				// invalid input
				while(true) {
					try {
						rowGuess= Integer.parseInt(rowStrGuess);
						if(rowGuess >= 1 && rowGuess <= 8) {
							break;	
						}
						else {
							System.out.println("Invalid input, please try again."); 
							System.out.print("Please enter a row number: ");
							rowStrGuess=scan.nextLine();
						}

					}
					catch (NumberFormatException e) {
						System.out.println("Invalid input, please try again."); 
						System.out.print("Please enter a row number: ");
						rowStrGuess=scan.nextLine();
					}
				}
				// column guess
				String colStrGuess="";
				int colGuess=-1;
				System.out.print("Please enter a column number: ");
				colStrGuess=scan.nextLine();
				// parse into an int for proper guess format

				// invalid input
				while(true) {
					try {
						colGuess= Integer.parseInt(colStrGuess);
						if(colGuess>=1 && colGuess<=8) {
							break;
						}
						else {
							System.out.println("Invalid input, please try again."); 
							System.out.print("Please enter a column number: ");
							colStrGuess=scan.nextLine();
						}
					}
					catch (NumberFormatException e) {					
						System.out.println("Invalid input, please try again."); 
						System.out.print("Please enter a column number: ");
						colStrGuess=scan.nextLine();
					}
				}
				// row col question
				System.out.print("Does row "+rowGuess+" and column "+colGuess+ " contain a mine? (y/n): ");
				String mineGuess=scan.nextLine();
				while(!mineGuess.equals("y") && !mineGuess.equals("n")) {
					System.out.println("Invalid input, please try again."); 
					System.out.print("Does row "+rowGuess+" and column "+colGuess+ " contain a mine? (y/n): ");
					mineGuess=scan.nextLine();
				}
				// row col logic
				if(mineGuess.equals("y")) {

					if(peekBoard[rowGuess-1][colGuess-1].mineCheck()==true) {
						board[rowGuess-1][colGuess-1].setDisplay("M");
						minesGuessed++;
						if(minesGuessed==10) {
							System.out.println("You win!");
							first=false;
							run();
							break;
						}
					}
					else {
						System.out.println("Boom! You lose.");
						first=false;
						run();
						break;
					}
				}
				if(mineGuess.equals("n")) {
					if(peekBoard[rowGuess-1][colGuess-1].mineCheck()==true) {
						board[rowGuess-1][colGuess-1].setDisplay("M");
						System.out.println("Boom! You lose.");

						first=false;
						run();
						break;
					}
					expand(rowGuess-1,colGuess-1);

				}
			}
		}
	}
	/**
	 * Checks if input is valid
	 * @param row row of array to be checked
	 * @param col column of array to be checked
	 * @return true or false whether the input is valid
	 */
	private boolean isValidInput(int row, int col) {
		return row >= 0 && row < size && col >= 0 && col < size && !peekBoard[row][col].mineCheck();
	}
	/**
	 * expand method that expands to nearby empty slots until they reach a spot with an adjacent mine.
	 * @param row row of the selected start cell
	 * @param col column of the selected start cell
	 */
	public void expand(int row, int col) {
		Stack<int[]> expand = new Stack<>();
		boolean[][] checked = new boolean[size][board[0].length];

		expand.push(new int[] {row,col});
		checked[row][col]=true;

		while (!expand.isEmpty()) {
			int[] current = expand.pop();
			int currentRow = current[0];
			int currentCol = current[1];

			if (!peekBoard[currentRow][currentCol].mineCheck()) {

				int minesAround = countMines(currentRow, currentCol);
				board[currentRow][currentCol].setDisplay(Integer.toString(minesAround));
				peekBoard[currentRow][currentCol].setDisplay(Integer.toString(minesAround));

				if (minesAround==0) {
					// Expand in all directions
					for (int i = currentRow - 1; i <= currentRow + 1; i++) {
						for (int j = currentCol - 1; j <= currentCol + 1; j++) {
							if (isValidInput(i, j) && !checked[i][j]) {
								expand.push(new int[] {i,j});
								checked[i][j]=true;

							}
						}
					}
				}
			}
		}
	}
	/**
	 * counts mines adjacent to input cell
	 * @param row row of selected cell
	 * @param col column of selected cell
	 * @return int number of mines adjacent to selected cell
	 */
	private int countMines(int row, int col) {
		int count = 0;

		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = col - 1; j <= col + 1; j++) {
				if (i >= 0 && i < size && j >= 0 && j < size) {
					if (peekBoard[i][j].mineCheck()) {
						count++;
					}
				}
			}
		}
		return count;
	}
	/**
	 * displays the game board
	 */
	public void displayBoard() {

		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				System.out.print(board[i][j].getDisplay());
				System.out.print(" ");
			}
			System.out.println();

		}
		System.out.println();
	}
	/**
	 * displays a peekBoard; the current game board but just with the mines displayed on it for peeking.
	 */
	public void peekBoard() {
		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				if(!peekBoard[i][j].mineCheck()) {
					peekBoard[i][j].setDisplay(board[i][j].getDisplay());

				}
			}
		}
		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				System.out.print(peekBoard[i][j].getDisplay());
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	/**
	 * creates a cell in each array slot and places mines.
	 */
	public void startBoard() {
		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				board[i][j] = new Cell("-");
				peekBoard[i][j] = new Cell("-");
			}
		}
		placeMines();
	}
	/**
	 * places mines on the game board at the beginning of a game
	 */
	public void placeMines() {
		int placed=0;
		while (placed<mines) {
			int row= random.nextInt(size);
			int col= random.nextInt(size);
			if (peekBoard[row][col].mineCheck()!=true) {

				peekBoard[row][col].setDisplay("M");
				placed++;
			}
		}
	}
}