
public class CollisionDetector {
	public static boolean isColliding(SpaceObject sObj1, SpaceObject sObj2) {
		if(((sObj2.getX() < sObj1.getX() && sObj2.getX() + sObj2.getWidth() > sObj1.getX()) || 
				((sObj1.getX() < sObj2.getX() && sObj1.getX() + sObj1.getWidth() > sObj2.getX()))) &&
				((sObj2.getY() < sObj1.getY() && sObj2.getY() + sObj2.getHeight() > sObj1.getY()) ||
				((sObj1.getY() < sObj2.getY() && sObj1.getY() + sObj1.getHeight() > sObj2.getY()))))
			return true;
		return false;
	}
}
