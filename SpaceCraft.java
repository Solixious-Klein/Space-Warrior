import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;


public class SpaceCraft implements SpaceObject {
	
	private static int SPEED = 5;
	
	private SpaceWarriorPanel swp;
	private BufferedImage craft;
	
	private int x, y, width, height;
	private int dx, dy;
	private int health;
	
	private long chargeStart;
	
	private boolean isAlive, isCharging;
	
	public SpaceCraft(SpaceWarriorPanel swp) {
		this.swp = swp;
		width = 32;
		height = 32;
		health = 10;
		isAlive = true;
		isCharging = false;
		craft = SpriteSheets.image1.getSubimage(23, 289, width, height);
		x = 256 - width/2;
		y = 256 + 128;
	}
	
	@Override
	public BufferedImage getImage() {
		return craft;
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
        if(x + dx >= 0 && x + dx <= 512 - width)
            x = x + dx;
        if(y + dy >= 0 && y + dy <= 512 - width)
            y = y + dy;
    }
	
	public void damaged(int h) {
		health -= h;
		 if(health <= 0) {
			health = 0;
			isAlive = false;
		 }
		 if(health>10)
			 health = 10;
	}
	public int getHealth() {
		return health;
	}
	public boolean isAlive() {
		return isAlive;
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
}
