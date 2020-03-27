package com.lpreciado.Quoridor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class GUI {
	int frameHeight = 700;
	int frameWidth = 700;
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
	
	
	public GUI() {
		game = new Game(10);
		this.frame = new MyFrame(); 
		frame.setFocusable(true);
		//frame.setResizable(false);
		frame.addKeyListener(new MyFrame());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadTiles(); //initialize the tiles in a hashSet

		gb = new GameBoard();
		gb.setFocusable(true);
		// North Panel
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridLayout());
		northPanel.setPreferredSize(new Dimension(frameWidth, 82));

		JLabel gameLabel = new JLabel("Quoridor", SwingConstants.CENTER);
		northPanel.add(gameLabel);
		JLabel PlayerOneWallsLabel = new JLabel("Player 2 Walls: " + 10 , SwingConstants.LEFT);
		JLabel PlayerTwoWallsLabel = new JLabel("Player 1 Walls: " + 10 , SwingConstants.RIGHT);
		gameLabel.setFont(new Font("Serif", Font.BOLD, 20));
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
	
	class GameBoard extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(new Color(20, 20, 20));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			char[][] board = game.getGameBoard();
			int previousX = 8;
			int previousY = 8;
			int fatIncrease = 50;//TILE HEIGHT
			int slimIncrease = 10; //TILE WIDTH
			for (int y = 0; y < board.length; y++) {
				
				for (int x = 0; x < board[y].length; x++) {
					char thisCharacter = board[y][x];
					int X;
					int Y = previousY;
					if(x == 0) { //New Row
						 X = 8;
						 previousX = X + fatIncrease;
					} else if (x % 2 != 0) {
						X = previousX;
						previousX = X  + slimIncrease ;
					} else {
						X = previousX;
						previousX = X  + fatIncrease;
					}
					
					
					if(y %2 == 0 && x == board[y].length - 1 || y == 0 && x == board[y].length -1) {
						previousY = Y + fatIncrease;
					}
					if(y %2 != 0 && x == board[y].length -1) {
						previousY = Y + slimIncrease;
					}
					
					if (Tiles.containsKey(thisCharacter)) { 
						ImageIcon thisTile = Tiles.get(thisCharacter);
						thisTile.paintIcon(this, g, X, Y);
					}

				}
			}
		}

	}
	
	class MyFrame extends JFrame implements KeyListener {
		int cols = gameRows;
		int rows = gameColumns;
		
		@Override
		public void keyPressed(KeyEvent e) {

		}
		private void manageGame(Game game, GameBoard gb) {
			System.out.print("Manage State");
		}
		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			if (game.getGameState() == GameState.P1_TURN) {
				switch (key) {
				case (KeyEvent.VK_UP):
					System.out.println("UP");
					break;
				case KeyEvent.VK_DOWN:
					System.out.println("DOWN");
					break;
				case KeyEvent.VK_LEFT:
					System.out.println("LEFT");
					break;
				case KeyEvent.VK_RIGHT:
					System.out.println("RIGHT");
					break;
				}
				
			} 

		}

		@Override
		public void keyTyped(KeyEvent e) {

		}
	}
}