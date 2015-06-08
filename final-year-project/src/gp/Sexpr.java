/*
 * This Java source code is authored by Gagondeep Srai(W1374553),  
 * for use in his final year project at University of Westminster.
 * Created: 08/03/2015
 */
package gp;

import game.Game;

/*
 * Class Sexpr (stands for Symbolic Expression) is an abstract class
 * that represents a node in tree data structure. The function and
 * terminal sets all inherit from Sexpr. The member variable
 * isTerminal is used to distinguish nodes(or Sexpr) between the two sets.
 * 
 * All Symbolic Expressions must implement the method evaluate which
 * is used to recursively evaluate the tree of Symbolic Expressions.
 * For this particular genetic programming problem evaluate returns
 * an integer which can represent the cardinal directions or the
 * shortest distance. 
 * 
 * Classes that extend Sexpr override the method toString which prints
 * the expression tree (program).
 */
public abstract class Sexpr {
	
	public static Game game;
	
	public boolean isTerminal;
	
	public abstract int evaluate();
	
	public void display() {
		System.out.print(this);
	}
	
	public String getExpr() {
		return this.toString();
	}
	
	/*
	 * Create a new game for each individual in the population.
	 */
	public static void newGame() {
		game = new Game();
	}
	
	public static void resetGame() {
		game.reset();
	}
	
	public static int runExpr(Sexpr expression) {
		
		while(!game.dead)
			expression.evaluate();
		
		return game.player.getScore();
	}
}