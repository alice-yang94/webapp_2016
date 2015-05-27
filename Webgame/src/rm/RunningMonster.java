package rm;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import controller.GameController;

public class RunningMonster extends Applet implements Runnable {

	//default serialVersionUID
	private static final long serialVersionUID = 1L;

	// board will be updated every TIMESLICE
	public static final int TIMESLICE = 10;

	private GameController gc = new GameController();

	public void start() {
		new Thread(this).start();
	}

	@Override
	public void run() {

		setSize(960, 960);

		BufferedImage screen = new BufferedImage(960, 960,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = screen.getGraphics();
		Graphics appletG = getGraphics();

		while (true) {
			g.setColor(Color.black);
			g.fillRect(0, 0, 960, 960);

			gc.update(TIMESLICE);
			gc.view(g);

			appletG.drawImage(screen, 0, 0, null);

			if (!isActive()) {
				return;
			}
		}

	}
	
	public boolean eventHandler(Event e) {
		return gc.eventHandler(e);
	}

}
