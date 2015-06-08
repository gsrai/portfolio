package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Gagondeep Srai
 * 
 */

public class Ghost {
	
	public enum Direction {
		UP, DOWN, LEFT, RIGHT;
	}
	
	public static int ghostNum = 0;
	private int ghosty; // save the ghostNum locally
	private Board board;
	public int posX, posY;
	private Direction direction;
	private double velocity = 1D;
	private double speed = 1D;
	private boolean wall;
	private Direction newDirection;
	
	public boolean frightened;
	public boolean eaten;
	public boolean postEaten; // has it already been eaten
	public boolean normalGhost;
	public boolean reversed; // when the ghost reverse, call once 
	
	public boolean chasing;
	private long changeTime;
	private int interval; // the modes change in different intervals
	private int cycle;
	public boolean pauseTimer;
	
	// target system
	public int targetX;
	public int targetY;
	
	private Map<String, Sprite> sprites;
	private BufferedImage currentSprite;
	private String sprite;
	
	public Ghost(Board b, Map<String, Sprite> s) {
		this.board = b;
		this.sprites = (HashMap<String, Sprite>)s;
	}
	
	public void init() {
		ghostNum++;
		ghosty = ghostNum;
		sprite = "ghost" + ghosty;
		this.direction = Direction.UP; 
		this.newDirection = Direction.LEFT;
		this.wall = false;
		frightened = false;
		eaten = false;
		reversed = false;
		normalGhost = true;
		chasing = false;
		postEaten = false;
		this.getInitPos();
		changeTime = System.currentTimeMillis();
		interval = 7000;
		cycle = 0;
		pauseTimer = false;
		scatter();
	}
	
	public void reset(Board b) {
		this.board = b;
		sprite = "ghost" + ghosty;
		this.direction = Direction.UP; 
		this.newDirection = Direction.LEFT;
		this.wall = false;
		frightened = false;
		eaten = false;
		reversed = false;
		normalGhost = true;
		chasing = false;
		postEaten = false;
		this.getInitPos();
		changeTime = System.currentTimeMillis();
		interval = 7000;
		cycle = 0;
		pauseTimer = false;
		scatter();
	}
	
	public void update(double dt) {
		if (eaten) { // go home
			if (isInDen()) {
				eaten = false;
				setNormal();
			}
		} else if (frightened) { 
			velocity = speed * dt/1600;
			if (isIntersection() || wall) randND();
			move();
		}
		
		if (!frightened) {
			if (eaten) {
				velocity = 1;
				targetTile(8, 8);
			} else {
				velocity = speed * dt/1600;
				chooseMode();
				if (!chasing) scatter();
				targetTile(targetX, targetY);
			}
			
			move();
		}
	}
	
	public void render(Graphics2D g) {
		
		if (frightened) {
			this.currentSprite = sprites.get("ghostPill").getSprite();
		} else if(eaten) {
			this.currentSprite = sprites.get("ghostE").getSprite();
		} else {
			this.currentSprite = sprites.get(sprite).getSprite();
		}
		
		g.drawImage(currentSprite, null, posX, posY);
	}
	
	public void kill() {
		board.setBlock(9, 9, Board.DEN);
		eaten = false;
		setNormal();
		chasing = false;
		this.direction = Direction.UP; 
		this.newDirection = Direction.LEFT;
		getInitPos();
		changeTime = System.currentTimeMillis();
		interval = 7000;
		cycle = 0;
		scatter();
	}
	
	public void setVulnerable() {
		if (!eaten) {
			if (!reversed) { // makes ghost reverse once not indefinately
				reverse(); // direction aka run away 
				reversed = true;
			}
			postEaten = false;
			frightened = true;
			normalGhost = false;
		}		
	}
	
	public void setEaten() {
		eaten = true;
		frightened = false;
		normalGhost = false;
		postEaten = true;
	}
	
	
	public void setNormal() {
		if (!eaten) {
			postEaten = true;
			frightened = false;
			normalGhost = true;
		}
	}
	
	private boolean isInDen() {
		int x = posX / board.blockSize;
		int y = posY / board.blockSize;
		
		if (inBlock(posX, posY)) {
			//return board.getBlock(x, y) == Board.DEN;
			return x == 8 && y == 8;
		}
		return false;
	}
	
	public void getInitPos() {	
		int startPosY = 0;
		int startPosX = 0;

		if (ghosty == 1) {
			startPosY = 9;
			startPosX = 9;
		} else {
			startPosY = 10;
			startPosX = 6 + ghosty;
		}
		
		for (int i = 0; i < board.arrY; i++) {
			for (int j = 0; j < board.arrX; j++) {
				if (board.map[i][j] == Board.DEN && i == startPosY && j == startPosX) {
					this.posX = j * board.blockSize;
					this.posY = i * board.blockSize;
				}
			}
		}
	}
	
	public void scatter() {
		switch(ghosty) {
			case 1:
				targetX = 0; 
				targetY = 0;
				break;
			case 2:
				targetX = 0; 
				targetY = 22;
				break;
			case 3:
				targetX = 18; 
				targetY = 0;
				break;
			case 4:
				targetX = 18; 
				targetY = 22;
				break;
		}
	}
	
	public void chase(int playerX, int playerY, Player.Direction playerDir, Ghost blinky) {
		switch(ghosty) {
			case 1:
				targetX = playerX; 
				targetY = playerY;
				break;
			case 2:
				if (Math.abs(calcEuclideanDistance(playerX, playerY)) > 4) {
					targetX = playerX; 
					targetY = playerY;
				} else {
					targetX = 0; 
					targetY = 22;
				}
				break;
			case 3:
				int bx = blinky.posX / board.blockSize;
				int by = blinky.posY / board.blockSize;
				int xx = playerX; // 2 steps ahead of pacman
				int yy = playerY;
				
				if (playerDir == Player.Direction.UP)
					yy = playerY - 2;
				else if (playerDir == Player.Direction.DOWN)
					yy = playerY + 2;
				else if (playerDir == Player.Direction.LEFT)
					xx = playerX - 2;
				else if (playerDir == Player.Direction.RIGHT)
					xx = playerX + 2;
				
				targetX = bx + (distance(xx, bx) * 2); 
				targetY = by + (distance(yy, by) * 2);

				break;
			case 4:
				targetX = playerX; 
				targetY = playerY;
				if (playerDir == Player.Direction.UP)
					targetY = playerY - 4;
				else if (playerDir == Player.Direction.DOWN)
					targetY = playerY + 4;
				else if (playerDir == Player.Direction.LEFT)
					targetX = playerX - 4;
				else if (playerDir == Player.Direction.RIGHT)
					targetX = playerX + 4;

				break;
		}
	}
	
	private double calcEuclideanDistance(int px, int py) {
		int gx = posX / board.blockSize;
		int gy = posY / board.blockSize;
		
		return Math.sqrt(Math.pow((gx - px), 2) + Math.pow((gy - py), 2));
	}
	
	// methods related to movement
	private boolean isValidDirection() {
		int x = posX / board.blockSize;
		int y = posY / board.blockSize;
		
		//System.out.println("x " + x + " y " + y);
		
		if (newDirection == Direction.UP) {
			return !board.isWall(x, y - 1);
		}
		
		if (newDirection == Direction.DOWN) {
			return !board.isWall(x, y + 1);
		}
		
		if (newDirection == Direction.LEFT) {
			return !board.isWall(x - 1, y);
		}
		
		if (newDirection == Direction.RIGHT) {
			return !board.isWall(x + 1, y);
		}
		
		return false;
	}
	
	private boolean isCoordWhole(int n) {
		return n % board.blockSize == 0;
	}
	
	/*
	 * Has pacman reached the origin of a grid block.
	 */
	private boolean inBlock(int x, int y) {
		return isCoordWhole(x) && isCoordWhole(y);
	}
	
	private boolean inSamePlane(Direction od, Direction nd) {
		
		if (od == Direction.LEFT && nd == Direction.RIGHT)
			return true;
		
		if (od == Direction.RIGHT && nd == Direction.LEFT)
			return true;
		
		if (od == Direction.UP && nd == Direction.DOWN)
			return true;
		
		if (od == Direction.DOWN && nd == Direction.UP)
			return true;
		
		return false;
	}
	
	private boolean inTunnel() {
		int x = posX / board.blockSize;
		int y = posY / board.blockSize;
		// call when in block
		// if going into left tunnel
		if ((x == 0 && y == 10)) {
			return true;
		}
		
		if ((x == 18 && y == 10)) {
			return true;
		}
		
		return false;
	}
	
	public void move() {
		int x = posX / board.blockSize;
		int y = posY / board.blockSize;
		
		if (inSamePlane(direction, newDirection) && isValidDirection()) {
	
			direction = newDirection;
			
		} else if (inBlock(posX, posY) && inTunnel()) {
			/*
			 * i used 19 so that the player teleports
			 * right outside the tunnel exit without
			 * returning true for inTunnel(). As inTunnel()
			 * checks if your outside the enterance and after
			 * teleporting your technically still outside the
			 * enterance and therefore you will never move.
			 */
			if(direction == Direction.LEFT)
				posX = 17 * board.blockSize + 19; 
			
			if(direction == Direction.RIGHT)
				posX = 1 * board.blockSize - 19;
			
		} else if (inBlock(posX, posY)) {
			// only when in a block can you change direction
			if (newDirection != direction && isValidDirection()) {
				direction = newDirection; 
			}
			// when in a block check if its safe to move, else dont
			// if the current direction is up, down, left or right
			if (direction == Direction.UP) {
				if (!board.isWall(x, y - 1)) {
					posY -= velocity;
				} else {
					wall = true;
				}
			}
			
			if (direction == Direction.DOWN) {
				if (!board.isWall(x, y + 1)) {
					posY += Math.ceil(velocity /* * 2*/);
				} else {
					wall = true;
				}
			}
			
			if (direction == Direction.LEFT) {
				if (!board.isWall(x - 1, y)) {
					posX -= velocity;
				} else {
					wall = true;
				}
			}
			
			if (direction == Direction.RIGHT) {
				if (!board.isWall(x + 1, y)) {
					posX += Math.ceil(velocity /* * 2*/); // double because movement looks slower imo
				} else {
					wall = true;
				}
			}
			
		} else {
			if (direction == Direction.UP) {
				posY -= velocity;
			}
			
			if (direction == Direction.DOWN) {
				posY += Math.ceil(velocity /* * 2*/);
			}
			
			if (direction == Direction.LEFT) {
				posX -= velocity;
			}
			
			if (direction == Direction.RIGHT) {
				posX += Math.ceil(velocity /* * 2*/); // double because movement looks slower imo
			}
		}
	}
	
	// random new direction
	public void randND() {
		Random rand = new Random();
		
		int r = rand.nextInt(4);

		switch(r) {
			case 0:
				if (direction != Direction.DOWN) { // can't backtrack
					newDirection = Direction.UP;
					wall = false; // check for the wall in the new direction
				}
				break;
			case 1:
				if (direction != Direction.UP) {
					newDirection = Direction.DOWN;
					wall = false;
				}
				break;
			case 2:
				if (direction != Direction.RIGHT) {
					newDirection = Direction.LEFT;
					wall = false;
				}
				break;
			case 3:
				if (direction != Direction.LEFT) {
					newDirection = Direction.RIGHT;
					wall = false;
				}
				break;
		}
	}
	
	
	public void reverse() {
		if (direction == Direction.UP) {
			newDirection = Direction.DOWN;
		}
		
		if (direction == Direction.DOWN) {
			newDirection = Direction.UP;
		}
		
		if (direction == Direction.LEFT) {
			newDirection = Direction.RIGHT;
		}
		
		if (direction == Direction.RIGHT) {
			newDirection = Direction.LEFT; 
		}
	}
	
	private boolean isIntersection() {
		int x = posX / board.blockSize;
		int y = posY / board.blockSize;
		
		if (inBlock(posX, posY)) {
			if (direction == Direction.UP) {
				return board.isSpaceFriendly(x - 1, y - 1) && 
					   board.isSpaceFriendly(x + 1, y - 1);
			}
			
			if (direction == Direction.DOWN) {
				return board.isSpaceFriendly(x - 1, y + 1) &&
					   board.isSpaceFriendly(x + 1, y + 1);
			}
			
			if (direction == Direction.LEFT) {
				return board.isSpaceFriendly(x - 1, y - 1) &&
					   board.isSpaceFriendly(x - 1, y + 1);
			}
			
			if (direction == Direction.RIGHT) {
				return board.isSpaceFriendly(x + 1, y - 1) &&
					   board.isSpaceFriendly(x + 1, y + 1);
			}
		}
		
		return false;
	}
	

	
	public void targetTile(int tx, int ty) {
		// logic to travel to a specific tile, shortest path
		int x = posX / board.blockSize;
		int y = posY / board.blockSize;
		
		int currentShortestPath = 1000; 
		
		/*
		 * target tile, checks 1 tile ahead, and changes
		 * the direction of movement depending on the 
		 * shortest path. you cannot check behind you
		 * other wise you will infinitely backtrack.
		 */
		if (inBlock(posX, posY)) {
			if (direction == Direction.UP) {
				if (!board.isWall(x, y - 1)) {
					// check shortest path?
					int newY = y - 1;
					int newShortestPath = distance(tx, x) + distance(ty, newY);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						newDirection = Direction.UP;
					}
				}
				
				if (!board.isWall(x - 1, y)) {
					int newX = x - 1;
					int newShortestPath = distance(tx, newX) + distance(ty, y);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						newDirection = Direction.LEFT;
					}
				}
				
				if (!board.isWall(x + 1, y)) {
					int newX = x + 1;
					int newShortestPath = distance(tx, newX) + distance(ty, y);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						newDirection = Direction.RIGHT;
					}
				}
			}
			
			if (direction == Direction.DOWN) {
				if (!board.isWall(x, y + 1)) {
					int newY = y + 1;
					int newShortestPath = distance(tx, x) + distance(ty, newY);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						newDirection = Direction.DOWN;
					}
				}
				
				if (!board.isWall(x - 1, y)) {
					int newX = x - 1;
					int newShortestPath = distance(tx, newX) + distance(ty, y);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						newDirection = Direction.LEFT;
					}
				}
				
				if (!board.isWall(x + 1, y)) {
					int newX = x + 1;
					int newShortestPath = distance(tx, newX) + distance(ty, y);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						newDirection = Direction.RIGHT;
					}
				}
			}
			
			if (direction == Direction.LEFT) {
				if (!board.isWall(x, y - 1)) {
					// check shortest path?
					int newY = y - 1;
					int newShortestPath = distance(tx, x) + distance(ty, newY);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						newDirection = Direction.UP;
					}
				}
				
				if (!board.isWall(x, y + 1)) {
					int newY = y + 1;
					int newShortestPath = distance(tx, x) + distance(ty, newY);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						newDirection = Direction.DOWN;
					}
				}
				
				if (!board.isWall(x - 1, y)) {
					int newX = x - 1;
					int newShortestPath = distance(tx, newX) + distance(ty, y);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						newDirection = Direction.LEFT;
					}
				}
			}
			
			if (direction == Direction.RIGHT) {
				if (!board.isWall(x, y - 1)) {
					// check shortest path?
					int newY = y - 1;
					int newShortestPath = distance(tx, x) + distance(ty, newY);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						newDirection = Direction.UP;
					}
				}
				
				if (!board.isWall(x, y + 1)) {
					int newY = y + 1;
					int newShortestPath = distance(tx, x) + distance(ty, newY);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						newDirection = Direction.DOWN;
					}
				}
				
				if (!board.isWall(x + 1, y)) {
					int newX = x + 1;
					int newShortestPath = distance(tx, newX) + distance(ty, y);
					if (newShortestPath < currentShortestPath) {
						//change direction
						currentShortestPath = newShortestPath;
						newDirection = Direction.RIGHT;
					}
				}
			}
		}
	}
	
	private int distance(int n, int m) {
		return Math.abs(n - m);
	}
	
	public void chooseMode() {
		
		if (System.currentTimeMillis() - changeTime > interval && !pauseTimer) {
			
			changeTime = System.currentTimeMillis();
			chasing = (chasing) ? false : true;
		
			if (chasing) cycle++;
			
			switch(cycle) {
				case 1:
					interval = (chasing) ? 20000 : 7000;
					break;
				case 2:
					interval = (chasing) ? 20000 : 7000;
					break;
				case 3:
					interval = (chasing) ? 20000 : 5000;
					break;
				case 4:
					interval = (chasing) ? Integer.MAX_VALUE : 5000;
					break;
			}
			
			reverse(); // makes ghost reverse (see update)
			
			// to stop the ghosts from going back into the den
			board.setBlock(9, 9, Board.WALL);
		}
	}
	
}


