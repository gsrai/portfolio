/*
 * This Java source code is authored by Gagondeep Srai(W1374553),  
 * for use in his final year project at University of Westminster.
 * Created: 10/03/2015
 */
package gp;

/*
 * Class DisFood
 * Terminal that returns the distance between pacman and the 
 * nearest uneaten pellet.
 */
public class DisFood extends Sexpr {
	
	public DisFood() {
		this.isTerminal = true;
	}
	
	@Override
	public int evaluate() {
		int px = Sexpr.game.player.posX/ Sexpr.game.board.blockSize;
		int py = Sexpr.game.player.posY/ Sexpr.game.board.blockSize;
		
		int [] coord = Sexpr.game.board.findNearestPill(px, py);
		
		// don't need to check for both as -1 -1 is returned only when no pellet is found
		if (coord[0] != -1)
			return distance(coord[0], coord[1], px, py);
		else 
			return Integer.MAX_VALUE; // return a really large number if there are no pellets
		
	}
	
	@Override
    public String toString() {
		return "DisFood";
	}
	
	private int distance(int x1, int y1, int x2, int y2) {
		return (int) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}
}

