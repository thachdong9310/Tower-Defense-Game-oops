import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Screen extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	public Thread thread = new Thread(this);
	private Frame frame;

	public static double fps = 60.0;

	public static Image[] tilesetGround = new Image[100];
	public static Image[] tilesetAir = new Image[100];
	public static Image[] tilesetRes = new Image[100];
	public static Image[] tilesetMob = new Image[100];

	public static int myWidth, myHeight;
	public static int coins = 10, life = 100;
	public static int killed = 0, killsToWin = 0;
	public static int level = 1, maxLevel = 3;

	public static boolean isFirst = true;
	public static boolean isDebug = false;
	public static boolean won = false;

	public static Point mse = new Point(0, 0);

	public static Room room;
	public static Save save;
	public static Store store;

	public static Mob[] mobs = new Mob[100];

	public Screen(Frame frame) {
		this.frame = frame;
		frame.addMouseListener(new KeyHandler());
		frame.addMouseMotionListener(new KeyHandler());
		thread.start();

	}

	public static void hasWon() {
		if (killed >= killsToWin) {
			killed = 0;
			won = true;
		}
	}

	public void define() {
		room = new Room();
		save = new Save();
		store = new Store();


		tilesetGround[0]= new ImageIcon("res/grass1.png").getImage();
		tilesetGround[1]= new ImageIcon("res/grass3.png").getImage();
		tilesetGround[2]= new ImageIcon("res/grass2.png").getImage();
		tilesetGround[3]= new ImageIcon("res/grass4.png").getImage();
		tilesetGround[4]= new ImageIcon("res/grass5.png").getImage();
		tilesetGround[5]= new ImageIcon("res/grass6.png").getImage();
		tilesetGround[6]= new ImageIcon("res/grass7.png").getImage();
		tilesetGround[7]= new ImageIcon("res/grass8.png").getImage();
		tilesetGround[8]= new ImageIcon("res/grass9.png").getImage();
		tilesetGround[9]= new ImageIcon("res/grass10.png").getImage();
		tilesetGround[10]= new ImageIcon("res/grass11.png").getImage();
		tilesetGround[11]= new ImageIcon("res/grass12.png").getImage();

		tilesetAir[0] = new ImageIcon("res/home.png").getImage();
		tilesetAir[1] = new ImageIcon("res/click.png").getImage();
		tilesetAir[2] = new ImageIcon("res/tower1.png").getImage();
		tilesetAir[3] = new ImageIcon("res/tower2.png").getImage();
		tilesetAir[4] = new ImageIcon("res/tower3.png").getImage();

		tilesetRes[0] = new ImageIcon("res/cell.png").getImage();
		tilesetRes[1] = new ImageIcon("res/coin.png").getImage();
		tilesetRes[2] = new ImageIcon("res/heart.png").getImage();

		tilesetMob[0] = new ImageIcon("res/mob.png").getImage();

		save.loadSave(new File("Save/Mission" + level + ".TD"));

		for (int i = 0; i < mobs.length; i++) {
			mobs[i] = new Mob();
		}
	}

	public void paintComponent(Graphics g) {
		if (isFirst) {
			myWidth = getWidth();
			myHeight = getHeight();
			define();

			isFirst = false;
		}

		if (!won) {

			g.setColor(new Color(90, 90, 90));
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(new Color(0, 0, 0));
			g.drawLine(room.block[0][0].x - 1, 0, room.block[0][0].x - 1, room.block[room.worldHeight - 1][0].y + room.blockSize);
			g.drawLine(room.block[0][room.worldWidth - 1].x + room.blockSize, 0, room.block[0][room.worldWidth - 1].x + room.blockSize, room.block[room.worldHeight - 1][0].y + room.blockSize);
			g.drawLine(room.block[0][0].x - 1, room.block[room.worldHeight - 1][0].y + room.blockSize, room.block[0][room.worldWidth - 1].x + room.blockSize, room.block[room.worldHeight - 1][0].y + room.blockSize);

			room.draw(g);

			for (int i = mobs.length - 1; i >= 0; i--) {
				if (mobs[i].inGame) {
					mobs[i].draw(g);
				}
			}

			store.draw(g);


			if (life < 1) {
				g.setColor(new Color(240, 20, 20));
				g.fillRect(0, 0, myWidth, myHeight);
				g.setColor(new Color(255, 255, 255));
				g.setFont(new Font("Courier New", Font.BOLD, 14));
				g.drawString("Game Over", 10, 20);
			}

		} else {
			g.setColor(new Color(255, 255, 255, 255));
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(new Color(0, 0, 0));
			g.setFont(new Font("Courier New", Font.BOLD, 14));
			String str = "";
			if (level < maxLevel)
				str += "Level complete. Please wait for the next level..";
			else
				str += "You won the game! Congratulations! The game will close shortly.";
			g.drawString(str, 10, 20);
		}
	}

	public double spawnTime = 1 * (double) (fps), spawnFrame = spawnTime - fps;

	public void mobSpawner() {
		if (spawnFrame >= spawnTime) {
			for (int i = 0; i < mobs.length; i++) {
				if (!mobs[i].inGame) {
					mobs[i] = new Mob();
					mobs[i].spawnMob(Value.mobGreen);
					break;
				}
			}
			spawnFrame = 1;
		} else {
			spawnFrame++;
		}
	}

	public static double timera = 0;

	public static double winFrame = 1, winTime = 5 * (double) (fps);

	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / fps;
		double delta = 0;
		int updates = 0, frames = 0;

		while (true) {

			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			// Update 60 times a second
			while (delta >= 1) {
				//update();
				timera++;
				updates++;

				if (!isFirst && life > 0 && !won) {
					room.physics();
					mobSpawner();
					for (int i = 0; i < mobs.length; i++) {
						if (mobs[i].inGame) mobs[i].physics(i);
					}
				} else if (won) {
					if (winFrame >= winTime) {
						if (level >= maxLevel) {
							System.exit(0);
						} else {
							won = false;
							level++;
							coins = 10;
							define();
						}
						winFrame = 1;
					} else {
						winFrame++;
					}
				}

				delta--;
			}

			repaint();
			frames++;

			if (System.currentTimeMillis() - timer >= 1000) {
				timer += 1000;
				frame.setTitle(Frame.title + " | ups: " + updates + " | fps: " + frames);
				updates = 0;
				frames = 0;
			}
		}
	}
}
