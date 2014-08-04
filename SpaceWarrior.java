import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SpaceWarrior {
	public static JFrame window;
	
	public static int WIDTH;
	public static int HEIGHT;
	
	static {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screen = tk.getScreenSize();
		WIDTH = screen.width;
		HEIGHT = screen.height;
		window = new JFrame();
		window.setSize(WIDTH, HEIGHT);
		window.setLayout(null);
		window.setFocusable(false);
		window.setUndecorated(true);
		window.setAlwaysOnTop(true);
		
		BufferedImage cursorImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = tk.createCustomCursor(cursorImage, new Point(0,0), "Blank Cursor");
		window.getContentPane().setCursor(blankCursor);
	}
	
	public static void exit() {
		window.dispose();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SpaceWarriorPanel swp = new SpaceWarriorPanel();
				swp.setBounds(0, 0, window.getWidth(), window.getHeight());
				swp.setFocusable(true);
				swp.requestFocusInWindow();
				window.add(swp);
				
				window.setVisible(true);
			}
		});
	}
}
