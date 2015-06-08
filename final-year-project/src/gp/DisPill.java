/*
 * This Java source code is authored by Gagondeep Srai(W1374553),  
 * for use in his final year project at University of Westminster.
 * Created: 10/03/2015
 */
package gp;

import game.Board;

/*
 * Class DisPill
 * Calculates and returns the distance between the agent 
 * and the nearest uneaten Pill.
 */
public class DisPill extends Sexpr {
	
	public DisPill() {
		this.isTerminal = true;
	}
	
	@Override
	public int evaluate() {
		
		if (Sexpr.game.player.pillEatenCount < 4)
			return nearestPill();
		else
			return Integer.MAX_VALUE; // return a really large number if there are no pills
		
	}
	
	@Override
    public String toString() {
		return "DisPill";
	}
	
	private int nearestPill() {
		int px = Sexpr.game.player.posX / Sexpr.game.board.blockSize;
		int py = Sexpr.game.player.posY / Sexpr.game.board.blockSize;
		
		int csd = 1000; // current shortest distance 
		
		if(isPill(1, 2) && distance(1, 2, px, py) < csd) {
			csd = distance(1, 2, px, py);
		}
		
		if(isPill(17, 2) && distance(17, 2, px, py) < csd) {
			csd = distance(17, 2, px, py);
		}
		
		if(isPill(1, 16) && distance(1, 16, px, py) < csd) {
			csd = distance(1, 16, px, py);
		}
		
		if(isPill(17, 16) && distance(17, 16, px, py) < csd) {
			csd = distance(17, 16, px, py);
		}
		
		return csd;
	}
	
	private boolean isPill(int x, int y) {
		return Sexpr.game.board.getBlock(x, y) == Board.PILL;
	}
	
	private int distance(int x1, int y1, int x2, int y2) {
		return (int) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}
}

