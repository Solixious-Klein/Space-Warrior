import java.awt.image.BufferedImage;


public class SpaceBomb implements SpaceObject {
	
	private static final int SPEED_OF_ROTATION = 10;
	private static final int SPEED = 2;
	
	private int x, y, height, width;
	private int health, score, strength;
	private int currentX, currentY;
	private int updateCounter;
	
	private boolean isOnScreen;
	
	public BufferedImage bombImages[][] = new BufferedImage[16][2];
	
	public SpaceBomb() {
		health = 10;
		strength = 8;
		score = 100;
		
		x = (int)(Math.random() * SpaceWarrior.WIDTH + 1);
		y = 0;
		
		height = 28;
		width = 29;
		for(int i = 0; i < 16; i++) {
			bombImages[i][0] = SpriteSheets.image1.getSubimage(46 + (i * width), 360, width, height);
		}
		for(int i = 0; i < 16; i++) {
			bombImages[i][1] = SpriteSheets.image1.getSubimage(46 + (i * width), 405, width, height);
		}
		currentX = 0;
		currentY = 0;
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
	public void damaged(int d) {
		health -= d;
		if(health <= 0) {
			health = 0;
			isOnScreen = false;
		}
	}
	@Override
	public void move() {
		updateCounter = (updateCounter + 1) % SPEED_OF_ROTATION;
		if(updateCounter == 0) {
			currentX = (currentX + 1) % 16;
			currentY = (currentY + 1) % 2;
		}
		if(x < SpaceWarriorPanel.sc.getX() && y < SpaceWarriorPanel.sc.getY()) {
			x = x + (SPEED);
		}
		if(x > SpaceWarriorPanel.sc.getX()  && y < SpaceWarriorPanel.sc.getY()) {
			x = x - (SPEED);
		}
		y += SPEED;
	}
	@Override
	public BufferedImage getImage() {
		return bombImages[currentX][currentY];
	}
	public int getStrength() {
		return strength;
	}
	public int getScore() {
		return score;
	}
	
	public boolean isOnScreen() {
		return isOnScreen;
	}
}
