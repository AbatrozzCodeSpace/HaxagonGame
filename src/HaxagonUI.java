import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class HaxagonUI extends JPanel {

	// firstPage
	private ImageIcon CP38 = new ImageIcon(getClass().getResource(
			"Pic/CP38.png"));
	private ImageIcon bgIcon = new ImageIcon(getClass().getResource(
			"Pic/haxagonMainmenu.png"));

	private ImageIcon startIcon = new ImageIcon(getClass().getResource(
			"Pic/start.gif"));
	private ImageIcon startRollOverIcon = new ImageIcon(getClass().getResource(
			"Pic/startred.gif"));

	private ImageIcon aboutIcon = new ImageIcon(getClass().getResource(
			"Pic/aboutus.gif"));
	private ImageIcon aboutRollOverIcon = new ImageIcon(getClass().getResource(
			"Pic/aboutusred.gif"));

	private ImageIcon exitIcon = new ImageIcon(getClass().getResource(
			"Pic/exit.gif"));
	private ImageIcon exitRollOverIcon = new ImageIcon(getClass().getResource(
			"Pic/exitred.gif"));

	private JButton startButton = new JButton(startIcon);
	private JButton aboutButton = new JButton(aboutIcon);
	private JButton exitButton = new JButton(exitIcon);

	private JLabel bgLabel = new JLabel(bgIcon);

	private PopupUI popup;

	public HaxagonUI() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		Point hotSpot = new Point(0,0);
		Image cus = toolkit.getImage(getClass().getResource("Pic/iconse.png"));
		//Image curs = toolkit.getImage(getClass().getResource("Pic/Wand.png"));
		//Cursor oriCursor = toolkit.createCustomCursor(curs, hotSpot, "wand"); 
		//setCursor(oriCursor);
	     Cursor GameCursor = toolkit.createCustomCursor(cus, hotSpot, "wand");  //this command is the killer
	     startButton.setCursor(GameCursor);
	     aboutButton.setCursor(GameCursor);
	     exitButton.setCursor(GameCursor);


		 
		killBorder(startButton);
		killBorder(aboutButton);
		killBorder(exitButton);

		startButton.setRolloverIcon(startRollOverIcon);
		aboutButton.setRolloverIcon(aboutRollOverIcon);
		exitButton.setRolloverIcon(exitRollOverIcon);

		setSize(800, 600);

		createFirstTitle();

		setVisible(true);

		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameLoopState.effector.openEffect("charge.wav");
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				popup = new PopupUI();
				popup.setVisible(true);
			}
		});

		aboutButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				GameLoopState.effector.openEffect("charge.wav");
				JOptionPane
						.showMessageDialog(
								null,
								"Created by \n5430295521 Nontawat Charoenphakdee  \n5430297821 Noppayut Sriwatanaksakdi  \n5430305121 Nawapat Mahatanarat \n5430376721 Pongsathorn Panyanithisakul \n5430560821 Wichayut Eaksarayut \n5431028521 Park Netrakom \n5431003821 Kawin Worrasangasilpa \n5431019921 Nuttapong Chairatanakul \n5431040021 Sarin Durongdamrongchai    \nDepartment of Computer Engineering\nChulalongkorn University\nYear 2557",
								"About Us", 1, CP38);

			}
		});

		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameLoopState.effector.openEffect("charge.wav");
				String[] choices = { "Yes", "No" };
				int response = JOptionPane.showOptionDialog(null // Center in
																	// window.
						, "Are you sure you want to quit game?" // Message
						, "Confirmation" // Title in titlebar
						, JOptionPane.YES_NO_OPTION // Option type
						, JOptionPane.PLAIN_MESSAGE // messageType
						, null // Icon (none)
						, choices // Button text as above.
						, "None of your business" // Default button's label
				);
				if (response == 0){

					System.exit(0);
				}

			}
		});

	}

	public void createFirstTitle() {
		removeAll();
		setLayout(null);

	
		add(startButton);
		add(aboutButton);
		add(exitButton);
		add(bgLabel);
		add(new JLabel());
		Dimension size = startButton.getPreferredSize();
		startButton.setBounds(getWidth() / 2 - size.width + 130, 365,
				size.width, size.height);
		size = aboutButton.getPreferredSize();
		aboutButton.setBounds(getWidth() / 2 - size.width + 100, 435,
				size.width, size.height);
		size = exitButton.getPreferredSize();
		exitButton.setBounds(getWidth() / 2 - size.width + 110, 505, size.width,
				size.height);
		size = bgLabel.getMaximumSize();
		bgLabel.setBounds(0, 0, size.width, size.height);

		repaint();
	}

	public void killBorder(JButton x) {
		x.setBorder(BorderFactory.createEmptyBorder());
		x.setContentAreaFilled(false);
		x.setFocusPainted(false);
	}

	public int getP1() {
		return popup.choose1;
	}
	
	public int getP2() {
		return popup.choose2;
	}
}
