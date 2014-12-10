/* Liesbeth Flobbe
 * Hexxagon game
 * file: Hexxagon.java
 */

/* The actual game... */


public class Hexxagon {
	public static final int INVALID = -1;
	public static final int BLANK = 0;
	public static final int RED = 1;
	public static final int BLUE = 2;
	
	public static Player p1, p2;
	
	static HaxagonUI ui;
	static State state;
	static Board board;
	static GameLoopState gameLoop;
	static Thread hexxThread;
	static Thread arbiThread;
	static Arbiter arbi;
	
	
	public static void main(String args[]) {
		hexxThread = new Thread(new Runnable() {
			@Override
			public void run() {
				state = new State();
				ui = new HaxagonUI();
				board = new Board(5, state, ui);
				synchronized (state) {
					try {
						state.wait();
					} catch (InterruptedException e) {
					}
				}
				board.gotoMain();
				gameLoop = new GameLoopState();
				System.out.println("P1="+ui.getP1()+" P2="+ui.getP2());
				runArbi();
			}
		});
		hexxThread.start();
	}
	
	public static void runArbi() {
		if(arbiThread != null) {
			arbiThread.interrupt();
		}
		
		arbiThread = new Thread(new Runnable() {
			@Override
			public void run() {
				p1 = getPlayer(ui.getP1());
				p2 = getPlayer(ui.getP2());
				arbi = new Arbiter(p1, p2, 1000, 1000, gameLoop, state, board);
				arbi.showGame();
			}
		});
		arbiThread.start();
	}
	
	public static void resetMatch(){
		System.out.println("Restart");
		state = new State();
		board.setGameState(state);
		runArbi();
	}
	public static Player getPlayer(int player) {
		Player p;
		switch (player) {
		case 1:
			p = new Human();
			break;
		case 2:
			p = new RandomPlayer();
			break;
		case 3:
			p = new EagerPlayer();
			break;
		case 4:
			p = new MinimaxPlayer(1);
			break;
		case 5:
			p = new MinimaxPlayer(2);
			break;
		default:
			p = new Human();
		}
		return p;
	}
}
