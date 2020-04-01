package com.lpreciado.Quoridor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class GUI {
	int frameHeight = 645;
	int frameWidth = 580;
	int gameBoardSize = 400;
	int marginSize = 16;
	int highScore;
	int gameRows = 18;
	int gameColumns = 18;

	Color backgroundColor = new Color(152, 0, 0);
	Hashtable<Character, ImageIcon> Tiles;
	Game game;
	GameBoard gb;
	JFrame frame;
	Font largeFeedbackFont = new Font("SansSerif", 0, 40);
	Font smallFeedbackFont = new Font("SansSerif", 0, 20);
	JLabel playerTurn;
	JLabel numberWalls;
	JLabel PlayerOneWallsLabel;
	JLabel PlayerTwoWallsLabel;

	int playerOneWalls;
	int playerTwoWalls;

	public GUI() {
		game = new Game(10);
		this.frame = new MyFrame();
		frame.setFocusable(true);
		frame.setResizable(false);
		frame.addKeyListener(new MyFrame());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadTiles(); // initialize the tiles in a hashSet

		gb = new GameBoard();
		gb.setFocusable(true);
		// North Panel
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridLayout());
		northPanel.setPreferredSize(new Dimension(frameWidth, 82));

		JLabel gameLabel = new JLabel("Quoridor", SwingConstants.CENTER);
		northPanel.add(gameLabel);
		playerOneWalls = game.p1.getWalls();
		playerTwoWalls = game.p2.getWalls();
		PlayerOneWallsLabel = new JLabel("Player 2 Walls: " + playerOneWalls, SwingConstants.LEFT);
		PlayerTwoWallsLabel = new JLabel("Player 1 Walls: " + playerTwoWalls, SwingConstants.RIGHT);
		playerTurn = new JLabel("Player 1", SwingConstants.LEFT);
		gameLabel.setFont(new Font("Serif", Font.BOLD, 20));
		northPanel.add(playerTurn);
		northPanel.add(PlayerOneWallsLabel);
		northPanel.add(PlayerTwoWallsLabel);

		northPanel.setBackground(backgroundColor);

		// West Panel
		JPanel westBuffer = new JPanel();
		westBuffer.setPreferredSize(new Dimension(marginSize, gameBoardSize));
		westBuffer.setBackground(backgroundColor);
		// East Panel
		JPanel eastBuffer = new JPanel();
		eastBuffer.setPreferredSize(new Dimension(marginSize, gameBoardSize));
		eastBuffer.setBackground(backgroundColor);
		// South Panel
		JPanel southBuffer = new JPanel();
		southBuffer.setPreferredSize(new Dimension(frameWidth, marginSize));
		southBuffer.setBackground(backgroundColor);

		// Add Panels to Frame
		frame.getContentPane().add(northPanel, BorderLayout.NORTH);
		frame.getContentPane().add(westBuffer, BorderLayout.WEST);
		frame.getContentPane().add(eastBuffer, BorderLayout.EAST);
		frame.getContentPane().add(southBuffer, BorderLayout.SOUTH);
		frame.getContentPane().add(gb, BorderLayout.CENTER);

		frame.getContentPane().setPreferredSize(new Dimension(frameWidth, frameHeight));
		frame.pack();
		frame.setVisible(true);
	}

	private void loadTiles() {
		Tiles = new Hashtable<Character, ImageIcon>();
		ClassLoader cldr = this.getClass().getClassLoader();
		URL urlPlayer1 = cldr.getResource("images/tile_player1.png");
		URL urlPlayer2 = cldr.getResource("images/tile_player2.png");
		URL urlEmptyTile = cldr.getResource("images/tile_empty.png");
		URL urlWall = cldr.getResource("images/tile_wall.png");
		URL urlEmptyWallPoint = cldr.getResource("images/point.png");
		URL urlEmptyWallHorizontal = cldr.getResource("images/tile_wall_empty_horizontal.png");
		URL urlWallHorizontal = cldr.getResource("images/tile_wall_horizontal.png");
		URL urlEmptyWallVertical = cldr.getResource("images/tile_empty_wall.png");
		URL urlWallVertical = cldr.getResource("images/tile_wall_vertical.png");

		Tiles.put('1', new ImageIcon(urlPlayer1));
		Tiles.put('2', new ImageIcon(urlPlayer2));
		Tiles.put('o', new ImageIcon(urlEmptyTile));
		Tiles.put('W', new ImageIcon(urlWall));
		Tiles.put('.', new ImageIcon(urlEmptyWallPoint));
		Tiles.put('-', new ImageIcon(urlEmptyWallHorizontal));
		Tiles.put('|', new ImageIcon(urlEmptyWallVertical));
		Tiles.put('h', new ImageIcon(urlWallHorizontal));
		Tiles.put('v', new ImageIcon(urlWallVertical));

	}

	class GameBoard extends JPanel implements MouseListener {
		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(new Color(20, 20, 20));
			System.out.println("GB WIDTH: " + this.getWidth());
			System.out.println("GB HEIGHT: " + this.getHeight());
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			gb.addMouseListener(this);
			addMouseListener(this);
			char[][] board = game.getGameBoard();
			playerOneWalls = game.p1.getWalls();
			playerTwoWalls = game.p2.getWalls();
			int previousX = 8;
			int previousY = 8;
			int fatIncrease = 50;// TILE HEIGHT
			int slimIncrease = 10; // TILE WIDTH
			for (int y = 0; y < board.length; y++) {
				for (int x = 0; x < board[y].length; x++) {
					char thisCharacter = board[y][x];
					int X;
					int Y = previousY;
					if (x == 0) { // New Row
						X = 8;
						previousX = X + fatIncrease;
					} else if (x % 2 != 0) {
						X = previousX;
						previousX = X + slimIncrease;
					} else {
						X = previousX;
						previousX = X + fatIncrease;
					}

					if (y % 2 == 0 && x == board[y].length - 1 || y == 0 && x == board[y].length - 1) {
						previousY = Y + fatIncrease;
					}
					if (y % 2 != 0 && x == board[y].length - 1) {
						previousY = Y + slimIncrease;
					}

					if (Tiles.containsKey(thisCharacter)) {
						ImageIcon thisTile = Tiles.get(thisCharacter);
						thisTile.paintIcon(this, g, X, Y);
					}

				}
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Point p = e.getPoint();
			isValidWall(p.getX(), p.getY());


		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

		private boolean isValidWall(double x, double y){
			if(x <= 9 || x >= 539 || y <=9 || y>=539) return false;
			int small = 10;
			double previousX = 59;
			for(int i = 0; i < 8; i++){ //loop thru the board
				double min = previousX;
				double max = min + small;
				boolean ValidXCoord = x > min && x < max;
				boolean ValidYCoord = y > min && y < max;
				if(ValidXCoord || ValidYCoord){
					i = convertToBoardCoord(i);
					if(ValidXCoord){ //Valid X Coord
						// i is equal to the col number
						// check in the game if 
						
						// System.out.println("Row" + y + "Column" + i);
						game.hasAvailableWallGivenColumn(y, i);
					}else{ //Valid Y Coord
						// i is equal to the row number
						// System.out.println("Row" + i + "Column" + y);
						game.hasAvailableWallGivenRow(x, i);
					}
					return true;
				}
				previousX = max + 50;
			}
			
			return false;
		}

		private int convertToBoardCoord(int i ){
			if(i==0){
				i += 1;
			} else if(i!=0) i = 2*i + 1;
			return i;
		}
		
	}

	class MyFrame extends JFrame implements KeyListener {
		int cols = gameRows;
		int rows = gameColumns;

		@Override
		public void keyPressed(KeyEvent e) {

		}

		private void manageGame(Game game) {
			System.out.println("Manage State Called");
			game.updateBoard();
			updatePlayerTurn();
			gb.repaint();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			GameState gameState = game.getGameState();
			boolean playerHasAlreadyWon = gameState == GameState.P1_WIN || gameState == GameState.P2_WIN;
			if (playerHasAlreadyWon)
				System.out.println("Player has already Won");
			int key = e.getKeyCode();

			switch (key) {
				case (KeyEvent.VK_UP):
					if (gameState == GameState.P1_TURN) {
						game.p1.moveUp();
					} else {
						game.p2.moveUp();
					}
					break;
				case KeyEvent.VK_DOWN:
					if (gameState == GameState.P1_TURN) {
						game.p1.moveDown();
					} else {
						game.p2.moveDown();
					}
					break;
				case KeyEvent.VK_LEFT:
					if (gameState == GameState.P1_TURN) {
						game.p1.moveLeft();
					} else {
						game.p2.moveLeft();
					}
					break;
				case KeyEvent.VK_RIGHT:
					if (gameState == GameState.P1_TURN) {
						game.p1.moveRight();
					} else {
						game.p2.moveRight();
					}
					break;
			}

			manageGame(game);

		}

		@Override
		public void keyTyped(KeyEvent e) {

		}
	}

	public void updatePlayerTurn() {
		GameState gameState = game.getGameState();
		String message = "";
		switch (gameState) {
			case P1_TURN:
				message = "Player 1";
				break;
			case P2_TURN:
				message = "Player 2";
				break;
			case P1_WIN:
				message = "Player 1 Win";
				break;
			case P2_WIN:
				message = "Player 2 Win";
				break;
			case CONTINUE:
				message = "CONTINUE";
				break;
		}
		playerTurn.setText(message);

	}

}
