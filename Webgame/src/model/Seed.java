package model;

public class Seed {

		private int x;
		private int y;
		
		public Seed(int x, int y) {
			this.setX(x);
			this.setY(y);
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

		public boolean equals(int sx, int sy) {
			if (sx == x && sy == y) {
				return true;
			}
			return false;
		}
}
