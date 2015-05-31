package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {

	public static final int WIDTH = 750 / 25;
	public static final int HEIGHT = 750 / 25;

	private static Random random = new Random();

	private Object[][] board;
	private List<Monster> monsters = new ArrayList<Monster>();
	private List<Seed> seeds = new ArrayList<Seed>();
	private Player player;
	private int numOfMonsters;
	private int numOfSeeds;

	public Board(Player player) throws Exception {
		this.player = player;
		int level = player.getLevel();
		board = new Object[HEIGHT][WIDTH];
		numOfMonsters = level * level;

		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				board[j][i] = null;
			}
		}

		// add player to the center of board
		board[(int) player.getY()][player.getX()] = player;

		// add number of monsters with seeds related to player's level
		for (int i = 0; i < numOfMonsters; i++) {
			// generate monsters
			int x = random.nextInt(WIDTH);
			int y = random.nextInt(HEIGHT);
			while (board[y][x] != null) {
				x = random.nextInt(WIDTH);
				y = random.nextInt(HEIGHT);
			}
			board[y][x] = new Monster(x, y);
			monsters.add((Monster) board[y][x]);

			// generate seeds
			if (random.nextInt(2) == 1) {
				int sx = generateNeighbourPoint(x, true);
				int sy = generateNeighbourPoint(y, false);
				while (board[sy][sx] != null) {
					sx = generateNeighbourPoint(x, true);
					sy = generateNeighbourPoint(y, false);
				}
				board[sy][sx] = new Seed(sx, sy);
				seeds.add((Seed) board[sy][sx]);
				numOfSeeds++;

				if (Math.abs(x - sx) > 2 || Math.abs(y - sy) > 2) {
					throw new Exception("Exception: incorrect seed generated!!");
				}

			}

		}
	}

	private int generateNeighbourPoint(int x, boolean isWidth) {
		int result = x - 1 + random.nextInt(3);
		int compareWith;
		if (isWidth) {
			compareWith = WIDTH;
		} else {
			compareWith = HEIGHT;
		}
		while (result < 0 || result >= compareWith) {
			result = x - 1 + random.nextInt(3);
		}
		return result;
	}

	public List<Monster> getMonsters() {
		return monsters;
	}

	public List<Seed> getSeeds() {
		return seeds;
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

	public void removeUsedSeeds(Seed seed) throws Exception {
		if (!seeds.remove(seed)) {
			throw new Exception("The Game has bugs");
		}
		numOfSeeds--;
	}

	public int getNumOfSeeds() {
		return numOfSeeds;
	}

	// public Object[][] getBoard() {
	// return board;
	// }

}