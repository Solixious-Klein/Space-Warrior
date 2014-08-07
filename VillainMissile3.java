import java.awt.image.BufferedImage;


public class VillainMissile3 implements SpaceObject {

	private double x, y;
	private int width, height;
	private int strength;
	private BufferedImage missile;
	private static int speed;
	
	private boolean isOnScreen;
	
	
	public VillainMissile3(int x, int y) {
		this.x = x;
		this.y = y;
		width = 13;
		height = 13;
		missile = SpriteSheets.image1.getSubimage(200, 215, width, height);
		speed = 10;
		isOnScreen = true;
		strength = 8;
	}
	
	@Override
	public int getX() {
		return (int)x;
	}
	@Override
	public int getY() {
		return (int)y;
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
	public void move() {
		if(!isOnScreen)
			return;
		
		y += speed;
		
		if(y > SpaceWarrior.HEIGHT) {
			isOnScreen = false;
		}
	}
	@Override
	public BufferedImage getImage() {
		return missile;
	}
	@Override
	public boolean isOnScreen() {
		return isOnScreen;
	}

	@Override
	public int getStrength() {
		return strength;
	}

	@Override
	public void damaged(int v) {
		isOnScreen = false;
	}
}
