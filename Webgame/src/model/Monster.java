package model;

import java.util.Random;

public class Monster {
	
	private int x;
	private int y;
	private int life;
	private final int MAXLIFE = 9;
	private Random randomGenerator = new Random();
	
	public Monster(int x, int y) {
		this.setX(x);
		this.setY(y);
		this.life = randomGenerator.nextInt(MAXLIFE);
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

}