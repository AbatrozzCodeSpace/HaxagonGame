/* Liesbeth Flobbe
 * Hexxagon game
 * file: AppWindow.java
 */

/* Create an application window */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AppWindow extends JFrame {
	State state;
	GameLoopState gameLoop;
	Board board;

	public AppWindow(Board board) {
		this.board = board;
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
	}

	public AppWindow(State s) {
		state = s;
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
	}

	public void paint(Graphics g) {
		// call State's paint
		state.paint(board);
	}

	public void setState(State s) {
		state = s;
	}
	
	public void setGameLoopState( GameLoopState gameLoop ) {
		this.gameLoop = gameLoop;
	}
	
	public void setBoard( Board board ){
		this.board= board;
	}
	
	public void setPanel( JPanel panel ){
		this.remove(0);
		this.add(panel);
	}
} 
