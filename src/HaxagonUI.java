import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HaxagonUI extends JFrame {

	private int currentPage = 0; // 0 = Title 1 = admin 2 =login page
	private Container pane = getContentPane();

	// firstPage
	private ImageIcon CP38 = new ImageIcon(getClass().getResource(
			"Pic/CP38.png"));
	private ImageIcon bgIcon = new ImageIcon(getClass().getResource(
			"Pic/haxagonMainmenu.png"));


	private ImageIcon startIcon = new ImageIcon(getClass().getResource(
			"Pic/startIcon.png"));
	private ImageIcon startRollOverIcon = new ImageIcon(getClass().getResource(
			"Pic/startRollOverIcon.png"));

	private ImageIcon aboutIcon = new ImageIcon(getClass().getResource(
			"Pic/aboutIcon.png"));
	private ImageIcon aboutRollOverIcon = new ImageIcon(getClass().getResource(
			"Pic/aboutRollOver.png"));

	private ImageIcon exitIcon = new ImageIcon(getClass().getResource(
			"Pic/exitIcon.png"));
	private ImageIcon exitRollOverIcon = new ImageIcon(getClass().getResource(
			"Pic/exitRollOver.png"));

	private JButton startButton = new JButton(startIcon);
	private JButton aboutButton = new JButton(aboutIcon);
	private JButton exitButton = new JButton(exitIcon);

	private JLabel bgLabel = new JLabel(bgIcon);

	// admin page

	// login page

	public HaxagonUI() {

		killBorder(startButton);
		killBorder(aboutButton);
		killBorder(exitButton);

		startButton.setRolloverIcon(startRollOverIcon);
		aboutButton.setRolloverIcon(aboutRollOverIcon);
		exitButton.setRolloverIcon(exitRollOverIcon);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Haxagon");
		setSize(800, 600);

		createFirstTitle();

		setVisible(true);
		setResizable(false);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception ex) {
					ex.printStackTrace();
				}

						new PopupUI(pane).setVisible(true);
				
		
			}
		});

		aboutButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JOptionPane
						.showMessageDialog(
								null,
								"Created by \n5430295521 Nontawat Charoenphakdee  \n5430297821 Noppayut Sriwatanaksakdi  \n5430305121 Nawapat Mahatanarat \n5430376721 Pongsathorn Panyanithisakul \n5430560821 Wichayut Eaksarayut \n5430376721 Pongsathorn Panyanithisakul \n5431028521 Park Netrakom \n5431003821 Kawin Worrasangasilpa \n5431019921 Nuttapong Chairatanakul \n5431040021 Sarin Durongdumrongchai    \nDepartment of Computer Engineering\nChulalongkorn University\nYear 2557",
								"About Us", 1, CP38);

			}
		});

		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pane.repaint();
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
				if (response == 0)
					System.exit(0);
				pane.repaint();
			}
		});

		setLocationRelativeTo(null);

	}

	public void createFirstTitle() {
		currentPage = 0;
		pane.removeAll();
		pane.add(startButton);
		pane.add(aboutButton);
		pane.add(exitButton);

		pane.add(bgLabel);
		pane.add(new JLabel());
		Dimension size = startButton.getPreferredSize();
		startButton.setBounds(getWidth() / 2 - size.width + 55, 380,
				size.width, size.height);
		size = aboutButton.getPreferredSize();
		aboutButton.setBounds(getWidth() / 2 - size.width + 55, 425,
				size.width, size.height);
		size = exitButton.getPreferredSize();
		exitButton.setBounds(getWidth() / 2 - size.width + 55, 465, size.width,
				size.height);
		size = bgLabel.getPreferredSize();
		bgLabel.setBounds(0, 0, size.width, size.height);

		pane.repaint();
	}

	public void killBorder(JButton x) {
		x.setBorder(BorderFactory.createEmptyBorder());
		x.setContentAreaFilled(false);
		x.setFocusPainted(false);
	}

	public static void main(String[] args) {
		HaxagonUI game = new HaxagonUI();
	}
}
