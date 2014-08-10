import java.awt.image.BufferedImage;


public class Boss implements SpaceObject{
	public static int ENTERING = 1;
	public static int BATTLING = 2;
	
	private BufferedImage bossImages[];
	private BufferedImage explosionImages[];
	private int currentImage;
	private int phase;
	
	private int health, strength;
	private int x, y;
	private int speed;
	private int explosionItr;
	
	private int itr, itr2;
	
	private boolean isOnScreen, isExploding;
	
	private SpaceWarriorPanel swp;
	
	public Boss(SpaceWarriorPanel swp) {
		this.swp = swp;
		bossImages = new BufferedImage[8];
		
		bossImages[0] = SpriteSheets.image3.getSubimage(270, 128, 107, 86);
		bossImages[1] = SpriteSheets.image3.getSubimage(498, 106, 125, 125);
		bossImages[2] = SpriteSheets.image3.getSubimage(535, 247, 86, 107);
		bossImages[3] = SpriteSheets.image3.getSubimage(392, 231, 125, 125);
		bossImages[4] = SpriteSheets.image3.getSubimage(269, 261, 107, 86);
		bossImages[5] = SpriteSheets.image3.getSubimage(123, 233, 125, 125);
		bossImages[6] = SpriteSheets.image3.getSubimage(13, 244, 86, 107);
		bossImages[7] = SpriteSheets.image3.getSubimage(5, 104, 125, 125);
		
		currentImage = 0;
		phase = ENTERING;
		
		health = 5;
		y = 1 - bossImages[currentImage].getHeight();
		x = (SpaceWarrior.WIDTH / 2) - (bossImages[currentImage].getWidth() / 2);
		strength = 999;
		speed = 2;
		
		explosionImages = new BufferedImage[16];
		int in = 2;
		for(int i = 0; i < 16; i++) {
			explosionImages[i] = SpriteSheets.explosion[in].getSubimage(64 * (i % 4), 64 * (i / 4), 64, 64);
		}
	}
	public int getPhase() {
		return phase;
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
		return bossImages[currentImage].getWidth();
	}
	@Override
	public int getHeight() {
		return bossImages[currentImage].getHeight();
	}
	@Override
	public int getStrength() {
		return strength;
	}
	public int getHealth() {
		return health;
	}
	@Override
	public boolean isOnScreen() {
		return isOnScreen;
	}
	@Override
	public void move() {
		itr = (itr + 1) % 30;
		itr2 = (itr2 + 1) % 145;
		if(phase == ENTERING) {
			y += speed;
			if(y >= SpaceWarrior.HEIGHT/3 - getHeight())
				phase = BATTLING;
		}
		int scX = SpaceWarriorPanel.sc.getX();
		int scY = SpaceWarriorPanel.sc.getY();
		int scWidth = SpaceWarriorPanel.sc.getWidth();
		if(itr == 10 && x - scWidth <= scX && x + scWidth + getWidth() >= scX && y + getHeight() < scY)
			fire();
		if(itr2 == 25)
			fire2();
		if(x > scX) {
			x -= speed;
		}
		else if(x + getWidth() < scX) {
			x += speed;
		}
		
		if(explosionItr == 15)
			isOnScreen = false;
	}
	@Override
	public void damaged(int v) {
		health -= v;
		if(health <= 0) {
			explode();
		}
	}
	@Override
	public BufferedImage getImage() {
		if(!isExploding)
			return bossImages[currentImage];
		else if(explosionItr <= 15) {
			return explosionImages[explosionItr++];
		}
		return null;
	}
	public void enter() {
		isOnScreen = true;
	}
	public void fire() {
		VillainMissile3 vm1 = new VillainMissile3(x - 2, y + getHeight());
		VillainMissile3 vm2 = new VillainMissile3(x - 2 + vm1.getWidth(), y + getHeight());
		VillainMissile3 vm3 = new VillainMissile3(x + 2 + getWidth() - vm1.getWidth(), y + getHeight());
		VillainMissile3 vm4 = new VillainMissile3(x + 2 + getWidth() - (2 * vm1.getWidth()), y + getHeight());
		swp.addVillainMissile3(vm1);
		swp.addVillainMissile3(vm2);
		swp.addVillainMissile3(vm3);
		swp.addVillainMissile3(vm4);
	}
	public void fire2() {
		VillainMissile4 vm5 = new VillainMissile4(x + (getWidth() / 2), y + (getHeight() / 2));
		swp.addVillainMissile4(vm5);
	}
	public void explode() {
		strength = 0;
		isExploding = true;
	}
}
