package game;


import game.Player.Direction;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

/**
 * @author Gagondeep Srai
 */

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public static final String OS = System.getProperty("os.name");
	public static int WIDTH = 370;
	public static int HEIGHT = 430;
	public static final String WINDOW_TITLE = "Pac-Man";
	
	// these are platform specific window dimensions.
	// so the application looks consistent on all platforms.
	private int bgWidth = 0;
	private int bgHeight = 0;
	private int winWidth = 10;
	private int winHeight = 10;
	private int macWidth = 380;
	private int macHeight = 440;
	
	public JFrame frame;
	public Thread t;
	
	public boolean running = false;
	
	public InputHandler input;
	public Board board;
	public Player player;
	
	public Ghost ghost1;
	public Ghost ghost2;
	public Ghost ghost3;
	public Ghost ghost4;
	
	// data members related to the death of pacman
	public boolean dead = false;
	private long ToD = 0; // time of death
	
	public Map<String, Sprite> sprites = new HashMap<String, Sprite>();
	
	public Game() {
		setOS();
		this.init();
		running = true;
		t = new Thread(this);
		t.start();
	}
	
	private void setOS() {
		
		if (OS.startsWith("Windows")) {
			System.out.println("Running for Windows");
			bgWidth = WIDTH + winWidth;
			bgHeight = HEIGHT + winHeight;
		} else if (OS.startsWith("Mac")) {
			System.out.println("Running for Mac OS X");
			WIDTH = macWidth;
			HEIGHT = macHeight;
			bgWidth = WIDTH;
			bgHeight = HEIGHT;
		} else {
			System.out.println("Running for Linux");
			bgWidth = WIDTH;
			bgHeight = HEIGHT;
		}
	}
	
	public void run() {
		long lastTime = System.currentTimeMillis();
		int updates = 0;  // how many times update() is called
		int frames = 0; // how many times render() is called
		BufferStrategy bs = this.getBufferStrategy();
		
		long oldTime = System.currentTimeMillis();
		double deltaT = 1000 / 60D;
		long accumulator = 0;

		while(running) {
			long now = System.currentTimeMillis();
			long passed = (now - oldTime);
			oldTime = now;

			accumulator += passed;
			while(accumulator >= deltaT) {
				// animate at 60 frames per second
				updates++;
				update(deltaT);
				accumulator -= deltaT;
			}
			
			do {
				do {
					
					Graphics2D g = (Graphics2D)bs.getDrawGraphics();
					
					g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
							RenderingHints.VALUE_ANTIALIAS_ON);
					
					frames++;
					render(g);
					g.dispose();
					
				} while(bs.contentsRestored());
				
				bs.show();
				
			} while(bs.contentsLost());	
			
			if(System.currentTimeMillis() - lastTime > 1000) {
				lastTime += 1000;
				frames = 0;
				updates = 0;
			}
		}
	}
	
	public void init() {
		/*
		 * Keep the Canvas at one size, no resizing.
		 * Build the Window/JFrame.
		 */
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setIgnoreRepaint(true);
		
		frame = new JFrame(WINDOW_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setIgnoreRepaint(true);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack(); // sizes the frame, to the size set earlier.
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		this.createBufferStrategy(2);
		this.requestFocus();
		
		// Loading assets in the game thread (loop) slows the game down
		sprites.put("pacman", new Sprite("/pacman.jpg"));
		sprites.put("pacmanUp", new Sprite("/pacmanup.jpg"));
		sprites.put("pacmanDown", new Sprite("/pacmandown.jpg"));
		sprites.put("pacmanLeft", new Sprite("/pacmanleft.jpg"));
		sprites.put("pacmanRight", new Sprite("/pacmanright.jpg"));
		
		sprites.put("ghost1", new Sprite("/ghost10.jpg"));
		sprites.put("ghost2", new Sprite("/ghost20.jpg"));
		sprites.put("ghost3", new Sprite("/ghost30.jpg"));
		sprites.put("ghost4", new Sprite("/ghost40.jpg"));
		sprites.put("ghostPill", new Sprite("/ghostPill.jpg"));
		sprites.put("ghostE", new Sprite("/ghostEaten.jpg"));
		sprites.put("fruit", new Sprite("/fruit.jpg"));
		
		this.input = new InputHandler(this);
		this.board = new Board(20, sprites);
		
		this.ghost1 = new Ghost(this.board, this.sprites);
		this.ghost1.init();
		
		this.ghost2 = new Ghost(this.board, this.sprites);
		this.ghost2.init();
		this.ghost3 = new Ghost(this.board, this.sprites);
		this.ghost3.init();
		this.ghost4 = new Ghost(this.board, this.sprites);
		this.ghost4.init();
		
		
		this.player = new Player(this.board, this.sprites);
		this.player.init();
	}
	
	public void update(double dt) {
		/*
		 * dt is the change in time. it allows for
		 * frame rate independant animation. They game
		 * updates 60 times a second. This is synced with the 
		 * frame rate 60 fps, however sometimes you can get more
		 * or less than 60 fps. i used dt because it keeps the animation
		 * consistent no matter what the frames, so same on all systems.
		 * 60 fps means it takes 16.66667 milliseconds to load a new frame.
		 */
		if (input.up.isPressed()) {
			this.player.newDirection = Direction.UP;
		}
		
		if (input.down.isPressed()) {
			this.player.newDirection = Direction.DOWN;
		}
		
		if (input.left.isPressed()) {
			this.player.newDirection = Direction.LEFT;
		}
		
		if (input.right.isPressed()) {
			this.player.newDirection = Direction.RIGHT;
		}

		if (input.escape.isPressed()) {
			quit();
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}
		
		if (input.reset.isPressed()) {
			reset();
		}
		
		if (!dead) { // if pacman is not dead then update the game
			
			if (!this.player.hungry) {
				if (this.ghost1.chasing) {
					this.ghost1.chase(this.player.posX / board.blockSize, 
									  this.player.posY / board.blockSize,
									  this.player.direction, this.ghost1);
					this.ghost2.chase(this.player.posX / board.blockSize, 
									  this.player.posY / board.blockSize,
									  this.player.direction, this.ghost1);
					this.ghost3.chase(this.player.posX / board.blockSize, 
									  this.player.posY / board.blockSize,
									  this.player.direction, this.ghost1);
					this.ghost4.chase(this.player.posX / board.blockSize, 
									  this.player.posY / board.blockSize,
									  this.player.direction, this.ghost1);
				}
				
				// after the pacman is no longer energized, optimization
				this.ghost1.setNormal();
				this.ghost2.setNormal();
				this.ghost3.setNormal();
				this.ghost4.setNormal();
				
				// when pacman eats a pill again.
				if (this.player.eatenPill) {
					this.ghost1.postEaten = false;
					this.ghost2.postEaten = false;
					this.ghost3.postEaten = false;
					this.ghost4.postEaten = false;
					
					this.player.energizedTime = System.currentTimeMillis(); // start the energized timer
					this.player.hungry = true;
					this.player.eatenPill = false;
				}
				
			} else {
				if (!this.ghost1.postEaten) this.ghost1.setVulnerable();
				if (!this.ghost2.postEaten) this.ghost2.setVulnerable();
				if (!this.ghost3.postEaten) this.ghost3.setVulnerable();
				if (!this.ghost4.postEaten) this.ghost4.setVulnerable();
				
				// when pacman eats a pill again.
				if (this.player.eatenPill) {
					this.ghost1.postEaten = false;
					this.ghost2.postEaten = false;
					this.ghost3.postEaten = false;
					this.ghost4.postEaten = false;
					
					this.player.energizedTime = System.currentTimeMillis(); // start the energized timer
					this.player.hungry = true;
					this.player.eatenPill = false;
				}
			}
			
			this.ghost1.update(dt);
			this.ghost2.update(dt);
			this.ghost3.update(dt);
			this.ghost4.update(dt);
			this.player.update(dt);
			this.board.update(this.player.numPelletsEaten);
			
			// if the player collides with the ghost sprite or the scared ghost sprite
			boolean collision1 = this.player.AABBIntersect(this.sprites.get("ghost1"), 
									this.ghost1.posX, this.ghost1.posY) ||
									this.player.AABBIntersect(this.sprites.get("ghostPill"), 
											this.ghost1.posX, this.ghost1.posY);
			
			boolean collision2 = this.player.AABBIntersect(this.sprites.get("ghost2"), 
					this.ghost2.posX, this.ghost2.posY) ||
					this.player.AABBIntersect(this.sprites.get("ghostPill"), 
							this.ghost2.posX, this.ghost2.posY);
			
			boolean collision3 = this.player.AABBIntersect(this.sprites.get("ghost3"), 
					this.ghost3.posX, this.ghost3.posY) ||
					this.player.AABBIntersect(this.sprites.get("ghostPill"), 
							this.ghost3.posX, this.ghost3.posY);
			
			boolean collision4 = this.player.AABBIntersect(this.sprites.get("ghost4"), 
					this.ghost4.posX, this.ghost4.posY) ||
					this.player.AABBIntersect(this.sprites.get("ghostPill"), 
							this.ghost4.posX, this.ghost4.posY);
			
			if (collision1 || collision2 || collision3 || collision4) {
				if (this.player.hungry) {
					if (!ghost1.eaten && !ghost1.postEaten && collision1) { // if its not eaten or has not been eaten already.
						this.ghost1.setEaten();
						this.player.numGhostsEaten++;
						this.player.eatGhost();
					} else if (!ghost2.eaten && !ghost2.postEaten && collision2) {
						this.ghost2.setEaten();
						this.player.numGhostsEaten++;
						this.player.eatGhost();
					} else if (!ghost3.eaten && !ghost3.postEaten && collision3) {
						this.ghost3.setEaten();
						this.player.numGhostsEaten++;
						this.player.eatGhost();
					} else if (!ghost4.eaten && !ghost4.postEaten && collision4) {
						this.ghost4.setEaten();
						this.player.numGhostsEaten++;
						this.player.eatGhost();
					}
					
					if (ghost1.normalGhost && collision1) {
						ToD = System.currentTimeMillis();
						dead = true;
					}
					
					if (ghost2.normalGhost && collision2) {
						ToD = System.currentTimeMillis();
						dead = true;
					}
					
					if (ghost3.normalGhost && collision3) {
						ToD = System.currentTimeMillis();
						dead = true;
					}
					
					if (ghost4.normalGhost && collision4) {
						ToD = System.currentTimeMillis();
						dead = true;
					}
					
				} else { 
					// if your not energized
					//ToD = System.currentTimeMillis();
					//dead = true;
					if (ghost1.normalGhost && collision1) {
						ToD = System.currentTimeMillis();
						dead = true;
					}
					
					if (ghost2.normalGhost && collision2) {
						ToD = System.currentTimeMillis();
						dead = true;
					}
					
					if (ghost3.normalGhost && collision3) {
						ToD = System.currentTimeMillis();
						dead = true;
					}
					
					if (ghost4.normalGhost && collision4) {
						ToD = System.currentTimeMillis();
						dead = true;
					}
				}
			}
		} else {
			// this is executed 3 seconds after a collision
			if (System.currentTimeMillis() - ToD > 3000) {
				this.ghost1.kill();
				this.ghost2.kill();
				this.ghost3.kill();
				this.ghost4.kill();
				this.player.die();
				dead = false;
			}
		}		
	}
	
	public void render(Graphics2D g) {
		// draw the background
		g.setPaint(Color.BLACK);
		g.fillRect(0, 0, bgWidth, bgHeight);
		this.board.render(g);
		this.ghost1.render(g);
		this.ghost2.render(g);
		this.ghost3.render(g);
		this.ghost4.render(g);
		this.player.render(g);
		this.player.renderFont(g);
	}
	
	public void reset() {
		this.dead = false;
		this.board = new Board(20, sprites);
		this.player.reset(board);
		this.ghost1.reset(board);
		
		this.ghost2.reset(board);
		this.ghost3.reset(board);
		this.ghost4.reset(board);
	}
	
	public void quit() {
		running = false;
	}
}
