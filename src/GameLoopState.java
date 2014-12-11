
public class GameLoopState {
	public static enum GameState {
		MAINMENU,
		IN_GAME,
		GAME_OVER,
	}
	public static GameState state;
	public static SongWAV wav= new SongWAV(2);
	public static SongWAV effector= new SongWAV(1);
	public static boolean isChangeOwner = false;
	public static boolean isPlaySound = true;
}
