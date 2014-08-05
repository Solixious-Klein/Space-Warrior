import java.awt.image.BufferedImage;


public class Asteroid implements SpaceObject {
	private static final int MAX_SPEED = 6;
	
	private int rotationSpeedLimiter = 10;
	private int speedOfRotation;
	
	private int speed;
	private int x, y, width, height;
	private int type;
	private int strength, health;
	private int score;
	private int updateCounter;
	
	private BufferedImage[] asteroids = new BufferedImage[8];

	private int currentImage;
	private boolean isOnScreen;
	
	public Asteroid() {
		type = (int)(Math.random()*12+1);
		isOnScreen = true;
		
		if(type >= 1 && type <= 4) {
			if(type == 1) {
				asteroids = getImages(60, 15, 22, 18);
				width = 22;
				height = 18;
			}
			else if(type == 2) {
				asteroids = getImages(60, 42, 22, 18);
				width = 22;
				height = 18;
			}
			else if(type == 3) {
				asteroids = getImages(64, 66, 22, 18);
				width = 22;
				height = 18;
			}
			else if(type == 4) {
				asteroids = getImages(72, 90, 21, 18);
				width = 21;
				height = 18;
			}
			strength = 2;
			health = 2;
		}
		else if(type >= 5 && type <= 7) {
			if(type == 5) {
				asteroids = getImages(251, 13, 34, 34);
				width = 33;
				height = 34;
			}
			else if(type == 6) {
				asteroids = getImages(271, 55, 31, 30);
				width = 31;
				height = 30;
			}
			else if(type == 7) {
				asteroids = getImages(258, 89, 34, 31);
				width = 34;
				height = 31;
			}
			strength = 3;
			health = 3;
		}
		else if(type >= 8 && type <= 10) {
			if(type == 8) {
				asteroids = getImages(128, 157, 46, 60);
				width = 46;
				height = 60;
			}
			else if(type == 9) {
				asteroids = getImages(98, 214, 51, 62);
				width = 51;
				height = 62;
			}
			else if(type == 10) {
				asteroids = getImages(100, 279, 50, 56);
				width = 50;
				height = 56;
			}
			strength = 5;
			health = 5;
		}
		else if(type == 11 || type == 12) {
			if(type == 11) {
				asteroids = getImages(230, 347, 33, 31);
				width = 33;
				height = 31;
			}
			else if(type == 12) {
				asteroids = getImages(230, 380, 33, 31);
				width = 33;
				height = 31;
			}
			strength = 4;
			health = 4;
		}
		x = (int)(Math.random() * (SpaceWarrior.WIDTH - 2 * width) + 1 + width);
		y = 1 - height;
		speed = (int)(Math.random() * MAX_SPEED + 1);
		score = speed * strength;
		speedOfRotation = (int)(Math.random() * 3);
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
	public int getHeight() {
		return height;
	}
	@Override
	public int getWidth() {
		return width;
	}
	@Override
	public BufferedImage getImage() {
		return asteroids[currentImage];
	}
	@Override
	public void move() {
		updateCounter = (updateCounter + 1) % (rotationSpeedLimiter - speedOfRotation);
		if(!isOnScreen)
			return;
		y += speed;
		if(y >= SpaceWarrior.HEIGHT) {
			isOnScreen = false;
		}
		if(updateCounter % 10 == 0) {
			if(currentImage == 7)
				y += (height * 0.2);
			currentImage = (currentImage+1) % 8;
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
		return strength;
	}
	@Override
	public void damaged(int h) {
		health -= h;
		if(health <= 0) {
			health = 0;
			strength = 0;
			isOnScreen = false;
		}
	}
	public int getScore() {
		return score;
	}
	
	private BufferedImage[] getImages(int x, int y, int width, int height) {
		BufferedImage[] bi = new BufferedImage[8];
		for(int i = 0; i < 8; i++) {
			bi[i] = SpriteSheets.image2.getSubimage(x + i*width, y, width, height);
		}
		return bi;
	}
}
