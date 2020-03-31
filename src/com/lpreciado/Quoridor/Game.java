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
//		game.printBoard();
//		game.p1.placeWall(new int[][] {{0,1},{1,1},{2,1}}, game.p1ActiveWalls);
//		game.updateBoard();
//		game.printBoard();
		
//		game.p1.moveRight();
//		game.updateBoard();
//		game.printBoard();
//
//		game.p2.moveLeft();
//		game.updateBoard();
//		game.printBoard();
//
//		game.p1.moveDown();
//		game.updateBoard();
//		game.printBoard(); 
	
		

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
				boolean isInitialPosOfPlayer1 = i == this.initialRowPlayerOne && j == this.initialColPlayerOne;
				boolean isInitialPosOfPlayer2 = i == this.initialRowPlayerTwo && j == this.initialColPlayerTwo;
				boolean isAvailablePlayerSpot = j % 2 == 0 && i % 2 ==0;
				boolean isAvailableSmallWallSpot = j % 2 != 0 && i % 2 !=0;
				boolean isAvailableVerticalWall = j %2 !=0;
				boolean isAvailableHorizontalWall = i %2 !=0;
				
				if (isInitialPosOfPlayer1) {
					p1 = new Player(1, i, j, this.boardRows, this.boardCols, wallsPerPlayer);
					p1.setTurn(true);
					this.board[i][j] = (char) (p1.getPlayerNumber() + '0'); //Add Player number to board
				} else if (isInitialPosOfPlayer2) {
					p2 = new Player(2, i, j, this.boardRows, this.boardCols, wallsPerPlayer);
					this.board[i][j] = (char) (p2.getPlayerNumber() + '0');//Add Player number to board
				} else if (isAvailablePlayerSpot) {
					this.board[i][j] = 'o';
				} else if (isAvailableSmallWallSpot){
					this.board[i][j] = '.';
				} else if(isAvailableVerticalWall){ // Vertical or 
					this.board[i][j] = '|';
				} else if(isAvailableHorizontalWall) {
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
		checkIfAPlayerWonAndUpdateState(); // Check if a player won
		boolean noPlayerPlacedAWallInPreviousMove = !(p1.placedAWallInPreviousMove || p2.placedAWallInPreviousMove);
		if(noPlayerPlacedAWallInPreviousMove) {
			updatePlayerPositions();
		}
		//Walls would be already updated in updateWallOnBoard Call
		
	}

	public void checkIfAPlayerWonAndUpdateState() {
		updatePlayersTurn();
		updateWallsOnBoard();
		if (this.initialRowPlayerOne < this.initialRowPlayerTwo) { // p1 on top
			if (p1.getRowPosition() == this.boardRows - 1) {
				state = GameState.P1_WIN;
			} else if (p2.getRowPosition() == 0) {
				state = GameState.P2_WIN;
			}

		} else { // p2 on top
			if (p1.getRowPosition() == 0) {
				state = GameState.P1_WIN;
			} else if (p2.getRowPosition() == this.boardRows - 1) {
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
	private void updatePlayerPositions() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				boolean isPlayerOneCurrentPosition = i == p1.getRowPosition() && j == p1.getColPosition();
				boolean isPlayerTwoCurrentPosition = i == p2.getRowPosition() && j == p2.getColPosition();
				boolean isReplaceablePlayerSpot = (board[i][j] != '.' && board[i][j] != '-' && board[i][j] != '|');
	
				if(isPlayerOneCurrentPosition) {
					board[i][j] = '1';
				} else if (isPlayerTwoCurrentPosition) {
					board[i][j] = '2';
				}else if (isReplaceablePlayerSpot){
					board[i][j] = 'o';
				}
				
			}
		}
	}
}
