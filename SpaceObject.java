import java.awt.image.BufferedImage;


public interface SpaceObject {
	public int getX();
	public int getY();
	public int getWidth();
	public int getHeight();
	public int getStrength();
	
	public boolean isOnScreen();
	
	public void move();
	public void damaged(int v);
	
	public BufferedImage getImage();
}
