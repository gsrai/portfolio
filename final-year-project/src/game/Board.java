package game;


import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gagondeep Srai
 * 
 */

public class Board {

	public static final int WALL = 0;
	public static final int PELLET = 1;
	public static final int EMPTY = 2;
	public static final int DEN = 3; // ghost den
	public static final int PILL = 4; // Energizer 
	public static final int PACMAN_START = 5;
	public static final int FRUIT = 6;
	
	// 19 x 22 map (x,y), accessed by map[y][x]
	public int[][] map = new int[][] {
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
			{0, 4, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 4, 0},
			{0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0},
			{0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
			{0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0},
			{0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0},
			{0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
			{2, 2, 2, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 2, 2, 2},
			{0, 0, 0, 0, 1, 0, 1, 0, 0, 3, 0, 0, 1, 0, 1, 0, 0, 0, 0},
			{2, 2, 2, 2, 1, 1, 1, 0, 3, 3, 3, 0, 1, 1, 1, 2, 2, 2, 2},
			{0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0},
			{2, 2, 2, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 2, 2, 2},
			{0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0},
			{0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
			{0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0},
			{0, 4, 1, 0, 1, 1, 1, 1, 1, 5, 1, 1, 1, 1, 1, 0, 1, 4, 0},
			{0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0},
			{0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0},
			{0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0},
			{0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
	};
	
	// for Collision Detection and Drawing
	public int height; 
	public int width;
	public int blockSize;
	public int arrX = 19;
	public int arrY = 22;
	private long fruitTime;
	public boolean fruitVisible;
	private int prevBlock;
	public boolean fruitEaten;
	private boolean fruitTimeOver = false;
	private boolean gotBlock;
	
	private Map<String, Sprite> sprites;
	
	// we don't want the width in pixels for the map, we want in blocks
	public Board(int blockSize, Map<String, Sprite> s) {
		this.blockSize = blockSize;
		this.sprites = (HashMap<String, Sprite>)s;
		init();
	}
	
	public void init() {
		this.width = arrX;
		this.height = arrY;
		this.fruitVisible = false;
		fruitEaten = false;
		this.gotBlock = false;
	}
	
	public void update(int numPelletsEaten) {
		fruitTimer();
		if (numPelletsEaten == 124) {
			fruitEaten = false;
			fruitTime = System.currentTimeMillis();
			if (!gotBlock) {
				this.prevBlock = this.getBlock(9, 12); // save incase block was a pellet
				gotBlock = true;
			}
			this.setBlock(9, 12, 6);
		} else if (numPelletsEaten == 53) {
			fruitTime = System.currentTimeMillis();
			if (!gotBlock) {
				this.prevBlock = this.getBlock(9, 12); // save incase block was a pellet
				gotBlock = true;
			}
			this.setBlock(9, 12, 6); // set to fruit
		}
	}
	
	public void render(Graphics2D g) {
		drawWall(g);
		drawPellets(g);
		drawPills(g);
		drawEmpty(g);
		
		if (fruitVisible) drawFruit(g);
	}
	
	// return if the position x, y is within the bounds of the map
	public boolean withinBounds(int x, int y) {
		return (x >= 0 && x < width) && (y >= 0 && y < height);
	}
	
	public boolean isWall(int x, int y) {
		return withinBounds(x, y) && map[y][x] == WALL;
	}
	
	public boolean isSpaceFriendly(int x, int y) {
		
		if (!withinBounds(x, y)) return false; // check if position is within bounds
		
		int mapPos = map[y][x];
		return mapPos == PELLET || mapPos == EMPTY || mapPos == PILL || mapPos == FRUIT;
	}
	
	public int getBlock(int x, int y) {
		return map[y][x];
	}
	
	public void setBlock(int x, int y, int type) {
		map[y][x] = type;
	}
	
	public void drawWall(Graphics2D g) {
		g.setPaint(Color.BLUE);
		
		for (int i = 0; i < arrY; i++) 
			for (int j = 0; j < arrX; j++) 
				if (map[i][j] == WALL) 
					g.fillRect(j * blockSize, i * blockSize, blockSize, blockSize);
		
	}
	
	public void drawPellets(Graphics2D g) {
		g.setPaint(Color.WHITE);
		// displacement (d) is used to center the pellets
		int d = blockSize/3;
		// radius (r) is the width and height of the circle
		int r = blockSize/4;
		
		for (int i = 0; i < arrY; i++) 
			for (int j = 0; j < arrX; j++) 
				if (map[i][j] == PELLET) 
					g.fillOval((j * blockSize) + d + 2, (i * blockSize) + d + 2, r, r);
		
	}
	
	public void drawPills(Graphics2D g) {
		// displacement (d) is used to center the pills
		int d = blockSize/4;
		// radius (r) is the width and height of the circle
		int r = blockSize/2;
			
		for (int i = 0; i < arrY; i++) 
			for (int j = 0; j < arrX; j++) 
				if (map[i][j] == PILL) 
					g.fillOval((j * blockSize) + d + 1, (i * blockSize) + d + 1, r, r);
		
	}
	
	public void drawEmpty(Graphics2D g) {
		g.setPaint(Color.BLACK);
		
		for (int i = 0; i < arrY; i++) 
			for (int j = 0; j < arrX; j++) 
				if (map[i][j] == EMPTY) 
					g.fillRect(j * blockSize, i * blockSize, blockSize, blockSize);
				
	}
	
	public void drawFruit(Graphics2D g) {
		g.drawImage(sprites.get("fruit").getSprite(), null, 9 * blockSize, 12 * blockSize);
	}
	
	public void fruitTimer() {
		if (System.currentTimeMillis() - fruitTime > 10000) {
			fruitVisible = false;
			
			if (fruitTimeOver) {
				this.setBlock(9, 12, prevBlock);
				gotBlock = false; // so as to save the block next time
				fruitTimeOver = false;
			}
		} else { // start of timer
			if (fruitEaten) {
				fruitVisible = false;
			} else {
				fruitVisible = true;
			}
			// now when the timer finished the block will be returned to its previous state
			fruitTimeOver = true; 
			
		}
	}
	
	public int[] findNearestPill(int x, int y) {
		int[] coord = {-1, -1};
		
		for (int i = 0; i < 22; i++) {
			if (withinBounds(x, y + i) && map[y + i][x] == PELLET) {
				
				coord[0] = x;
				coord[1] = y + i;
				return coord;
				
			} else if (withinBounds(x + i, y + i) && map[y + i][x + i] == PELLET) {
				
				coord[0] = x + i;
				coord[1] = y + i;
				return coord;
			
			} else if (withinBounds(x + i, y) && map[y][x + i] == PELLET) {
				
				coord[0] = x + i;
				coord[1] = y;
				return coord;
			
			} else if (withinBounds(x + i, y - i) && map[y - i][x + i] == PELLET) {
				
				coord[0] = x + i;
				coord[1] = y - i;
				return coord;
			
			} else if (withinBounds(x, y - i) && map[y - i][x] == PELLET) {
				
				coord[0] = x;
				coord[1] = y - i;
				return coord;
			
			} else if (withinBounds(x - i, y - i) && map[y - i][x - i] == PELLET) {
				
				coord[0] = x - i;
				coord[1] = y - i;
				return coord;
			
			} else if (withinBounds(x - i, y) && map[y][x - i] == PELLET) {
				
				coord[0] = x - i;
				coord[1] = y;
				return coord;
			
			} else if (withinBounds(x - i, y + i) && map[y + i][x - i] == PELLET) {
				
				coord[0] = x - i;
				coord[1] = y + i;
				return coord;
			
			}
		}
		
		return coord;
	}
}

