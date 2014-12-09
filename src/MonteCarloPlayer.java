/* Liesbeth Flobbe
 * Hexxagon game
 * file: MinimaxPlayer.java
 */

import java.util.*;

public class MonteCarloPlayer implements Player {
    int maxdepth;
    int numSimulate;
    
    public MonteCarloPlayer(int md,int numSimulate) {
    	this.maxdepth = md;
    	this.numSimulate = numSimulate;
    }
    
    public Move chooseMove(State s) {
    	TreeNode rootNode = new TreeNode();
    	rootNode.setState(s);
    	TreeNode.nowScore = (s.whoseTurn()=="red")?s.getnRed()-s.getnBlue():s.getnBlue()-s.getnRed();
    	if(rootNode.expand()==false){
    		return null;
    	}
    	rootNode.setPlayer(s.whoseTurn());
    	for(int i=0;i<numSimulate;i++){
    		rootNode.selectAction();
    	}
    	double score = 0;
    	Move bestMove = null;
		for(int i=0;i<rootNode.children.length;i++){
			TreeNode cnode = rootNode.children[i];
			if(rootNode.children[i]!=null){
				double cscore = cnode.totValue/cnode.nVisits;
				if(score < cscore){
					score = cscore;
					bestMove = cnode.getMove();
				}
				System.out.println(cscore+" ");
			}
		}
		System.out.println("-----------------");
		System.out.println(score);
		System.out.println("-----------------");
    	return bestMove;
    }
}




