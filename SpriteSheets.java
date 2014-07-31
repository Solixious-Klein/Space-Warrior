import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class SpriteSheets {
	private static String file1 = "sprite1.png";
	private static String file2 = "sprite2.png";
	private static String file3 = "sprite3.png";
	
	public static BufferedImage image1, image2, image3;
	
	static {
		try {
			image1 = ImageIO.read(new File(file1));
			image2 = ImageIO.read(new File(file2));
			image3 = ImageIO.read(new File(file3));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
