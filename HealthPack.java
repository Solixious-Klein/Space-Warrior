import java.awt.image.BufferedImage;


public class HealthPack implements SpaceObject {
	
	private int speed;
	private int x, y, width, height;
	private int currentImage;
	
	private boolean isOnScreen;
	
	private BufferedImage[] healthPack = new BufferedImage[6];
	
	public HealthPack() {
		isOnScreen = true;
		width = 14;
		height = 13;
		currentImage = 0;
		speed = 1;
		
		for(int i = 0; i < 6; i++) {
			healthPack[i] = SpriteSheets.image1.getSubimage(59, (height * i) + 194, width, height);
		}
		
		x = (int)(Math.random() * 512 + 1 - width);
		y = 1-height;
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
		y += speed;
		if(y%height == 0)
		currentImage = (currentImage + 1) % 6;
		if(y >= 512) {
			isOnScreen = false;
		}
	}
	@Override
	public BufferedImage getImage() {
		return healthPack[currentImage];
	}
	public boolean isOnScreen() {
		return isOnScreen;
	}
	public void removeFromScreen() {
		isOnScreen = false;
	}
}
