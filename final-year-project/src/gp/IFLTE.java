/*
 * This Java source code is authored by Gagondeep Srai(W1374553),  
 * for use in his final year project at University of Westminster.
 * Created: 08/03/2015
 */
package gp;

/*
 * Class IFLTE (stands for If Less Than or Equal to) is a
 * a member of the function set.
 */
public class IFLTE extends Sexpr {
	
	public Sexpr arg1;
	public Sexpr arg2;
	public Sexpr arg3;
	public Sexpr arg4;
	
	public IFLTE(Sexpr arg1, Sexpr arg2, Sexpr arg3, Sexpr arg4) {
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.arg3 = arg3;
		this.arg4 = arg4;
		
		this.isTerminal = false;
	}
	
	@Override
	public int evaluate() {
		if (arg1.evaluate() <= arg2.evaluate())
			return arg3.evaluate();
		else
			return arg4.evaluate();
	}
	
	@Override
    public String toString() {
		return "(IFLTE " + arg1 + " " + arg2 + " " + arg3 + " " + arg4 +")";
	}
	
}
