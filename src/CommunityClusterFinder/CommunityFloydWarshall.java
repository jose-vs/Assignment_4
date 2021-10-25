package CommunityClusterFinder;

/**
 * A class that demonstrates the Floyd-Warshall algorithm for solving the
 * all-pairs shortest paths problem in O(n^3).
 *
 * References
 * ---------------------------------------------------------------------------------------
 * Ensor, A. (2021). COMP611 Algorithm Design and Analysis: All-Pairs Shortest Paths 
 *      [Course Manual]. Chapter 6.4, p 136. Blackboard. https://blackboard.aut.ac.nz/
 * 
 * James, J. (2016, January 25). Floyd Warshall Algorithm on Undirected Graph - Dynamic  
 *      Programming Example [Video]. YouTube. https://www.youtube.com/watch?v=B06q2yjr-Cc  
 *
 * @author Dr. Andrew Ensor: Main author.
 * @author Amos Foong <18044418>: Made small modifications to suit an application.
 */
public class CommunityFloydWarshall {

    public static final double INFINITY = Double.MAX_VALUE;
    public static final int NO_VERTEX = -1;
    private int n; // number of vertices in the graph
    private double[][][] d; //d[k][i][i] is weight of path from v_i to v_j
    private int[][][] p; //p[k][i][i] is penultimate vertex in path

    private String[] actors; // Names of social actors in the community.

    public CommunityFloydWarshall(double[][] associations, String[] actors) {
        n = associations.length;
        this.actors = actors;
        
        System.out.println("Original Associations:");
        System.out.println(stringifyTable(associations, actors));

        // Initialise d[0] with computed weights of associations.
        d = new double[n + 1][][];
        d[0] = computeWeights(associations);
        
        System.out.println("Initial Weights:");
        System.out.println(stringifyTable(getInitialWeightTable(), actors));

        // create p[0]
        initPredecessorT0();

        // Find all pairs shortest path between people.
        performFloydWarshall();
    }

    public double[][] getShortestPathsTable() {
        return d[n];
    }

    public int[][] getPredecessorVertexTable() {
        return p[n];
    }

    public double[][] getInitialWeightTable() {
        return d[0];
    }

    public String[] getActors() {
        return actors;
    }

    /**
     * Helper method that receives an association table and compute its
     * respective weights by using the absolute value of the natural
     * log(association strength).
     *
     * @param associations
     * @return a weighted table
     */
    private double[][] computeWeights(double[][] associations) {
        double[][] convertedTable = new double[associations.length][associations.length];

        for(int i = 0; i < associations.length; i++) {
            for(int j = 0; j < associations.length; j++) {
                if(associations[i][j] == 0D) {
                    // Assign the weight to be a big number(infinity) if association is 0.
                    convertedTable[i][j] = INFINITY;
                } else {
                    // Find the -ln (abs Natural log) of the association (conversion-to-weight).
                    convertedTable[i][j] = Math.abs(Math.log(associations[i][j]));
                }
            }
        }

        return convertedTable;
    }

    /**
     * Helper method to initialise the first set of data for the predecessor table which
     * stores the initial indices of persons (vertex) that will lead to the shortest path.
     */
    private void initPredecessorT0() {
        p = new int[n + 1][][];
        p[0] = new int[n][n];

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(getInitialWeightTable()[i][j] < INFINITY) {
                    p[0][i][j] = i;
                } else {
                    p[0][i][j] = NO_VERTEX;
                }
            }
        }
    }

    /**
     * Method to find the all-pairs shortest path using Floyd-Warshall's
     * algorithm. It uses 2 multi-leveled tables, one to record the sum of
     * shortest path weights and the other to record the person(vertex) where
     * the shortest path can be found via (predecessor). Each level of table  
     * represents the association with a stop, e.g. level 0: direct, 
     * level 1: via Anna, level 2: via Bill, etc.. up to level n+1: actor[n-1].
     */
    private void performFloydWarshall() {
        // build d[1],...,d[n] and p[1],...,p[n] dynamically
        for(int k = 1; k <= n; k++) {
            d[k] = new double[n][n];
            p[k] = new int[n][n];

            for(int i = 0; i < n; i++) {
                for(int j = 0; j < n; j++) {
                    if(i == j) { // Skips when it has reached itself (e.g. Anna-Anna).
                        // Zeros the diagonals of the table as e^0=1. A person 
                        // will have a very strong association with themselves.
                        d[k][i][j] = 0D; 
                        p[k][i][j] = i; // Assigns itself in the predecessor table diagonals.
                        continue; // Move to next cell.
                    }
                    
                    double s;

                    // Assess if there is a path available via another person, if
                    // so, sum the weights found from previous tables (skips when 
                    // both i & j are infinity values).
                    if(d[k - 1][i][k - 1] != INFINITY && d[k - 1][k - 1][j] != INFINITY) {                        
                        s = d[k - 1][i][k - 1] + d[k - 1][k - 1][j];
                    } else {
                        s = INFINITY;
                    }
 
                    // If previous weight is less than/equal the newly assessed weight,
                    // copy over data from the previous table (if condition). Otherwise, 
                    // update tables with the new shortest/least weight and the person 
                    // (predecessor vertex) where the shortest path was found at (else cond).
                    if(d[k - 1][i][j] <= s) {
                        d[k][i][j] = d[k - 1][i][j];
                        p[k][i][j] = p[k - 1][i][j];
                    } else {
                        d[k][i][j] = s;
                        p[k][i][j] = p[k - 1][k - 1][j];
                    }
                    
                }
            }
//            System.out.println(stringifyTable(d[k], actors));
        }
    }

    /**
     * Method to stringify a table so that data can be organised
     * and output as structured information to user.
     *
     * @param table : table containing data (weights of association)
     * @param actors : headers for table's row and columns
     * @return String containing information about the table.
     */
    public static String stringifyTable(double[][] table, String[] actors) {
        String output = "";
        for(String actor : actors) {
            output += "\t" + actor;
        }
        output += "\n";

        for(int i = 0; i < table.length; i++) {
            output += actors[i];
            for(int j = 0; j < table.length; j++) {
                if(table[i][j] != INFINITY) {
                    output += ("\t" + String.format("%.3f", table[i][j]));
                } else {
                    output += "\tinfin";
                }
            }
            output += "\n";
        }

        return output;
    }

    // returns a string representation of matrix d[n] and p[n]
    @Override
    public String toString() {
        String output = "Shortest lengths:\n";

        output += stringifyTable(d[n], this.actors);

        output += "Previous persons on shortest paths (via):\n";
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(p[n][i][j] != NO_VERTEX) {
                    output += ("\t" + p[n][i][j] + ": " + this.actors[p[n][i][j]]);
                } else {
                    output += "\tnull";
                }
            }
            output += "\n";
        }
        return output;
    }

    public static void main(String[] args) {
        // Test data.
        double[][] associations = {
            {0D, 0.5D, 0.4D, 0D, 0D, 0D},
            {0.5D, 0D, 0D, 0.4D, 0D, 0D},
            {0.4D, 0D, 0D, 0.3D, 0.5D, 0D},
            {0D, 0.4D, 0.3D, 0D, 0.8D, 0D},
            {0D, 0D, 0.5D, 0.8D, 0D, 0.7D},
            {0D, 0D, 0D, 0D, 0.7D, 0D}};
        // Social actors involved in the table.
        String[] actors = {"Anna", "Bill", "Carl", "Dave", "Emma", "Fred"};
        CommunityFloydWarshall apfw = new CommunityFloydWarshall(associations, actors);
        System.out.println(apfw);
    }
}
