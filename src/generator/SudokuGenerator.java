package generator;

import java.util.Collections;
import java.util.Random;

public class SudokuGenerator extends SudokuSolver {

	private static final int EASY = 2;
	private static final int MEDIUM = 3;
	private static final int HARD = 5;
	
	public SudokuGenerator(int size) {
		super(size);
	}
	
	public int[][] make_unique_board(int difficulty) {
		while (true) {
			int[][] board = this.generate(difficulty);
			super.solve();
			if(super.num_solutions == 1) {
				return board;
			}
		}
	}

	public int[][] generate(int difficulty) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = 0;
			}
		}
		generateSudoku();
		makeHoles(difficulty*super.board.length);
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

	public boolean generateSudoku() {
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				if (board[row][col] == 0) {
					Collections.shuffle(allowed);
					for (int number : allowed) {
						if (isSafe(row, col, number)) {
							board[row][col] = number;
							if (generateSudoku()) {
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
		SudokuGenerator g1 = new SudokuGenerator(2);
		g1.make_unique_board(1);
		for (int i = 0; i < g1.board.length; i++) {
			for (int j = 0; j < g1.board.length; j++) {
				System.out.print(g1.board[i][j]);
			}
		}
		System.out.println();
		SudokuSolver s1 = new SudokuSolver(g1.board);
		System.out.println(s1);
		s1.solve();
		System.out.println(s1);
		System.out.println(s1.num_solutions);
	}

}
