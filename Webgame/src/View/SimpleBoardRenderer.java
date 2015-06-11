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
import model.Emit;
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

		if (board.canStart()) {
			if (board.hasPlayer()) {
				// render the board
				int cellSize = 25;
				g.setColor(Color.LIGHT_GRAY);
				for (int i = 0; i < Board.WIDTH; i++) {
					g.drawLine(i * cellSize, 0, i * cellSize, Board.HEIGHT
							* cellSize);
					if (i < Board.WIDTH) {
						g.drawLine(0, i * cellSize, Board.WIDTH * cellSize, i
								* cellSize);
					}
				}

				// render player
				Player player = board.getPlayer();
				int px = player.getX();
				int py = player.getY();

				BufferedImage imgp = getImage("player.jpeg");

				int x = (int) (px * cellSize);
				int y = (int) (py * cellSize);
				g.drawImage(imgp, x - 5, y - 5, x + 34, y + 34, 0, 0,
						imgp.getWidth(), imgp.getHeight(), null);

				// render seeds
				BufferedImage imgs = getImage("watermelon.png");
				Iterator<Seed> seedIter = board.getSeeds().iterator();
				while (seedIter.hasNext()) {
					Seed seed = seedIter.next();
					x = (int) (seed.getX() * cellSize);
					y = (int) (seed.getY() * cellSize);
					g.drawImage(imgs, x + 2, y + 2, x + 27, y + 27, 0, 0,
							imgs.getWidth(), imgs.getHeight(), null);
				}

				// render emitting seeds
				g.setColor(Color.black);
				int size = board.getEmitSeedsTo().size();

				Iterator<Emit> eIter = board.getEmitSeedsTo().iterator();
				while (eIter.hasNext()) {
					Emit e = eIter.next();
					int d = e.incD();

					if (d + px < 750) {
						x = (int) (px * cellSize);
						y = (int) (py * cellSize);

						g.fillOval(x + 12, y + 12 - d, 7, 7);// py-d
						g.fillOval(x + 12, y + 12 + d, 7, 7);// py+d
						g.fillOval(x + 12 - d, y + 12, 7, 7);// px-d
						g.fillOval(x + 12 + d, y + 12, 7, 7);// px+d
					} else {
						eIter.remove();
					}
				}

				// render monsters
				BufferedImage imgm = getImage("monster.png");
				Iterator<Monster> iter = board.getMonsters().iterator();
				while (iter.hasNext()) {
					Monster monster = iter.next();
					x = (int) (monster.getX() * cellSize);
					y = (int) (monster.getY() * cellSize);
					g.drawImage(imgm, x + 2, y + 2, x + 27, y + 27, 0, 0,
							imgm.getWidth(), imgm.getHeight(), null);
				}

				g.setFont(new Font("TimesRoman", Font.BOLD, 14));
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
				
//				char[] life = "Player life:".toCharArray();
//				g.drawChars(life, 0, 11, 790, 190);
				
				BufferedImage imgl = getImage("life.jpg");
				
				g.drawImage(imgl, 790, 210, 810, 230, 0, 0,
						imgl.getWidth(), imgl.getHeight(), null);
				
				char[] playerLife = Integer.toString(player.getLife())
						.toCharArray();
				g.drawChars(playerLife, 0, 1, 790, 210);

			} else { // die
				g.setColor(Color.red);
				g.setFont(new Font("TimesRoman", Font.BOLD, 72));
				char[] gameOver = "GAME OVER".toCharArray();
				g.drawChars(gameOver, 0, 9, 200, 300);
				g.setColor(Color.black);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 36));
				char[] pressEnter = "Press Enter To Restart".toCharArray();
				g.drawChars(pressEnter, 0, 22, 250, 400);
			}
		} else { // the start game screen
			BufferedImage background = getImage("bg.JPG");
			g.drawImage(background, 0, 0, 900, 750, 0, 0,
					background.getWidth(), background.getHeight(), null);

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
