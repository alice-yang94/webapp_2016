package View;

import java.awt.Color;
import java.awt.Graphics;

import model.Board;
import model.Monster;
import model.Player;

public class SimpleBoardRenderer implements Renderer {

	private Board board;
	
	public SimpleBoardRenderer(Board board) {
		this.board = board;
	}
	
	@Override
	public void render(Graphics g) {
		//render the board
		int cellSize = 25;
		g.setColor(Color.blue);
		for (int i = 0; i< Board.WIDTH; i++) {
			g.drawLine(i * cellSize, 0, i * cellSize, Board.HEIGHT * cellSize);
			if (i < Board.WIDTH) {
				g.drawLine(0, i * cellSize, Board.WIDTH * cellSize, i * cellSize);
			}
		}
		
		//render player
		g.setColor(Color.red);
		Player player = board.getPlayer();
		int x = (int) (player.getX() * cellSize);
		int y = (int) (player.getY() * cellSize);
		g.fillOval(x + 2, y + 2, cellSize - 4, cellSize - 4);
		//render square on player
		g.setColor(Color.white);
		g.fillRect(x + 10, y + 10, cellSize - 20, cellSize - 20);
		
		
		//render monsters
		g.setColor(Color.green);
		for (Monster monster : board.getMonsters()) {
			x = (int) (monster.getX() * cellSize);
			y = (int) (monster.getY() * cellSize);
			g.fillOval(x + 2, y + 2, cellSize - 4, cellSize - 4);
		}
	}

}
