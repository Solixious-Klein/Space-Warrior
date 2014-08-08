import java.awt.image.BufferedImage;


public class VillainMissile4 implements SpaceObject{
	private static int SPEED;
	private static int LIFE = 10;
	private int x, y;
	private int strength;
	
	private boolean isOnScreen;
	
	private BufferedImage missiles[];
	private int currentImage;
	
	private Thread life;
	
	public VillainMissile4(int x, int y) {
		this.x = x;
		this.y = y;
		isOnScreen = true;
		SPEED = 4;
		strength = 10;
		currentImage = 0;
		missiles = new BufferedImage[8];
		missiles[0] = SpriteSheets.image1.getSubimage(51, 85, 7, 19);
		missiles[1] = SpriteSheets.image1.getSubimage(123, 60, 16, 16);
		missiles[2] = SpriteSheets.image1.getSubimage(62, 77, 19, 7);
		missiles[3] = SpriteSheets.image1.getSubimage(123, 82, 16, 16);
		missiles[4] = SpriteSheets.image1.getSubimage(51, 59, 7, 19);
		missiles[5] = SpriteSheets.image1.getSubimage(98, 82, 16, 16);
		missiles[6] = SpriteSheets.image1.getSubimage(28, 77, 19, 7);
		missiles[7] = SpriteSheets.image1.getSubimage(101, 60, 16, 16);
		life = new Thread(new MissileLifeThread());
		life.start();
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
		return missiles[currentImage].getWidth();
	}

	@Override
	public int getHeight() {
		return missiles[currentImage].getHeight();
	}

	@Override
	public int getStrength() {
		return strength;
	}

	@Override
	public boolean isOnScreen() {
		return isOnScreen;
	}

	@Override
	public void move() {
		int scX = SpaceWarriorPanel.sc.getX();
		int scY = SpaceWarriorPanel.sc.getY();
		int scWidth = SpaceWarriorPanel.sc.getWidth();
		int scHeight = SpaceWarriorPanel.sc.getHeight();
		
		int diffX = (scX + (scWidth / 2)) - (x + (getWidth() / 2));
		int diffY = (scY + (scHeight / 2)) - (y + (getHeight() / 2));
		
		if(Math.abs(diffX) < 10)
			diffX = 0;
		if(Math.abs(diffY) < 10)
			diffY = 0;
		
		if(diffX > 0 && diffY > 0) {
			currentImage = 1;
			x += SPEED;
			y += SPEED;
		}
		else if(diffX > 0 && diffY < 0) {
			currentImage = 3;
			x += SPEED;
			y -= SPEED;
		}
		else if(diffX < 0 && diffY < 0) {
			currentImage = 5;
			x -= SPEED;
			y -= SPEED;
		}
		else if(diffX < 0 && diffY > 0) {
			currentImage = 7;
			x -= SPEED;
			y += SPEED;
		}
		else if(diffY > 0) {
			currentImage = 0;
			y += SPEED;
		}
		else if(diffY < 0) {
			currentImage = 4;
			y -= SPEED;
		}
		
		else if(diffX > 0) {
			currentImage = 2;
			x += SPEED;
		}
		else if(diffX < 0) {
			currentImage = 6;
			x -= SPEED;
		}
	}

	@Override
	public void damaged(int v) {
		isOnScreen = false;
	}

	@Override
	public BufferedImage getImage() {
		return missiles[currentImage];
	}
	
	private class MissileLifeThread implements Runnable {
		@Override
		public void run() {
			for(int i = 0; i < LIFE; i++) {
				try {
					Thread.sleep(1000);
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			isOnScreen = false;
		}
	}
}
