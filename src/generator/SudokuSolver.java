package generator;

import java.util.*;

public class SudokuSolver {

	public int[][] board;
	protected final int size;
	protected final int root;
	protected ArrayList<Integer> allowed = new ArrayList<Integer>();
	public int num_solutions;

	public SudokuSolver(int size) {
		this.board = new int[size][size];
		this.size = size;
		this.root = (int) Math.floor(Math.sqrt(size));
		this.num_solutions = 0;
		for (int i = 1; i <= size; i++) {
			allowed.add(i);
		}
	}

	public SudokuSolver(int[][] board) {
		this.board = board;
		this.size = board.length;
		this.root = (int) Math.floor(Math.sqrt(size));
		for (int i = 1; i <= size; i++) {
			allowed.add(i);
		}
	}

	public int[][] solve() {
		num_solutions = 0;
		return solveSudoku();
	}

	public int[][] solveSudoku() {
		int last = 1;
		int j = 0;
		int i = 0;
		
		while(true) {
			if(board[i][j] == 0) {
				while(last <= board.length) {
					if(isSafe(i,j,last)) {
						board[i][j] = last;
						solveSudoku();
					}
					last++;
				}
				board[i][j] = 0;
				return board;
			} else if(i < board.length-1) {
				i++;
			} else if( i == board.length-1 && j < board.length-1) {
				i = 0;
				j++;
			} else {
				num_solutions++;
				return board;
			}
		}
	}

	protected boolean isSafe(int row, int col, int n) {
		return (safeRow(row, n) && safeCol(col, n) && safeBox(row, col, n));
	}

	private boolean safeRow(int row, int n) {
		for (int i = 0; i < this.size; i++) {
			if (board[row][i] == n) {
				return false;
			}
		}
		return true;
	}

	private boolean safeCol(int col, int n) {
		for (int i = 0; i < this.size; i++) {
			if (board[i][col] == n) {
				return false;
			}
		}
		return true;
	}

	private boolean safeBox(int row, int col, int n) {
		int r = row - row % root;
		int c = col - col % root;
		for (int i = r; i < r + root; i++) {
			for (int j = c; j < c + root; j++) {
				if (board[i][j] == n) {
					return false;
				}
			}
		}
		return true;
	}

	private String encodeBoard() {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				out.append(board[i][j] + ";");
			}
		}
		return out.toString();
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int val = board[i][j];
				if (val == 0) {
					out.append(".");
				} else {
					out.append(String.valueOf(val));
				}
				// out.append(';');
			}
		}
		return out.toString();
	}

	public static void main(String[] args) {
		SudokuSolver s1 = new SudokuSolver(9);
		s1.solve();
		System.out.println(s1.toString());
		System.out.println(s1.encodeBoard());
		System.out.println(s1.num_solutions);
	}
}
