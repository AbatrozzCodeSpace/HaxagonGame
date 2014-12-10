
public class AIArena {
	public static class ArenaPlayer{
		Player player;
		String name;
		public ArenaPlayer(Player p,String name){
			this.player = p;
			this.name = name;
		}
	}
	public static void main(String[] args){
		//ArenaPlayer p1 = new ArenaPlayer(new ParkMinimaxPlayer(2,100,60,48,30),"oldPark");
		ArenaPlayer p1 = new ArenaPlayer(new ParkMinimaxPlayer(2,100,60,48,30),"oldPark");
		ArenaPlayer p2 = new ArenaPlayer(new ParkMinimaxPlayer(2,100,71,61,44),"newPark");
		p1.name = "oldPark";
		p2.name = "newPark";
		Player red;
		Player blue;
		Player win;
		int game = 10;
		int delay = 1;
		int p1win = 0;
		int p2win = 0;
		for (int i=0;i<game;i++) {
						State s = new State();
						int turn = 0;
						while (true) {
							Move m;
							if(i%2==0){
							red = p1.player;
							blue = p2.player;
							}
							else{
								red = p1.player;
								blue = p2.player;
							}
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
								if (s.whoseTurn().equals("red")) {
									blueScore = 58 - redScore;
								} else {
									redScore = 58 - blueScore;
								}

							}
							if (blueScore + redScore == 58) {
								{
									if (blueScore > redScore) {
										win = blue;
									} else {
										if(blueScore<redScore){
										win = red;
										}
										else{
											win = null;
											System.out.print("draw");
										}
									}
									if(win == p1.player){
										System.out.print(p1.name);
										p1win+=1;
									}
									else{
										if(win == p2.player){
										System.out.print(p2.name);
										p2win+=1;
										}
									}
									System.out.println(" : "+redScore + " - " +blueScore + " : " + turn + " turns");
								}
								break;
							}
							s.applyMove(m);
							turn++;
							// System.out.println("move "+(s.getnBlue()+s.getnRed()));
						}
					}
		System.out.println("------- result -----------");
		System.out.println(p1.name+ " : "+p1win);
		System.out.println(p2.name+ " : "+p2win);
		System.out.println("draw : "+(game-p1win-p2win));
				}
}
