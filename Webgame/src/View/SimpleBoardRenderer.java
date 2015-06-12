package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;

import javax.imageio.ImageIO;

import controller.BoardController;

import model.Board;
import model.Emit;
import model.Monster;
import model.Player;
import model.Seed;

public class SimpleBoardRenderer implements Renderer {

	private Board board;
	private int count = 0;
	private int addition = 0;

	public SimpleBoardRenderer(Board board) {
		this.board = board;
	}

	@Override
	public synchronized void render(Graphics g) {

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
				g.drawLine(750, 0, 750, 750);
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

				/*try {
					GraphicsEnvironment ge = GraphicsEnvironment
							.getLocalGraphicsEnvironment();
					ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,
							new File("fonts/" + "JOKERMAN.TTF")));
				} catch (IOException | FontFormatException e) {
				} */

				g.setFont(new Font("JOKERMAN", Font.BOLD, 18));
				g.setColor(Color.white);
				g.fillRect(750, 0, 150, 750);
				g.setColor(Color.LIGHT_GRAY);
				g.drawLine(750, 0, 750, 750);
				g.setColor(Color.black);
				char[] name = "Player Name:".toCharArray();
				g.drawChars(name, 0, 12, 760, 50);
				char[] playerName = player.getName().toCharArray();
				g.drawChars(playerName, 0, playerName.length, 790, 80);
				char[] level = "Player Level:".toCharArray();
				g.drawChars(level, 0, 13, 760, 120);
				int playerlev = player.getLevel();
				char[] playerLevel = Integer.toString(playerlev).toCharArray();
				g.drawChars(playerLevel, 0, 1, 810, 150);

				// show player life
				BufferedImage imgl = getImage("life.png");
				g.drawImage(imgl, 790, 300, 835, 335, 0, 0, imgl.getWidth(),
						imgl.getHeight(), null);
				g.setFont(new Font("JOKERMAN", Font.BOLD, 25));
				char[] playerLife = (" * ".concat(Integer.toString(player
						.getLife()))).toCharArray();
				g.drawChars(playerLife, 0, playerLife.length, 835, 328);

				// show bullet number
				g.drawImage(imgs, 790, 400, 835, 445, 0, 0, imgs.getWidth(),
						imgs.getHeight(), null);
				char[] bulletNumber = (" * ".concat(Integer.toString(player
						.getBullets()))).toCharArray();
				g.drawChars(bulletNumber, 0, bulletNumber.length, 835, 440);

				// show no. of monster killed by player
				g.drawImage(imgm, 790, 515, 835, 570, 0, 0, imgm.getWidth(),
						imgm.getHeight(), null);
				char[] monsterKilled = (" * ".concat(Integer
						.toString(BoardController.getMonsterkilled())))
						.toCharArray();
				g.drawChars(monsterKilled, 0, monsterKilled.length, 835, 552);

				count = 0;
				addition = 0;

			} else { // die or win
				if (BoardController.win) {
					BufferedImage background = getImage("green.JPG");
					g.drawImage(background, 0, 0, 900, 750, 0, 0,
							background.getWidth(), background.getHeight(), null);
					
					g.setColor(Color.orange);
					g.setFont(new Font("JOKERMAN", Font.BOLD, 72));
					char[] youwin = "YOU WIN".toCharArray();
					g.drawChars(youwin, 0, youwin.length, 200, 300);
					
					BufferedImage imgg = getImage("awesome.jpg");
					g.drawImage(imgg, 600, 200, 750, 320, 0, 0, imgg.getWidth(),
							imgg.getHeight(), null);
					
					count++;
					BufferedImage imgp = getImage("player.jpeg");
					g.drawImage(imgp, 600, 200, 750, 320, 0, 0, imgp.getWidth(),
							imgp.getHeight(), null);
					
					
				} else {

					g.setColor(Color.red);
					g.setFont(new Font("JOKERMAN", Font.BOLD, 72));
					char[] gameOver = "GAME OVER".toCharArray();
					g.drawChars(gameOver, 0, 9, 200, 300);

					g.setColor(Color.black);
					addition = count / 10;
					g.setFont(new Font("JOKERMAN", Font.PLAIN, 36 + addition));
					char[] pressEnter = "Press Enter To Restart".toCharArray();
					g.drawChars(pressEnter, 0, 22, 240 - (addition * 5), 400);
					if (count < 200) {
						count++;
					}
				}
			}
		} else { // the start game screen
			BufferedImage background = getImage("bg.JPG");
			g.drawImage(background, 0, 0, 900, 750, 0, 0,
					background.getWidth(), background.getHeight(), null);
		}
	}

	private String getDir() {
		String dir = this.getClass().getProtectionDomain()
				.getCodeSource().getLocation().getPath();
		return dir;
	}

    private BufferedImage getImage(String name) {
        try {
            return ImageIO.read(this.getClass().getResource("images/" + name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
