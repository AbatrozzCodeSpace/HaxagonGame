import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TreeNode {
    static Random r = new Random();
    static int nActions = 100;
    static double epsilon = 1e-6;
    public static int expandThd = 10;
    public static int maxDepth = 0;
    private static int acceptDiff = 10;
    public static int nowScore = 0;
    
    private State state;
    private Move move;
    private String me;

    TreeNode[] children;
    double nVisits, totValue;

    public void selectAction() {
        List<TreeNode> visited = new LinkedList<TreeNode>();
        TreeNode cur = this;
        visited.add(this);
        int depth = 0;
        while (!cur.isLeaf()) {
            cur = cur.select();
            visited.add(cur);
            depth+=1;
        }
        if(depth<maxDepth && cur.nVisits>expandThd){
	        cur.expand();
	        TreeNode newNode = cur.select();
	        visited.add(newNode);
        }
        double value = randomSimulate(cur.state);
        for (TreeNode node : visited) {
            // would need extra logic for n-player game
            node.updateStats(value);
        }
    }

    public boolean expand() {
        children = new TreeNode[nActions];
        MyList moves = state.findMoves();
        if(moves.size()==0)
        	return false;
        
        Iterator it = moves.iterator();
        int i=0;
        while(it.hasNext()){
        	Move newmove = (Move) it.next();
        	children[i] = new TreeNode();
        	children[i].state = state.tryMove(newmove);
        	children[i].move = newmove;
        	children[i].me = me;
        	i++;
        }
        return true;
    }

    private TreeNode select() {
        TreeNode selected = null;
        double bestValue = Double.MIN_VALUE;
        for (TreeNode c : children) {
        	if(c!=null){
	            double uctValue = c.totValue / (c.nVisits + epsilon) +
	                       Math.sqrt(Math.log(nVisits+1) / (c.nVisits + epsilon)) +
	                           r.nextDouble() * epsilon;
	            // small random number to break ties randomly in unexpanded nodes
	            if (uctValue > bestValue) {
	                selected = c;
	                bestValue = uctValue;
	            }
        	}
        	else{
        		break;
        	}
        }
        return selected;
    }

    public boolean isLeaf() {
        return children == null;
    }

    public void updateStats(double value) {
        nVisits++;
        totValue += value;
    }

    public int arity() {
        return children == null ? 0 : children.length;
    }

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Move getMove() {
		return move;
	}

	public void setMove(Move move) {
		this.move = move;
	}
	
    private int randomSimulate(State s){
    	while(true){
    		Move m = s.findRandomMove(s.whoseTurn());
    		if(m==null){
    			if(s.getnBlue()+s.getnRed() == 58){
    				if(me.equals("red")){
    					return s.getnRed()>s.getnBlue()?1:0;
    				}
    				else{
    					return s.getnBlue()>s.getnRed()?1:0;
    				}
    			}
    			else return s.whoseTurn().equals(me)?0:1;
    		}
    		int score = (me=="red")?s.getnRed()-s.getnBlue():s.getnBlue()-s.getnRed();
    		if(s.whoseTurn().equals(me)){
    			if(score<nowScore-acceptDiff){
    				return 0;
    			}
    		}
    		else{
    			if(score>nowScore+acceptDiff){
    				return 1;
    			}
    		}
    		s = s.tryMove(m);
    	}
    }
    
    public void setPlayer(String me){
    	this.me = me;
    }
    
    public String getPlayer(){
    	return this.me;
    }
    
}