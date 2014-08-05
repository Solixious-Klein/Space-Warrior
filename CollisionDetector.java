import java.awt.Rectangle;


public class CollisionDetector {
	public static boolean isColliding(SpaceObject sObj1, SpaceObject sObj2) {
		Rectangle r1 = new Rectangle(sObj1.getX(), sObj1.getY(), sObj1.getWidth(), sObj1.getHeight());
		Rectangle r2 = new Rectangle(sObj2.getX(), sObj2.getY(), sObj2.getWidth(), sObj2.getHeight());
		
		return r1.intersects(r2);
	}
}
