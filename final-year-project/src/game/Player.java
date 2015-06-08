package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gagondeep Srai
 * 
 */

public class Player {
	
	public enum Direction {
		UP, RIGHT, DOWN, LEFT;
	}
	
	private Board board;
	public int posX, posY;
	public Direction direction;
	public boolean hungry; // if pacman can eat ghosts or not.
	public boolean eatenPill;
	public int pillEatenCount;
	public long energizedTime;
	private int score;
	private int lives;
	private double velocity = 1D;
	
	public int numPelletsEaten;
	public int numGhostsEaten;
	public boolean eatenFruit;
	public Direction newDirection;
	public boolean stopped;
	
	private Map<String, Sprite> sprites;
	private BufferedImage currentSprite;
	private int animationSpeed = 128;
	private int dtSum = 0; // sum of the change in time.
	
	
	public Player(Board b, Map<String, Sprite> s) {
		this.board = b;
		this.sprites = (HashMap<String, Sprite>)s;
	}
	
	public void init() {
		this.lives = 3;
		this.score = 0;
		this.direction = Direction.LEFT; // initial direction of player
		this.newDirection = Direction.LEFT;
		this.hungry = false; // player initially is not energized
		this.eatenPill = false;
		this.pillEatenCount = 0;
		this.numPelletsEaten = 0;
		this.numGhostsEaten = 0;
		this.eatenFruit = false;
		this.stopped = false;
		this.getInitPos();
		// remove the pacman start flag from the map.
		this.board.setBlock(posX/board.blockSize, posY/board.blockSize, Board.EMPTY);
		
	}
	
	public void reset(Board b) {
		this.board = b;
		init();
	}
	
	public void update(double dt) {
		if(hungry) velocity = 2;
		else velocity = dt/20;
		move();
		eatPellet();
		eatPill();
		eatFruit();
		animate(dt);
		energizedTimer();
	}
	
	public void render(Graphics2D g) {
		g.drawImage(currentSprite, null, posX, posY);
	}
	
	public void renderFont(Graphics2D g) {
		
		String s1 = "Created by Gagondeep Srai";
		String s2 = "Score: " + getScore();
		String s3 = "Lives: " + getLives();
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		g.drawString(s2, 20, 15);
		g.drawString(s3, 305, 15);
		g.setFont(new Font("Monospaced", Font.BOLD, 12));
		g.drawString(s1, 105, 434);
	}
	
	public void addScore(int score) {
		this.score += score;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public void loseLife() {
		this.lives -= 1;
	}
	
	public int getLives() {
		return this.lives;
	}
	
	public void getInitPos() {	
		for (int i = 0; i < board.arrY; i++) {
			for (int j = 0; j < board.arrX; j++) {
				if (board.map[i][j] == Board.PACMAN_START) {
					this.posX = j * board.blockSize;
					this.posY = i * board.blockSize;
				}
			}
		}
	}
	
	// methods related to collision
	public boolean AABBIntersect(Sprite s, int x, int y) {
		return (this.posX < x + s.width) && 
			   (x < this.posX + this.sprites.get("pacman").width) &&
			   (this.posY < y + s.height) &&
			   (y < this.posY + this.sprites.get("pacman").height);
	}
	
	/*
	 * when pacman dies he loses 1 life and
	 * his position is reset back to his start
	 * position, as well as his direction being
	 * reset to his inital direction.
	 */
	public void die() {
		loseLife();
		this.posX = 9 * board.blockSize;
		this.posY = 16 * board.blockSize;
		this.direction = Direction.LEFT; // initial direction of player
		this.newDirection = Direction.LEFT;
	}
	
	// methods related to movement
	private boolean isValidDirection() {
		int x = posX / board.blockSize;
		int y = posY / board.blockSize;
		
		if (newDirection == Direction.UP) 
			return board.isSpaceFriendly(x, y - 1);
		
		if (newDirection == Direction.DOWN) 
			return board.isSpaceFriendly(x, y + 1);
		
		if (newDirection == Direction.LEFT) 
			return board.isSpaceFriendly(x - 1, y);
		
		if (newDirection == Direction.RIGHT) 
			return board.isSpaceFriendly(x + 1, y);
		
		return false;
	}
	
	private boolean isCoordWhole(int n) {
		return n % board.blockSize == 0;
	}
	
	/*
	 * Has pacman reached the origin of a grid block.
	 */
	public boolean inBlock(int x, int y) {
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
		if ((x == 0 && y == 10)) return true;
		
		if ((x == 18 && y == 10)) return true;
		
		return false;
	}
	
	public void move() {
		int x = posX / board.blockSize;
		int y = posY / board.blockSize;
		
		if (inSamePlane(direction, newDirection) && isValidDirection()) {
			direction = newDirection;
			
		} else if (inBlock(posX, posY) && inTunnel()) {
			
			/*
			 * i used 18 so that the player teleports
			 * right outside the tunnel exit without
			 * returning true for inTunnel(). As inTunnel()
			 * checks if your outside the enterance and after
			 * teleporting your technically still outside the
			 * enterance and therefore you will never move.
			 */
			if(direction == Direction.LEFT)
				posX = 17 * board.blockSize + 18; 
			
			if(direction == Direction.RIGHT)
				posX = 1 * board.blockSize - 18;
			
		} else if (inBlock(posX, posY)) {
			
			// only when in a block can you change direction
			if (newDirection != direction && isValidDirection()) {
				direction = newDirection;
			}
			
			// when in a block check if its safe to move, else dont
			// if the current direction is up, down, left or right
			if (direction == Direction.UP) {
				if (!board.isSpaceFriendly(x, y - 1)) {
					stopped = true;
				} else {
					stopped = false;
				}
				
				if (!stopped)
					posY -= Math.ceil(velocity);;
			}
			
			if (direction == Direction.DOWN) {
				if (!board.isSpaceFriendly(x, y + 1)) {
					stopped = true;
				} else {
					stopped = false;
				}
				
				if (!stopped)
					posY += Math.ceil(velocity);;
			}
			
			if (direction == Direction.LEFT) {
				if (!board.isSpaceFriendly(x - 1, y)) {
					stopped = true;
				} else {
					stopped = false;
				}
				
				if (!stopped)
					posX -= Math.ceil(velocity);;
			}
			
			if (direction == Direction.RIGHT) {
				if (!board.isSpaceFriendly(x + 1, y)) {
					stopped = true;
				} else {
					stopped = false;
				}
								
				if (!stopped)
					posX += Math.ceil(velocity); // double because movement looks slower imo
			}
			
		} else {
			
			if (direction == Direction.UP) {
				posY -= Math.ceil(velocity);;
			}
			
			if (direction == Direction.DOWN) {
				posY += Math.ceil(velocity);
			}
			
			if (direction == Direction.LEFT) {
				posX -= Math.ceil(velocity);
			}
			
			if (direction == Direction.RIGHT) {
				posX += Math.ceil(velocity); // double because movement looks slower
			}
		}
	}
	
	public void eatPellet() {
		int x = posX / board.blockSize;
		int y = posY / board.blockSize;
		
		if (inBlock(posX, posY)) {
			if (board.getBlock(x, y) == Board.PELLET) {
				this.addScore(10);
				numPelletsEaten++;
				board.setBlock(x, y, Board.EMPTY);
			}
		}
	}
	
	public void eatPill() {
		int x = posX / board.blockSize;
		int y = posY / board.blockSize;
		
		if (inBlock(posX, posY)) {
			if (board.getBlock(x, y) == Board.PILL) {
				this.addScore(50);
				board.setBlock(x, y, Board.EMPTY);
				this.eatenPill = true;
				this.pillEatenCount++;
				this.numGhostsEaten = 0; // every time Pacman eats a pill, reset the ghosts eat counter
				
			}
		}
	}
	
	public void eatFruit() {
		int x = posX / board.blockSize;
		int y = posY / board.blockSize;
		
		if (inBlock(posX, posY)) {
			if (board.getBlock(x, y) == Board.FRUIT) {
				this.addScore(2000);
				board.setBlock(x, y, Board.EMPTY);
				board.fruitEaten = true;
			}
		}
	}
	
	public void eatGhost() {
		if (numGhostsEaten == 1)
			this.addScore(200);	
		if (numGhostsEaten == 2)
			this.addScore(400);	
		if (numGhostsEaten == 3)
			this.addScore(800);	
		if (numGhostsEaten == 4)
			this.addScore(1600);	
	}
	
	public void animate(double dt) {
		dtSum += Math.floor(dt);
		
		if (dtSum == animationSpeed) {
			currentSprite = sprites.get("pacman").getSprite();
		} else if (stopped) {
			currentSprite = sprites.get("pacman").getSprite();
		} else  if (dtSum == animationSpeed * 2){
			if (direction == Direction.UP) {
				currentSprite = sprites.get("pacmanUp").getSprite();
			}
			
			if (direction == Direction.DOWN) {
				currentSprite = sprites.get("pacmanDown").getSprite();
			}
			
			if (direction == Direction.LEFT) {
				currentSprite = sprites.get("pacmanLeft").getSprite();
			}
			
			if (direction == Direction.RIGHT) {
				currentSprite = sprites.get("pacmanRight").getSprite();
			}
		}
		
		if (dtSum == 256) {
			dtSum = 0;
		}
	}
	
	public void energizedTimer() {
		if (System.currentTimeMillis() - energizedTime > 10000) this.hungry = false;
	}
}

