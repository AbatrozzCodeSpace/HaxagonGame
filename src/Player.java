/* Liesbeth Flobbe
 * Hexxagon game
 * file: Player.java
 */

/* A Player must be able to generate a move for the player on turn in a
 * given State. The move *must* be legal (I see no way to handle illegal
 * moves without possible end-less loops, so the program stops if it
 * happens). You can use all the functionality of State to generate
 * moves (but don't apply a move, return it!).
 *
 * A Player is not aware of it's identity as "red" or "blue". It should be
 * able to generate moves for both, and even play against itself. A Player
 * simply implements a strategy for choosing the best move.
 */

public interface Player {
    public Move chooseMove(State s);
}
