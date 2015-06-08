package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * @author Gagondeep Srai
 * 
 */

public class Sprite {
	
	public String path;
	public BufferedImage image;
	public int x;
	public int y;
	public int width;
	public int height;
	
	public Sprite(String path) {
		this.path = path;
		
		try {
			this.image = ImageIO.read(Sprite.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.width = image.getWidth();
		this.height = image.getHeight();
		
	}
	
	public void render(Graphics2D g, int x, int y) {
		g.drawImage(image, null, x, y);
	}
	
	public BufferedImage getSprite() {
		return this.image;
	}
	
}

