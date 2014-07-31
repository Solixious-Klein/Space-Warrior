import java.awt.image.BufferedImage;


public interface SpaceObject {
	public int getX();
	public int getY();
	public int getWidth();
	public int getHeight();
	public void move();
	public BufferedImage getImage();
}
