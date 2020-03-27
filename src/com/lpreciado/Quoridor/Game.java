package com.lpreciado.Quoridor;

import java.util.*;

public class Game {

	private char[][] board;
	private GameState state = GameState.CONTINUE;
	int boardRows = 17;
	int boardCols = 17;
	int initialRowPlayerOne = 0;
	int initialColPlayerOne = 8;
	int initialRowPlayerTwo = 16;
	int initialColPlayerTwo = 8;
	Player p1;
	Player p2;
	private int moves = 0;

	 ArrayList<Wall> p1ActiveWalls;
	 ArrayList<Wall> p2ActiveWalls;

	public static void main(String[] args) {
		Game game = new Game(10);
		game.printBoard();
		game.p1.placeWall(new int[][] {{0,1},{1,1},{2,1}}, game.p1ActiveWalls);
		game.updateBoard();
		game.printBoard();

		game.p2.moveRight();
		game.updateBoard();
		game.printBoard();

		game.p1.moveDown();
		game.updateBoard();
		game.printBoard();
		
		

	}

	public GameState getGameState() {
		return this.state;
	}

	public Game(int wallsPerPlayer) {
		board = new char[this.boardRows][this.boardCols];
		p1ActiveWalls = new ArrayList<Wall>();
		p2ActiveWalls = new ArrayList<Wall>();
		state = GameState.P1_TURN;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (i == this.initialRowPlayerOne && j == this.initialColPlayerOne) {
					p1 = new Player(1, i, j, this.boardRows, this.boardCols, wallsPerPlayer);
					p1.setTurn(true);
					this.board[i][j] = (char) (p1.getPlayerNumber() + '0');
				} else if (i == this.initialRowPlayerTwo && j == this.initialColPlayerTwo) {
					p2 = new Player(2, i, j, this.boardRows, this.boardCols, wallsPerPlayer);
					this.board[i][j] = (char) (p2.getPlayerNumber() + '0');
				} else if (j % 2 == 0 && i % 2 ==0) {
					this.board[i][j] = 'o';
				} else if (j % 2 != 0 && i % 2 !=0){
					this.board[i][j] = '.';
				} else {
					this.board[i][j] = 'x';
				}
			}
		}
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if(this.board[i][j] == 'x' && j %2 !=0) {
					this.board[i][j] = '|';
				}
				else if (this.board[i][j] == 'x' && i %2 !=0) {
					this.board[i][j] = '-';
				}
			}
		}
	}
	
	public char[][] getGameBoard(){
		return this.board;
	}
 
	public void printBoard() {
		System.out.println("=================================");
		System.out.println("Move: " + this.moves);
		System.out.println("GameState: " + this.state);
		for (int i = 0; i < 17; i++) {
			for (int j = 0; j < 17; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("Player 1 Walls: " + p1.getWalls());
		System.out.println("Player 2 Walls: " + p2.getWalls());

	}

	public void updateBoard() { // Function to be called after a player has made a move
		++this.moves;
		checkAndUpdateGameState();
		this.board[p1.getPreviousRowPosition()][p1.getPreviousColPosition()] = 'o';
		this.board[p2.getPreviousRowPosition()][p2.getPreviousColPosition()] = 'o';
		this.board[p1.getRowPosition()][p1.getColPosition()] = (char) (p1.getPlayerNumber() + '0');
		this.board[p2.getRowPosition()][p2.getColPosition()] = (char) (p2.getPlayerNumber() + '0');
	}

	public void checkAndUpdateGameState() {
		updatePlayersTurn();
		updateWallsOnBoard();
		if (this.initialRowPlayerOne < this.initialRowPlayerTwo) { // p1 on top
			if (this.initialRowPlayerOne == this.boardRows) {
				state = GameState.P1_WIN;
			} else if (this.initialRowPlayerTwo == 0) {
				state = GameState.P2_WIN;
			}

		} else { // p2 on top
			if (this.initialRowPlayerOne == 0) {
				state = GameState.P1_WIN;
			} else if (this.initialRowPlayerTwo == this.boardRows) {
				state = GameState.P2_WIN;
			}
		}

	}

	private void updatePlayersTurn() { // Will Determine and set the player turn based on if there was a change in
		if (state == GameState.P1_TURN && !this.p1.isTurn()) {
			p2.setTurn(true);
			state = GameState.P2_TURN;
		} else if (state == GameState.P2_TURN && !this.p2.isTurn()) {
			p1.setTurn(true);
			state = GameState.P1_TURN;
		}

	}
	private void coordsOnBoard(Wall w) {
		int[][] coords = w.getCoords();
		for(int row = 0; row< coords.length; row++) {
			board[coords[row][1]][coords[row][0]] = 'W';
		}
	}
	private void updateWallsOnBoard() {
		if (!this.p1ActiveWalls.isEmpty()) {
			for (Wall w : this.p1ActiveWalls) {
				coordsOnBoard(w);
			}
		}
		if (!this.p2ActiveWalls.isEmpty()) {
			for (Wall w : this.p2ActiveWalls) {
				coordsOnBoard(w);
			}
		}

	}
}
