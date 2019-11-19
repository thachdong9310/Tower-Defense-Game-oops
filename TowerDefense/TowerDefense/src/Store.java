import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Store {
	public static int shopWidth = 8;
	public static int buttonSize = 52;
	public static int cellSpace = 2;
	public static int largeCellSpace = 21;
	public static int iconSize = 20;
	public static int iconSpace = 6;
	public static int iconTextY = 15;
	public static int itemIn = 4;
	public static int heldId = -1, realId = -1;
	public static int[] buttonId = { Value.airTowerLaser, Value.airTowerLaser, Value.airAir, Value.airAir, Value.airAir, Value.airAir, Value.airAir, Value.airTrashCan };
	public static int[] buttonPrice = { 10, 15, 0, 0, 0, 0, 0, 0 };

	public Rectangle[] button = new Rectangle[shopWidth];
	public Rectangle buttonHealth, buttonCoins;

	public boolean holdsItem = false;

	public Store() {
		define();
	}

	public void click(int mouseButton) {
		if (mouseButton == 1) {
			for (int i = 0; i < button.length; i++) {
				if (button[i].contains(Screen.mse)) {
					if (buttonId[i] != Value.airAir) {
						if (buttonId[i] == Value.airTrashCan) {
							holdsItem = false;
							heldId = Value.airAir;
						} else {
							heldId = buttonId[i];
							realId = i;
							holdsItem = true;
						}
					}
				}
			}

			if (holdsItem) {
				if (Screen.coins >= buttonPrice[realId]) {
					for (int y = 0; y < Screen.room.block.length; y++) {
						for (int x = 0; x < Screen.room.block[0].length; x++) {
							if (Screen.room.block[y][x].contains(Screen.mse)) {
								if (Screen.room.block[y][x].groundId != Value.groundRoad && Screen.room.block[y][x].airId == Value.airAir) {
									Screen.room.block[y][x].airId = heldId;
									Screen.coins -= buttonPrice[realId];
								}
							}
						}
					}
				}
			}
		}
	}

	public void define() {
		for (int i = 0; i < button.length; i++)
			button[i] = new Rectangle(Screen.myWidth / 2 - shopWidth * (buttonSize + cellSpace) / 2 + (buttonSize + cellSpace) * i, Screen.room.block[Screen.room.worldHeight - 1][0].y + Screen.room.blockSize + largeCellSpace, buttonSize, buttonSize);
		buttonHealth = new Rectangle(Screen.room.block[0][0].x - 1, button[0].y, iconSize, iconSize);
		buttonCoins = new Rectangle(Screen.room.block[0][0].x - 1, button[0].y + button[0].height - iconSize, iconSize, iconSize);
	}

	public void draw(Graphics g) {

		for (int i = 0; i < button.length; i++) {
			if (button[i].contains(Screen.mse)) {
				g.setColor(new Color(255, 255, 255, 150));
				g.fillRect(button[i].x, button[i].y, button[i].width, button[i].height);
			}
			g.drawImage(Screen.tilesetRes[0], button[i].x, button[i].y, button[i].width, button[i].height, null);
			if (buttonId[i] != Value.airAir) g.drawImage(Screen.tilesetAir[buttonId[i]], button[i].x + itemIn, button[i].y + itemIn, button[i].width - itemIn * 2, button[i].height - itemIn * 2, null);
			if (buttonPrice[i] > 0) {
				g.setColor(new Color(255, 255, 255));
				g.setFont(new Font("Courier New", Font.BOLD, 14));
				g.drawString("$" + buttonPrice[i], button[i].x + itemIn, button[i].y + itemIn + 10);
			}
		}

		g.drawImage(Screen.tilesetRes[2], buttonHealth.x, buttonHealth.y, buttonHealth.width, buttonHealth.height, null);
		g.drawImage(Screen.tilesetRes[1], buttonCoins.x, buttonCoins.y, buttonCoins.width, buttonCoins.height, null);
		g.setFont(new Font("Courier New", Font.BOLD, 14));
		g.setColor(new Color(255, 255, 255));
		g.drawString("" + Screen.life, buttonHealth.x + buttonHealth.width + iconSpace, buttonHealth.y + iconTextY);
		g.drawString("$" + Screen.coins, buttonCoins.x + buttonCoins.width + iconSpace, buttonCoins.y + iconTextY);

		if (holdsItem) g.drawImage(Screen.tilesetAir[heldId], Screen.mse.x - (button[0].width - itemIn * 2) / 2 + itemIn, Screen.mse.y - (button[0].height - itemIn * 2) / 2 + itemIn, button[0].width - itemIn * 2, button[0].height - itemIn * 2, null);
	}
}
