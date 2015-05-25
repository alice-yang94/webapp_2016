package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {

	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	
	private static Random random = new Random();
	
	private Object[][] board;
	private List<Obstacle> obstacles = new ArrayList<Obstacle>();
	private List<Monster> monsters = new ArrayList<Monster>();
	private Player player;
	
	public Board(Player player) {
		this.player = player;
		board = new Object[HEIGHT][WIDTH];
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				board[j][i] = null;
			}
		}
	}
	
	//generate enemy and obstacle

}

