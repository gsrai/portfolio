/*
 * This Java source code is authored by Gagondeep Srai(W1374553),  
 * for use in his final year project at University of Westminster.
 * Created: 10/03/2015
 */
package gp;

import game.Board;

/*
 * Class RetPill
 * Advances the agent away from the nearest uneaten pill. It does this by
 * finding the position of the nearest uneaten pill and then calculating
 * the longest path to that position, thereby mutating the new direction
 * of the agent.
 * 
 * Evaluate will return the agents facing direction encoded as a modulo 4
 * number.
 */
public class RetPill extends Sexpr {
	
	public RetPill() {
		this.isTerminal = true;
	}
	
	@Override
	public int evaluate() {
		if (Sexpr.game.player.pillEatenCount < 4) {
			int [] coord = nearestPill();
			Utils.retreatTile(coord[0], coord[1]);
		} 

		return Sexpr.game.player.direction.ordinal(); 
	}
	
	@Override
    public String toString() {
		return "RPill";
	}
	
	private int[] nearestPill() {
		int[] retCoord = new int[2];
		
		int px = Sexpr.game.player.posX / Sexpr.game.board.blockSize;
		int py = Sexpr.game.player.posY / Sexpr.game.board.blockSize;
		
		int csd = 1000; // current shortest distance 
		
		if(isPill(1, 2) && distance(1, 2, px, py) < csd) {
			csd = distance(1, 2, px, py);
			retCoord[0] = 1;
			retCoord[1] = 2;
		}
		
		if(isPill(17, 2) && distance(17, 2, px, py) < csd) {
			csd = distance(17, 2, px, py);
			retCoord[0] = 17;
			retCoord[1] = 2;
		}
		
		if(isPill(1, 16) && distance(1, 16, px, py) < csd) {
			csd = distance(1, 16, px, py);
			retCoord[0] = 1;
			retCoord[1] = 16;
		}
		
		if(isPill(17, 16) && distance(17, 16, px, py) < csd) {
			csd = distance(17, 16, px, py);
			retCoord[0] = 17;
			retCoord[1] = 16;
		}
		
		return retCoord;
	}
	
	private boolean isPill(int x, int y) {
		return Sexpr.game.board.getBlock(x, y) == Board.PILL;
	}
	
	private int distance(int x1, int y1, int x2, int y2) {
		return (int) Math.round(Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2)));
	}
	
}
