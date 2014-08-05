import java.awt.image.BufferedImage;


public class Villain implements SpaceObject{
	private SpaceWarriorPanel swp;
	
	private int speed;
	private int x, y, width, height, health;
	private int itr;
	private int score;
	
	private BufferedImage image;
	
	private boolean isOnScreen;
	
	public Villain(SpaceWarriorPanel swp) {
		this.swp = swp;
		int type = (int)(Math.random()*2) + 1;
		if(type == 1) {
			width = 27;
			height = 22;
			image = SpriteSheets.image1.getSubimage(92, 188, width, height);
		}
		else if(type == 2){
			width = 27;
			height = 22;
			image = SpriteSheets.image1.getSubimage(92, 161, width, height);
		}
		speed = 3;
		x = (int)(Math.random() * SpaceWarrior.WIDTH);
		y = 0;
		isOnScreen = true;
		health = 4;
		score = 50;
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
		itr = (itr + 1) % 100;
		if(itr == 99) {
			fire();
		}
		if(y > SpaceWarrior.HEIGHT + height) {
			isOnScreen = false;
		}
	}
	@Override
	public BufferedImage getImage() {
		return image;
	}
	@Override
	public int getStrength() {
		return 3;
	}
	public int getScore() {
		return score;
	}
	@Override
	public boolean isOnScreen() {
		return isOnScreen;
	}
	@Override
	public void damaged(int d) {
		health -= d;
		if(health <= 0) {
			isOnScreen = false;
		}
	}
	public void fire() {
		VillainMissile1 v = new VillainMissile1(x + (int)(0.5 * width), y + (int)(0.5 * height));
		swp.addVillainMissile1(v);
	}
}
