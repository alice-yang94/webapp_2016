package controller;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import View.Renderer;
import View.SimpleBoardRenderer;
import model.Board;
import model.Player;

public class GameController {

	private Board board;
	private Player player;
	private Renderer renderer;
	private BoardController controller;
	
	private int level = 3;   //TODO:Connect to database to get level
	private int centerPoint = 14;
	
	public GameController() {
		player = new Player(level);
		player.setX(centerPoint);
		player.setY(centerPoint);
		board = new Board(player);
		
		//setup view
		renderer = new SimpleBoardRenderer(board);
		
		//setup controller
		controller = new BoardController(board);
	}
	
	public boolean eventHandler(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:
			controller.pressUp();
			break;
		case KeyEvent.VK_DOWN:
			controller.pressDown();
			break;
		case KeyEvent.VK_LEFT:
			controller.pressLeft();
			break;
		case KeyEvent.VK_RIGHT:
			controller.pressRight();
			break;
		}
		return false;
	}
	
	public void update(int time) {
		controller.update(time);
	}
	
	public void view(Graphics g) {
		renderer.render(g);
	}
	
}
