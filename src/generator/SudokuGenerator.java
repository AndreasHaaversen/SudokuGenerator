package generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuGenerator extends SudokuSolver {

	private static final int EASY = 0;
	private static final int MEDIUM = 1;
	private static final int HARD = 2;
	private static final int DEFAULT_PATIENCE = 50;
	
	private List<Tuple<Integer, Integer>> positions = new ArrayList<Tuple<Integer, Integer>>();

	public SudokuGenerator(int size) {
		super(size);
	}


	public int[][] generate(int difficulty, int patience) {
		boolean satisfied = false;
		int holes = getNumHoles(difficulty);
		while (!satisfied) {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					board[i][j] = 0;
				}
			}

			generateSudoku();
			getPositions(size);
			satisfied = makeHoles(holes, patience);
		}

		return board;
	}
	
	private void getPositions(int size) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				positions.add(new Tuple<Integer, Integer>(i, j));
			}
		}
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
			if (tries > patience || positions.isEmpty()) {
				return false;
			}

			lastRemoved = removed;
			Tuple<Integer, Integer> candidate = positions.remove(rand.nextInt(positions.size()));
			int x = candidate.x;
			int y = candidate.y;
			if (x != y && board[x][y] != 0 && board[y][x] != 0) {
				if (removeAndTestPair(x, y, y, x)) {
					positions.removeIf(p -> p.x == y && p.y == x);
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

	public boolean removeAndTestPair(int x1, int y1, int x2, int y2) {
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
	
	private int getNumHoles(int difficulty) {
		int out = 0;
		if (size == 2) {
			switch(difficulty) {
			case(EASY): out = 1; break;
			case(MEDIUM): out = 2; break;
			case(HARD): out = 3; break;
			default: out = 0; break;
			}
		} else if(size == 4) {
			switch(difficulty) {
			case(EASY): out = 5; break;
			case(MEDIUM): out = 7; break;
			case(HARD): out = 9; break;
			default: out = 0; break;
			}
		} else if(size == 9) {
			switch(difficulty) {
			case(EASY): out = 45; break;
			case(MEDIUM): out = 50; break;
			case(HARD): out = 55; break;
			default: out = 0; break;
			}
		} else if(size == 16) {
			switch(difficulty) {
			case(EASY): out = 88; break;
			case(MEDIUM): out = 108; break;
			case(HARD): out = 130; break;
			default: out = 0; break;
			}
		}
		return out;
	}

	public static void main(String[] args) {
		SudokuGenerator g1 = new SudokuGenerator(9);
		g1.generate(MEDIUM, DEFAULT_PATIENCE);
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
