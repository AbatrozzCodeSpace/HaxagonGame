import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * A Swing program that demonstrates how to create and use JComboBox component.
 * 
 * @author www.codejava.net
 * 
 */
@SuppressWarnings("serial")
public class PopupUI extends JFrame {
	private JButton buttonSelect = new JButton("Select");
	int choose1;
	int choose2;

	public PopupUI() {
		super("Pick an implementation for player");

		setPreferredSize(new Dimension(590, 120));
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		setResizable(false);

		String[] bookTitles = new String[] { "1. Human (interactive) player",
				"2. RandomPlayer (makes random moves)",
				"3. EagerPlayer (picks best move, but does not look ahead",
				"4. MinimaxPlayer, look ahead one move",
				"5. MinimaxPlayer, look ahead two moves",
				"6. ParkKawinSa Edition", "7. Nuttapong Edition", };

		// create a combo box with items specified in the String array:
		final JComboBox<String> typeList = new JComboBox<String>(bookTitles);
		// customize some appearance:
		typeList.setForeground(Color.BLUE);
		typeList.setFont(new Font("Arial", Font.BOLD, 14));
		typeList.setMaximumRowCount(10);
		final JComboBox<String> typeList2 = new JComboBox<String>(bookTitles);
		// customize some appearance:
		typeList2.setForeground(Color.BLUE);
		typeList2.setFont(new Font("Arial", Font.BOLD, 14));
		typeList2.setMaximumRowCount(10);
		// make the combo box editable so we can add new item when needed

		// add an event listener for the combo box
		typeList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				@SuppressWarnings("unchecked")
				JComboBox<String> combo = (JComboBox<String>) event.getSource();
				String selectedBook = (String) combo.getSelectedItem();

				DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) combo
						.getModel();

				int selectedIndex = model.getIndexOf(selectedBook);
				if (selectedIndex < 0) {
					// if the selected book does not exist before,
					// add it into this combo box
					model.addElement(selectedBook);
				}

			}
		});
		typeList2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				@SuppressWarnings("unchecked")
				JComboBox<String> combo = (JComboBox<String>) event.getSource();
				String selectedBook = (String) combo.getSelectedItem();

				DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) combo
						.getModel();

				int selectedIndex = model.getIndexOf(selectedBook);
				if (selectedIndex < 0) {
					// if the selected book does not exist before,
					// add it into this combo box
					model.addElement(selectedBook);
				}

			}
		});

		// add event listener for the button Select
		buttonSelect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				choose1 = typeList.getSelectedIndex() + 1;
				choose2 = typeList2.getSelectedIndex() + 1;
				JOptionPane.showMessageDialog(
						PopupUI.this,
						"Player 1 is " + (String) typeList.getSelectedItem()
								+ "\nPlayer 2 is "
								+ (String) typeList2.getSelectedItem());
				setVisible(false);
				Hexxagon.hexxThread.interrupt();
			}
		});

		// add components to this frame
		add(new JLabel("Player 1"));
		add(typeList);
		add(buttonSelect);
		add(new JLabel("Player 2"));
		add(typeList2);

		pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}
}