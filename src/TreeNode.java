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
    private static int acceptDiff = 20;
    public static int nowScore = 0;
    private static boolean print = false;
    public static int count = 0;
    
    private State state;
    private Move move;
    private String me;

    TreeNode[] children;
    double nVisits=0;
    double totValue=0;

    public void selectAction() {
        List<TreeNode> visited = new LinkedList<TreeNode>();
        TreeNode cur = this;
        visited.add(this);
        int depth = 0;
        while (cur.children!=null) {
            cur = cur.select();
            visited.add(cur);
            depth+=1;
            if(cur==null) return;
        }
        if(depth<maxDepth && cur.nVisits>expandThd){
        	
	        if(!cur.expand()){
	        	if(cur.state.whoseTurn().equals(me)){
	        		cur.totValue = -100;
	        	}
	        	else{
	        		cur.totValue = 100;
	        	}
	        	return;
	        }
	        depth+=1;
        	print = false;
	        cur = cur.select();
	        visited.add(cur);
	        if(cur==null){
	        	System.out.println("null new node");
	        }
	        print = false;
        }

        double value = randomSimulate(cur.state);
        //System.out.println("depth "+depth);
        for (TreeNode node : visited) {
            // would need extra logic for n-player game
            node.updateStats(value);
        }
    }

    public boolean expand() {
        children = new TreeNode[nActions];
        MyList moves = state.findMoves();
        if(moves.size()==0){
        	return false;
        }
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
    	if(print){
    		System.out.println("check select");
    	}
        TreeNode selected = null;
        double bestValue = 0;
        if(this.state.whoseTurn().equals(me))
        	bestValue = Double.MIN_VALUE;
        else
        	bestValue = 100000;
        for (TreeNode c : children) {
        	if(c!=null){
        		
	            double uctValue = c.totValue / (c.nVisits + epsilon) +
	                       Math.sqrt(Math.log(nVisits+1) / (c.nVisits + epsilon)) +
	                           r.nextDouble() * epsilon;
	            if(c.nVisits<1){
	            	if(state.whoseTurn().equals(me))
	            		uctValue = 10+Math.random();
	            	else
	            		uctValue = -10+Math.random();
        		}
	            // small random number to break ties randomly in unexpanded nodes
	            if(this.state.whoseTurn().equals(me)){
		            if (uctValue > bestValue) {
		                selected = c;
		                bestValue = uctValue;
		            }
	            }
	            else{
	            	if (uctValue < bestValue) {
		                selected = c;
		                bestValue = uctValue;
		            }
	            }
	            if(print){
	            	System.out.println(this.state.whoseTurn().equals(me)+" "+uctValue + " "+bestValue);
	            }
        	}
        	else{
        		break;
        	}
        }
        return selected;
    }

    public boolean isLeaf() {
        return children == null || totValue>-0.5;
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
	
    private double randomSimulate(State s){
    	while(true){
    		Move m = s.findRandomMove(s.whoseTurn());
    		if(m==null){
    			if(s.getnBlue()+s.getnRed() == 58){
    				if(me.equals("red")){
    					//return s.getnRed()>s.getnBlue()?1:0;
    					return sigmoid(s.getnRed()-s.getnBlue());
    				}
    				else{
    					//return s.getnBlue()>s.getnRed()?1:0;
    					return sigmoid(s.getnBlue()-s.getnRed());
    				}
    			}
    			else return s.whoseTurn().equals(me)?0:1;
    		}
    		s = s.tryMove(m);
    		int score = (me.equals("red"))?s.getnRed()-s.getnBlue():s.getnBlue()-s.getnRed();
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
    	}
    }
    
    public void setPlayer(String me){
    	this.me = me;
    }
    
    public String getPlayer(){
    	return this.me;
    }
    
    public static final double sigmoid(double x){
    	return 1.0/(1.0+Math.exp(-x));
    }
    
}