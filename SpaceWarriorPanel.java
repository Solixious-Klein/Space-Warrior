import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JPanel;


public class SpaceWarriorPanel extends JPanel { 
	private static final long serialVersionUID = 1L;

	private SpaceWarriorPanel thisPanel;
	
	private static final int FPS = 64;	//Frames per second
	private static final int WAIT_TIME_CONSTANT = 1000/FPS;	//time in ms the code will wait before repainting the screen
	private static final int UPDATE_TIME_CONSTANT = 24;

	//rate limiters [minimum rate] = once object per [rate limiter] cycles of updation
	private static final int ASTEROIDS_RATE_LIMITER = 100;
	private static final int STARS_RATE_LIMITER = 100;
	private static final int HEALTHPACKS_RATE_LIMITER = 3500;
	private static final int VILLAIN_RATE_LIMITER = 400;
	private static final int SHIELDS_RATE_LIMITER = 2500;
	private static final int SPACE_BOMB_RATE_LIMITER = 250;
	
	private static int RATE_OF_ASTEROIDS = 80; //rate of asteroids fall
	private static int RATE_OF_STARS = 50;	//rate of stars appearing
	private static int RATE_OF_HEALTHPACKS = 0;	//rate of appearance of health packs
	private static int RATE_OF_VILLAINS = 300;	//rate of appearance of villains
	private static int RATE_OF_SHIELDS = 10;	//rate of appearance of shield ups
	private static int RATE_OF_SPACE_BOMBS = 50;	//rate of space bombs
	
	
	private ArrayList<Missile> missiles;	//list of player's missiles on screen
	private ArrayList<Star> stars;	//list of starts currently visible
	private ArrayList<Asteroid> asteroids;	//list of asteroids on screen
	private ArrayList<HealthPack> healthPacks;	//list of health packs on screen
	private ArrayList<Villain> villains;	//list of villains on screen 
	private ArrayList<VillainMissile1> villainMissiles1;
	private ArrayList<Shield> shields;
	private ArrayList<SpaceBomb> spaceBombs;
	
	
	protected static SpaceCraft sc;	//the space craft's object
	private Thread at;	//animation thread
	private Thread ut; //updation thred
	
	private boolean isAnimating, isPaused;	//animation stops if this is false
	
	private int starItr, asteroidItr, healthPackItr, villainItr, shieldItr, spaceBombItr;	//iterators for appearance of various occasional objects like stars, asteroids and healthpacks on screen
	private int score, actualScore, highScore;	//stores the score of player
	
	private long initialTime;
	
	public SpaceWarriorPanel() {
		highScore = 1000;
		thisPanel = this;
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_ESCAPE) {
					isAnimating = false;
					SpaceWarrior.exit();
				}
				else if(!isAnimating && key == KeyEvent.VK_ENTER) {
					initialize();
				}
				else if(key == KeyEvent.VK_CONTROL) {
					isPaused = !isPaused;
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
		initialize();
	}
	
	private void initialize() {
		score = 0;
		sc = new SpaceCraft(this);
		
		at = new Thread(new AnimationThread());
		ut = new Thread(new UpdationThread());
		
		missiles = new ArrayList<Missile>();
		stars = new ArrayList<Star>();
		asteroids = new ArrayList<Asteroid>();
		healthPacks = new ArrayList<HealthPack>();
		villains = new ArrayList<Villain>();
		villainMissiles1 = new ArrayList<VillainMissile1>();
		shields = new ArrayList<Shield>();
		spaceBombs = new ArrayList<SpaceBomb>();
		
		isAnimating = true;
		isPaused = false;
		
		initialTime = System.currentTimeMillis();
		
		at.start();
		ut.start();
	}
	
	public void addMissile(Missile m) {
		missiles.add(m);
	}
	
	public void addVillainMissile1(VillainMissile1 v) {
		villainMissiles1.add(v);
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
		drawShields(g2d);
		drawVillainMissiles1(g2d);
		drawSpaceBombs(g2d);
		drawVillains(g2d);
		drawHealthBar(g2d);
		drawScore(g2d);
		
		if(!isAnimating) {
			g2d.setColor(Color.white);
			Font f = new Font("Comic Sans MS", Font.BOLD, 20);
			g2d.setFont(f);
			g2d.drawString("Press Enter to start the game", SpaceWarrior.WIDTH/2 - 150, SpaceWarrior.HEIGHT/2 - 15);
		}
	}
	private void drawStars(Graphics2D g2d) {
		synchronized(stars) {
			for(int i = 0; i < stars.size(); i++) {
				Star s = stars.get(i);
				g2d.drawImage(s.getImage(), s.getX(), s.getY(), this);
			}
		}
	}
	private void drawCraft(Graphics2D g2d) {
		if(sc.isOnScreen()) {
			g2d.drawImage(sc.getImage(), sc.getX(), sc.getY(), this);
			if(sc.isShieldActive()) {
				g2d.drawImage(sc.getShieldImage(), sc.getX(), sc.getY(), this);
				g2d.setColor(Color.white);
				g2d.drawString(String.valueOf(sc.getShieldRemainingTime()), 20, SpaceWarrior.HEIGHT - 40);
			}
		}
		else
			isAnimating = false;
	}
	private void drawMissiles(Graphics2D g2d) {
		synchronized(missiles) {
			for(int i = 0; i < missiles.size(); i++) {
				Missile m = missiles.get(i);
				g2d.drawImage(m.getImage(), m.getX(), m.getY(), this);
			}
		}
	}
	private void drawVillainMissiles1(Graphics2D g2d) {
		synchronized(missiles) {
			for(int i = 0; i < villainMissiles1.size(); i++) {
				VillainMissile1 vm = villainMissiles1.get(i);
				g2d.drawImage(vm.getImage(), vm.getX(), vm.getY(), this);
			}
		}
	}
	private void drawAsteroids(Graphics2D g2d) {
		synchronized(asteroids) {
			for(int i = 0; i < asteroids.size(); i++) {
				Asteroid a = asteroids.get(i);
				g2d.drawImage(a.getImage(), a.getX(), a.getY(), this);
			}
		}
	}
	private void drawHealthPacks(Graphics2D g2d) {
		synchronized(healthPacks) {
			for(int i = 0; i < healthPacks.size(); i++) {
				HealthPack hp = healthPacks.get(i);
				g2d.drawImage(hp.getImage(), hp.getX(), hp.getY(), this);
			}
		}
	}
	private void drawVillains(Graphics2D g2d) {
		synchronized(villains) {
			for(int i = 0; i < villains.size(); i++) {
				Villain v = villains.get(i);
				g2d.drawImage(v.getImage(), v.getX(), v.getY(), this);
			}
		}
	}
	private void drawSpaceBombs(Graphics2D g2d) {
		synchronized(spaceBombs) {
			for(int i = 0; i < spaceBombs.size(); i++) {
				SpaceBomb sb = spaceBombs.get(i);
				g2d.drawImage(sb.getImage(), sb.getX(), sb.getY(), this);
			}
		}
	}
	private void drawShields(Graphics2D g2d) {
		synchronized(shields) {
			for(int i = 0; i < shields.size(); i++) {
				Shield s = shields.get(i);
				g2d.drawImage(s.getImage(), s.getX(), s.getY(), this);
			}
		}
	}
	private void drawHealthBar(Graphics2D g2d) {
		int health = sc.getHealth();
		
		g2d.setColor(Color.white);
		g2d.fillRoundRect(19, 19, 102, 12, 2, 2);
		if(health == 10)
			g2d.setColor(new Color(0,255,0));
		else if(health >= 7)
			g2d.setColor(new Color(255,255,0));
		else if(health >= 4)
			g2d.setColor(new Color(0,255,255));
		else
			g2d.setColor(new Color(0,0,255));
		
		g2d.fillRect(20, 20, 10 * health, 10);
		g2d.setColor(new Color(255,0,0));
		g2d.fillRect(20 + 10 * health, 20, 10 * (10-health), 10);
	}
	private void drawScore(Graphics2D g2d) {
		long timeSurvived = (System.currentTimeMillis() - initialTime)/1000;
		
		Font f = new Font("Comic Sans MS", Font.BOLD, 14);
		actualScore = (int)(score + timeSurvived);
		g2d.setFont(f);
		g2d.setColor(Color.white);
		g2d.drawString("Score:"+String.valueOf(actualScore), 20, 60);
		g2d.drawString("Time Survived:"+String.valueOf(timeSurvived)+"s", 20, 90);
		g2d.drawString("High Score:"+String.valueOf(highScore), SpaceWarrior.WIDTH - 150, 60);
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
				while(isPaused) {
					try {
						Thread.sleep(100);
					}
					catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
				long initial = System.currentTimeMillis();
				wait = System.currentTimeMillis() - initial;
				
				repaint();
			}
			repaint();
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
				while(isPaused) {
					try {
						Thread.sleep(100);
					}
					catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
				long initial = System.currentTimeMillis();
				wait = System.currentTimeMillis() - initial;
				
				if(highScore < actualScore)
					highScore = actualScore;
				
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
				synchronized(villainMissiles1) {
					updateVillainMissiles1();
				}
				synchronized(shields) {
					updateShields();
				}
				synchronized(spaceBombs) {
					updateSpaceBombs();
				}
			}
		}
		private void updateMissiles() {
			ArrayList<Missile> deadMissile = new ArrayList<Missile>();
			for(int i = 0; i < missiles.size(); i++) {
				Missile m = missiles.get(i);
				m.move();
				if(!m.isOnScreen())
					deadMissile.add(m);
				
				ArrayList<Asteroid> deadAsteroid = new ArrayList<Asteroid>();
				for(int j = 0; j < asteroids.size(); j++) {
					Asteroid a = asteroids.get(j);
					if(CollisionDetector.isColliding(m, a)) {
						a.damaged(m.getStrength());
						deadMissile.add(m);
					}
					if(!a.isOnScreen()) {
						deadAsteroid.add(a);
						score += (m.getStrength() * a.getScore());
					}
				}
				for(int j = 0; j < deadAsteroid.size(); j++) {
					Asteroid a = deadAsteroid.get(j);
					asteroids.remove(a);
				}
				
				ArrayList<Villain> deadVillain = new ArrayList<Villain>();
				for(int j = 0; j < villains.size(); j++) {
					Villain v = villains.get(j);
					if(CollisionDetector.isColliding(m, v)) {
						v.damaged(m.getStrength());
						deadMissile.add(m);
					}
					if(!v.isOnScreen()) {
						deadVillain.add(v);
						score += v.getScore();
					}
				}
				for(int j = 0; j < deadVillain.size(); j++) {
					Villain v = deadVillain.get(j);
					villains.remove(v);
				}
				
				ArrayList<SpaceBomb> deadSpaceBombs = new ArrayList<SpaceBomb>();
				for(int j = 0; j < spaceBombs.size(); j++) {
					SpaceBomb sb = spaceBombs.get(j);
					if(CollisionDetector.isColliding(m, sb)) {
						sb.damaged(m.getStrength());
						deadMissile.add(m);
					}
					if(!sb.isOnScreen()) {
						deadSpaceBombs.add(sb);
						score += sb.getScore();
					}
				}
				for(int j = 0; j < deadSpaceBombs.size(); j++) {
					SpaceBomb sb = deadSpaceBombs.get(j);
					spaceBombs.remove(sb);
				}
			}
			
			
			for(int i = 0; i < deadMissile.size(); i++) {
				Missile m = deadMissile.get(i);
				missiles.remove(m);
			}
		}
		private void updateSpaceBombs() {
			ArrayList<SpaceBomb> deadSpaceBombs = new ArrayList<SpaceBomb>();
			for(int i = 0; i < spaceBombs.size(); i++) {
				SpaceBomb sb = spaceBombs.get(i);
				sb.move();
				if(CollisionDetector.isColliding(sc, sb)) {
					if(!sc.isShieldActive())
						sc.damaged(sb.getStrength());
					else
						score += sb.getScore();
					deadSpaceBombs.add(sb);
				}
				if(!sb.isOnScreen())
					deadSpaceBombs.add(sb);
			}
			
			for(int i = 0; i < deadSpaceBombs.size(); i++) {
				SpaceBomb sb = deadSpaceBombs.get(i);
				spaceBombs.remove(sb);
			}
			
			spaceBombItr = (spaceBombItr + 1) % (SPACE_BOMB_RATE_LIMITER - RATE_OF_SPACE_BOMBS);
			if(spaceBombItr == 0) 
				spaceBombs.add(new SpaceBomb());
		}
		private void updateStars() {
			ArrayList<Star> deadStar = new ArrayList<Star>();
			for(int i = 0; i < stars.size(); i++) {
				Star s = stars.get(i);
				s.move();
				if(!s.isOnScreen())
					deadStar.add(s);
			}
			for(int i = 0; i < deadStar.size(); i++) {
				Star s = deadStar.get(i);
				stars.remove(s);
			}
			
			starItr = (starItr + 1) % (STARS_RATE_LIMITER - RATE_OF_STARS);
			if(starItr == 0)
				stars.add(new Star());
		}
		private void updateHealthPacks() {
			ArrayList<HealthPack> deadHealthPack = new ArrayList<HealthPack>();
			for(int i = 0; i < healthPacks.size(); i++) {
				HealthPack hp = healthPacks.get(i);
				hp.move();
				if(CollisionDetector.isColliding(sc, hp)) {
					sc.damaged(-2);
					deadHealthPack.add(hp);
				}
				if(!hp.isOnScreen())
					deadHealthPack.add(hp);
			}
			
			for(int i = 0; i < deadHealthPack.size(); i++) {
				HealthPack hp = deadHealthPack.get(i);
				healthPacks.remove(hp);
			}
			
			healthPackItr = (healthPackItr+1) % (HEALTHPACKS_RATE_LIMITER - RATE_OF_HEALTHPACKS);
			if(healthPackItr == 999)
				healthPacks.add(new HealthPack());
		}
		private void updateCraft() {
			sc.move();
		}
		private void updateShields() {
			ArrayList<Shield> deadShields = new ArrayList<Shield>();
			for(int i = 0; i < shields.size(); i++) {
				Shield s = shields.get(i);
				s.move();
				if(CollisionDetector.isColliding(s, sc)) {
					sc.activateShield();
					deadShields.add(s);
				}
				if(!s.isOnScreen())
					deadShields.add(s);
			}
			for(int i = 0; i < deadShields.size(); i++) {
				Shield s = deadShields.get(i);
				shields.remove(s);
			}
			
			shieldItr = (shieldItr + 1) % (SHIELDS_RATE_LIMITER - RATE_OF_SHIELDS);
			if(shieldItr == SHIELDS_RATE_LIMITER - RATE_OF_SHIELDS - 1) {
				shields.add(new Shield());
			}
		}
		private void updateAsteroids() {
			ArrayList<Asteroid> deadAsteroid = new ArrayList<Asteroid>();
			for(int i = 0; i < asteroids.size(); i++) {
				Asteroid a = asteroids.get(i);
				a.move();
				if(CollisionDetector.isColliding(sc, a)) {
					if(!sc.isShieldActive())
						sc.damaged(a.getStrength());
					else
						score += a.getScore();
					deadAsteroid.add(a);
				}
				if(!a.isOnScreen())
					deadAsteroid.add(a);
			}
			
			for(int i = 0; i < deadAsteroid.size(); i++) {
				Asteroid a = deadAsteroid.get(i);
				asteroids.remove(a);
			}
			
			asteroidItr = (asteroidItr + 1) % (ASTEROIDS_RATE_LIMITER - RATE_OF_ASTEROIDS);
			if(asteroidItr == 0)
				asteroids.add(new Asteroid());
		}
		private void updateVillains() {
			
			ArrayList<Villain> deadVillain = new ArrayList<Villain>();
			for(int i = 0; i < villains.size(); i++) {
				Villain v = villains.get(i);
				v.move();
				if(CollisionDetector.isColliding(v, sc)) {
					if(!sc.isShieldActive())
						sc.damaged(v.getStrength());
					else
						score += v.getScore();
					deadVillain.add(v);
				}
				if(!v.isOnScreen()) {
					deadVillain.add(v);
				}
			}
			
			for(int i = 0; i < deadVillain.size(); i++) {
				Villain v = deadVillain.get(i);
				villains.remove(v);
			}
			villainItr = (villainItr + 1) % (VILLAIN_RATE_LIMITER - RATE_OF_VILLAINS);
			if(villainItr == 0)
				villains.add(new Villain(thisPanel));
		}
		private void updateVillainMissiles1() {
			ArrayList<VillainMissile1> deadVillainMissiles = new ArrayList<VillainMissile1>();
			for(int i = 0; i < villainMissiles1.size(); i++) {
				VillainMissile1 v = villainMissiles1.get(i);
				v.move();
				if(CollisionDetector.isColliding(v, sc)) {
					if(!sc.isShieldActive())
						sc.damaged(2);
					deadVillainMissiles.add(v);
				}
				if(!v.isOnScreen()) {
					deadVillainMissiles.add(v);
				}
			}
			for(int i = 0; i < deadVillainMissiles.size(); i++) {
				VillainMissile1 v = deadVillainMissiles.get(i);
				villainMissiles1.remove(v);
			}
		}
	}
}
