package model;

import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Monster {
	
	private int x;
	private int y;
	private int life;
	private final int MAXLIFE = 3;
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
	
	public boolean equalsCoordinate(int mx, int my) {
		if (mx == x && my == y) {
			return true;
		}
		return false;
	}

}
