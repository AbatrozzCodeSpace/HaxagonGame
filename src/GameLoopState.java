
public class GameLoopState {
	public static enum GameState {
		MAINMENU,
		IN_GAME,
		GAME_OVER,
	}
	
	public GameState state;
	
	public GameLoopState() {
		state = GameState.IN_GAME;
	}
}
