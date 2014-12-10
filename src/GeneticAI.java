import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class GeneticAI {

	static Player red;
	static Player blue;
	static int delay = 100;
	static final int PLAYER_SIZE = 8;
	static String boundFile = "GeneticBoundFile";
	static int[] poolUpperBound = { 100, 90, 80, 70 };
	static int[] poolLowerBound = { 100, 50, 30, 25 };

	public static void main(String[] args) throws FileNotFoundException,
			UnsupportedEncodingException {

		loadBound();

		Player[] players = new Player[PLAYER_SIZE];
		int[][] playersParams = new int[PLAYER_SIZE][4];

		while (true) {
			int[] sumPara = new int[4];
			for (int i = 0; i < players.length; i++) {
				for (int j = 0; j < 4; j++) {
					playersParams[i][j] = (int) (Math.random() * (poolUpperBound[j] - poolLowerBound[j]))
							+ poolLowerBound[j];
					System.out.print(playersParams[i][j] + " ");
				}
				System.out.println();
				players[i] = new ParkMinimaxPlayer(2, playersParams[i][0],
						playersParams[i][1], playersParams[i][2],
						playersParams[i][3]);
				// players[i] = new MinimaxPlayer(0);
			}
			int game = 0;
			for (int i = 0; i < players.length; i++) {
				for (int j = 0; j < players.length; j++) {
					if (i != j) {
						game += 1;
						State s = new State();
						while (true) {
							Move m;
							red = players[i];
							blue = players[j];
							// ask the right player to make a move
							if (s.whoseTurn().equals("red")) {
								try {
									Thread.sleep(delay);
								} catch (Exception e) {
								}
								m = red.chooseMove(s);
							} else {
								try {
									Thread.sleep(delay);
								} catch (Exception e) {
								}
								m = blue.chooseMove(s);
							}

							// fail miserably if the move is illegal or if the
							// player fails
							// te
							// produce a move
							int blueScore = s.getnBlue();
							int redScore = s.getnRed();
							if (m == null || !s.legalMove(m)) {

								System.out.println("endGame"
										+ (i * players.length + j));
								if (s.whoseTurn().equals("red")) {
									blueScore = 58 - redScore;
								} else {
									redScore = 58 - blueScore;
								}

							}
							if (blueScore + redScore == 58) {
								for (int k = 0; k < 4; k++) {
									if (blueScore > redScore) {
										sumPara[k] += playersParams[j][k];
									} else {
										sumPara[k] += playersParams[i][k];
									}
								}
								break;
							}
							s.applyMove(m);
							// System.out.println("move "+(s.getnBlue()+s.getnRed()));
						}
					}
				}
			}
			for (int i = 0; i < 4; i++) {
				int length = poolUpperBound[i] - poolLowerBound[i];
				sumPara[i] /= game;
				poolUpperBound[i] = sumPara[i] + length / 4;
				poolLowerBound[i] = sumPara[i] - length / 4;
			}
			saveBound();
		}
	}

	public static boolean loadBound() throws FileNotFoundException {
		File file = new File(boundFile);
		if (file.exists()) {
			// FileReader reader = new FileReader(file);
			Scanner in = new Scanner(file);
			for (int i = 0; i < 4; i++) {
				poolUpperBound[i] = in.nextInt();
				poolLowerBound[i] = in.nextInt();
			}
			in.close();
			return true;
		}
		return false;
	}

	public static void saveBound() throws FileNotFoundException,
			UnsupportedEncodingException {
		PrintWriter w = new PrintWriter(boundFile, "UTF-8");
		for (int i = 0; i < 4; i++) {
			w.println(poolUpperBound[i]);
			w.println(poolLowerBound[i]);
		}
		w.close();
	}
}
