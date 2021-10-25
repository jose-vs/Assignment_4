package CommunityClusterFinder;

/**
 * A class that implements a disjoint set collection using a tree for each set,
 * where each node has a link to the parent node in the tree and the
 * representative is at the root
 * 
 * Reference
 * -------------------------------------------------------------------------------------------
 * Ensor, A. (2021). COMP611 Algorithm Design and Analysis: Disjoint Sets [Course Manual].  
 *       Chapter 5.4, 106 - 108. Blackboard. https://blackboard.aut.ac.nz/
 * 
 * @author Andrew Ensor
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ForestDisjointSets<E> implements DisjointSetsADT<E> {

    private List<Node<E>> repNodes; // root for each tree in forest
    private Map<Node<E>, Integer> setRanks; //each repNode gives set rank
    private Map<E, Node<E>> elementMap; // map of elements to locators

    public ForestDisjointSets() {
        repNodes = new ArrayList<>();
        setRanks = new HashMap<>();
        elementMap = new HashMap<>();
    }

    @Override
    public E makeSet(E x) {
        if(elementMap.containsKey(x)) {
            throw new IllegalArgumentException("element already used");
        }
        Node<E> node = new Node<>(x);
        node.parentNode = node; // parent of the new node is itself  
        repNodes.add(node); // add the root of the new tree to the list
        setRanks.put(node, 0); // initial rank is zero
        elementMap.put(x, node); // add the new element to the map
        return x;
    }

    @Override
    public E union(E x, E y) {
        Node<E> nodeX = elementMap.get(x);
        Node<E> nodeY = elementMap.get(y);
        if(nodeX == null && nodeY == null) {
            return null; // neither element is in any set
        }
        if(nodeX == null) {
            return getRootNode(nodeY).x; // x was not in any set
        } else if(nodeY == null) {
            return getRootNode(nodeX).x; // y was not in any set
        }
        Node<E> repX = getRootNode(nodeX);
        Node<E> repY = getRootNode(nodeY);
        if(repX == repY) {
            return repX.x; // same set
        } else { // add set with smaller rank to larger set for efficiency        
            int rankX = setRanks.get(repX);
            int rankY = setRanks.get(repY);
            if(rankX < rankY) {
                return link(repY, repX); // add set with x to set with y
            } else {
                return link(repX, repY); // add set with y to set with x
            }
        }
    }

    // helper method that returns root node of tree with specified node
    private Node<E> getRootNode(Node<E> node) {
        while(node.parentNode != node) {
            node = node.parentNode;
        }
        return node;
    }

    // helper method adds second non-empty set to first where each set
    // is specified by the node of its representative
    private E link(Node<E> repX, Node<E> repY) {  // add the tree rooted at repY as a child of tree rooted at repX
        repY.parentNode = repX;
        // update the map of set ranks and list of repNodes
        int rankX = setRanks.get(repX);
        int rankY = setRanks.get(repY);
        if(rankX == rankY) {
            setRanks.put(repX, ++rankX);//add 1 to setX rank
        }
        setRanks.remove(repY);
        repNodes.remove(repY); // setY no longer exists
        return repX.x;
    }

    @Override
    public E findSet(E x) {
        Node<E> node = elementMap.get(x);
        if(node == null) {
            return null; // element not in any set
        } else { // element is in a set
            return pathCompress(node).x; // return representative of set
        }
    }

    // recursive helper method that path compresses path up from node
    // to root so all nodes along path are now children of root
    // returns the eventual parent node (root node) of node
    private Node<E> pathCompress(Node<E> node) {
        if(node.parentNode == node) {
            return node; // node is the root node
        }
        Node<E> rootNode = pathCompress(node.parentNode);
        node.parentNode = rootNode;
        return rootNode;
    }

    @Override
    public String toString() {
        String output = "Sets: ";
        // determine each set
        Map<Node<E>, List<E>> setsMap = new HashMap<>();
        // create a list to hold each set
        repNodes.forEach((Node<E> repNode) -> {
            setsMap.put(repNode, new ArrayList<>());
        });
        Set<Map.Entry<E, Node<E>>> entries = elementMap.entrySet();
        entries.forEach(entry -> {
            List<E> set = setsMap.get(getRootNode(entry.getValue()));
            set.add(entry.getKey());
        });
        for(Node<E> repNode : repNodes) {
            List<E> set = setsMap.get(repNode);
            output += "{";
            Iterator<E> iterator = set.iterator();
            while(iterator.hasNext()) {
                output += iterator.next().toString();
                if(iterator.hasNext()) {
                    output += ",";
                }
            }
            output += "}(rank=" + setRanks.get(repNode) + ") ";
        }
        return output + "\n";
    }

    // inner class that represents a node in one of the trees
    private class Node<E> {

        public E x; // element held in the node
        public Node<E> parentNode; // link to parent node in tree

        public Node(E x) {
            this.x = x;
            parentNode = null;
        }
    }

    public static void main(String[] args) {
        DisjointSetsADT<Character> sets = new ForestDisjointSets<Character>();
        System.out.println("Creating singleton sets for a,b,c,d,e,f,g");
        Character a = sets.makeSet(new Character('a'));
        Character b = sets.makeSet(new Character('b'));
        Character c = sets.makeSet(new Character('c'));
        Character d = sets.makeSet(new Character('d'));
        Character e = sets.makeSet(new Character('e'));
        Character f = sets.makeSet(new Character('f'));
        Character g = sets.makeSet(new Character('g'));
        System.out.println(sets);
        System.out.println("Union {a} with {e}, {b} with {d}, {f} with {g}");
        Character ae = sets.union(a, e);
        Character bd = sets.union(b, d);
        Character fg = sets.union(f, g);
        System.out.println(sets);
        System.out.println("Union {b,d} with {f,g}");
        sets.union(bd, fg);
        System.out.println(sets);
    }
}
