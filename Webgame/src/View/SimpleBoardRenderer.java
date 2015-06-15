package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;

import java.awt.image.BufferedImage;
import java.io.IOException;
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
	private int newcount = 0;
	private int counter = 0;
	private int addition = 0;
	private int cloudMove = 0;
	private int fishMove = 0;
	BufferedImage bg, imgp, imgs, imgm, imgl, imgg,imgsea,imgfish, imgwing, imgcloud,
			background;

	public SimpleBoardRenderer(Board board) {
		this.board = board;
		imgp = getImage("player.jpeg");
		imgs = getImage("watermelon.png");
		imgm = getImage("monster.png");
		imgl = getImage("life.png");
		imgg = getImage("awesomenew.png");
		imgsea = getImage("sea.png");
		imgfish = getImage("fish.png");
		imgwing = getImage("wing.png");
		imgcloud = getImage("cloud.png");
		background = getImage("green.JPG");
		bg = getImage("bg.png");
		try {
			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, this.getClass()
					.getResource("fonts/" + "JOKERMAN.TTF").openStream()));
		} catch (IOException | FontFormatException e) {
		}
	}

	@Override
	public synchronized void render(Graphics g) {

		if (board.canStart()) {
			if (board.hasPlayer()) {
				if (board.getPlayer().getLevel() > 5) {
					cloudMove++;
					int cloudAdd = cloudMove / 35;
					g.drawImage(imgcloud, 0 + cloudAdd, 0, 500
							+ cloudAdd, 250, 0, 0,
							imgcloud.getWidth(), imgcloud.getHeight(), null);
					if (cloudAdd > 750) {
						cloudMove = (-35) * imgcloud.getWidth();
					}
					if (board.getPlayer().getLevel() > 10) {
						
						g.drawImage(imgsea, 0, 500, 750,750, 0, 0,
								imgsea.getWidth(), imgsea.getHeight(), null);
						
						fishMove++;
						int fishAdd = cloudMove / 20;
						g.drawImage(imgfish, 0 + fishAdd, 600, imgfish.getWidth()/3
								+ fishAdd, 600 + imgfish.getHeight()/3, 0, 0,
								imgfish.getWidth(), imgfish.getHeight(), null);
						g.drawImage(imgfish, 200 + fishAdd, 600, 200 + imgfish.getWidth()/3
								+ fishAdd, 600 + imgfish.getHeight()/3, 0, 0,
								imgfish.getWidth(), imgfish.getHeight(), null);
						if (fishAdd > 750) {
							fishMove = (-20) * imgcloud.getWidth();
						}
					} else {
						fishMove = 0;
					}
				} else {
					fishMove = 0;
					cloudMove = 0;
				}
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

				int x = (int) (px * cellSize);
				int y = (int) (py * cellSize);
				g.drawImage(imgp, x - 5, y - 5, x + 34, y + 34, 0, 0,
						imgp.getWidth(), imgp.getHeight(), null);

				// render seeds
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
				counter++;
				Iterator<Emit> eIter = board.getEmitSeedsTo().iterator();
				while (eIter.hasNext()) {
					Emit e = eIter.next();
					int d = e.getD();
					if (counter % 3 == 0) {
						d = e.incD();
					}

					if (d + px < 750) {
						x = (int) (px * cellSize);
						y = (int) (py * cellSize);

						g.fillOval(x + 12, y + 12 - d, 7, 7);// py-d
						g.fillOval(x + 12, y + 12 + d, 7, 7);// py+d
						g.fillOval(x + 12 - d, y + 12, 7, 7);// px-d
						g.fillOval(x + 12 + d, y + 12, 7, 7);// px+d
					} else {
						board.getEmitSeedsTo().remove(e);
					}
				}

				// render monsters
				Iterator<Monster> iter = board.getMonsters().iterator();
				while (iter.hasNext()) {
					Monster monster = iter.next();
					x = (int) (monster.getX() * cellSize);
					y = (int) (monster.getY() * cellSize);
					g.drawImage(imgm, x + 2, y + 2, x + 27, y + 27, 0, 0,
							imgm.getWidth(), imgm.getHeight(), null);
				}

				g.setFont(new Font("JOKERMAN", Font.BOLD, 18));
				g.setColor(Color.white);
				g.fillRect(750, 0, 150, 750);
				g.setColor(Color.LIGHT_GRAY);
				g.drawLine(750, 0, 750, 750);
				g.setColor(Color.black);
				char[] name = "Player Name:".toCharArray();
				g.drawChars(name, 0, 12, 760, 50);

				char[] playerName = player.getName().toCharArray();
//				char[] playerName = "player 1".toCharArray();
				g.drawChars(playerName, 0, playerName.length, 790, 80);
				char[] level = "Player Level:".toCharArray();
				g.drawChars(level, 0, 13, 760, 120);
				int playerlev = player.getLevel();
				char[] playerLevel = Integer.toString(playerlev).toCharArray();
				g.drawChars(playerLevel, 0, playerLevel.length, 810, 150);

				// show goal
				char[] goal = ("Goal: " + board.getMonsterToKill())
						.toCharArray();
				g.drawChars(goal, 0, goal.length, 760, 190);

				// show player life
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

				// show no of wings
				g.drawImage(imgwing, 780, 620, 845, 675, 0, 0,
						imgwing.getWidth(), imgwing.getHeight(), null);

				char[] wingsGained = (" * ".concat(Integer.toString(board
						.getJump()))).toCharArray();
				g.drawChars(wingsGained, 0, wingsGained.length, 835, 664);
				count = 0;
				addition = 0;
				newcount = 0;

			} else { // die or win
				if (BoardController.win) {// win
					g.drawImage(background, 0, 0, 900, 750, 0, 0,
							background.getWidth(), background.getHeight(), null);

					g.setColor(Color.RED);
					g.setFont(new Font("JOKERMAN", Font.BOLD, 72));
					char[] youwin = "YOU WIN".toCharArray();
					g.drawChars(youwin, 0, youwin.length, 200, 200);

					g.drawImage(imgg, 600, 100, 750, 220, 0, 0,
							imgg.getWidth(), imgg.getHeight(), null);

					count += 1;
					addition = count / 10;
					g.drawImage(imgp, 280, 330, 620, 660, 0, 0,
							imgp.getWidth(), imgp.getHeight(), null);

					// watermelon slices
					for (int i = 0; i < 7; i++) {
						g.drawImage(imgs, (i * 150) + addition, 10, 40
								+ (i * 150) + addition, 50, 0, 0,
								imgs.getWidth(), imgs.getHeight(), null);
					}
					for (int i = 0; i < 7; i++) {
						g.drawImage(imgs, (i * 150) + addition, 700, 40
								+ (i * 150) + addition, 740, 0, 0,
								imgs.getWidth(), imgs.getHeight(), null);
					}

					if (count > 900) {
						count = -900;
					}

					// render how long takes to win
					g.setColor(new Color(255, 128, 0));
					g.setFont(new Font("JOKERMAN", Font.BOLD, 36));
					float seconds = (float) ((float) (BoardController.timeUsedToWin / 1000000) / 1000.0);
					char[] wintime = ("YOU SPENT " + seconds + "s TO WIN")
							.toCharArray();
					g.drawChars(wintime, 0, wintime.length, 180, 280);

					int jump = board.getJumpGainedThisRound();
					char[] winJump = ("YOU EARN " + jump + " THIS ROUND")
							.toCharArray();
					g.drawChars(winJump, 0, winJump.length, 180, 330);

					// render restart or next level
					newcount++;
					g.setColor(new Color(255 - (newcount / 5),
							255 - (newcount / 5), 255 - (newcount / 5)));
					g.setFont(new Font("JOKERMAN", Font.PLAIN, 30));
					char[] pressEnter = "Press Enter To Restart".toCharArray();
					g.drawChars(pressEnter, 0, 22, 35, 660);
					char[] next = "Press Backspace To Next Level".toCharArray();
					g.drawChars(next, 0, next.length, 415, 660);
					if (newcount == 1275) {
						newcount = 0;
					}

				} else {// game over
					g.drawImage(background, 0, 0, 900, 750, 0, 0,
							background.getWidth(), background.getHeight(), null);
					g.setColor(Color.gray);
					g.setFont(new Font("JOKERMAN", Font.BOLD, 72));
					char[] gameOver = "GAME OVER".toCharArray();
					g.drawChars(gameOver, 0, 9, 200, 300);

					addition = count / 10;
					g.setColor(Color.black);
					g.setFont(new Font("JOKERMAN", Font.PLAIN, 36 + addition));
					char[] pressEnter = "Press Enter To Restart".toCharArray();
					g.drawChars(pressEnter, 0, 22, 240 - (addition * 5), 400);

					// monsters
					newcount++;
					for (int i = 0; i < 7; i++) {
						g.drawImage(imgm, (i * 150) + (newcount / 10), 100, 40
								+ (i * 150) + (newcount / 10), 150, 0, 0,
								imgm.getWidth(), imgm.getHeight(), null);
					}
					for (int i = 0; i < 7; i++) {
						g.drawImage(imgm, (i * 150) + (newcount / 10), 620, 40
								+ (i * 150) + (newcount / 10), 670, 0, 0,
								imgm.getWidth(), imgm.getHeight(), null);
					}

					if (newcount > 1000) {
						newcount = -900;
					}

					if (count < 200) {
						count++;
					}
				}
			}
		} else { // the start game screen
			count++;
			g.drawImage(bg, 0, 0, 900, 750, 0, 0, bg.getWidth(),
					bg.getHeight(), null);
			g.setColor(new Color(100, 250, 0));
			g.setFont(new Font("JOKERMAN", Font.PLAIN, 90));
			char[] thehunted = "THE HUNTED".toCharArray();
			g.drawChars(thehunted, 0, thehunted.length, 160, 300);

			addition = count / 5;
			g.setColor(new Color(255 - addition, 255 - addition, 255 - addition));
			g.setFont(new Font("JOKERMAN", Font.PLAIN, 36));
			char[] pressEnter = "Press Enter To Start".toCharArray();
			g.drawChars(pressEnter, 0, pressEnter.length, 500, 600);

			if (count == 1275) {
				count = 0;
			}

		}
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
