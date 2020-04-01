package com.lpreciado.Quoridor;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class GUI {
	int frameHeight = 645;
	int frameWidth = 580;
	int gameBoardSize = 400;
	int marginSize = 16;
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
	JButton rulesButton = new JButton();


	public GUI() {
		startGame();
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
		private int counter = 0;
		@Override
		protected void paintComponent(Graphics g) {
			counter = 0;
			g.setColor(new Color(20, 20, 20));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			gb.addMouseListener(this);
			addMouseListener(this);
			char[][] board = game.getGameBoard();
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
			Point p = e.getPoint();
			if(counter == 0){
				if(game.manageScreenClick(p.getX(), p.getY())) {
					manageGame(game);
					++counter;
				}
			}else{
				System.out.println("COUNTER NOT ZERO");
			}

		}

		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		
	}

	class MyFrame extends JFrame implements KeyListener {
		int cols = gameRows;
		int rows = gameColumns;

		@Override
		public void keyPressed(KeyEvent e) {}

		@Override
		public void keyReleased(KeyEvent e) {
			GameState gameState = game.getGameState();
			boolean playerHasAlreadyWon = gameState == GameState.P1_WIN || gameState == GameState.P2_WIN;
			if (playerHasAlreadyWon) System.out.println("Player has already Won");
			int key = e.getKeyCode();
			char[][] gameBoard = game.getGameBoard();
			switch (key) {	
				case (KeyEvent.VK_UP):
					if (gameState == GameState.P1_TURN) {
						game.p1.moveUp(gameBoard);
					} else {
						game.p2.moveUp(gameBoard);
					}
					break;
				case KeyEvent.VK_DOWN:
					if (gameState == GameState.P1_TURN) {
						game.p1.moveDown(gameBoard);
					} else {
						game.p2.moveDown(gameBoard);
					}
					break;
				case KeyEvent.VK_LEFT:
					if (gameState == GameState.P1_TURN) {
						game.p1.moveLeft(gameBoard);
					} else {
						game.p2.moveLeft(gameBoard);
					}
					break;
				case KeyEvent.VK_RIGHT:
					if (gameState == GameState.P1_TURN) {
						game.p1.moveRight(gameBoard);
					} else {
						game.p2.moveRight(gameBoard);
					}
					break;
			}

			manageGame(game);

		}

		@Override
		public void keyTyped(KeyEvent e) {}
	}

	private void manageGame(Game game) {
		System.out.println("Manage Game Called");
		game.updateBoard();
		updatePlayerTurn();
		updateJLabels();
		gb.repaint();
		game.printBoard();
	}
	private void updateJLabels(){
		PlayerOneWallsLabel.setText("Player 1 Walls: " + game.p1.getWalls());
		PlayerTwoWallsLabel.setText("Player 2 Walls: " + game.p2.getWalls());
	}
	
	private void updatePlayerTurn() {
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
	
	private void startGame(){
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
		PlayerOneWallsLabel = new JLabel("Player 2 Walls: " + game.p1.getWalls(), SwingConstants.LEFT);
		PlayerTwoWallsLabel = new JLabel("Player 1 Walls: " + game.p2.getWalls(), SwingConstants.RIGHT);
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

	public void displayRules(){
		
	}
}
