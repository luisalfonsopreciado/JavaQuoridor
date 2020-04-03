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

	private boolean hasAvailableWallGivenRow(double x, int rowNumber){
		int previousX = 9;
		for(int i = 0; i < 8; i++){
			int max = previousX + 60;
			if(x > previousX && x < max){
				i = convertToBoardCoord(i) - 1;
				System.out.println("CLICKED ON COORD" + rowNumber + " " + i);
				if(hasAvailableWall(rowNumber, i)){
					if( p1.getWalls() > 0 && isP1Turn()){
						p1.placeWall(new int[]{rowNumber, i}, p1ActiveWalls);
						// updateBoard();
						// printBoard();
						return true;
					}else if( p2.getWalls() > 0 && !isP1Turn()){
						p2.placeWall(new int[]{rowNumber, i}, p1ActiveWalls);
						// updateBoard();
						// printBoard();
						return true;
					}
				}
				
				
			}
			previousX = max;
		}	
		System.out.println("HAS AVAILABLE WALL GIVEN ROW NOT PASSED");
		return false;
	}
	
	private boolean hasAvailableWallGivenColumn(double x, int colNumber){
		int previousX = 9;
		for(int i = 0; i < 8; i++){
			int max = previousX + 60;
			if(x > previousX && x < max){
				i = convertToBoardCoord(i) - 1;
				System.out.println("CLICKED ON COORD" + i + " " + colNumber);
				if(hasAvailableWall(i, colNumber)){
					if(isP1Turn() && p1.getWalls() > 0){
						p1.placeWall(new int[]{i, colNumber}, p1ActiveWalls);
						return true;
					} else if(!isP1Turn() && p2.getWalls() > 0){
						p2.placeWall(new int[]{i, colNumber}, p1ActiveWalls);
						return true;
					}		
					updateBoard();
				}
			}
			previousX = max;
		}
		System.out.println("HAS AVAILABLE WALL GIVEN COLUMN NOT PASSED");
		return false;
	}

	private boolean hasAvailableWall(int r, int c){
		if(board[r][c] == 'W') {
			System.out.println("THERE IS ALREADY A WALL HERE");
			return false;
		}
			int[][] result = new int[3][2];
			int col = c;
			int row = r;
			//{{row,col}, {row,col},{row,col}}
			result[0][1] = row;
			result[0][0] = col;
		
			if(row % 2 == 0 || row == 0){
				result[1][1] = row + 1;
				result[1][0] = col;
				result[2][1] = row + 2;
				result[2][0] = col;
		
			}else{
				result[1][1] = row;
				result[1][0] = col + 1;
				result[2][1] = row;
				result[2][0] = col + 2;
			}

			for(int i = 0; i < result.length; i++){
					if(board[i][0] == 'W' || board[i][1] == 'W'){
						return false;
					}		
			}
		return true;
	}

	private boolean isP1Turn(){
		if(this.getGameState() == GameState.P1_TURN) return true;
		return false;
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
		
		boolean noPlayerPlacedAWallInPreviousMove = !(p1.placedAWallInPreviousMove || p2.placedAWallInPreviousMove);
		if(noPlayerPlacedAWallInPreviousMove) {
			updatePlayerPositions();
		} else {
			updateWallsOnBoard();
			p1.placedAWallInPreviousMove = false;
			p2.placedAWallInPreviousMove = false;
		}
		//Walls would be already updated in updateWallOnBoard Call
		checkIfAPlayerWonAndUpdateState();
	}

	private void checkIfAPlayerWonAndUpdateState() {
		updatePlayersTurn();
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
	
	private void placeWallOnBoard(Wall w) {
		int[][] coords = w.getCoords();
		for(int row = 0; row< coords.length; row++) {
			board[coords[row][1]][coords[row][0]] = 'W';
		}
	}
	
	private void updateWallsOnBoard() {
		if (!this.p1ActiveWalls.isEmpty()) {
			for (Wall w : this.p1ActiveWalls) {
				placeWallOnBoard(w);
			}
		}
		if (!this.p2ActiveWalls.isEmpty()) {
			for (Wall w : this.p2ActiveWalls) {
				placeWallOnBoard(w);
			}
		}

	}
	
	private void updatePlayerPositions() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				boolean isPlayerOneCurrentPosition = i == p1.getRowPosition() && j == p1.getColPosition();
				boolean isPlayerTwoCurrentPosition = i == p2.getRowPosition() && j == p2.getColPosition();
				boolean isReplaceablePlayerSpot = (board[i][j] != '.' && board[i][j] != '-' && board[i][j] != '|' && board[i][j] != 'W');
	
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

	public boolean manageScreenClick(double x, double y){
		boolean notInGameFrame = x <= 9 || x >= 539 || y <=9 || y>=539;
		if(notInGameFrame) return false;
		int smallTile = 10;
		double previousX = 59;
		for(int i = 0; i < 8; i++){ //loop thru the board
			double min = previousX;
			double max = min + smallTile;
			boolean ValidXCoord = x > min && x < max;
			boolean ValidYCoord = y > min && y < max;
			if(ValidXCoord || ValidYCoord){
				i = convertToBoardCoord(i);
				if(ValidXCoord){ //Valid X Coord
					// i is equal to the col number
					// check in the game if 
					return hasAvailableWallGivenColumn(y, i);
				}else{ //Valid Y Coord
					// i is equal to the row number
					return hasAvailableWallGivenRow(x, i);	
				}
			}
			previousX = max + 50;
		}	
		System.out.println("INVALID WALL COORDINATE");
		return false;
	}

	private int convertToBoardCoord(int i ){
		if(i==0){
			i += 1;
		} else if(i!=0) i = 2*i + 1;
		return i;
	}
}
