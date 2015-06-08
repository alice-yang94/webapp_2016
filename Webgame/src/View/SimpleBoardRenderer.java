package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;

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
			char[] level = "Player level:".toCharArray();
			g.drawChars(level, 0, 13, 790, 120);
			int playerlev = player.getLevel();
			char[] playerLevel = Integer.toString(playerlev).toCharArray();
			g.drawChars(playerLevel, 0, 1, 790, 150);
			char[] life = "Player life:".toCharArray();
			g.drawChars(life, 0, 11, 790, 190);
			char[] playerLife = Integer.toString(player.getLife())
					.toCharArray();
			g.drawChars(playerLife, 0, 1, 790, 210);
			
		} else {    //die
//			g.setColor(Color.white);
//			g.setFont(new Font("TimesRoman", Font.BOLD, 72));
//			char[] gameOver = "GAME OVER".toCharArray();
//			g.drawChars(gameOver, 0, 9, 100, 300);

			BufferedImage img = getImage("monster.png");

			g.drawImage(img, 0, 0, 25, 25, 0, 0, img.getWidth(),
					img.getHeight(), null);
		}
	}
	
	private BufferedImage getImage(String name) {
		String currentDirectory = this.getClass().getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		String imaged = currentDirectory.substring(0,
				currentDirectory.length() - 4);

		try {
			return ImageIO.read(new File(imaged + "src/images/" + name));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
