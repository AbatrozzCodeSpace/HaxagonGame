
public class MixPlayer implements Player{
	MonteCarloPlayer mc;
	MinimaxPlayer mp;
	
	public MixPlayer(int md,int numSimulate) {
		this.mc = new MonteCarloPlayer(md,numSimulate);
		this.mp = new MinimaxPlayer(2);
    }

	@Override
	public Move chooseMove(State s) {
		if(s.getnBlue()+s.getnRed()<25){
			return mp.chooseMove(s);
		}
		else{
			return mc.chooseMove(s);
		}
	}
	
	
}
