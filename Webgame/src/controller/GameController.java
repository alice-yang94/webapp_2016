package controller;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import View.Renderer;
import View.SimpleBoardRenderer;
import model.Board;
import model.Player;

public class GameController {

    private final String dbConnString = "jdbc:postgresql://db.doc.ic.ac.uk/g1427101_u";
    private final String dbUsername   = "g1427101_u";
    private final String dbPassword   = "ZfOfLyHLTA";

	private Board board;
	private Player player;
	private Renderer renderer;
	private BoardController controller;
	
	private int level;
	private String name;
	private int centerPoint = 14;
	private int jump;
	
	public GameController(String username) throws Exception {
        name = username;

        level = getUsernameLevel(name);
        jump = getUsernameJumps(name);

		player = new Player(level, name, jump);
		player.setX(centerPoint);
		player.setY(centerPoint);
		board = new Board(player);
		
		//setup view
		renderer = new SimpleBoardRenderer(board);
		
		//setup controller
		controller = new BoardController(board);
	}

    private int getUsernameJumps(String username) {
        return sendGet("jumps", username);
    }

    private int getUsernameLevel(String username) {
        return sendGet("level", username);
    }

    private int sendGet(String action, String username) {
        String result;
        try {
            URL getURL = new URL("http://localhost:59999/main/submit?action=" + action + "&username=" + username);
            HttpURLConnection conn = (HttpURLConnection) getURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

            InputStream instr = conn.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(instr));
            StringBuilder res = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                res.append(line);
            }
            instr.close();

            result = res.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

        return Integer.valueOf(result);
    }

    public boolean eventHandler(KeyEvent e) throws Exception {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:
			controller.pressUp();
			break;
		case KeyEvent.VK_DOWN:
			controller.pressDown();
			break;
		case KeyEvent.VK_LEFT:
			controller.pressLeft();
			break;
		case KeyEvent.VK_RIGHT:
			controller.pressRight();
			break;
		case KeyEvent.VK_SPACE:
			controller.pressSpace();
			break;
		case KeyEvent.VK_ENTER:
			controller.pressEnter();
			break;
		case KeyEvent.VK_BACK_SPACE:
			controller.pressBack();
			break;
		case KeyEvent.VK_J:
			controller.pressJump();
			break;
		}
		return false;
	}
	
	public void update(long initialTime) throws Exception {
		controller.update(initialTime);
	}
	
	public void view(Graphics g) {
		renderer.render(g);
	}
	
	public void addMonsters() throws Exception {
		controller.addMonsters();
	}

	public void moveMonsters() {
		controller.monsterMove();
		controller.ifDie();
	}
	
	public void removeDueSeeds() throws Exception {
		controller.removeDueSeeds();
	}
	
	public synchronized boolean isPlayerAlive() {
		return player.isAlive() && controller.canAddMonsters();
	}
}
