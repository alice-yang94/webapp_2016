package controller;

import model.Board;
import model.Monster;
import model.Player;
import model.Seed;

public class BoardController {

	private Board board;
	private Player player;

	private int targetX, targetY;
	private boolean hasInput = false; // true if player input on keyboard

	public BoardController(Board board) {
		this.board = board;
		player = board.getPlayer();
	}

	public void update(int time) throws Exception {
		if (hasInput) {
			// if new coordinate is inside the board
			if (board.WIDTH > targetX && targetX > 0 && board.HEIGHT > targetY
					&& targetY > 0) {
				player.setX(targetX);
				player.setY(targetY);

				// Object[][] currentBoard = board.getBoard();
				// Object item = currentBoard[targetY][targetX];
				boolean empty = true;
				for (Monster monster : board.getMonsters()) {
					if (monster.equals(targetX, targetY)) {
						player.loseLife();
						board.removeDeadMonster(monster);
						empty = false;
						break;
					}
				}

				if (empty) {
					for (Seed seed : board.getSeeds()) {
						if (seed.equals(targetX, targetY)) {
							player.getBullets();
							break;
						}
					}
				}

			}
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
}
