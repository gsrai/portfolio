/*
 * This Java source code is authored by Gagondeep Srai(W1374553),  
 * for use in his final year project at University of Westminster.
 * Created: 10/03/2015
 */
package gp;

public class Main {

	public static void main(String[] args) {
		System.out.println(">> Running Pacman Genetic Programming");
		final int DEPTH = 6;
		Population.initialize(DEPTH);
		
		for (int i = 0; i < Population.GENERATIONS; i++) {
			System.out.println("\nGeneration " + (i + 1));
			System.out.println("---------------------------");
			Population.fitness();
			Population.crossover();
			Population.mutate();
		}
		System.out.println("end");
	}

}
