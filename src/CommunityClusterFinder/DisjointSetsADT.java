package CommunityClusterFinder;

/**
 * An interface that defines the abstract data type for a disjoint set
 * collection whose sets hold elements with type E
 */
public interface DisjointSetsADT<E> {

    /**
     * Creates a new set containing just the element x where x is presumed not
     * to be in any set in the collection
     *
     * @param x The element to place in the set
     * @return A representative of the set (must be x)
     */
    public E makeSet(E x);

    /**
     * Forms the union of the sets which currently contain the elements x and y
     *
     * @param x, y Elements in each set to union (merge) together
     * @return A representative of the set
     */
    public E union(E x, E y);

    /**
     * Returns a representative of the set which currently contains x
     *
     * @param x The element in the set
     * @return A representative of the set
     */
    public E findSet(E x);
}
