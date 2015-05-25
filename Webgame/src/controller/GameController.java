package controller;

import java.awt.Event;
import java.awt.Graphics;

import model.Board;
import src/RunningMoster;

public class GameController {

	private Board board;
	private RunningMonster rm;
	
	public boolean eventHandler(Event e) {
		switch(e.id) {
		case Event.UP:
			break;
		case Event.DOWN:
			break;
		case Event.LEFT:
			break;
		case Event.RIGHT:
			break;
		}
		return false;
	}
	
	public void update(float time) {
		
	}
	
	public void view(Graphics g) {
		
	}
	
}
