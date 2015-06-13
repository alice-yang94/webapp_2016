package controller;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import model.Board;
import model.Monster;
import model.Player;
import model.Seed;

public class BoardController {

	private Board board;
	private Player player;

	private int targetX, targetY, seedCounter;
	private boolean hasInput = false; // true if player input on keyboard
	private int endGame = 0;
	private static int killedMonster = 0;
	public static boolean win = false;
	public static long timeUsedToWin = -1;

	public static final int INITIAL_COUNTER = 0;
	public static final int SEED_COUNTER = 2;
	public static final int MONSTER_INC_NUMBER = 3;
	private AudioInputStream audioInputStream;
	private Clip clip;
	private boolean canAddMonsters;

	public BoardController(Board board) {
		this.board = board;
		seedCounter = INITIAL_COUNTER;
		player = board.getPlayer();
		canAddMonsters = false;

		try {
			audioInputStream = AudioSystem.getAudioInputStream(this.getClass()
					.getResource("music/backgroundMusic.wav"));
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		} catch (Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}

		clip.start();
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public synchronized void update(long initialTime) throws Exception {
		if (board.hasPlayer()) {
			win = false;
			// player wins if he kills the certain amount of monster
			if (killedMonster >= board.getMonsterToKill()) {
				timeUsedToWin = System.nanoTime() - initialTime;
				win = true;
				endGame++;
				while (playerLoseLife()) {
					playerLoseLife();
				}
				// TODO: SHOW WIN MSG AND CLEAREVERYTHING, RESTART AND
				// GOTO NEXT LEVEL
			}
		}
		if (hasInput || endGame > 0) {
			boolean notMonster = true;

			// if player meets monster, player loselife, monster die
			Iterator<Monster> iter = board.getMonsters().iterator();
			while (iter.hasNext()) {
				Monster monster = iter.next();
				if (monster.equalsCoordinate(targetX, targetY)) {
					if (!playerLoseLife()) {
						endGame++;
						break;
					}

					iter.remove();
					board.clearMonsterWhenHitBySeed(monster);
					killedMonster++;
					notMonster = false;
					break;
				}
			}

			// if the grid contains seed, player gets a bullet
			if (notMonster) {
				Iterator<Seed> iterS = board.getSeeds().iterator();
				while (iterS.hasNext()) {
					Seed seed = iterS.next();
					if (seed.equals(targetX, targetY)) {
						player.winBullets();
						board.removeUsedSeeds(seed);
						break;
					}
				}
			}
			if (endGame == 0) {
				if (board.hasPlayer()) {
					board.changePlayerPos(targetX, targetY);
				}
				monsterMove();
			}

			if (endGame > 0) {
				endGame++;
				playerLoseLife();
			}
			ifDie();

		}
		hasInput = false;
	}

	public void pressUp() {
		int y = player.getY() - 1;
		if (withinIndexMove(y)) {
			targetX = player.getX();
			targetY = y;
			hasInput = true;
			return;
		} else { // hit boundary, lose a life
			playerLoseLife();
		}
	}

	public void pressDown() {
		int y = player.getY() + 1;
		if (withinIndexMove(y)) {
			targetX = player.getX();
			targetY = y;
			hasInput = true;
			return;
		} else { // hit boundary, lose a life
			playerLoseLife();
		}
	}

	public void pressRight() {
		int x = player.getX() + 1;
		if (withinIndexMove(x)) {
			targetX = x;
			targetY = player.getY();
			hasInput = true;
			return;
		} else { // hit boundary, lose a life
			playerLoseLife();
		}
	}

	public void pressLeft() {
		int x = player.getX() - 1;
		if (withinIndexMove(x)) {
			targetX = x;
			targetY = player.getY();
			hasInput = true;
			return;
		} else { // hit boundary, lose a life
			playerLoseLife();
		}
	}

	public synchronized void pressSpace() throws Exception {
		if (player.decreaseBullets()) {
			seedCounter = 2;
			hitMonster();
		}
	}

	public synchronized void pressEnter() throws Exception { // restart the game or start	
		if (!board.hasPlayer()) {
			board.restartBoard();
			endGame = 0;
		}
		board.setStart(true);
		canAddMonsters = true;
	}
	
	public synchronized boolean canAddMonsters() {
		return canAddMonsters;
	}

	public synchronized void hitMonster() throws Exception {
		Iterator<Monster> iter = board.getMonsters().iterator();
		while (iter.hasNext()) {
			Monster monster = iter.next();

			if (seedCounter > 0) {
				int mx = monster.getX();
				int my = monster.getY();
				int px = player.getX();
				int py = player.getY();
				if (mx == px || my == py) {
					if (monster.loseLife()) {
						seedCounter--;
					} else {
						iter.remove();
						board.clearMonsterWhenHitBySeed(monster);
						killedMonster++;
					}
				}
			} else {
				break;
			}
		}

		board.addEmitSeedsTo();

	}

	private boolean withinIndexMove(int x) {
		if (x >= 0 && x < Board.WIDTH) {
			return true;
		}
		return false;
	}

	private synchronized boolean playerLoseLife() {
		if (endGame >= 20) {
			board.clearEverything();
			killedMonster = 0;
			canAddMonsters = false;
		}
		if (player.loseLife()) { // still have life
			hasInput = false; // invalid move, has to wait another input
			return true;
		} else { // restart game
			endGame++;
			return false;
		}
	}

	private int getDistance(int[] a1, int[] a2) {
		int x = Math.abs(a1[0] - a2[0]);
		int y = Math.abs(a1[1] - a2[1]);
		return x + y;
	}

	private class Item implements Comparable<Item> {
		public int[] value;
		public int d;

		public Item(int[] value, int d) {
			this.value = value;
			this.d = d;
		}

		public int compareTo(Item item) {
			if (d < item.getD()) {
				return -1;
			}
			return 1;
		}

		public int[] getValue() {
			return value;
		}

		public int getD() {
			return d;
		}
	}

	private Item[] ordering(int[] m, int[] up, int[] down, int[] left,
			int[] right) {

		Item[] items = new Item[4];
		items[0] = new Item(up, getDistance(m, up));
		items[1] = new Item(down, getDistance(m, down));
		items[2] = new Item(left, getDistance(m, left));
		items[3] = new Item(right, getDistance(m, right));

		Arrays.sort(items);

		return items;
	}

	public synchronized void monsterMove() {
		Iterator<Monster> iter = board.getMonsters().iterator();
		int px, py;
		while (iter.hasNext()) {
			Monster monster = iter.next();
			int monsterX = monster.getX();
			int monsterY = monster.getY();

			int[] monsterIntend = null;

			px = player.getX();
			py = player.getY();

			// int[0] = x, int[1] = y
			int[] up = { px, py - 1 };
			int[] down = { px, py + 1 };
			int[] left = { px - 1, py };
			int[] right = { px + 1, py };

			int[] m = { monsterX, monsterY };

			Item[] items = ordering(m, up, down, left, right);

			monsterIntend = items[0].getValue();

			boolean empty = false;
			Monster mstr = null;
			int index = 0;

			while (!empty) {
				if (index >= 3) {
					break;
				}
				empty = true;
				Iterator<Monster> iterM = board.getMonsters().iterator();
				while (iterM.hasNext()) {
					Monster mon = iterM.next();
					if (mon.equalsCoordinate(monsterIntend[0], monsterIntend[1])) {
						index++;
						mstr = mon;
						monsterIntend = items[index].getValue();
						empty = false;
						break;
					}
				}
				if (mstr == null) {
					Iterator<Seed> iterS = board.getSeeds().iterator();
					while (iterS.hasNext()) {
						Seed sd = iterS.next();
						if (sd.equals(monsterIntend[0], monsterIntend[1])) {
							index++;
							monsterIntend = items[index].getValue();
							empty = false;
							break;
						}
					}
				}
				mstr = null;
			}

			if (!(monsterIntend[0] == player.getX() && monsterIntend[1] == player
					.getY())) {
				if (index < 3) {
					Random random = new Random();
					int rand = random.nextInt(2);
					if (rand == 0) {
						moveX(m, monsterIntend, monster);
						moveY(m, monsterIntend, monster);
					} else {
						moveY(m, monsterIntend, monster);
						moveX(m, monsterIntend, monster);
					}
				}
			}

		}
	}

	public void ifDie() {
		int px = player.getX();
		int py = player.getY();
		Iterator<Monster> iterM = board.getMonsters().iterator();
		int count = 0;
		while (iterM.hasNext()) {
			Monster monster = iterM.next();
			if (monster.equalsCoordinate(px + 1, py)
					|| monster.equalsCoordinate(px - 1, py)
					|| monster.equalsCoordinate(px, py + 1)
					|| monster.equalsCoordinate(px, py - 1)) {
				count++;
			}
			if (count == 4) {
				while (playerLoseLife()) {
					playerLoseLife();
				}
				break;
			}
			if (count == 3 && (isInBoarder(px) || isInBoarder(py))) {
				while (playerLoseLife()) {
					playerLoseLife();
				}
				// endGame++;
				break;
			}
			if (count == 2 && (isInBoarder(px) && isInBoarder(py))) {
				while (playerLoseLife()) {
					playerLoseLife();
				}
				// endGame++;
				break;
			}
		}
	}

	private boolean isInBoarder(int n) {
		if (n == 0 || n == 29) {
			return true;
		}
		return false;
	}

	private boolean ifEmpty(int x, int y) {
		Iterator<Monster> iter = board.getMonsters().iterator();
		while (iter.hasNext()) {
			Monster mon = iter.next();
			if (mon.equalsCoordinate(x, y)) {
				return false;
			}
		}

		Iterator<Seed> iterS = board.getSeeds().iterator();
		while (iterS.hasNext()) {
			Seed sd = iterS.next();
			if (sd.equals(x, y)) {
				return false;
			}
		}
		return true;
	}

	private void moveX(int[] m, int[] monsterIntend, Monster monster) {
		if (m[0] < monsterIntend[0] && withinIndexMove(m[0] + 1)) {
			if (ifEmpty(m[0] + 1, m[1])) {
				board.changeMonsterPos(monster, m[0] + 1, m[1]);
			}
		} else if (m[0] > monsterIntend[0] && withinIndexMove(m[0] - 1)) {
			if (ifEmpty(m[0] - 1, m[1])) {
				board.changeMonsterPos(monster, m[0] - 1, m[1]);
			}

		}
	}

	private void moveY(int[] m, int[] monsterIntend, Monster monster) {
		if (m[1] < monsterIntend[1] && withinIndexMove(m[1] + 1)) {
			if (board.isEmpty(m[0], m[1] + 1)) {
				board.changeMonsterPos(monster, m[0], m[1] + 1);
			}
		} else if (m[1] > monsterIntend[1] && withinIndexMove(m[1] - 1)) {
			if (board.isEmpty(m[0], m[1] - 1)) {
				board.changeMonsterPos(monster, m[0], m[1] - 1);
			}
		}
	}

	public synchronized void addMonsters() throws Exception {
		board.generateMonsterAndSeed(MONSTER_INC_NUMBER);
	}

	public synchronized void removeDueSeeds() throws Exception {
		Iterator<Seed> iterS = board.getSeeds().iterator();
		while (iterS.hasNext()) {
			Seed seed = iterS.next();
			if ((System.nanoTime() - seed.getBornTime()) > 6000000000L) {
				board.removeUsedSeeds(seed);
				removeDueSeeds();
				break;
			}
		}
	}

	public static synchronized int getMonsterkilled() {
		return killedMonster;
	}
}
