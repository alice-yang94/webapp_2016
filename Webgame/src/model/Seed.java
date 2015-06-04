package model;

public class Seed {

		private int x;
		private int y;
		private long bornTime;
		
		public Seed(int x, int y, long bornTime) {
			this.setX(x);
			this.setY(y);
			this.bornTime = bornTime;
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
		
		public long getBornTime() {
			return bornTime;
		}
}
