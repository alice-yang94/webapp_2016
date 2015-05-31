package controller;

import model.Board;

public class BoardController {
	
	private Board board;
	private int cellSize = 25;

	private int cellX, cellY;
	private boolean moving = false;    //true if player moves
	
	public BoardController(Board board) {
		this.board = board;
	}
	
	public void update(int time) {
		
	}
}
