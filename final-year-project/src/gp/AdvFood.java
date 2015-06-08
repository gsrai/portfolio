/*
 * This Java source code is authored by Gagondeep Srai(W1374553),  
 * for use in his final year project at University of Westminster.
 * Created: 10/03/2015
 */
package gp;

/*
 * Class AdvFood
 * Changes the new direction of pacman by calculating
 * the shortest path to the nearest food pellet. 
 * This terminal will return the direction Pac-Man is facing.
 */
public class AdvFood extends Sexpr {
	
	public AdvFood() {
		this.isTerminal = true;
	}
	
	@Override
	public int evaluate() {
		// get the nearest pill depending on your position
		int [] coord = Sexpr.game.board.findNearestPill(
				Sexpr.game.player.posX/ Sexpr.game.board.blockSize, 
				Sexpr.game.player.posY/ Sexpr.game.board.blockSize );
		
		// don't need to check for both as -1 -1 is returned only when no pellet is found
		if (coord[0] != -1) Utils.AdvanceTile(coord[0], coord[1]);
		
		// returns the facing direction encoded as a modilo 4 number
		return Sexpr.game.player.direction.ordinal(); 
	}
	
	@Override
    public String toString() {
		return "AFood";
	}
	
}