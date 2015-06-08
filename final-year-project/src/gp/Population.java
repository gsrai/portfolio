/*
 * This Java source code is authored by Gagondeep Srai(W1374553),  
 * for use in his final year project at University of Westminster.
 * Created: 10/03/2015
 */
package gp;

import java.util.Random;

public class Population {
	// must use even population sizes (crossover will throw out of bounds)
	public final static int SIZE = 200; 
	public final static int GENERATIONS = 25;
	private static Random rand = new Random();
	private static LoggerHandler lh;
	private static Sexpr[] expressions;
	private static Sexpr[] newExpressions = new Sexpr[SIZE];
	
	private static int[] fitnesses;
	private static int totalFitness;
	private static int expressionDepth;
	
	private static double mutationRate = 0.08;
	
	public static void initialize(int depth) {
		expressions = new Sexpr[SIZE];
		expressionDepth = depth;
		Sexpr.newGame();
		for (int i = 0; i < SIZE; i++)
			expressions[i] = createExpression(depth);
		
		lh = new LoggerHandler();
	}
	
	public static void display() {
		for (int i = 0; i < SIZE; i++) {
			System.out.print("expression " + (i + 1) + " : ");
			expressions[i].display();
			System.out.println("");
		}
		System.out.println("");
	}
	
	public static void fitness() {
		fitnesses = new int[SIZE];
		totalFitness = 0;
		
		int tempFitness;
		
		for (int i = 0; i < SIZE; i++) {
			System.out.println("Calculating fitness of individual " + (i+1));
			System.out.print("expression " + (i + 1) + " : ");
			expressions[i].display();
			tempFitness = 0;
			// start fitness test
			Sexpr.resetGame();
			tempFitness = Sexpr.runExpr(expressions[i]);
			// end fitness test
			fitnesses[i] = tempFitness;
			System.out.println("\nfitness: " + fitnesses[i]);
			System.out.println("");
			if (fitnesses[i] > 2000) {
				lh.log("Expression of Individual " + (i + 1) + " :\n" + expressions[i].getExpr() + "\nfitness: " + fitnesses[i] + "\n");
			}
		}	
		
		for (int k = 0; k < fitnesses.length; k++)
			totalFitness = totalFitness + fitnesses[k];
		
	}
	
	/*
	 * Using Fitness proportionate selection (also known as roulette wheel selection),
	 * the crossover function can select potentially useful Expressions for recombination.
	 * The probability of an individual expression being selected is equivalent to the
	 * fitness of the individual expression over the sum of the fitnesses of the whole
	 * population.
	 */
	private static Sexpr selection() {
		
		int ball = rand.nextInt(totalFitness) + 1; // Don't want ball to be zero
		int rollingSum = 0;
		
		for (int i = 0; i < fitnesses.length; i++) {
			rollingSum = rollingSum + fitnesses[i];
			
			if (ball <= rollingSum) {
				return expressions[i];
			}
		}
		
		return null; // Never reached, needed to avoid compilation errors.
	}
	
	// take two samples from the population and create two children
	public static void crossover() {
		System.out.println("crossing over");
		SymbolicExpressionReference exprRef1;
		SymbolicExpressionReference exprRef2;
		
		for (int i = 0; i < SIZE; i = i + 2) {
			
			Sexpr selection1;
			Sexpr selection2;
			selection1 = selection();
			do {
				selection2 = selection();
			} while (selection2 == selection1);
			
			// Don't change original population
			newExpressions[i] = selection1;
			newExpressions[i + 1] = selection2;
			
			// Create new pointers to the parents of the random expressions
			exprRef1 = new SymbolicExpressionReference();
			exprRef2 = new SymbolicExpressionReference();
			// Find two random expressions which will be crossovered
			Sexpr expr1 = findExpression(newExpressions[i], exprRef1);
			Sexpr expr2 = findExpression(newExpressions[i + 1], exprRef2);
			
			// Now swap or cross over the two expressions
			if (exprRef1.parent instanceof IfHungry) {
				IfHungry ifh = (IfHungry)exprRef1.parent;
				
				if (exprRef1.child == 1) {
					ifh.arg1 = expr2; 
				} else if (exprRef1.child == 2){
					ifh.arg2 = expr2;
				}
				
			} else {
				IFLTE iflte = (IFLTE)exprRef1.parent;
				
				if (exprRef1.child == 1) {
					iflte.arg1 = expr2; 
				} else if (exprRef1.child == 2){
					iflte.arg2 = expr2;
				} else if (exprRef1.child == 3){
					iflte.arg3 = expr2;
				} else if (exprRef1.child == 4){
					iflte.arg4 = expr2;
				}
			}
			
			if (exprRef2.parent instanceof IfHungry) {
				IfHungry ifh = (IfHungry)exprRef2.parent;
				
				if (exprRef2.child == 1) {
					ifh.arg1 = expr1; 
				} else if (exprRef2.child == 2){
					ifh.arg2 = expr1;
				}
				
			} else {
				IFLTE iflte = (IFLTE)exprRef2.parent;
				
				if (exprRef2.child == 1) {
					iflte.arg1 = expr1; 
				} else if (exprRef2.child == 2){
					iflte.arg2 = expr1;
				} else if (exprRef2.child == 3){
					iflte.arg3 = expr1;
				} else if (exprRef2.child == 4){
					iflte.arg4 = expr1;
				}
			}			
		}
		// replace original population
		expressions = newExpressions;
	}
	
	private static Sexpr findExpression(Sexpr root, SymbolicExpressionReference exprRef) {
		Sexpr expression = root;
		int searchDepth = rand.nextInt(expressionDepth + 1) + 1;
		
		while(true) {
			expression = searchTree(searchDepth, expression, exprRef);
			if (expression == null) {
				searchDepth = rand.nextInt(expressionDepth + 1) + 1; // search again
			} else {
				return expression;
			}
		}
		
	}
	
	private static Sexpr searchTree(int node, Sexpr root, 
			SymbolicExpressionReference exprRef) {
		
		int depth = 0;
		Sexpr focusNode = root;
		Sexpr parentNode = null;
		
		while(depth != node) {
			if (focusNode.isTerminal) {
				exprRef.parent = parentNode;
				return focusNode;
			} else {
				parentNode = focusNode;
				
				if (focusNode instanceof IfHungry) {
					IfHungry ifh = (IfHungry)focusNode;
					int r = rand.nextInt(2);
					focusNode = (r == 0) ? ifh.arg1 : ifh.arg2;
					exprRef.child = (r == 0) ? 1 : 2;
					
				} else {
					IFLTE iflte = (IFLTE)focusNode;
					int r = rand.nextInt(4);
					switch(r) {
						case 0:
							focusNode = iflte.arg1;
							exprRef.child = 1;
							break;
						case 1:
							focusNode = iflte.arg2;
							exprRef.child = 2;
							break;
						case 2:
							focusNode = iflte.arg3;
							exprRef.child = 3;
							break;
						case 3:
							focusNode = iflte.arg4;
							exprRef.child = 4;
							break;
					}
				}
			}
			
			if (focusNode == null)
				return null; // probably do not need this
			
			depth++;
		}
		exprRef.parent = parentNode;
		return focusNode;
	}
	
	public static void mutate() {
		System.out.println("mutating");
		// take new population and mutate 
		for (int i = 0; i < SIZE; i++) {
			if (rand.nextDouble() <= mutationRate) {
				
				expressions[i] = mutateExpr(expressions[i]);

			}
		}
	}
	
	/*
	 * the search depth must be a random integer in the range 0 to expressionDepth,
	 * however as nextInt excludes the bound in its distribution, one must increase
	 * this range by 1 (also to ignore the root node at 0).
	 * 
	 */
	private static Sexpr mutateExpr(Sexpr se) {
		int searchDepth = rand.nextInt(expressionDepth) + 1;
		int depth = 0;
		
		SymbolicExpressionReference exprRef = new SymbolicExpressionReference();
		Sexpr focusNode = se;
		Sexpr parentNode = null;
		
		while(depth != searchDepth) {
			if (focusNode.isTerminal) {
				// changing a terminal node.
				focusNode = randTerminal();
				
				if (parentNode instanceof IfHungry) {
					IfHungry ifh = (IfHungry)parentNode;
					if (exprRef.child == 1)
						ifh.arg1 = focusNode; 
					else if (exprRef.child == 2)
						ifh.arg2 = focusNode;
					
				} else {
					IFLTE iflte = (IFLTE)parentNode;
					
					if (exprRef.child == 1)
						iflte.arg1 = focusNode; 
					else if (exprRef.child == 2)
						iflte.arg2 = focusNode;
					else if (exprRef.child == 3)
						iflte.arg3 = focusNode;
					else if (exprRef.child == 4)
						iflte.arg4 = focusNode;
				}
				return se;
			} else {
				parentNode = focusNode;
				
				if (focusNode instanceof IfHungry) {
					IfHungry ifh = (IfHungry)focusNode;
					int r = rand.nextInt(2);
					focusNode = (r == 0) ? ifh.arg1 : ifh.arg2;
					exprRef.child = (r == 0) ? 1 : 2;
					
				} else {
					IFLTE iflte = (IFLTE)focusNode;
					int r = rand.nextInt(4);
					switch(r) {
						case 0:
							focusNode = iflte.arg1;
							exprRef.child = 1;
							break;
						case 1:
							focusNode = iflte.arg2;
							exprRef.child = 2;
							break;
						case 2:
							focusNode = iflte.arg3;
							exprRef.child = 3;
							break;
						case 3:
							focusNode = iflte.arg4;
							exprRef.child = 4;
							break;
					}
				}
			}
			
			depth++;
		}
		// changing a non terminal node. IfHungry, IFLTE.

		focusNode = genExpr(expressionDepth - depth);
		
		// now replace the old node in the expression
		if (parentNode instanceof IfHungry) {
			IfHungry ifh = (IfHungry)parentNode;
			if (exprRef.child == 1)
				ifh.arg1 = focusNode; 
			else if (exprRef.child == 2)
				ifh.arg2 = focusNode;
			
		} else {
			IFLTE iflte = (IFLTE)parentNode;
			
			if (exprRef.child == 1)
				iflte.arg1 = focusNode; 
			else if (exprRef.child == 2)
				iflte.arg2 = focusNode;
			else if (exprRef.child == 3)
				iflte.arg3 = focusNode;
			else if (exprRef.child == 4)
				iflte.arg4 = focusNode;
		}
		
		return se;
	}
	
	private static Sexpr randTerminal() {
		switch (rand.nextInt(13)) {
			case 0: return new AdvPill();
			case 1: return new RetPill();
			case 2: return new DisPill();
			case 3: return new AdvGA();
			case 4: return new RetGA();
			case 5: return new DisGA();
			case 6: return new AdvGB();
			case 7: return new RetGB();
			case 8: return new DisGB();
			case 9: return new AdvFood();
			case 10: return new DisFood();
			case 11: return new AdvFruit();
			case 12: return new DisFruit();
		}
  
        return null; // Never reached, needed to avoid compilation errors.
	}
	
	private static Sexpr genExpr(int depth) {
		// Base case
		
		if (depth == 0) {
			switch (rand.nextInt(13)) {
				case 0: return new AdvPill();
				case 1: return new RetPill();
				case 2: return new DisPill();
				case 3: return new AdvGA();
				case 4: return new RetGA();
				case 5: return new DisGA();
				case 6: return new AdvGB();
				case 7: return new RetGB();
				case 8: return new DisGB();
				case 9: return new AdvFood();
				case 10: return new DisFood();
				case 11: return new AdvFruit();
				case 12: return new DisFruit();
			}
		}
		// Create a randomly generated Expression, root does not need to be a
		// member of the functional set.
		switch(rand.nextInt(depth)) {
			case 0: return new IfHungry(genExpr(depth - 1), genExpr(depth - 1));
			case 1: return new IFLTE(genExpr(depth - 1), genExpr(depth - 1), 
									genExpr(depth - 1), genExpr(depth - 1));
			case 2: return new AdvPill();
			case 3: return new RetPill();
			case 4: return new DisPill();
			case 5: return new AdvGA();
			case 6: return new RetGA();
			case 7: return new DisGA();
			case 8: return new AdvGB();
			case 9: return new RetGB();
			case 10: return new DisGB();
			case 11: return new AdvFood();
			case 12: return new DisFood();
			case 13: return new AdvFruit();
			case 14: return new DisFruit();
		}
		
		return null; // Never reached, needed to avoid compilation errors.
	}
	
	private static Sexpr createExpression(int depth) {
		// Base case
		if (depth == 0) {
			switch (rand.nextInt(13)) {
				case 0: return new AdvPill();
				case 1: return new RetPill();
				case 2: return new DisPill();
				case 3: return new AdvGA();
				case 4: return new RetGA();
				case 5: return new DisGA();
				case 6: return new AdvGB();
				case 7: return new RetGB();
				case 8: return new DisGB();
				case 9: return new AdvFood();
				case 10: return new DisFood();
				case 11: return new AdvFruit();
				case 12: return new DisFruit();
			}
		}
		// Create a randomly generated Expression.
		switch(rand.nextInt(depth == expressionDepth ? 2 : 15)) {
			case 0: return new IfHungry(createExpression(depth - 1), createExpression(depth - 1));
			case 1: return new IFLTE(createExpression(depth - 1), createExpression(depth - 1), 
									createExpression(depth - 1), createExpression(depth - 1));
			case 2: return new AdvPill();
			case 3: return new RetPill();
			case 4: return new DisPill();
			case 5: return new AdvGA();
			case 6: return new RetGA();
			case 7: return new DisGA();
			case 8: return new AdvGB();
			case 9: return new RetGB();
			case 10: return new DisGB();
			case 11: return new AdvFood();
			case 12: return new DisFood();
			case 13: return new AdvFruit();
			case 14: return new DisFruit();
		}
		
		return null; // Never reached, needed to avoid compilation errors.
	}
}

class SymbolicExpressionReference {
	// a terminal cannot be a parent
	public Sexpr parent;
	public int child; // left child is 1, and 2 is right child
}
