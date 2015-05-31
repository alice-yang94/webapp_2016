package model;

public class Player {
	
	private int x;
	private int y;
	private int level;
	private int life;
	private int bullets;
	private final int[] bulletsByLevel = {70, 60, 50, 40, 30};
	private final int MAXLIFE = 3;
	
	public Player(int level) {
		this.level = level;
		life = MAXLIFE;
		bullets = bulletsByLevel[level];
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
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public boolean loseLife() {
		if (life >= 1) {
			life--;
			return true;
		}
		return false;
	}
	
	public int getBullets() {
		return bullets;
	}

	public void decreaseBullets() {
		bullets--;
	}

	public int getLevel() {
		return level;
	}
}