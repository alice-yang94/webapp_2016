package View;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import model.Board;
import model.Monster;
import model.Player;
import model.Seed;

public class SimpleBoardRenderer implements Renderer {

	private Board board;

	public SimpleBoardRenderer(Board board) {
		this.board = board;
	}

	@Override
	public void render(Graphics g) {

		if (board.hasPlayer()) {
			// render the board
			int cellSize = 25;
			g.setColor(Color.blue);
			for (int i = 0; i < Board.WIDTH; i++) {
				g.drawLine(i * cellSize, 0, i * cellSize, Board.HEIGHT
						* cellSize);
				if (i < Board.WIDTH) {
					g.drawLine(0, i * cellSize, Board.WIDTH * cellSize, i
							* cellSize);
				}
			}

			// render player
			g.setColor(Color.red);
			Player player = board.getPlayer();
			int x = (int) (player.getX() * cellSize);
			int y = (int) (player.getY() * cellSize);
			g.fillOval(x + 2, y + 2, cellSize - 4, cellSize - 4);

			// render square on player
			g.setColor(Color.white);
			g.fillRect(x + 10, y + 10, cellSize - 20, cellSize - 20);

			// render monsters
			g.setColor(Color.green);
			Iterator<Monster> iter = board.getMonsters().iterator();
			while (iter.hasNext()) {
				Monster monster = iter.next();
				x = (int) (monster.getX() * cellSize);
				y = (int) (monster.getY() * cellSize);
				g.fillOval(x + 2, y + 2, cellSize - 4, cellSize - 4);
			}

			// render seeds
			g.setColor(Color.yellow);
			Iterator<Seed> seedIter = board.getSeeds().iterator();
			while (seedIter.hasNext()) {
				Seed seed = seedIter.next();
				x = (int) (seed.getX() * cellSize);
				y = (int) (seed.getY() * cellSize);
				g.fillOval(x + 2, y + 2, cellSize - 4, cellSize - 4);
			}
			
			g.setColor(Color.white);
			g.fillRect(750, 0, 150, 750);
			g.setColor(Color.black);
			char[] name = "Player name:".toCharArray();
			g.drawChars(name, 0, 12, 790, 50);
			char[] playerName = player.getName().toCharArray();
			g.drawChars(playerName, 0, playerName.length, 790, 80);
			
			
		}
	}

}
