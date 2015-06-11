package model;

public class Emit {

	int d;// distance from player

	public Emit() {
		d = 0;
	}

	public synchronized int incD() {
		return d++;
	}

	public synchronized int getD() {
		return d;
	}

}
