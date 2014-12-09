
public class Engine {

	public static final int INVALID = -1;
	public static final int BLANK = 0;
	public static final int RED = 1;
	public static final int BLUE = 2;
	
	public static final int BOARD_HEIGHT = 17;
	public static final int BOARD_WIDTH = 9;
	
	private int[][] state;
	private Board board;
	
	public Engine() {
		board = new Board(5);
		state = board.init();
	}
	
	public static int distance(Hex h1, Hex h2) {
		return distance(h1.getI(), h1.getJ(), h2.getI(), h2.getJ());
	}
	
	public static int distance(int i1, int j1, int i2, int j2) {
		return (Math.abs(i1-i2)/2+Math.abs(j1-j2));
	}
	
	public static void main(String[] args) {
		new Engine();
	}
}
