/* Liesbeth Flobbe
 * Hexxagon game
 * file: Hexpos.java
 */

/* The class Hexpos is used to represent positions that may or may not
 * be on the board. The class does not forbid you from representing 
 * positions outside the board, although it will not return such positions
 * (in function that return positions).
 */

public class Hexpos {
    private int row;
    private int col;

    public Hexpos(int r, int c) {
	row = r;
	col = c;
    }

    public int row() {
	return row;
    }

    public int col() {
	return col;
    }

    /* Two Hexpos-object are equal iff their row and col are equal
     * (this overrides the default method in Object, and is used
     *  by List.contains())
     */
    public boolean equals(Object o) {
	Hexpos other = (Hexpos) o;
	return ((row == other.row) && (col == other.col));
    }

    /* If two objects are equal, they must also produce the same
     * hashCode (used by HashMap). We produce a hashCode that is also
     * different for different object (efficient, but not necessary).
     */
    public int hashCode() {
	return row * 9 + col;
    }

    /* determine if a position is on the board. */
    public boolean onBoard() {
	return (((col + row) % 2 == 0) &&
		((col + row) >= 6)     &&  // northwest border
		((col + row) <= 22)    &&  // northeast border
		(col >= 1)             &&  // west border
		(col <= 9)             &&  // east border
		(row >= 1)             &&  // north border
		(row <= 17)            &&  // south border
		((row - col) <= 12)    &&  // southwest border
		((col - row) <= 4)     &&  // northeast border
		(! hole()));               // not a hole in the board
    }

    /* get a list of all positions on the board */
    public static MyList board() {
	// generate positions for a square board, and let the 
	// onBoard() function filter out the wrong positions
	MyList boardlist = new MyList();

	for (int r = 1; r <= 17; r++) { // for every row
	    int c = ((r % 2 == 0) ? 2 : 1); // what col to start?
	    for (; c <= 9; c += 2) {
		Hexpos h = new Hexpos(r, c);
		if (h.onBoard()) boardlist.add(h);
	    }
	}
	
	return boardlist;
	
    }

    /* the board has three holes in it */
    private boolean hole() {
	return (((row == 7)  && (col == 5))   ||
		((row == 10) && (col == 4))   ||
		((row == 10) && (col == 6)));
    }

    /* get a list of all neighbouring positions */
    public MyList neighbours() {

	// first create a list of neighbours
	MyList neighbourlist = new MyList();
	neighbourlist.add(new Hexpos(row + 1, col + 1));
	neighbourlist.add(new Hexpos(row + 1, col - 1));
	neighbourlist.add(new Hexpos(row - 1, col + 1));
	neighbourlist.add(new Hexpos(row - 1, col - 1));
	neighbourlist.add(new Hexpos(row + 2, col));
	neighbourlist.add(new Hexpos(row - 2, col));

	// and filter out those position that are not on the board
	return neighbourlist.filter(
				    new ListFilter() {
					public boolean test(Object o) {
					    return ((Hexpos) o).onBoard();
					}
				    });
    }	

    /* determine whether 'other' is a neighbour of this Hexpos */
    public boolean neighbour(Hexpos other) {
	// Just check if 'other' occurs in the list of all neighbours
	// (not efficient, but better than dupl. code)

	if (neighbours().contains(other))
	    return true;

	return false;
    }

    /* get a list of all positions that can be reached with a jump */
    public MyList jumpNeighbours() {

	// first create a list of jump-neighbours
	MyList neighbourlist = new MyList();
	neighbourlist.add(new Hexpos(row + 4, col));
	neighbourlist.add(new Hexpos(row - 4, col));
	neighbourlist.add(new Hexpos(row + 2, col + 2));
	neighbourlist.add(new Hexpos(row + 2, col - 2));
	neighbourlist.add(new Hexpos(row - 2, col + 2));
	neighbourlist.add(new Hexpos(row - 2, col - 2));
	neighbourlist.add(new Hexpos(row + 3, col + 1));
	neighbourlist.add(new Hexpos(row + 3, col - 1));
	neighbourlist.add(new Hexpos(row - 3, col + 1));
	neighbourlist.add(new Hexpos(row - 3, col - 1));
	neighbourlist.add(new Hexpos(row, col + 2));
	neighbourlist.add(new Hexpos(row, col - 2));

	// and filter out those position that are not on the board
	return neighbourlist.filter(
				    new ListFilter() {
					public boolean test(Object o) {
					    return ((Hexpos) o).onBoard();
					}
				    });
    }		

    /* determine whether 'other' is a jump-neighbour of this Hexpos */
    public boolean jumpNeighbour(Hexpos other) {
	if (jumpNeighbours().contains(other))
	    return true;

	return false;
    }

    public String toString() {
	return "(" + row + ", " + col + ")";
    }

    public static void main(String args[]) {
	System.out.println(Hexpos.board());
    }

}

