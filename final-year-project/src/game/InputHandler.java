package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author Gagondeep Srai
 * 
 */

public class InputHandler implements KeyListener {
	
	public InputHandler(Game game) {
		game.addKeyListener(this);
	}
	
	public class Key {
		private boolean pressed = false;
		
		public boolean isPressed() {
			return pressed;
		}
		
		public void toggle(boolean isPressed) {
			pressed = isPressed;
		}
	}
	
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key escape = new Key();
	public Key reset = new Key();
	
	@Override
	public void keyPressed(KeyEvent e) {
		toggleKey(e.getKeyCode(), true);
	
	}

	@Override
	public void keyReleased(KeyEvent e) {
		toggleKey(e.getKeyCode(), false);
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// not used
	}
	
	/*
	 * toggleKey processes key presses, it accepts both
	 * WASD and Arrow Keys, which are the standard input  
	 * keys for 2D movement.
	 */
	public void toggleKey(int keyCode, boolean isPressed) {
		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W)
			up.toggle(isPressed);
		
		if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S)
			down.toggle(isPressed);
		
		if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A)
			left.toggle(isPressed);
		
		if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D)
			right.toggle(isPressed);
		
		if (keyCode == KeyEvent.VK_ESCAPE)
			escape.toggle(isPressed);
		
		if (keyCode == KeyEvent.VK_R)
			reset.toggle(isPressed);
	}
	
}

