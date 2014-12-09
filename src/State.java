/* Liesbeth Flobbe
 * Hexxagon game
 * file: State.java
 */
 
 /* the (actual or possible) state of the game */

import java.util.*;
import java.awt.*;

public class State {
    // We store the state in a hash with the Hexpos's as keys and their
    // owner (none, red, blue) as value.
    private HashMap owner = new HashMap();

    // Some redundant, but useful info: number of 'squares' possessed by
    // each player. (Expensive to calculate from HashMap, so it is 
    // updated with every change.)
    private HashMap nSquares = new HashMap();

    // whose turn it is
    private String turn;

    // construct a state that is a copy of another state
    public State(State other) {

	// We need to manually copy the HashMap (cloning the State
	// object will give a duplicate reference to the HashMap, which is
	// *not* what we want).
	// Hexpos-refs *can* be copied, since Hexpos's do not change after
	// their creation.

	// iterate over other's state
	Set set = other.owner.keySet();
	Iterator it = set.iterator();
	while(it.hasNext()) {
	    Object hexpos = it.next();
	    owner.put(hexpos, other.owner.get(hexpos));
	}

	nSquares.put("red", other.nSquares.get("red"));
	nSquares.put("blue", other.nSquares.get("blue"));
	nSquares.put(null, other.nSquares.get(null));

	turn = other.turn;
    }

    // construct the start state
    public State() {
	// get a list of all Hexpos's
	MyList board = Hexpos.board();

	// push every Hexpos in the hashmap with no owner
	Iterator it = board.iterator();
	while(it.hasNext()) {
	    owner.put(it.next(), null);
	}

	// fill in the beginning positions of both players
	owner.put(new Hexpos(1, 5), "blue");
	owner.put(new Hexpos(13, 1), "blue");
	owner.put(new Hexpos(13, 9), "blue");
	owner.put(new Hexpos(5, 1), "red");
	owner.put(new Hexpos(5, 9), "red");
	owner.put(new Hexpos(17, 5), "red");

	nSquares.put("blue", new Integer(3));
	nSquares.put("red", new Integer(3));
	nSquares.put(null, new Integer(52));

	turn = "red";
    }

    public int getnEmpty() {
	return ((Integer) nSquares.get(null)).intValue();
    }

    public int getnBlue() {
	return ((Integer) nSquares.get("blue")).intValue();
    }

    public int getnRed() {
	return ((Integer) nSquares.get("red")).intValue();
    }

    public String whoseTurn() {
	return turn;
    }

    public String otherPlayer(String s) {
	if (s.equals("red"))
	    return "blue";
	else if (s.equals("blue"))
	    return "red";
	else 
	    return null;
    }

    /* find the owner of a given hexpos */
    public String owner(Hexpos hp) {
	return (String) owner.get(hp);
    }

    /* get a list of all Hex's owned by player */
    public MyList ownees(String player) {
	MyList ownees = new MyList();

	if (! (player.equals("red") || player.equals("blue")))
	    return null;

	// Iterate over all Hexpos's
	Set set = owner.keySet();
	Iterator it = set.iterator();
	while(it.hasNext()) {
	    Hexpos hp = (Hexpos) it.next();
	    if (owner.get(hp) != null && owner.get(hp).equals(player))
		ownees.add(hp);
	}

	return ownees;
    }

    /* change ownership of a given hexpos */
    public boolean changeOwner(String newOwner, Hexpos hp) {
	String oldOwner = (String) owner.get(hp);

	// check if newOwner is a valid string
	if (! (newOwner == null || newOwner.equals("red") || 
	       newOwner.equals("blue")))
	    return false;
	
	// decrease the count of squares of the old owner,
	// and increase for new owner
	int oldN = ((Integer) nSquares.get(oldOwner)).intValue();
	nSquares.put(oldOwner, new Integer(--oldN));

	int newN = ((Integer) nSquares.get(newOwner)).intValue();
	nSquares.put(newOwner, new Integer(++newN));
	
	// now make the actual change in the hashmap
	owner.put(hp, newOwner);

	return true;
    }

    /* take over all neighbouring pieces */
    private boolean takeNeighbours(String player, Hexpos hp) {
	// return immediately if player is not a valid string
	if (! (player.equals("red") || player.equals("blue")))
	    return false;

	// iterate over a list of all neighbours
	MyList neighbours = hp.neighbours();
	Iterator it = neighbours.iterator();
	while (it.hasNext()) {
	    Hexpos neighbour = (Hexpos) it.next();
	    // only 'change' ownership if the square was not empty
	    if (owner(neighbour) != null)
		changeOwner(player, neighbour);
	}
	
	return true;
    }

    /* true iff m is a legal move in the current state */
    public boolean legalMove(Move m) {
	return (  // the player must exist
		(m.player.equals("red") || m.player.equals("blue")) &&
		// it must be his turn
		(m.player.equals(turn)) &&
		// the player must own the piece he wants to move 
		(owner(m.begin) != null) &&
		(owner(m.begin).equals(m.player)) &&
		// the goal position must be empty
		(owner(m.end) == null) &&
		// the goal position can be reached with a step or a jump
		(m.begin.neighbour(m.end) || 
		 m.begin.jumpNeighbour(m.end)));
    }

    public boolean applyMove(Move m) {
	// illegal move
	if (! legalMove(m))
	    return false;

	// in case of a jump move, empty the beginning square
	if (m.begin.jumpNeighbour(m.end))
	    changeOwner(null, m.begin);

	// let the player become owner of the goal square
	changeOwner(m.player, m.end);

	// take over neighbouring pieces
	takeNeighbours(m.player, m.end);

	// change who's turn it is
	turn = otherPlayer(turn);

	return true;
    }

    /* returns a *new* State that results from applying move m, without
     * changing the current object
     */
    public State tryMove(Move m) {
	// copy the state
	State newstate = new State(this);

	if (newstate.applyMove(m))
	    return newstate;
	
	// apparently, move was not succesful
	return null;
    }

    /* find all moves from a given Hexpos */
    public MyList findMovesFrom(Hexpos begin, String player) {
	// check if player owns begin
	if (owner.get(begin) == null || ! owner.get(begin).equals(player))
	    return null;

	// make a list to store the moves
	MyList moves = new MyList();

	// try all possible end positions
	MyList ends = begin.neighbours();    // neighbours
	ends.addAll(begin.jumpNeighbours()); // jump-neighbours
	Iterator it = ends.iterator();
	while(it.hasNext()) {
	    Hexpos end = (Hexpos) it.next();
	    if (owner.get(end) == null)      // check if end pos is empty
		moves.add(new Move(begin, end, player));
	}

	return moves;
    }

    public MyList findMovesFrom(Hexpos begin) {
	return findMovesFrom(begin, turn);
    }

    /* Find all moves to a given Hexpos 
     * If there is a neighbour-step move, only one is returned because
     * they are all equivalent. (and superior to jumps)
     * If there is no neighbour-step move, all jump-moves are returned.
     */
    public MyList findMovesTo(Hexpos end, String player) {
	// check if goal position is empty
	if (owner.get(end) != null)
	    return null;
	
	// make a list to store the moves
	MyList moves = new MyList();

	// try to find *one* neighbour-step move
	MyList neighbours = end.neighbours();      // neighbours
	Iterator it = neighbours.iterator();
	while(it.hasNext()) {
	    Hexpos begin = (Hexpos) it.next();
	    if (owner.get(begin) != null &&        // check if begin pos is
		owner.get(begin).equals(player)) { // owned by player
		moves.add(new Move(begin, end, player));
		return moves;                      // return immediately
	    }
	}
	
	// apparently, no neighbour-step move found -> try jumps
	MyList jumps = end.jumpNeighbours();
	it = jumps.iterator();
	while(it.hasNext()) {
	    Hexpos begin = (Hexpos) it.next();
	    if (owner.get(begin) != null &&        // check if begin pos is
		owner.get(begin).equals(player)) { // owned by player
		moves.add(new Move(begin, end, player));
	    }
	}

	return moves;
	       
    }

    public MyList findMovesTo(Hexpos end) {
	return findMovesTo(end, turn);
    }

    /* Get all moves that player can do.
     * We use findMovesTo, to avoid similar moves (moves that result in the
     * same state.
     */
    public MyList findMoves(String player) {
	// Check if player is valid (so we don't have to worry about
	// null pointers later).
	if (player == null || ! (player.equals("red") || 
			         player.equals("blue")))
	    return null;

	MyList moves = new MyList();

	// Iterate over all Hexpos's
	Set set = owner.keySet();
	Iterator it = set.iterator();
	while(it.hasNext()) {
	    Hexpos hp = (Hexpos) it.next();
	    MyList movesTo = findMovesTo(hp, player);
	    if (movesTo != null)
		moves.addAll(findMovesTo(hp, player));
	}

	return moves;
    }

    public MyList findMoves() {
	return findMoves(turn);
    }

    /* paint a state */
    public void paint(Graphics g) {
	int radius = 50;

	// Iterate over all Hexpos's
	Set set = owner.keySet();
	Iterator it = set.iterator();
	while(it.hasNext()) {
	    Hexpos hp = (Hexpos) it.next();

	    double cart_x = .8660 * radius * hp.col();
	    double cart_y = 15 + .5 * radius * hp.row();
	    
	    g.setColor(Color.black);

	    if (owner.get(hp) != null) {
		g.setColor(owner.get(hp).equals("red") ? Color.red : Color.blue);
		g.fillOval((int) cart_x,
			   (int) cart_y, 
			   (int) (.8 * radius),
			   (int) (.8 * radius));
		
		g.setColor(Color.white);
	    }

	    g.drawString(hp.row() + ", " + hp.col(), 
			 (int) cart_x + 8, 
			 (int) cart_y + 24);
	    g.setColor(Color.black);
	    g.drawOval((int) cart_x,
		       (int) cart_y, 
		       (int) (.8 * radius),
		       (int) (.8 * radius));

	}
	
	g.setColor(Color.red);
	g.drawString("Red: " + nSquares.get("red"), 10, 500);
	g.setColor(Color.blue);
	g.drawString("Blue: " + nSquares.get("blue"), 440, 500);
	
    }
	
		
}
