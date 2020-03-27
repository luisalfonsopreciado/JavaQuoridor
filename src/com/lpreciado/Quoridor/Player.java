package com.lpreciado.Quoridor;
import java.util.*;

public class Player {
	private int rowPosition;
	private int colPosition;
	private int previousRowPosition;
	private int previousColPosition;
	private int boardHeight;
	private int boardWidth;
	private boolean isPlayerTurn;
	private int walls;
	private int playerNumber;
	Stack<Wall> availableWalls;
	
	
	public Player(int playerNumber, int rowPosition, int colPosition, int boardHeight, int boardWidth, int Walls) {
		this.rowPosition = rowPosition;
		this.colPosition = colPosition;
		this.previousColPosition = colPosition;
		this.previousRowPosition = rowPosition;
		this.boardHeight = boardHeight;
		this.boardWidth = boardWidth;
		this.playerNumber = playerNumber;
		initializeWalls(10);
	}
	
	public int getWalls() {
		return this.walls;
	}
	
	public int getPlayerNumber() {
		return this.playerNumber;
	}
	
	public boolean isTurn() {
		return this.isPlayerTurn;
	}
	
	public void setTurn(boolean isTurn) {
		this.isPlayerTurn = isTurn;
	}
	
	private void finishMove() {
		this.isPlayerTurn = !this.isPlayerTurn;
	}
	
	public int getPreviousRowPosition() {
		return this.previousRowPosition;
	}
	
	public int getPreviousColPosition() {
		return this.previousColPosition;
	}
	
	public int getRowPosition() {
		return this.rowPosition;
	}
	
	public int getColPosition() {
		return this.colPosition;
	}
	
	public void updatePosition(int rowPosition, int colPosition) {
		this.rowPosition = rowPosition;
		this.colPosition = colPosition;
	}
	
	private boolean canMoveUp() {
		if(getRowPosition() >= 2 && isTurn()) {
			return true;
		}
		return false;
	}
	
	private boolean canMoveDown() {
		if(getRowPosition() <= (this.boardHeight - 3) && getRowPosition() >= 0 && isTurn()) {
			return true;
		}
		return false;
	}
	
	private boolean canMoveRight() {
		if(getColPosition() < (this.boardWidth - 3) && isTurn()) {
			return true;
		}
		return false;
	}
	
	private boolean canMoveLeft() {
		if(getColPosition() >= 2 && isTurn()) {
			return true;
		}
		return false;
	}
	
	public void moveUp() {
		if(canMoveUp()) {
			this.previousRowPosition = this.rowPosition;
			this.rowPosition = getRowPosition() - 2;
			System.out.println("Player " + this.playerNumber + " MoveUp Called True");
			finishMove();
		} else {
			System.out.println("Player " + this.playerNumber + " MoveUp Called False");
		}
		
		
	}
	
	public void moveDown() {
		if(canMoveDown()) {
			this.previousRowPosition = this.rowPosition;
			this.rowPosition = getRowPosition() + 2;
			System.out.println("Player " + this.playerNumber + " MoveDown Called True");
			finishMove();
		} else {
			System.out.println("Player " + this.playerNumber + " MoveDown Called False");
		}
		
	}
	
	public void moveRight() {
		if(canMoveRight()) {
			this.previousColPosition = this.colPosition;
			this.colPosition = getColPosition() + 2;
			finishMove();
		}
		
	}
	
	public void moveLeft() {
		if(canMoveLeft()) {
			this.colPosition = getColPosition() - 2;
			finishMove();
		}
	}
	
	private void initializeWalls(int wallsPerPlayer) {
		availableWalls = new Stack<Wall>();
		this.walls = wallsPerPlayer;
		for(int i = 0 ; i < wallsPerPlayer; i++) {
			availableWalls.push(new Wall());
			availableWalls.push(new Wall());		
		}
	}
	
	public boolean placeWall(int[][] coords, ArrayList<Wall> activeWalls) {
			if(this.walls > 0) {
				Wall w = availableWalls.pop();
				--walls;
				w.place(coords);
				activeWalls.add(w);
				finishMove();
				return true;
			}else {
				return false;
			}
			
	}
		
}
