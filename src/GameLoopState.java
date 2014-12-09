
public class GameLoopState {
	public enum GameState {
		MAINMENU,
		IN_GAME,
		GAME_OVER,
	}
	
	public GameState gameState;
	
	public GameLoopState() {
		gameState = GameState.MAINMENU;
	}
}
