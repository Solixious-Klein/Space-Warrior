import java.awt.image.BufferedImage;


public class Villain implements SpaceObject{
	private int speed;
	private int x, y, width, height, health;
	
	private BufferedImage image;
	
	private boolean isOnScreen;
	
	public Villain() {
		int type = (int)(Math.random()*2) + 1;
		if(type == 1) {
			image = SpriteSheets.image1.getSubimage(92, 188, 27, 22);
			height = 27;
			width = 22;
		}
		else if(type == 2){
			image = SpriteSheets.image1.getSubimage(92, 161, 27, 22);
			height = 27;
			width = 22;
		}
		speed = 1;
		x = (int)(Math.random()*512);
		y = 0;
		isOnScreen = true;
		health = 4;
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
		if(y > 512 + height) {
			isOnScreen = false;
		}
	}
	@Override
	public BufferedImage getImage() {
		return image;
	}
	
	public int getStrength() {
		return 3;
	}
	public boolean isOnScreen() {
		return isOnScreen;
	}
	public void damaged(int d) {
		health -= d;
		if(health <= 0) {
			isOnScreen = false;
		}
	}
}
