import java.awt.image.BufferedImage;


public class Star implements SpaceObject {
	public final static int FAR = 1;
	public final static int MEDIUM = 2;
	public final static int NEAR = 3;
	
	private int speed;
	private int x, y, width, height;
	private int type;
	
	private BufferedImage star;
	
	private boolean isOnScreen;
	
	public Star() {
		type = (int)(Math.random()*2+1);
		isOnScreen = true;
		
		if(type == FAR) {
			width = 32;
			height = 32;
			star = SpriteSheets.image1.getSubimage(482, 29, width, height);
		}
		else if(type == MEDIUM) {
			width = 40;
			height = 40;
			star = SpriteSheets.image1.getSubimage(478, 66, width, height);
		}
		else if(type == NEAR) {
			width = 52;
			height = 56;
			star = SpriteSheets.image1.getSubimage(472, 118, width, height);
		}
		x = (int)(Math.random() * SpaceWarrior.WIDTH + 1);
		y = 1-height;
		
		speed = type;
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
		return star;
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
	
	public int getType() {
		return type;
	}
	@Override
	public boolean isOnScreen() {
		return isOnScreen;
	}
	@Override
	public int getStrength() {
		return 0;
	}
	@Override
	public void damaged(int v) {
		
	}
}
