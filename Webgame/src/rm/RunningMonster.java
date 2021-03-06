package rm;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import controller.GameController;

public class RunningMonster extends Applet implements Runnable, KeyListener {

    // default serialVersionUID
    private static final long serialVersionUID = 1L;

    private GameController gc;

    public void init() {
        try {
            gc = new GameController(getParameter("username"));
            addKeyListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        setSize(900, 750);

        BufferedImage screen = new BufferedImage(900, 750,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = screen.getGraphics();
        Graphics appletG = getGraphics();

        long delta = 01;
        long lastTime = System.nanoTime();

        while (true) {
            g.setColor(Color.white);

            g.fillRect(0, 0, 900, 750);

            try {
                gc.update(System.nanoTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
            gc.view(g);
            appletG.drawImage(screen, 0, 0, null);

            delta = System.nanoTime() - lastTime;

            if (!gc.isPlayerAlive()) {
            	lastTime = System.nanoTime();
            }
            
            if (delta > 3000000000L && gc.isPlayerAlive()) {
            	try {
                    lastTime = System.nanoTime();
                    gc.addMonsters();
                    gc.moveMonsters();
                    gc.removeDueSeeds();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!isActive()) {
                return;
            }
        }

    }

    public boolean eventHandler(KeyEvent e) throws Exception {
        return gc.eventHandler(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            eventHandler(e);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
