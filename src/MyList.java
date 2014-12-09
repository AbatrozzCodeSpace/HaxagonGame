/* Liesbeth Flobbe
 * Hexxagon game
 * file: MyList.java
 */

import java.util.*;

/* A class with some utilities that I want to use on lists. */
public class MyList extends ArrayList {
   
    /* 'filter' is similar to 'filter' in Lisp and Perl. That is, it
     * extracts those element of a list for which a certain test condition
     * holds. The test function is passed through an interface.
     * Note that the resulting List consists of Objects that have to be
     * cast back to what they actually are.
     */
    public MyList filter(ListFilter lf) {
	MyList newlist = new MyList();
	Iterator it = iterator();
	while(it.hasNext()) {
	    Object o = it.next();
	    if (lf.test(o)) newlist.add(o);
	}
	return newlist;
    }

    /* 'listToList' is similar to 'map' in Lisp and 'grep' in Perl. It
     * produces a new list by applying a certain function to every element
     * of the old list and inserting the results in the new list.
     */
    public MyList listToList(MapFilter mf) {
	MyList newlist = new MyList();
	Iterator it = iterator();
	while(it.hasNext()) {
	    newlist.add(mf.map(it.next()));
	}
	return newlist;
    }

    /* 'listToMap' works like 'listToList', but instead of a list, it
     * produces a HashMap, whose keys are the elements of the
     * original list, and whose values are the result from a certain 
     * function applied to those elements. This allows you to reconstruct
     * the original list again, for example after sorting the HashMap by it's
     * values.
     * It is assumed that your list contains no duplicates, or that you
     * do not care about losing your duplicates. The resulting Map can
     * have only unique keys, so duplicates will be overwritten.
     */
    public HashMap listToMap(MapFilter mf) {
	HashMap newmap = new HashMap();
	Iterator it = iterator();
	while(it.hasNext()) {
	    Object o = it.next();
	    newmap.put(o, mf.map(o));
	}
	return newmap;
    }
}
