package controller;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import model.Board;
import model.Monster;
import model.Player;
import model.Seed;

public class BoardController {

	private Board board;
	private Player player;

	private int targetX, targetY, seedCounter;
	private boolean hasInput = false; // true if player input on keyboard

	public static final int INITIAL_COUNTER = 0;
	public static final int SEED_COUNTER = 2;
	public static final int MONSTER_INC_NUMBER = 3;

	public BoardController(Board board) {
		this.board = board;
		seedCounter = INITIAL_COUNTER;
		player = board.getPlayer();
	}

	public void update(int time) throws Exception {
		if (hasInput) {
			boolean notMonster = true;

			// if player meets monster, player loselife, monster die
			Iterator<Monster> iter = board.getMonsters().iterator();
			while (iter.hasNext()) {
				Monster monster = iter.next();
				if (monster.equalsCoordinate(targetX, targetY)) {
					player.loseLife();
					board.removeDeadMonster(monster);
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

			// if space is pressed within 2 rounds, hit monster on the cross
			if (seedCounter > 0) {
				hitMonster();
			}

			board.changePlayerPos(targetX, targetY);
			monsterMove();

			// System.out.println(player.getX() + " " + player.getY());
			// board.printAllCoodinateOfMonsters();
			// board.printAllMonsterOnBoard();
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

	public void pressSpace() throws Exception {
		if (player.decreaseBullets()) {
			seedCounter = 2;
			hitMonster();
		}
	}

	public void hitMonster() throws Exception {
		Iterator<Monster> iter = board.getMonsters().iterator();
		while (iter.hasNext()) {
			Monster monster = iter.next();

			if (seedCounter > 0) {
				if (monster.getX() == player.getX()
						|| monster.getY() == player.getY()) {
					if (monster.loseLife()) {
						seedCounter--;
					} else {
						iter.remove();
						board.clearMonsterWhenHitBySeed(monster);
					}
				}
			} else {
				break;
			}
		}
	}

	private boolean withinIndexMove(int x) {
		if (x >= 0 && x < Board.WIDTH) {
			return true;
		}
		return false;
	}

	private void playerLoseLife() {
		if (player.loseLife()) { // still have life
			hasInput = false; // invalid move, has to wait another input
		} else {

			// TODO: restart the game

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

	public void monsterMove() {
		Iterator<Monster> iter = board.getMonsters().iterator();
		while (iter.hasNext()) {
			Monster monster = iter.next();
			int monsterX = monster.getX();
			int monsterY = monster.getY();

			int[] monsterIntend = null;

			// int[0] = x, int[1] = y
			int[] up = { targetX, targetY - 1 };
			int[] down = { targetX, targetY + 1 };
			int[] left = { targetX - 1, targetY };
			int[] right = { targetX + 1, targetY };

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

	public void addMonsters() throws Exception {
		board.generateMonsterAndSeed(MONSTER_INC_NUMBER);
		
	}

}
