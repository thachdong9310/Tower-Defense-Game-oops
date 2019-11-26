import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileInputStream;


import javax.swing.JFrame;

public class Frame extends JFrame {

	private static final long serialVersionUID = 1L;
	public static String title = "Tower Defense";
	public static Dimension size = new Dimension(1000, 660);



	public Frame() {
		Sound_cdjv sound=new Sound_cdjv("res/audio1.wav");
		sound.start();

				setTitle(title);
				setSize(size);
				setResizable(true);
				setLocationRelativeTo(null);
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				init();
			}

			public void init() {
				setLayout(new GridLayout(1, 1, 0, 0));

				Screen screen = new Screen(this);
				add(screen);

				setVisible(true);
			}

			public static void main(String[] args) {
				Frame frame = new Frame();
			}
		}
