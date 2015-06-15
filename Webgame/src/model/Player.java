package model;

public class Player {

	private int x;
	private int y;
	private int level;
	private int life;
	private int bullets;
	private int jump;
	private final int MAXLIFE = 3;
	private final String name;


	public Player(int level, String name, int jump) {
		this.level = level;
		life = MAXLIFE;
		bullets = getBullets(level);
		this.name = name;
		this.jump = jump;
	}

    private int getBullets(int level) {
        return 5 - level > 0 ? 5 - level : 0;
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

	public void reborn() {
		x = 14;
		y = 14;
		life = MAXLIFE;
		bullets = getBullets(level);
	}
	
	public void setJump(int n) {
		jump = n;
	}
	
	public void getJump() {
		jump++;
	}

    public int getJumps() {
        return jump;
    }

	public synchronized int getLife() {
		return life;
	}

	public synchronized void setLife(int life) {
		this.life = life;
	}

	public synchronized boolean loseLife() {
		if (life > 1) {
			setLife(life - 1);
			return true;
		}
		setLife(0);
		return false;
	}

	public synchronized int getBullets() {
		return bullets;
	}

	public synchronized void winBullets() {
		bullets++;
	}

	public synchronized boolean decreaseBullets() {
		if (bullets > 0) {
			bullets--;
			return true;
		}
		return false;
	}

	public int getLevel() {
		return level;
	}
	
	public int incLevel() {
		return level++;
	}

	public boolean isAlive() {
		return life > 0;
	}

	public String getName() {
		return name;
	}

}