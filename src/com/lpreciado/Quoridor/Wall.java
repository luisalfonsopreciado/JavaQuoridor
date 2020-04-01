package com.lpreciado.Quoridor;

public class Wall {
	private int[][] coords;
	public boolean isOnTheBoard;
	
	
	public Wall() {
		this.isOnTheBoard = false;	
	}
	
	public void place(int[] coords) {
		int[][] result = new int[3][2];
		result[0][0] = coords[0];
		result[0][1] = coords[1];
		if(coords[0] % 2 == 0 || coords[0] == 0){
			result[1][0] = coords[0];
			result[1][1] = coords[1] + 1;
			result[2][0] = coords[0];
			result[2][1] = coords[1] + 2;
		}
		this.coords = result;
	}
	
	public int[][] getCoords(){
		return this.coords;
	}
	
	
}
