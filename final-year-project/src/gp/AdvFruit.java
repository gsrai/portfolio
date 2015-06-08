/*
 * This Java source code is authored by Gagondeep Srai(W1374553),  
 * for use in his final year project at University of Westminster.
 * Created: 10/03/2015
 */
package gp;

/*
 * Class AdvFruit
 * Changes the new direction of pacman by calculating
 * the shortest path to the Fruit. 
 * This terminal will return the direction Pac-Man is facing.
 */
public class AdvFruit extends Sexpr {

	public AdvFruit() {
		this.isTerminal = true;
	}
	
	@Override
	public int evaluate() {
		if (Sexpr.game.board.fruitVisible)
			Utils.AdvanceTile(9, 12);
		
		
		// returns the facing direction encoded as a modilo 4 number
		return Sexpr.game.player.direction.ordinal(); 
	}
	
	@Override
    public String toString() {
		return "AFruit";
	}
	
}