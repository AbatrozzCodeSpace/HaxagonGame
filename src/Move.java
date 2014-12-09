/* Liesbeth Flobbe
 * Hexxagon game
 * file: Move.java
 */

/* A Move is just a simple record. No functionality is found in this class,
 * since Moves are meaninglesse outside the context of a state. E.g. to
 * find out if a Move is valid, ask a State-object if it's valid for that
 * particular State.
 */

public class Move {
    public Hexpos begin;
    public Hexpos end;
    public String player;

    public Move(Hexpos b, Hexpos e, String p) {
	begin = b;
	end = e;
	player = p;
    }

    public String toString() {
	return "[" + player + "]: " + begin + " -> " + end;
    }
}

