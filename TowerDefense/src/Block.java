import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Block extends Rectangle {

	private static final long serialVersionUID = 1L;
	public Rectangle towerSquare;
	public static int towerSquareSize = 104;
	public int airId;
	public int groundId;

	public int shotMob = -1;
	public boolean isShooting = false;

	public Block(int x, int y, int width, int height, int groundId, int airId) {
		setBounds(x, y, width, height);
		towerSquare = new Rectangle(x - towerSquareSize / 2, y - towerSquareSize / 2, width + towerSquareSize, height + towerSquareSize);
		this.airId = airId;
		this.groundId = groundId;
	}

	public void draw(Graphics g) {
		g.drawImage(Screen.tilesetGround[groundId], x, y, width, height, null);

		if (airId != Value.airAir) {
			g.drawImage(Screen.tilesetAir[airId], x, y, width, height, null);
		}
	}

	public double loseFrame = 1, loseTime = 1 / (double) (Screen.fps);

	public void physics() {
		if (shotMob != -1 && towerSquare.intersects(Screen.mobs[shotMob])) {
			isShooting = true;
		} else
			isShooting = false;

		if (!isShooting) {
			if (airId != Value.airAir && airId != Value.airCave) {
				for (int i = Screen.mobs.length - 1; i >= 0; i--) {
					if (Screen.mobs[i].inGame) {
						if (towerSquare.intersects(Screen.mobs[i])) {
							isShooting = true;
							shotMob = i;
						}
					}
				}
			}
		}
		if (isShooting) {
			if (loseFrame >= loseTime) {
				Screen.mobs[shotMob].loseHealthEnemy1(airId);
				loseFrame -= loseTime;
			} else {
				loseFrame++;
			}

			if (Screen.mobs[shotMob].isDead()) {
				shotMob = -1;
				isShooting = false;
				loseFrame = 1;
			}
		}
	}

	public void fight(Graphics g) {
		if (Screen.isDebug) {
			if (airId == Value.airTowerLaser) {
				g.setColor(new Color(0, 0, 0));
				g.drawRect(towerSquare.x, towerSquare.y, towerSquare.width, towerSquare.height);
			}
		}
		if (isShooting) {
			g.setColor(new Color(255, 255, 0));
			g.drawLine(x + width / 2, y + height / 2, Screen.mobs[shotMob].x + Screen.mobs[shotMob].width / 2, Screen.mobs[shotMob].y + Screen.mobs[shotMob].height / 2);
		}

	}
}
