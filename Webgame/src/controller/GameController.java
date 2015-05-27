package controller;

import java.awt.Event;
import java.awt.Graphics;

import View.Renderer;
import View.SimpleBoardRenderer;
import model.Board;
import model.Player;

public class GameController {

	private Board board;
	private Player player;
	private Renderer renderer;
	
	private int level = 3;   //TODO:Connect to database to get level
	private int centerPoint = 14;
	
	public GameController() {
		player = new Player(level);
		player.setX(centerPoint);
		player.setY(centerPoint);
		board = new Board(player);
		
		//setup view
		renderer = new SimpleBoardRenderer(board);
	}
	
	public boolean eventHandler(Event e) {
		switch(e.id) {
		case Event.UP:
			break;
		case Event.DOWN:
			break;
		case Event.LEFT:
			break;
		case Event.RIGHT:
			break;
		}
		return false;
	}
	
	public void update(float time) {
		
	}
	
	public void view(Graphics g) {
		renderer.render(g);
	}
	
}
