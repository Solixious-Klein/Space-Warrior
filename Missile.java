import java.awt.image.BufferedImage;


public class Missile implements SpaceObject{
	
	public final static int LOW = 1;
	public final static int MEDIUM = 3;
	public final static int HIGH = 5;
	
	private static int SPEED = 10;
	private int x, y, width, height;
	private int strength;
	
	private BufferedImage missile;
	
	private boolean isOnScreen;
	
	public Missile(int x, int y, int strength) {
		this.x = x;
		this.y = y;
		this.strength = strength;
		
		isOnScreen = true;
		
		if(strength == LOW) {
			width = 5;
			height = 5;
			missile = SpriteSheets.image1.getSubimage(108, 218, width, height);
		}
		else if(strength == MEDIUM) {
			width = 8;
			height = 8;
			missile = SpriteSheets.image1.getSubimage(121, 217, width, height);
		}
		else if(strength == HIGH) {
			width = 13;
			height = 13;
			missile = SpriteSheets.image1.getSubimage(150, 215, width, height);
		} 
	}
	
	@Override
	public int getX() {
		return x;
	}
	@Override
	public int getY() {
		return y;
	}
	@Override
	public int getWidth() {
		return width;
	}
	@Override
	public int getHeight() {
		return height;
	}
	@Override
	public BufferedImage getImage() {
		return missile;
	}
	@Override
	public void move() {
		if(!isOnScreen)
			return;
		y -= SPEED;
		if(y <= -height) {
			isOnScreen = false;
		}
	}
	
	
	public int getStrength() {
		return strength;
	}
	public boolean isOnScreen() {
		return isOnScreen;
	}
	public void removeFromScreen() {
		isOnScreen = false;
	}
}
