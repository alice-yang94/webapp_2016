package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {

	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	
	private static Random random = new Random();
	
	private Object[][] board;
	private List<Monster> monsters = new ArrayList<Monster>();
	private Player player;
	private int numOfMonsters;
	private int numOfSeeds;
	
	public Board(Player player) {
		this.player = player;
		int level = player.getLevel();
		board = new Object[HEIGHT][WIDTH];
		numOfMonsters = level * level;
		numOfSeeds = 40 - level * level; 
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				board[j][i] = null;
			}
		}
		
		//add player to the center of board
		board[(int) player.getY()][player.getX()] = player;
		
		//add number of monsters related to player's level 
		for (int i = 0; i < numOfMonsters; i++) {
			//generate monsters
			int x = random.nextInt(WIDTH);
			int y = random.nextInt(HEIGHT);
			while (board[y][x] != null) {
				x = random.nextInt(WIDTH);
				y = random.nextInt(HEIGHT);
			}
			board[y][x] = new Monster(x, y);
			monsters.add((Monster) board[y][x]);
			
			//generate seeds
			if (random.nextInt(2) == 1) {
				int sx = (x-1) + random.nextInt(3);
				int sy = (y-1) + random.nextInt(3);
				while (board[sy][sx] != null) {
					sx = (x-1) + random.nextInt(3);
					sy = (y-1) + random.nextInt(3);
				}
				board[sy][sx] = new Seed(sx, sy);
			}	
		}
	}

	
	public List<Monster> getMonsters() {
		return monsters;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void removeDeadMonster(Monster deadMonster) throws Exception {
		if (!monsters.remove(deadMonster)) {
			throw new Exception("The Game has Bugs!");
		}
		numOfMonsters--;
	}


	public int getNumOfSeeds() {
		return numOfSeeds;
	}

}