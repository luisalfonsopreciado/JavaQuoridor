package com.lpreciado.Quoridor;

public class Wall {
	private int[][] coords;
	public boolean isOnTheBoard;
	
	
	public Wall() {
		this.isOnTheBoard = false;	
	}
	
	public void place(int[][] coords) {
		this.coords = coords;
	}
	
	public int[][] getCoords(){
		return this.coords;
	}
	
	
}
