/*
 * This Java source code is authored by Gagondeep Srai(W1374553),  
 * for use in his final year project at University of Westminster.
 * Created: 23/03/2015
 */
package gp;

import game.Player.Direction;

/*
 * Class Utils contains commonly used methods used by the terminals.
 */
public class Utils {
	
	private static int distance(int x1, int y1, int x2, int y2) {
		return (int) Math.round(Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2)));
	}
	
	public static void AdvanceTile(int tx, int ty) {
		// logic to travel to a specific tile, shortest path
		int x = Sexpr.game.player.posX / Sexpr.game.board.blockSize;
		int y = Sexpr.game.player.posY / Sexpr.game.board.blockSize;
		
		int currentShortestPath = 1000; 
		
		/*
		 * target tile, checks if the tile ahead is closer to the destination, and changes
		 * the direction of movement depending on the 
		 * shortest path. you cannot check behind you
		 * other wise you will infinitely backtrack.
		 */
		if (Sexpr.game.player.inBlock(Sexpr.game.player.posX, Sexpr.game.player.posY)) {
			if (Sexpr.game.player.direction == Direction.UP) {
				if (!Sexpr.game.board.isWall(x, y - 1)) {
					// check shortest path?
					int newY = y - 1;
					int newShortestPath = distance(tx, ty, x, newY);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						Sexpr.game.player.newDirection = Direction.UP;
					}
				}
				
				if (!Sexpr.game.board.isWall(x - 1, y)) {
					int newX = x - 1;
					int newShortestPath = distance(tx, ty, newX, y);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						Sexpr.game.player.newDirection = Direction.LEFT;
					}
				}
				
				if (!Sexpr.game.board.isWall(x + 1, y)) {
					int newX = x + 1;
					int newShortestPath = distance(tx, ty, newX, y);;
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						Sexpr.game.player.newDirection = Direction.RIGHT;
					}
				}
			}
			
			if (Sexpr.game.player.direction == Direction.DOWN) {
				if (!Sexpr.game.board.isWall(x, y + 1)) {
					int newY = y + 1;
					int newShortestPath = distance(tx, ty, x, newY);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						Sexpr.game.player.newDirection = Direction.DOWN;
					}
				}
				
				if (!Sexpr.game.board.isWall(x - 1, y)) {
					int newX = x - 1;
					int newShortestPath = distance(tx, ty, newX, y);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						Sexpr.game.player.newDirection = Direction.LEFT;
					}
				}
				
				if (!Sexpr.game.board.isWall(x + 1, y)) {
					int newX = x + 1;
					int newShortestPath = distance(tx, ty, newX, y);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						Sexpr.game.player.newDirection = Direction.RIGHT;
					}
				}
			}
			
			if (Sexpr.game.player.direction == Direction.LEFT) {
				if (!Sexpr.game.board.isWall(x, y - 1)) {
					// check shortest path?
					int newY = y - 1;
					int newShortestPath = distance(tx, ty, x, newY);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						Sexpr.game.player.newDirection = Direction.UP;
					}
				}
				
				if (!Sexpr.game.board.isWall(x, y + 1)) {
					int newY = y + 1;
					int newShortestPath = distance(tx, ty, x, newY);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						Sexpr.game.player.newDirection = Direction.DOWN;
					}
				}
				
				if (!Sexpr.game.board.isWall(x - 1, y)) {
					int newX = x - 1;
					int newShortestPath = distance(tx, ty, newX, y);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						Sexpr.game.player.newDirection = Direction.LEFT;
					}
				}
			}
			
			if (Sexpr.game.player.direction == Direction.RIGHT) {
				if (!Sexpr.game.board.isWall(x, y - 1)) {
					// check shortest path?
					int newY = y - 1;
					int newShortestPath = distance(tx, ty, x, newY);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						Sexpr.game.player.newDirection = Direction.UP;
					}
				}
				
				if (!Sexpr.game.board.isWall(x, y + 1)) {
					int newY = y + 1;
					int newShortestPath = distance(tx, ty, x, newY);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						Sexpr.game.player.newDirection = Direction.DOWN;
					}
				}
				
				if (!Sexpr.game.board.isWall(x + 1, y)) {
					int newX = x + 1;
					int newShortestPath = distance(tx, ty, newX, y);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						Sexpr.game.player.newDirection = Direction.RIGHT;
					}
				}
			}
		}
	}
	
	public static void retreatTile(int tx, int ty) {
		
		int x = Sexpr.game.player.posX / Sexpr.game.board.blockSize;
		int y = Sexpr.game.player.posY / Sexpr.game.board.blockSize;
		
		int currentLongestPath = 0;
		
		if (Sexpr.game.player.inBlock(Sexpr.game.player.posX, Sexpr.game.player.posY)) {
			
			if (!Sexpr.game.board.isWall(x, y - 1)) {
				// check shortest path?
				int newY = y - 1;
				int newLongestPath = distance(tx, ty, x, newY);
				if (newLongestPath > currentLongestPath) {
					//change direction
					currentLongestPath = newLongestPath;
					Sexpr.game.player.newDirection = Direction.UP;
				}
			}
			
			if (!Sexpr.game.board.isWall(x - 1, y)) {
				int newX = x - 1;
				int newLongestPath = distance(tx, ty, newX, y);
				if (newLongestPath > currentLongestPath) {
					//change direction
					currentLongestPath = newLongestPath;
					Sexpr.game.player.newDirection = Direction.LEFT;
				}
			}
			
			if (!Sexpr.game.board.isWall(x, y + 1)) {
				int newY = y + 1;
				int newLongestPath = distance(tx, ty, x, newY);
				if (newLongestPath > currentLongestPath) {
					//change direction
					currentLongestPath = newLongestPath;
					Sexpr.game.player.newDirection = Direction.DOWN;
				}
			}
			
			if (!Sexpr.game.board.isWall(x + 1, y)) {
				int newX = x + 1;
				int newLongestPath = distance(tx, ty, newX, y);;
				if (newLongestPath > currentLongestPath) {
					//change direction
					currentLongestPath = newLongestPath;
					Sexpr.game.player.newDirection = Direction.RIGHT;
				}
			}
			
		}
	}
}
