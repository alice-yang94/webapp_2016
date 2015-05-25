package model;

public enum Direction {
	NORTH, EAST, SOUTH, WEST;

	public Direction rotate(Rotation n) {
		Direction dir = this;
		if (n.equals(Rotation.RIGHT)) {
			switch(dir) {
			case NORTH: return EAST;
			case EAST: return SOUTH;
			case SOUTH: return WEST;
			case WEST: return NORTH;
			}
		} else {
			switch(dir) {
			case NORTH: return WEST;
			case EAST: return NORTH;
			case SOUTH: return EAST;
			case WEST: return SOUTH;
			}
		}
		return null;
	}

}
