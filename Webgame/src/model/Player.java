package model;

public class Player {
	
	private int x;
	private int y;
	private int level;
	private Direction dir;
	private int life;
	
	public Player(int level) {
		this.level = level;
		setX(14);
		setY(14);
		dir = Direction.NORTH;
	}

	public Direction rotateLeft() {
		dir = dir.rotate(Rotation.LEFT);
		return dir;
	}
	
	public Direction rotateRight() {
		dir = dir.rotate(Rotation.RIGHT);
		return dir;
	}
	
	public Direction getDirection() {
		return dir;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public static void main(String[] args) {
		Player player = new Player(1);
		player.rotateRight();
		System.out.print(player.getDirection());
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
}