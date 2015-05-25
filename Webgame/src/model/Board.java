package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {

	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	
	private static Random random = new Random();
	
	private Object[][] board;
	private List<Obstacle> obstacles = new ArrayList<Obstacle>();
	private List<Monster> monsters = new ArrayList<Monster>();
	private Player player;
	private int numOfMonsters;
	private int numOfObstacles;
	
	public Board(Player player, int level) {
		this.player = player;
		board = new Object[HEIGHT][WIDTH];
		numOfMonsters = level * level;
		numOfObstacles = level * 7;
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				board[j][i] = null;
			}
		}
		
		//add number of monsters related to player's level 
		for (int i = 0; i < numOfMonsters; i++) {
			int x = random.nextInt(WIDTH);
			int y = random.nextInt(HEIGHT);
			while (board[y][x] != null) {
				x = random.nextInt(WIDTH);
				y = random.nextInt(HEIGHT);
			}
			board[y][x] = new Monster(x, y);
			monsters.add((Monster) board[y][x]);
		}
		//add number of obstacles related to player's level
		for (int i = 0; i < numOfObstacles; i++) {
			int x = random.nextInt(WIDTH);
			int y = random.nextInt(HEIGHT);
			while (board[y][x] != null) {
				x = random.nextInt(WIDTH);
				y = random.nextInt(HEIGHT);
			}
			board[y][x] = new Obstacle(x, y);
			obstacles.add((Obstacle) board[y][x]);
		}
	}
	
	public List<Obstacle> getObstacleList() {
		return obstacles;
	}
	
	public List<Monster> getMonsters() {
		return monsters;
	}
	
	public Player getPlayer() {
		return player;
	}

}

