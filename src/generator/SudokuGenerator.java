package generator;

import java.util.Collections;
import java.util.Random;

public class SudokuGenerator extends SudokuSolver {

	private static final int EASY = 2;
	private static final int MEDIUM = 3;
	private static final int HARD = 5;
	private static final int DEFAULT_PATIENCE = 50;

	public SudokuGenerator(int size) {
		super(size);
	}

	public int[][] generate(int difficulty, int patience) {
		boolean satisfied = false;
		while (!satisfied) {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					board[i][j] = 0;
				}
			}

			generateSudoku();
			satisfied = makeHoles(difficulty, patience);
		}

		return board;
	}

	private boolean makeHoles(int difficulty, int patience) {
		Random rand = new Random();
		int removed = 0;
		int lastRemoved = 0;
		int tries = 0;
		while (removed < difficulty) {
			if (lastRemoved == removed) {
				tries++;
			}
			if (tries > patience) {
				return false;
			}

			lastRemoved = removed;
			int x = rand.nextInt(size);
			int y = rand.nextInt(size);
			if (x != y && board[x][y] != 0 && board[y][x] != 0) {
				if (removeAndTestTwo(x, y, y, x)) {
					removed += 2;
				}
			} else if (board[x][y] != 0) {
				if (removeAndTest(x, y)) {
					removed += 1;
				}
			} else if (board[y][x] != 0) {
				if (removeAndTest(x, y)) {
					removed += 1;
				}
			}
			System.out.println("Removed: " + removed);
		}
		return true;
	}

	public boolean removeAndTestTwo(int x1, int y1, int x2, int y2) {
		int tmp1 = board[x1][y1];
		int tmp2 = board[x2][y2];
		board[x1][y1] = 0;
		board[x2][y2] = 0;
		super.solve(board);
		if (super.num_solutions == 1) {
			return true;
		} else {
			board[x1][y1] = tmp1;
			board[x2][y2] = tmp2;
			return false;
		}
	}

	public boolean removeAndTest(int x, int y) {
		int tmp = board[x][y];
		board[x][y] = 0;
		super.solve(board);
		if (super.num_solutions == 1) {
			return true;
		} else {
			board[x][y] = tmp;
			return false;
		}
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
		SudokuGenerator g1 = new SudokuGenerator(9);
		g1.generate(55, DEFAULT_PATIENCE);
		for (int i = 0; i < g1.board.length; i++) {
			for (int j = 0; j < g1.board.length; j++) {
				System.out.print(g1.board[i][j] + "; ");
			}
			System.out.println();
		}
		System.out.println();
		SudokuSolver s1 = new SudokuSolver(g1.board);
		s1.solve(s1.board);
		System.out.println(s1);
		System.out.println(s1.num_solutions);
	}

}
