/*
 * This Java source code is authored by Gagondeep Srai(W1374553),  
 * for use in his final year project at University of Westminster.
 * Created: 10/03/2015
 */
package gp;

/*
 * Class AdvGB
 * Changes the new direction of pacman by calculating
 * the shortest path to the second nearest Ghost. 
 * This terminal will return the direction Pac-Man is facing.
 */
public class AdvGB extends Sexpr {
	
	public AdvGB() {
		this.isTerminal = true;
	}
	
	@Override
	public int evaluate() {
		int [] coordGB = secondNearestGhost();
		Utils.AdvanceTile(coordGB[0], coordGB[1]);

		// returns the facing direction encoded as a modilo 4 number
		return Sexpr.game.player.direction.ordinal(); 
	}
	
	@Override
    public String toString() {
		return "AGB";
	}
	
	/*
	 * this method finds the second nearest ghost by: 
	 * finding a new shortest distance and then assigning the old shortest distance
	 * to the second shortest distance.
	 * however if the shortest distance is not smaller than the current shortest 
	 * distance then it is checked if it is smaller than the second shortest distance
	 * if it is then it becomes the second shortest distance else it is ignored.
	 * 
	 * variables CSDindex and SSDindex store the index of the distances array which
	 * represents the ghost. so SSDindex (second shortest distance index) will contain
	 * the index of the second nearest ghost, + 1 because arrays start at 0. 
	 * 
	 */
	private int[] secondNearestGhost() {
		int[] retCoord = new int[2];
		
		int xp = Sexpr.game.player.posX / Sexpr.game.board.blockSize;
		int yp = Sexpr.game.player.posY / Sexpr.game.board.blockSize;
		
		int xg1 = Sexpr.game.ghost1.posX / Sexpr.game.board.blockSize;
		int yg1 = Sexpr.game.ghost1.posY / Sexpr.game.board.blockSize;
		
		int xg2 = Sexpr.game.ghost2.posX / Sexpr.game.board.blockSize;
		int yg2 = Sexpr.game.ghost2.posY / Sexpr.game.board.blockSize;
		
		int xg3 = Sexpr.game.ghost3.posX / Sexpr.game.board.blockSize;
		int yg3 = Sexpr.game.ghost3.posY / Sexpr.game.board.blockSize;
		
		int xg4 = Sexpr.game.ghost4.posX / Sexpr.game.board.blockSize;
		int yg4 = Sexpr.game.ghost4.posY / Sexpr.game.board.blockSize;
		
		int currentShortestDistance = 1000;
		int secondShortestDistance = 1001;
		int CSDindex = 0;
		int SSDindex = -1;

		int[] distances = new int[4];
		distances[0] = distance(xg1, yg1, xp, yp);
		distances[1] = distance(xg2, yg2, xp, yp);
		distances[2] = distance(xg3, yg3, xp, yp);
		distances[3] = distance(xg4, yg4, xp, yp);
		
		for (int i = 0; i < 4; i++) {
			if (isEaten(i)) continue; // skip check if ghost is eaten
			
			if (distances[i] < currentShortestDistance) {
				secondShortestDistance = currentShortestDistance;
				SSDindex = CSDindex;
				currentShortestDistance = distances[i];
				CSDindex = i;
			} else if (distances[i] < secondShortestDistance) {
				secondShortestDistance = distances[i];
				SSDindex = i;
			}
		}
		
		switch(SSDindex) {
			case 0:
				retCoord[0] = xg1;
				retCoord[1] = yg1;
				break;
			case 1:
				retCoord[0] = xg2;
				retCoord[1] = yg2;
				break;
			case 2:
				retCoord[0] = xg3;
				retCoord[1] = yg3;
				break;
			case 3:
				retCoord[0] = xg4;
				retCoord[1] = yg4;
				break;
		}
		
		return retCoord;
	}
	
	private int distance(int x1, int y1, int x2, int y2) {
		return (int) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}
	
	private boolean isEaten(int ghost) {
		switch(ghost) {
			case 0:
				return Sexpr.game.ghost1.eaten;
			case 1:
				return Sexpr.game.ghost2.eaten;
			case 2:
				return Sexpr.game.ghost3.eaten;
			case 3:
				return Sexpr.game.ghost4.eaten;
		}
		return true; // never called
	}
	
}