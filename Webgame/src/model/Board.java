package model;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Board {

	public static final int WIDTH = 750 / 25;
	public static final int HEIGHT = 750 / 25;

	private static Random random = new Random();

	private Object[][] board;
	private List<Monster> monsters = new CopyOnWriteArrayList<Monster>();
	private List<Seed> seeds = new CopyOnWriteArrayList<Seed>();

	private List<Emit> emitSeedsTo = new CopyOnWriteArrayList<Emit>();
	private Player constantPlayer;
	private Player player;
	private int numOfMonsters;
	private int numOfSeeds;
//	private final int[] numberOfMonstersInLevel = { 15, 20, 25, 30, 35 };
    private final int[] numberOfMonstersInLevel = { 2,2,2,2,2 };
	private int jump;
	private int jumpGainedThisRound;
	private boolean canStart;

	public Board(Player player) throws Exception {
		constantPlayer = player;
		this.player = player;
		int level = player.getLevel();
		board = new Object[HEIGHT][WIDTH];
		if (level > 4) {
			numOfMonsters = 40;
		} else {
			if (level < 5) {
				numOfMonsters = numberOfMonstersInLevel[level];
			} else {
				numOfMonsters = 40;
			}
		}
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				board[j][i] = null;
			}
		}

		// add player to the center of board
		board[(int) player.getY()][player.getX()] = player;

		generateMonsterAndSeed(numOfMonsters);
	}

	public synchronized void generateMonsterAndSeed(int numOfM)
			throws Exception {
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
			if (random.nextInt(5) > 0) {
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

	public synchronized int getMonsterToKill() {
		return (player.getLevel()+1) * 10;
	}

	public synchronized List<Monster> getMonsters() {
		return monsters;
	}

	public synchronized List<Seed> getSeeds() {
		return seeds;
	}

	public Player getPlayer() {
		return player;
	}

	public synchronized void addEmitSeedsTo() {
		emitSeedsTo.add(new Emit());
	}

	public synchronized List<Emit> getEmitSeedsTo() {
		return emitSeedsTo;
	}

	public synchronized void removeEmit(Emit e) {
		emitSeedsTo.remove(e);
	}

	public void restartBoard() throws Exception { // constant player won't
			// change in the process
		jumpGainedThisRound = 0;
		player = constantPlayer;
		player.reborn();
		board[14][14] = player;
		int level = player.getLevel();
		jumpGainedThisRound = 0;
		int numberOfMonster = numberOfMonstersInLevel[level];
		generateMonsterAndSeed(numberOfMonster);
	}

	public void startNextLevelBoard() throws Exception { // constant player
															// won't
		// change in the process
		jumpGainedThisRound = 0;
		player = constantPlayer;
		int level = player.incLevel();
		player.reborn();
		board[14][14] = player;
		if (level > 4) {
			numOfMonsters = 40;
		} else {
			numOfMonsters = numberOfMonstersInLevel[level];
		}
		generateMonsterAndSeed(numOfMonsters);
	}

	public synchronized void clearMonsterWhenHitBySeed(Monster deadMonster) {
		board[deadMonster.getY()][deadMonster.getX()] = null;
		numOfMonsters--;
	}

	public void removeUsedSeeds(Seed seed) throws Exception {
		board[seed.getY()][seed.getX()] = null;
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
	
	public void changePlayerPos(Player player) {
		board[player.getY()][player.getX()] = null;
		int x = random.nextInt(WIDTH);
		int y = random.nextInt(HEIGHT);
		while (board[y][x] != null) {
			x = random.nextInt(WIDTH);
			y = random.nextInt(HEIGHT);
		}
		board[y][x] = player;
		player.setX(x);
		player.setY(y);
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

	public synchronized void clearEverything() {
		constantPlayer.setJump(jump);
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				board[j][i] = null;
			}
		}
		monsters.clear();
		seeds.clear();
		emitSeedsTo.clear();
		player = null;
		numOfMonsters = 0;
		numOfSeeds = 0;
	}

	public boolean hasPlayer() {
		return player != null;
	}

	public void setStart(boolean start) {
		canStart = start;
	}

	public boolean canStart() {
		return canStart;
	}
	
	public synchronized void getOneJump() {
		jump++;
		constantPlayer.getJump();
	}
	
	public boolean useOneJump() {
		if (jump >= 1) {
			jump--;
			return true;
		}
		return false;
	}
	
	public int getJump() {
		return jump;
	}
	
	public synchronized void setJumpGainedInLevel(int jump) {
		jumpGainedThisRound = jump;
	}
	
	public synchronized int getJumpGainedThisRound() {
		return jumpGainedThisRound;
	}

}