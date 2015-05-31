package controller;

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

	public BoardController(Board board) {
		this.board = board;
		seedCounter = INITIAL_COUNTER;
		player = board.getPlayer();
	}

	public void update(int time) throws Exception {
		if (hasInput) {
			player.setX(targetX);
			player.setY(targetY);

			boolean notMonster = true;
			for (Monster monster : board.getMonsters()) {
				// if player meets monster, player loselife, monster die
				if (monster.equals(targetX, targetY)) {
					player.loseLife();
					board.removeDeadMonster(monster);
					notMonster = false;
					break;
				}

				// if space is pressed within 2 rounds, hit monster
				if (seedCounter > 0) {
					hitMonster(monster);
				}
			}

			// if the grid contains seed, player gets a bullet
			if (notMonster) {
				for (Seed seed : board.getSeeds()) {
					if (seed.equals(targetX, targetY)) {
						player.getBullets();
						break;
					}
				}
			}
			monsterMove();
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
			seedCounter = SEED_COUNTER;
			for (Monster monster : board.getMonsters()) {
				hitMonster(monster);
			}
		}
	}

	public void hitMonster(Monster monster) throws Exception {
		if (monster.getX() == player.getX() || monster.getY() == player.getY()) {
			if (monster.loseLife()) {
				seedCounter--;
			} else {
				board.removeDeadMonster(monster);
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
	
	public void monsterMove() {
		for (Monster monster : board.getMonsters()) {
			int monsterX = monster.getX();
			int monsterY = monster.getY();       
				if (monsterX > targetX && withinIndexMove(monsterX - 1)) {
					monster.setX(monsterX - 1);
				} else {
					if (monsterX < targetX && withinIndexMove(monsterX + 1)) {
					monster.setX(monsterX + 1);						
					}
				}
				if (monsterY > targetY && withinIndexMove(monsterY - 1)) {
					monster.setY(monsterY - 1);
				} else {
					if (monsterY < targetY && withinIndexMove(monsterY + 1)) {
						monster.setY(monsterY + 1);
					
				}
			}
		}
	}
}
