package model;

public class Player {
	
	private int x;
	private int y;
	private int level;
	private int life;
	private int bullets;
	private final int[] bulletsByLevel = {5, 4, 3, 2, 1};
	private final int MAXLIFE = 3;
	private final String name;
	
	public Player(int level, String name) {
		this.level = level;
		life = MAXLIFE;
		bullets = bulletsByLevel[level];
		this.name = name;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		System.out.println("dsdf");
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
		if (life > 1) {
			life--;
			return true;
		}
		return false;
	}
	
	public int getBullets() {
		return bullets;
	}

	public void winBullets() {
		bullets++;
	}
	
	public boolean decreaseBullets() {
		if (bullets > 0) {
			bullets--;
			return true;
		}
		return false;
	}
	
	public int getLevel() {
		return level;
	}
	
	public boolean isAlive() {
		return life >= 1;
	}

	public String getName() {
		return name;
	}

}