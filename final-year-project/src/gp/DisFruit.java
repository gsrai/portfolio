/*
 * This Java source code is authored by Gagondeep Srai(W1374553),  
 * for use in his final year project at University of Westminster.
 * Created: 10/03/2015
 */
package gp;

/*
 * Class DisFruit
 * Returns the shortest distance between the static fruit and pacman.
 */
public class DisFruit extends Sexpr {
	
	private final int FRUIT_X = 9;
	private final int FRUIT_Y = 12;
	
	public DisFruit() {
		this.isTerminal = true;
	}
	
	@Override
	public int evaluate() {
		int px = Sexpr.game.player.posX/ Sexpr.game.board.blockSize;
		int py = Sexpr.game.player.posY/ Sexpr.game.board.blockSize;
		
		if (Sexpr.game.board.fruitVisible)
			return distance(FRUIT_X, FRUIT_Y, px, py);
		else 
			// return a really large number if the fruit is not visible
			return Integer.MAX_VALUE; 
	
	}
	
	@Override
    public String toString() {
		return "DisFruit";
	}
	
	private int distance(int x1, int y1, int x2, int y2) {
		return (int) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}
}
