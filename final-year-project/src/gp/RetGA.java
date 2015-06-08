/*
 * This Java source code is authored by Gagondeep Srai(W1374553),  
 * for use in his final year project at University of Westminster.
 * Created: 10/03/2015
 */
package gp;

/*
 * Class RetGA
 * Changes the new direction of pacman by calculating
 * the furthest path from the nearest Ghost thereby retreating. 
 * This terminal will return the direction Pac-Man is facing.
 */
public class RetGA extends Sexpr {
	
	public RetGA() {
		this.isTerminal = true;
	}
	
	@Override
	public int evaluate() {
		
		int [] coordGA = nearestGhost();
		Utils.retreatTile(coordGA[0], coordGA[1]);
		
		// returns the facing direction encoded as a modilo 4 number
		return Sexpr.game.player.direction.ordinal(); 
	}
	
	@Override
    public String toString() {
		return "RGA";
	}
	
	private int[] nearestGhost() {
		int[] retCoord = new int[2];
		
		int px = Sexpr.game.player.posX / Sexpr.game.board.blockSize;
		int py = Sexpr.game.player.posY / Sexpr.game.board.blockSize;
		
		int xg1 = Sexpr.game.ghost1.posX / Sexpr.game.board.blockSize;
		int yg1 = Sexpr.game.ghost1.posY / Sexpr.game.board.blockSize;
		
		int xg2 = Sexpr.game.ghost2.posX / Sexpr.game.board.blockSize;
		int yg2 = Sexpr.game.ghost2.posY / Sexpr.game.board.blockSize;
		
		int xg3 = Sexpr.game.ghost3.posX / Sexpr.game.board.blockSize;
		int yg3 = Sexpr.game.ghost3.posY / Sexpr.game.board.blockSize;
		
		int xg4 = Sexpr.game.ghost4.posX / Sexpr.game.board.blockSize;
		int yg4 = Sexpr.game.ghost4.posY / Sexpr.game.board.blockSize;
		
		int currentShortestDistance = 1000; 
		
		// distance player ghost1 if < currentshortest distance.
		if(isNotEaten(0) && distance(xg1, yg1, px, py) < currentShortestDistance) {
			currentShortestDistance = distance(xg1, yg1, px, py);
			retCoord[0] = xg1;
			retCoord[1] = yg1;
		}
		
		if(isNotEaten(1) && distance(xg2, yg2, px, py) < currentShortestDistance) {
			currentShortestDistance = distance(xg2, yg2, px, py);
			retCoord[0] = xg2;
			retCoord[1] = yg2;
		}
		
		if(isNotEaten(2) && distance(xg3, yg3, px, py) < currentShortestDistance) {
			currentShortestDistance = distance(xg3, yg3, px, py);
			retCoord[0] = xg3;
			retCoord[1] = yg3;
		}
		
		if(isNotEaten(3) && distance(xg4, yg4, px, py) < currentShortestDistance) {
			currentShortestDistance = distance(xg4, yg4, px, py);
			retCoord[0] = xg4;
			retCoord[1] = yg4;
		}
		
		return retCoord;
	}
	
	private int distance(int x1, int y1, int x2, int y2) {
		return (int) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}
	
	private boolean isNotEaten(int ghost) {
		switch(ghost) {
			case 0:
				return !Sexpr.game.ghost1.eaten;
			case 1:
				return !Sexpr.game.ghost2.eaten;
			case 2:
				return !Sexpr.game.ghost3.eaten;
			case 3:
				return !Sexpr.game.ghost4.eaten;
		}
		return true; // never called
	}
}
