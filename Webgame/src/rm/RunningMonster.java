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

	private GameController gc;
	
	public RunningMonster() {
		 gc = new GameController();
	}

	public void start() {
		new Thread(this).start();
	}

	@Override
	public void run() {

		setSize(750, 750);

		BufferedImage screen = new BufferedImage(750, 750,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = screen.getGraphics();
		Graphics appletG = getGraphics();

		long delta = 01;
		
		while (true) {
			long lastTime = System.nanoTime();
			
			g.setColor(Color.black);
			g.fillRect(0, 0, 750, 750);

//			gc.update((float)(delta / 1000000000.0));
			gc.update(TIMESLICE);
			gc.view(g);

    		appletG.drawImage(screen, 0, 0, null);

//			delta = System.nanoTime() - lastTime;
//			if (delta < 20000000L) {
//				try {
//					Thread.sleep((20000000L - delta) / 1000000L);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
			
			
			
			if (!isActive()) {
				return;
			}
		}

	}
	
	public boolean eventHandler(Event e) {
//		return gc.eventHandler(e);
		return false;
	}

}
