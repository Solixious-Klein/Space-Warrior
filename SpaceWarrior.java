import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SpaceWarrior {
	public static JFrame window;
	
	static {
		window = new JFrame();
		window.setSize(512, 512);
		window.setLayout(null);
		window.setFocusable(false);
		window.setUndecorated(true);
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
