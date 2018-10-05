package generator;

import java.util.Collections;
import java.util.Random;

public class SudokuGenerator extends SudokuSolver {

	private static final int EASY = 32;
	private static final int MEDIUM = 42;
	private static final int HARD = 52;

	//test
	
	public SudokuGenerator(int size) {
		super(size);
	}

	public int[][] generate(int difficulty) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = 0;
			}
		}

		solveSudoku();
		makeHoles(difficulty);
		return board;
	}

	private int[][] makeHoles(int difficulty) {
		Random rand = new Random();
		int removed = 0;
		while (removed < difficulty) {
			int x = rand.nextInt(size);
			int y = rand.nextInt(size);
			if (board[x][y] != 0) {
				board[x][y] = 0;
				removed++;
			}
		}
		return board;
	}

	public boolean solveSudoku() {
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				if (board[row][col] == 0) {
					Collections.shuffle(allowed);
					for (int number : allowed) {
						if (isSafe(row, col, number)) {
							board[row][col] = number;
							if (solveSudoku()) {
								return true;
							} else {
								board[row][col] = 0;
							}
						}
					}
					return false;
				}
			}
		}
		return true;
	}

	public static void main(String[] args) {
		SudokuGenerator g1 = new SudokuGenerator(36);
		g1.generate(25);
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				System.out.print(g1.board[i][j] + " ");
			}
			System.out.println();
		}
	}

}
