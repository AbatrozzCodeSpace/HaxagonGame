/* Liesbeth Flobbe
 * Hexxagon game
 * file: AppWindow.java
 */

/* Create an application window */
import java.awt.*;
import java.awt.event.*;

public class AppWindow extends Frame {
    State state;

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
	state.paint(g);
    }

    public void setState(State s) {
	state = s;
    }
}
