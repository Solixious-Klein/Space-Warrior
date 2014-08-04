import java.awt.image.BufferedImage;


public class VillainMissile1 implements SpaceObject {

	private double x, y;
	private int scX, scY;
	private int width, height;
	private BufferedImage missile;
	private static int speed;
	
	private double dx, dy, direction;
	
	private boolean isOnScreen;
	
	
	public VillainMissile1(int x, int y) {
		scX = SpaceWarriorPanel.sc.getX() + (int)(0.5 * SpaceWarriorPanel.sc.getWidth());
		scY = SpaceWarriorPanel.sc.getY() + (int)(0.5 * SpaceWarriorPanel.sc.getHeight());
		this.x = x;
		this.y = y;
		missile = SpriteSheets.image1.getSubimage(174, 218, 5, 5);
		width = 5;
		height = 5;
		dx = scX - x;
		dy = scY - y;
		if(dx != 0) {
			direction = Math.atan(dy / dx);
		}
		speed = 10;
		isOnScreen = true;
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
		if(dx > 0) {
			x = x + (speed * Math.cos(direction));
			y = y + (speed * Math.sin(direction));
		}
		else if (dx == 0){
			if(dy > 0)
				y += speed;
			else
				y -= speed;
		}
		
		if(dx < 0) {
			x = x - (speed * Math.cos(direction));
			y = y - (speed * Math.sin(direction));
		}
		
		if(x < 1 - width || x > SpaceWarrior.WIDTH || y < 1 - height || y > SpaceWarrior.HEIGHT) {
			isOnScreen = false;
		}
	}
	@Override
	public BufferedImage getImage() {
		return missile;
	}
	public boolean isOnScreen() {
		return isOnScreen;
	}
}
