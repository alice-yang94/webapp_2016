package controller;

import model.Board;
import model.Player;

public class BoardController {
	
	private Board board;
	private Player player;

	private int targetX, targetY;
	private boolean hasInput = false;    //true if player input on keyboard
	
	public BoardController(Board board) {
		this.board = board;
		player = board.getPlayer();
	}
	
	public void update(int time) {
		if (hasInput) {
			player.setX(targetX);
			player.setY(targetY);
		}
		hasInput = false;
	}
	
	public void pressUp() {
		int y = player.getY() + 1;
		if (withinIndexMove(y)) {
			targetX = player.getX();
			targetY = y;
		} else {                        // hit boundary, lose a life
			if (player.loseLife()) {    //still have life
				hasInput = false;       //invalid move, has to wait another input
			} else {    
				//TODO: restart the game
				
			}
		}
		hasInput = true;
	}
	
	public boolean withinIndexMove(int x) {
		if (x >= 0 && x < Board.WIDTH) {
			return true;
		}
		return false;
	}
}
