package controller;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
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
        int res = 0;

        try {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                //
            }

            Connection conn = DriverManager.getConnection(dbConnString, dbUsername, dbPassword);

            Statement statement = conn.createStatement();

            ResultSet rs = statement.executeQuery("SELECT jumps FROM currentGame WHERE username='" +
                    username + "'");

            while (rs.next()) {
                res = rs.getInt("jumps");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    private int getUsernameLevel(String username) {
        int res = 1;

        try {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                //
            }

            Connection conn = DriverManager.getConnection(dbConnString, dbUsername, dbPassword);

            Statement statement = conn.createStatement();

            ResultSet rs = statement.executeQuery("SELECT level FROM currentGame WHERE username='" +
                    username + "'");

            while (rs.next()) {
                res = rs.getInt("level");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
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
