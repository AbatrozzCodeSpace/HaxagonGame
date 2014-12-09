import java.awt.Color;
import java.awt.Polygon;

@SuppressWarnings("serial")
public class Hex extends Polygon {
	private int i;
	private int j;
	private int xCenter;
	private int yCenter;
	private int value;
	private Color bg;
	
	public Hex(int i, int j, int xCenter, int yCenter) {
		this.i = i;
		this.j = j;
		this.xCenter = xCenter;
		this.yCenter = yCenter;
		value = Engine.BLANK;
		bg = Board.adjColor[0];
	}
	
	/////////// GETTERS AND SETTERS ///////////
	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}
	
	public int getxCenter() {
		return xCenter;
	}

	public int getyCenter() {
		return yCenter;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public String toString() {
		return "bind at "+i+" "+j+" has value = "+value; 
	}

	public Color getBg() {
		return bg;
	}

	public void setBg(Color bg) {
		this.bg = bg;
	}
}
