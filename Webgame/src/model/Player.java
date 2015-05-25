package model;

public class Player {
	
	private int x;
	private int y;
	private int level;
	private Direction dir;
	
	public Player(int level) {
		this.level = level;
		setX(14);
		setY(14);
		dir = Direction.NORTH;
	}

//	public Direction rotate() {
//		dir = (dir + 1) % 4;
//	}
//	
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
}
