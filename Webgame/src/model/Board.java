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

		generateMonsterAndSeed(numOfMonsters);
	}

	public void generateMonsterAndSeed(int numOfM) throws Exception {
		// add number of monsters with seeds related to player's level
		for (int i = 0; i < numOfM; i++) {
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
				int sx = generateNeighbourPoint(x);
				int sy = generateNeighbourPoint(y);
				while (board[sy][sx] != null) {
					sx = generateNeighbourPoint(x);
					sy = generateNeighbourPoint(y);
				}
				board[sy][sx] = new Seed(sx, sy, System.nanoTime());
				seeds.add((Seed) board[sy][sx]);
				numOfSeeds++;

				if (Math.abs(x - sx) > 2 || Math.abs(y - sy) > 2) {
					throw new Exception("Exception: incorrect seed generated!!");
				}
			}
		}
	}

	private int generateNeighbourPoint(int x) {
		int result = x - 1 + random.nextInt(3);
		while (result < 0 || result >= WIDTH) {
			result = x - 1 + random.nextInt(3);
		}
		return result;
	}

	public List<Monster> getMonsters() {
		return monsters;
	}

	public synchronized List<Seed> getSeeds() {
		return seeds;
	}

	public Player getPlayer() {
		return player;
	}

	public void removeDeadMonster(Monster deadMonster) throws Exception {
		int x = deadMonster.getX();
		int y = deadMonster.getY();

		if (!monsters.remove(deadMonster)) {
			throw new Exception("The Game has Bugs!");
		}
		board[y][x] = null;
		numOfMonsters--;

	}

	public void clearMonsterWhenHitBySeed(Monster deadMonster) {
		board[deadMonster.getY()][deadMonster.getX()] = null;
		numOfMonsters--;
	}

	public void removeUsedSeeds(Seed seed) throws Exception {
		board[seed.getY()][seed.getX()] = null;
		if (!seeds.remove(seed)) {
			throw new Exception("The Game has bugs");
		}
		numOfSeeds--;
	}

	public int getNumOfSeeds() {
		return numOfSeeds;
	}

	public void changePlayerPos(int x, int y) {
		board[player.getY()][player.getX()] = null;
		board[y][x] = player;
		player.setX(x);
		player.setY(y);
	}

	public boolean changeMonsterPos(Monster m, int newx, int newy) {
		board[m.getY()][m.getX()] = null;
		if (board[newy][newx] == null) {
			board[newy][newx] = m;
			m.setX(newx);
			m.setY(newy);
			return true;
		}
		return false;
	}

	public boolean isEmpty(int x, int y) {
		if (board[y][x] == null) {
			return true;
		}
		return false;
	}

	public Object getObject(int x, int y) {
		return board[y][x];
	}

	public void printAllCoodinateOfMonsters() { // for testing
		System.out.println("Monster List");
		for (Monster monster : monsters) {
			System.out.println(monster.getX() + " " + monster.getY());
		}
		System.out.println();
		System.out.println();
		System.out.println();
	}

	public void printAllMonsterOnBoard() {
		System.out.println("Monster on board");
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				if (board[j][i] instanceof Monster) {
					Monster monster = (Monster) board[j][i];
					System.out.println(i + " " + j + "     " + monster.getX()
							+ " " + monster.getY());
				}
			}
		}
		System.out.println();
		System.out.println();
		System.out.println();
	}

}