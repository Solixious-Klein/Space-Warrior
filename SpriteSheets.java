import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class SpriteSheets {
	private static String file1 = "sprite1.png";
	private static String file2 = "sprite2.png";
	private static String file3 = "sprite3.png";
	private static String file4 =  "explode_1.png";
	private static String file5 =  "explode_2.png";
	private static String file6 =  "explode_3.png";
	private static String file7 =  "explode_4.png";
	
	public static BufferedImage image1, image2, image3;
	public static BufferedImage[] explosion;
	
	static {
		try {
			image1 = ImageIO.read(SpriteSheets.class.getResourceAsStream(file1));
			image2 = ImageIO.read(SpriteSheets.class.getResourceAsStream(file2));
			image3 = ImageIO.read(SpriteSheets.class.getResourceAsStream(file3));
			
			explosion = new BufferedImage[4];
			explosion[0] = ImageIO.read(SpriteSheets.class.getResourceAsStream(file4));
			explosion[1] = ImageIO.read(SpriteSheets.class.getResourceAsStream(file5));
			explosion[2] = ImageIO.read(SpriteSheets.class.getResourceAsStream(file6));
			explosion[3] = ImageIO.read(SpriteSheets.class.getResourceAsStream(file7));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
