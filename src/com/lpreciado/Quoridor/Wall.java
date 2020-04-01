package com.lpreciado.Quoridor;

public class Wall {
	private int[][] coords;
	public boolean isOnTheBoard;
	
	
	public Wall() {
		this.isOnTheBoard = false;	
	}
	
	public void place(int[] coords) {
		int[][] result = new int[3][2];
		int col = coords[1];
		int row = coords[0];
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
		this.coords = result;
	}
	
	public int[][] getCoords(){
		return this.coords;
	}
	
	
}
