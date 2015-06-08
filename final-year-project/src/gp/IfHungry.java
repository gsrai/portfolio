/*
 * This Java source code is authored by Gagondeep Srai(W1374553),  
 * for use in his final year project at University of Westminster.
 * Created: 08/03/2015
 */
package gp;

/*
 * Class IfHungry is a member of the function set.
 */
public class IfHungry extends Sexpr {

	public Sexpr arg1;
	public Sexpr arg2;
	
	public IfHungry(Sexpr arg1, Sexpr arg2) {
		this.arg1 = arg1;
		this.arg2 = arg2;
		
		this.isTerminal = false;
	}
	
	@Override
	public int evaluate() {
		if (Sexpr.game.player.hungry)
			return arg1.evaluate();
		else
			return arg2.evaluate();
	}
	
	@Override
    public String toString() {
		return "(IFH " + arg1 + " " + arg2 + ")";
	}
	
}
