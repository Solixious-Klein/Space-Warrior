import java.awt.image.BufferedImage;


public class Shield implements SpaceObject{
	
	private int speed;
	private int x, y, width, height;
	
	private BufferedImage shield;
	
	private boolean isOnScreen;
	
	public Shield() {
		height = 17;
		width = 29;
		shield = SpriteSheets.image1.getSubimage(244, 239, width, height);
		speed = 1;
		x = (int)(Math.random() * SpaceWarrior.WIDTH + 1);
		y = 1-height;
		isOnScreen = true;
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
	public void move() {
		if(!isOnScreen)
			return;
		y += speed;
		if(y >= SpaceWarrior.HEIGHT) {
			isOnScreen = false;
		}
	}
	@Override
	public BufferedImage getImage() {
		return shield;
	}
	
	
	public boolean isOnScreen() {
		return isOnScreen;
	}
}
