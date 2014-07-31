import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;


public class SpaceWarriorPanel extends JPanel { 
	private static final long serialVersionUID = 1L;
	
	private static final int FPS = 100;	//Frames per second
	private static final int WAIT_TIME_CONSTANT = 1000/FPS;	//time in ms the code will wait before repainting the screen
	private static final int UPDATE_TIME_CONSTANT = 24;

	//rate limiters [minimum rate] = once object per [rate limiter] cycles of updation
	private static final int ASTEROIDS_RATE_LIMITER = 100;
	private static final int STARS_RATE_LIMITER = 100;
	private static final int HEALTHPACKS_RATE_LIMITER = 1000;
	private static final int VILLAIN_RATE_LIMITER = 100;
	
	private static int RATE_OF_ASTEROIDS = 50; //rate of asteroids fall
	private static int RATE_OF_STARS = 50;	//rate of stars appearing
	private static int RATE_OF_HEALTHPACKS = 0;	//rate of appearance of health packs
	private static int RATE_OF_VILLAINS = 50;	//rate of appearance of villains
	
	private ArrayList<Missile> missiles;	//list of player's missiles on screen
	private ArrayList<Star> stars;	//list of starts currently visible
	private ArrayList<Asteroid> asteroids;	//list of asteroids on screen
	private ArrayList<HealthPack> healthPacks;	//list of health packs on screen
	private ArrayList<Villain> villains;	//list of villains on screen 
	
	
	private SpaceCraft sc;	//the space craft's object
	private Thread at;	//animation thread
	private Thread ut; //updation thred
	
	private boolean isAnimating;	//animation stops if this is false
	
	private int starItr, asteroidItr, healthPackItr, villainItr;	//iterators for appearance of various occasional objects like stars, asteroids and healthpacks on screen
	private int score;	//stores the score of player
	
	private int xOffset, yOffset;
	private boolean mouseDown;
	
	public SpaceWarriorPanel() {
		
		sc = new SpaceCraft(this);
		
		at = new Thread(new AnimationThread());
		ut = new Thread(new UpdationThread());
		
		missiles = new ArrayList<Missile>();
		stars = new ArrayList<Star>();
		asteroids = new ArrayList<Asteroid>();
		healthPacks = new ArrayList<HealthPack>();
		villains = new ArrayList<Villain>();
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(!mouseDown) {
					mouseDown = true;
					xOffset = e.getX();
					yOffset = e.getY();
				}
			}
			@Override 
			public void mouseReleased(MouseEvent e) {
				if(mouseDown) {
					SpaceWarrior.window.setBounds(e.getXOnScreen() - xOffset, e.getYOnScreen() - yOffset, 512, 512);
					xOffset = 0;
					yOffset = 0;
					mouseDown = false;
				}
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if(mouseDown) {
					SpaceWarrior.window.setBounds(e.getXOnScreen() - xOffset, e.getYOnScreen() - yOffset, 512, 512);
				}
			}
		});
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_ESCAPE) {
					isAnimating = false;
					SpaceWarrior.exit();
				}
				else {
					sc.keyPressed(e);
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				sc.keyReleased(e);
			}
		});
		
		isAnimating = true;
		
		at.start();
		ut.start();
	}
	
	public void addMissile(Missile m) {
		missiles.add(m);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		drawStars(g2d);
		drawMissiles(g2d);
		drawHealthPacks(g2d);
		drawAsteroids(g2d);
		drawCraft(g2d);
		drawVillains(g2d);
		drawHealthBar(g2d);
		drawScore(g2d);
	}
	private void drawStars(Graphics2D g2d) {
		synchronized(stars) {
			Iterator<Star> itrStar = stars.iterator();
			while(itrStar.hasNext()) {
				Star s = itrStar.next();
				g2d.drawImage(s.getImage(), s.getX(), s.getY(), this);
			}
		}
	}
	private void drawCraft(Graphics2D g2d) {
		if(sc.isAlive())
			g2d.drawImage(sc.getImage(), sc.getX(), sc.getY(), this);
		else
			isAnimating = false;
	}
	private void drawMissiles(Graphics2D g2d) {
		synchronized(missiles) {
			Iterator<Missile> itrMissile = missiles.iterator();
			while(itrMissile.hasNext()) {
				Missile m = itrMissile.next();
				g2d.drawImage(m.getImage(), m.getX(), m.getY(), this);
			}
		}
	}
	private void drawAsteroids(Graphics2D g2d) {
		synchronized(asteroids) {
			Iterator<Asteroid> itrAsteroid = asteroids.iterator();
			
			while(itrAsteroid.hasNext()) {
				Asteroid a = itrAsteroid.next();
				g2d.drawImage(a.getImage(), a.getX(), a.getY(), this);
			}
		}
	}
	private void drawHealthPacks(Graphics2D g2d) {
		synchronized(healthPacks) {
			Iterator<HealthPack> itrHealthPack = healthPacks.iterator();
			while(itrHealthPack.hasNext()) {
				HealthPack hp = itrHealthPack.next();
				g2d.drawImage(hp.getImage(), hp.getX(), hp.getY(), this);
			}
		}
	}
	private void drawVillains(Graphics2D g2d) {
		synchronized(villains) {
			Iterator<Villain> itrVillain = villains.iterator();
			while(itrVillain.hasNext()) {
				Villain v = itrVillain.next();
				g2d.drawImage(v.getImage(), v.getX(), v.getY(), this);
			}
		}
	}
	private void drawHealthBar(Graphics2D g2d) {
		int health = sc.getHealth();
		
		g2d.setColor(Color.white);
		g2d.fillRoundRect(19, 19, 52, 12, 2, 2);
		if(health == 10)
			g2d.setColor(new Color(0,255,0));
		else if(health >= 7)
			g2d.setColor(new Color(255,255,0));
		else if(health >= 4)
			g2d.setColor(new Color(0,255,255));
		else
			g2d.setColor(new Color(0,0,255));
		
		g2d.fillRect(20, 20, 5 * health, 10);
		g2d.setColor(new Color(255,0,0));
		g2d.fillRect(20 + 5 * health, 20, 5 * (10-health), 10);
	}
	private void drawScore(Graphics2D g2d) {
		g2d.setColor(Color.white);
		g2d.drawString(String.valueOf(score), 20, 60);
	}
	
	private class AnimationThread implements Runnable {
		@Override
		public void run() {
			long wait = 0L;
			while(isAnimating) {
				try {
					Thread.sleep(WAIT_TIME_CONSTANT - wait);
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
				
				long initial = System.currentTimeMillis();
				wait = System.currentTimeMillis() - initial;
				
				repaint();
				
			}
		}
	}
	
	private class UpdationThread implements Runnable {
		@Override
		public void run() {
			long wait = 0L;
			while(isAnimating) {
				try {
					Thread.sleep(UPDATE_TIME_CONSTANT - wait);
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
				
				long initial = System.currentTimeMillis();
				wait = System.currentTimeMillis() - initial;
				
				updateCraft();
				synchronized(missiles) {
					updateMissiles();
				}
				synchronized(stars) {
					updateStars();
				}
				synchronized(asteroids) {
					updateAsteroids();
				}
				synchronized(healthPacks) {
					updateHealthPacks();
				}
				synchronized(villains) {
					updateVillains();
				}
			}
		}
		private void updateMissiles() {
			Iterator<Missile> itrMissile = missiles.iterator();
			ArrayList<Missile> deadMissile = new ArrayList<Missile>();
			while(itrMissile.hasNext()) {
				Missile m = itrMissile.next();
				m.move();
				if(!m.isOnScreen())
					deadMissile.add(m);
				
				Iterator<Asteroid> itrAsteroid = asteroids.iterator();
				ArrayList<Asteroid> deadAsteroid = new ArrayList<Asteroid>();
				while(itrAsteroid.hasNext()) {
					Asteroid a = itrAsteroid.next();
					if(CollisionDetector.isColliding(m, a)) {
						a.damaged(m.getStrength());
						deadMissile.add(m);
					}
					if(!a.isOnScreen()) {
						deadAsteroid.add(a);
						score += (m.getStrength() * a.getType() * 10);
					}
				}
				itrAsteroid = deadAsteroid.iterator();
				while(itrAsteroid.hasNext()) {
					Asteroid a = itrAsteroid.next();
					asteroids.remove(a);
				}
				
				Iterator<Villain> itrVillain = villains.iterator();
				ArrayList<Villain> deadVillain = new ArrayList<Villain>();
				while(itrVillain.hasNext()) {
					Villain v = itrVillain.next();
					if(CollisionDetector.isColliding(m, v)) {
						v.damaged(m.getStrength());
						deadMissile.add(m);
						deadVillain.add(v);
					}
					if(!v.isOnScreen()) {
						deadVillain.add(v);
						score += 100;
					}
				}
				itrAsteroid = deadAsteroid.iterator();
				while(itrAsteroid.hasNext()) {
					Asteroid a = itrAsteroid.next();
					asteroids.remove(a);
				}
				
			}
			
			itrMissile = deadMissile.iterator();
			while(itrMissile.hasNext()) {
				Missile m = itrMissile.next();
				missiles.remove(m);
			}
		}
		private void updateStars() {
			Iterator<Star> itrStar = stars.iterator();
			ArrayList<Star> deadStar = new ArrayList<Star>();
			while(itrStar.hasNext()) {
				Star s = itrStar.next();
				s.move();
				if(!s.isOnScreen())
					deadStar.add(s);
			}
			itrStar = deadStar.iterator();
			while(itrStar.hasNext()) {
				Star s = itrStar.next();
				stars.remove(s);
			}
			
			starItr = (starItr + 1) % (STARS_RATE_LIMITER - RATE_OF_STARS);
			if(starItr == 0)
				stars.add(new Star());
		}
		private void updateHealthPacks() {
			Iterator<HealthPack> itrHealthPack = healthPacks.iterator();
			ArrayList<HealthPack> deadHealthPack = new ArrayList<HealthPack>();
			while(itrHealthPack.hasNext()) {
				HealthPack hp = itrHealthPack.next();
				hp.move();
				if(CollisionDetector.isColliding(sc, hp)) {
					sc.damaged(-2);
					deadHealthPack.add(hp);
				}
				if(!hp.isOnScreen())
					deadHealthPack.add(hp);
			}
			itrHealthPack = deadHealthPack.iterator();
			while(itrHealthPack.hasNext()) {
				HealthPack hp = itrHealthPack.next();
				healthPacks.remove(hp);
			}
			
			healthPackItr = (healthPackItr+1) % (HEALTHPACKS_RATE_LIMITER - RATE_OF_HEALTHPACKS);
			if(healthPackItr == 999)
				healthPacks.add(new HealthPack());
		}
		private void updateCraft() {
			sc.move();
		}
		private void updateAsteroids() {
			Iterator<Asteroid> itrAsteroid = asteroids.iterator();
			ArrayList<Asteroid> deadAsteroid = new ArrayList<Asteroid>();
			while(itrAsteroid.hasNext()) {
				Asteroid a = itrAsteroid.next();
				a.move();
				if(CollisionDetector.isColliding(sc, a)) {
					sc.damaged(a.getStrength());
					deadAsteroid.add(a);
				}
				if(!a.isOnScreen())
					deadAsteroid.add(a);
			}
			itrAsteroid = deadAsteroid.iterator();
			while(itrAsteroid.hasNext()) {
				Asteroid a = itrAsteroid.next();
				asteroids.remove(a);
			}
			
			asteroidItr = (asteroidItr + 1) % (ASTEROIDS_RATE_LIMITER - RATE_OF_ASTEROIDS);
			if(asteroidItr == 0)
				asteroids.add(new Asteroid());
		}
		private void updateVillains() {
			Iterator<Villain> itrVillain = villains.iterator();
			ArrayList<Villain> deadVillain = new ArrayList<Villain>();
			while(itrVillain.hasNext()) {
				Villain v = itrVillain.next();
				v.move();
				if(CollisionDetector.isColliding(v, sc)) {
					sc.damaged(v.getStrength());
					deadVillain.add(v);
				}
				if(!v.isOnScreen()) {
					deadVillain.add(v);
				}
			}
			itrVillain = deadVillain.iterator();
			while(itrVillain.hasNext()) {
				Villain v = itrVillain.next();
				villains.remove(v);
			}
			villainItr = (villainItr + 1) % (VILLAIN_RATE_LIMITER - RATE_OF_VILLAINS);
			if(villainItr == 0)
				villains.add(new Villain());
		}
	}
}
