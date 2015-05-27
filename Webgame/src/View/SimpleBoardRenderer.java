package View;

import java.awt.Color;
import java.awt.Graphics;

import model.Board;

public class SimpleBoardRenderer implements Renderer {

	private Board board;
	
	public SimpleBoardRenderer(Board board) {
		this.board = board;
	}
	
	@Override
	public void render(Graphics g) {
		//render the board
		int cellSize = 32;
		g.setColor(Color.GREEN);
		for (int i = 0; i<= Board.WIDTH; i++) {
			g.drawLine(i * cellSize, 0, i * cellSize, Board.HEIGHT * cellSize);
			if (i <= Board.WIDTH) {
				g.drawLine(0, i * cellSize, Board.WIDTH * cellSize, i * cellSize);
			}
		}
		
		//render player
		
		
	}

}
