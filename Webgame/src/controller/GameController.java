package controller;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.IOException;

import View.Renderer;
import View.SimpleBoardRenderer;
import model.Board;
import model.Player;

public class GameController {

	private Board board;
	private Player player;
	private Renderer renderer;
	private BoardController controller;
	
	private int level = 2;   //TODO:Connect to database to get level
	private String name = "PlayerA";  //TODO: Connect to database to get level
	private int centerPoint = 14;
	
	public GameController() throws Exception {
		player = new Player(level, name);
		player.setX(centerPoint);
		player.setY(centerPoint);
		board = new Board(player);
		
		//setup view
		renderer = new SimpleBoardRenderer(board);
		
		//setup controller
		controller = new BoardController(board);
	}
	
	public boolean eventHandler(KeyEvent e) throws Exception {
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
		case KeyEvent.VK_SPACE:
			controller.pressSpace();
		case KeyEvent.VK_ENTER:
			controller.pressEnter();
		}
		return false;
	}
	
	public void update(long initialTime) throws Exception {
		controller.update(initialTime);
	}
	
	public void view(Graphics g) {
		renderer.render(g);
	}
	
	public void addMonsters() throws Exception {
		controller.addMonsters();
	}

	public void moveMonsters() {
		controller.monsterMove();
		controller.ifDie();
	}
	
	public void removeDueSeeds() throws Exception {
		controller.removeDueSeeds();
	}
	
	public boolean isPlayerAlive() {
		return player.isAlive();
	}
}
