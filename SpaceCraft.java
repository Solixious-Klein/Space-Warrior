import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;


public class SpaceCraft implements SpaceObject {
	
	private static int SPEED = 7;
	
	private SpaceWarriorPanel swp;
	private BufferedImage craft, shieldImage[], explosionImages[];
	
	private int x, y, width, height;
	private int dx, dy;
	private int health, strength;
	private int shieldTimer;
	private int currentShieldImage;
	private int explosionItr, explosionItr2;
	
	private long chargeStart;
	
	private boolean isOnScreen, isCharging, isExploding;
	
	public SpaceCraft(SpaceWarriorPanel swp) {
		this.swp = swp;
		width = 32;
		height = 32;
		health = 10;
		strength = 100;
		isOnScreen = true;
		isCharging = false;
		craft = SpriteSheets.image1.getSubimage(23, 289, width, height);
		shieldImage = new BufferedImage[2];
		shieldImage[0] = SpriteSheets.image1.getSubimage(158, 63, 37, 34);
		shieldImage[1] = SpriteSheets.image1.getSubimage(204, 63, 37, 34);
		
		explosionImages = new BufferedImage[16];
		int in = 0;
		for(int i = 0; i < 16; i++) {
			explosionImages[i] = SpriteSheets.explosion[in].getSubimage(64 * (i % 4), 64 * (i / 4), 64, 64);
		}
		x = SpaceWarrior.WIDTH / 2 - width / 2;
		y = 3 * SpaceWarrior.HEIGHT / 4;
		
	}
	
	@Override
	public BufferedImage getImage() {
		if(!isExploding)
			return craft;
		else if(explosionItr < 15){
			if(explosionItr2 == 0)
				explosionItr++;
	        explosionItr2 = (explosionItr2 + 1) % 3;
			return explosionImages[explosionItr];
		}
		return null;
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
	public void move()
    {
        if(x + dx >= 0 && x + dx <= SpaceWarrior.WIDTH - width)
            x = x + dx;
        if(y + dy >= 0 && y + dy <= SpaceWarrior.HEIGHT - width)
            y = y + dy;
        
        if(explosionItr == 15) {
        	isOnScreen = false;
        }
    }
	
	public void activateShield() {
		shieldTimer += 50;
		new Thread(new ShieldThread()).start();
	}
	public boolean isShieldActive() {
		return !(shieldTimer == 0);
	}
	public BufferedImage getShieldImage() {
		return shieldImage[currentShieldImage];
	}
	public int getShieldRemainingTime() {
		return shieldTimer;
	}
	
	
	public void damaged(int h) {
		 health -= h;
		 if(health <= 0) {
			 health = 0;
			 explode();
		 }
		 if(health>10)
			 health = 10;
	}
	public int getHealth() {
		return health;
	}
	@Override
	public boolean isOnScreen() {
		return isOnScreen;
	}
	@Override
	public int getStrength() {
		return strength;
	}
	public void fire() {
		long charge = System.currentTimeMillis() - chargeStart;
		int missileType = 0;
		if(charge < 200) {
			missileType = Missile.LOW;
			Missile m = new Missile(x + 5, y, missileType);
			swp.addMissile(m);
			m = new Missile(x + width - 5, y, missileType);
			swp.addMissile(m);
		}
		else if(charge < 500) {
			missileType = Missile.MEDIUM;
			Missile m = new Missile(x + 3, y, missileType);
			swp.addMissile(m);
			m = new Missile(x + width - 8, y, missileType);
			swp.addMissile(m);
		}
		else {
			missileType = Missile.HIGH;
			Missile m = new Missile(x, y, missileType);
			swp.addMissile(m);
			m = new Missile(x + width - 11, y, missileType);
			swp.addMissile(m);
		}
		charge = 0;
	}
	
	public void explode() {
		strength = 0;
		isExploding = true;
	}
	
	public void keyPressed(KeyEvent e) {
		int key=e.getKeyCode();
        if(key==KeyEvent.VK_UP)
            dy=-SPEED;
        if(key==KeyEvent.VK_DOWN)
            dy=SPEED;
        if(key==KeyEvent.VK_RIGHT)
            dx=SPEED;
        if(key==KeyEvent.VK_LEFT)
            dx=-SPEED;
        if(key==KeyEvent.VK_SPACE && !isCharging) {
        	chargeStart = System.currentTimeMillis();
        	isCharging = true;
        }
	}
	public void keyReleased(KeyEvent e)
    {
		int key=e.getKeyCode();
        if(key==KeyEvent.VK_UP || key== KeyEvent.VK_DOWN)
            dy=0;
        if(key==KeyEvent.VK_LEFT || key== KeyEvent.VK_RIGHT)
            dx=0;
        if(key == KeyEvent.VK_SPACE) {
        	isCharging = false;
        	fire();
        }
    }
	
	private class ShieldThread implements Runnable {
		@Override
		public void run() {
			for(int i = 0; i < 50; i++) {
				try {
					Thread.sleep(200);
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
				currentShieldImage = (currentShieldImage + 1) % 2;
				shieldTimer--;
			}
			shieldTimer = 0;
		}
	}
}
