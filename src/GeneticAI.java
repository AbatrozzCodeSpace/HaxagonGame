import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class GeneticAI {

	static Player red;
	static Player blue;
	static int delay = 1;
	static int PLAYER_SIZE = 12;
	static String boundFile = "GeneticBoundFile";
	static int[] poolUpperBound = { 100, 100, 100, 100 };
	static int[] poolLowerBound = { 100, 0, 0, 0 };

	public static class GeneticAIPlayer {
		Player player;
		int score;
		int[] params;
	}

	public static Comparator<GeneticAIPlayer> GeneticAIComparator = new Comparator<GeneticAIPlayer>() {

		public int compare(GeneticAIPlayer p1, GeneticAIPlayer p2) {
			if (p1.score > p2.score)
				return 1;
			else {
				return (p1.score == p2.score) ? 0 : -1;

			}
		}

	};
	
	public static int randomSlot(int slot,int max,int min){
		max = max+1;
		if(slot==0){
			return (int)(Math.random()*(max-min));
		}
		int randomMax = (slot*(slot+1))/2;
		int rand = (int)(Math.random()*randomMax);
		int check = 0;
		int i=0;
		for(i=1;i<=slot;i++){
			check+=i;
			if(check>rand){
				break;
			}
		}
		if(i>slot){
			System.out.println("bug");
		}
		i-=1;
		int randrange = (max-min) / slot;
		int randrange2 = randrange;
		if(i==slot){
			randrange2 = max - (max-min) / slot * (slot-1);
		}
		return  (int)(Math.random()*randrange2) + randrange * i + min;
	}

	public static void main(String[] args) throws FileNotFoundException,
			UnsupportedEncodingException {

		loadBound();

		// Player[] players = new Player[PLAYER_SIZE];
		// int[][] playersParams = new int[PLAYER_SIZE][4];
		// int[] score = new int[PLAYER_SIZE];

		GeneticAIPlayer[] gaPlayers = new GeneticAIPlayer[PLAYER_SIZE];

		while (true) {
			int[] sumPara = new int[4];
			for (int i = 0; i < PLAYER_SIZE; i++) {
				gaPlayers[i] = new GeneticAIPlayer();
				int max = 100;
				gaPlayers[i].params = new int[4];
				for (int j = 0; j < 4; j++) {
					max = Math.min(max, poolUpperBound[j]);
					if (max < poolLowerBound[j]) {
						j -= 1;
						continue;
					}
					
					if(j<3 && poolLowerBound[j]<poolUpperBound[j+1]){
						gaPlayers[i].params[j] = randomSlot(4-j,max,poolLowerBound[j]);
					}
					else{
					gaPlayers[i].params[j] = (int) (Math.random() * (max - poolLowerBound[j]))
							+ poolLowerBound[j];
					}
					max = gaPlayers[i].params[j];
					System.out.print(gaPlayers[i].params[j] + " ");
				}
				System.out.println();
				gaPlayers[i].player = new ParkMinimaxPlayer(2,
						gaPlayers[i].params[0], gaPlayers[i].params[1],
						gaPlayers[i].params[2], gaPlayers[i].params[3]);
				// gaPlayers[i].player = new MinimaxPlayer(0);
			}
			int game = 0;
			for (int i = 0; i < PLAYER_SIZE; i++) {
				for (int j = 0; j < PLAYER_SIZE; j++) {
					if (i != j) {
						game += 1;
						State s = new State();
						while (true) {
							Move m;
							red = gaPlayers[i].player;
							blue = gaPlayers[j].player;
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

								System.out.println("endGame" + game);
								if (s.whoseTurn().equals("red")) {
									blueScore = 58 - redScore;
								} else {
									redScore = 58 - blueScore;
								}

							}
							if (blueScore + redScore == 58) {
								// for (int k = 0; k < 4; k++) {
								if (blueScore > redScore) {
									// sumPara[k] += playersParams[j][k];
									gaPlayers[j].score += 1;
								} else {
									// sumPara[k] += gaPlayers[i].params[k];
									gaPlayers[i].score += 1;
								}
								// }
								break;
							}
							s.applyMove(m);
							// System.out.println("move "+(s.getnBlue()+s.getnRed()));
						}
					}
				}
			}
			Arrays.sort(gaPlayers,
					Collections.reverseOrder(GeneticAIComparator));
			int paramsShare = 5;
			int paramsDiv = 0;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 4; j++) {
					sumPara[j] += paramsShare * gaPlayers[i].params[j];
				}
				paramsDiv += paramsShare;
				paramsShare -= 2;
			}

			for (int i = 0; i < 4; i++) {
				int length = poolUpperBound[i] - poolLowerBound[i];
				sumPara[i] /= paramsDiv;
				poolUpperBound[i] = sumPara[i] + length / 3;
				poolLowerBound[i] = sumPara[i] - length / 3;
				if (poolLowerBound[i] < 0)
					poolLowerBound[i] = 0;
				if (poolUpperBound[i] > 100)
					poolUpperBound[i] = 100;
			}
			saveBound();
			PLAYER_SIZE = (PLAYER_SIZE>=6)? PLAYER_SIZE-2:PLAYER_SIZE;
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
