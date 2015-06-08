/*
 * This Java source code is authored by Gagondeep Srai(W1374553),  
 * for use in his final year project at University of Westminster.
 * Created: 10/03/2015
 */
package gp;

/*
 * Class DisGA
 * Terminal that returns the distance between pacman and 
 * the second nearest ghost
 */
public class DisGB extends Sexpr {
	
	public DisGB() {
		this.isTerminal = true;
	}
	
	@Override
	public int evaluate() {
	
		return secondNearestGhost();
	}
	
	@Override
    public String toString() {
		return "DisGB";
	}
	
	// returns the second nearest ghosts coordinates.
	private int secondNearestGhost() {
		
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

		int[] distances = new int[4];
		distances[0] = distanceBetween(xg1, yg1, xp, yp);
		distances[1] = distanceBetween(xg2, yg2, xp, yp);
		distances[2] = distanceBetween(xg3, yg3, xp, yp);
		distances[3] = distanceBetween(xg4, yg4, xp, yp);
		
		for (int i = 0; i < 4; i++) {
			if (isEaten(i)) continue; // skip check if ghost is eaten
			
			if (distances[i] < currentShortestDistance) {
				secondShortestDistance = currentShortestDistance;
				currentShortestDistance = distances[i];
			} else if (distances[i] < secondShortestDistance) {
				secondShortestDistance = distances[i];
			}
		}
		
		return secondShortestDistance;
	}
	
	private int distanceBetween(int x1, int y1, int x2, int y2) {
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