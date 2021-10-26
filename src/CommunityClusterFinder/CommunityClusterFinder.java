package CommunityClusterFinder;

import static CommunityClusterFinder.CommunityFloydWarshall.INFINITY;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that uses agglomerative hierarchical clustering technique alongside
 * complete linkage clustering to merge the social communities together.
 * It also constructs DendroNdoes so that the graph/community can be visually
 * represented later. 
 * 
 * References
 * ---------------------------------------------------------------------------------------
 * Bhatia, A. (2017, May 16). Hierarchical Agglomerative Clustering [HAC - Complete Link]
 *      [Video]. YouTube. https://www.youtube.com/watch?v=Cy3ci0Vqs3Y
 * 
 * Ensor, A. (2021). COMP611 Algorithm Design and Analysis: Disjoint Sets [Course Manual].  
 *       Chapter 5.4, 106 - 108. Blackboard. https://blackboard.aut.ac.nz/
 * 
 * @author Amos Foong <18044418>
 * @author Jose Santos <17993442>
 */
public class CommunityClusterFinder {
    
    public static boolean DEBUG_MODE = false;
    
    public CommunityFloydWarshall socialNetworkGraph; // Data structure that stores the social network graph.
    public DendroNode<String> rootNode;
    public ForestDisjointSets<String> clusters; // ForestDisjointSet to merge clusters together.
    
    private int n;
    private String[] actors; // Names of social actors in the community.
    private ArrayList<ArrayList<Double>> proximityMatrix;
        
    public CommunityClusterFinder(double[][] associations, String[] actors) {
        this.actors = actors;
        this.n = associations.length;
        
        // Set up the social network graph.
        socialNetworkGraph = new CommunityFloydWarshall(associations, actors);
        
        // Transform table into a 2D list.
        proximityMatrix = toList2D(socialNetworkGraph.getShortestPathsTable());
        
        // Initilise clusters.
        clusters = new ForestDisjointSets<>();
        
        // Agglomerate hierarchical clusters using the complete linkage  
        // clustering technique. 
        agglomerateHClusters(proximityMatrix);        
    }
    
    /**
     * Method to form a dendrogram using agglomerative hierarchical clustering with
     * complete linkage. Uses forest disjoint set to manage the agglomeration of 
     * communities together so that operations are efficient. 
     * 
     * @param proximityMatrix 
     * @return an agglomerated cluster.
     */
    public ForestDisjointSets<String> agglomerateHClusters(ArrayList<ArrayList<Double>> proximityMatrix) {
//        ForestDisjointSets<String> clusters = new ForestDisjointSets<>();
        List<String> clusterReps = new ArrayList<>();
        List<DendroNode<String>> baseNodes = new ArrayList<>();
        
        // Create singleton sets for all social actors.
        for(int i = 0; i < this.n; i++) {
            clusterReps.add(clusters.makeSet(this.actors[i]));
            baseNodes.add(new DendroNode<>(this.actors[i]));
        }

        int nMerge = this.n - 1;
        
        double[][] currentMin = new double[nMerge][3];

        // For loop to keep merging until there is only one big cluster left.
        for(int i = 0; i < nMerge; i++) {
            currentMin[i] = findClosestStats(proximityMatrix);
            int indX = (int) currentMin[i][1];
            int indY = (int) currentMin[i][2];
            
            outputBorder();
            System.out.printf("Current Strongest Weight: %.3f with clusters [%d: %s]-[%d: %s]\n", currentMin[i][0], indX, clusterReps.get(indX), indY, clusterReps.get(indY));
           
            // Construct dendrogram.
            baseNodes.set(indX, new DendroNode<>(new DendroNode<>(baseNodes.get(indX)), new DendroNode<>(baseNodes.get(indY))));
            baseNodes.remove(indY);
            this.rootNode = baseNodes.get(indX);
            
            // Merge the two-closest clusters together and add their weights.
            String rep = clusters.union(clusterReps.get(indX), clusterReps.get(indY));
            clusters.setProximity(rep, currentMin[i][0]);
            System.out.println(clusters);
            
            // Output the proximity table before merging.
            System.out.printf("Before Merge #%d:\n", i);
            System.out.println(stringifyList2D(proximityMatrix, clusterReps));

            // Perform proximity recalculation and remove merged clusters 
            // from the proximity matrix.
            completeLinkage(proximityMatrix, clusterReps, indX, indY);

            // Output the proximity table after merging.
            System.out.printf("After Merge #%d:\n", i);
            System.out.println(stringifyList2D(proximityMatrix, clusterReps));
        }
        
        return clusters;
    }
    
    /**
     * Random method to output borders.
     */
    public static void outputBorder() {
        for(int i = 0; i < 150; i++) {
            System.out.print("-");
        }
        System.out.println("");
    }

    /**
     * Helper method to convert a table into a 2D-list. 2D-Lists are
     * easier to remove whole rows/columns. Suitable for AHC operations.
     * 
     * @param table
     * @return 2D list
     */
    public static ArrayList<ArrayList<Double>> toList2D(double[][] table) {
        ArrayList<ArrayList<Double>> list2D = new ArrayList<>();
        for(int i = 0; i < table.length; i++) {
            list2D.add(new ArrayList<>());
            for(int j = 0; j < table[i].length; j++) {
                list2D.get(i).add(table[i][j]);
            }
        }
        return list2D;
    }

    /**
     * Helper method to find the strongest association weight between 
     * all current clusters. Also returns where the association indices
     * are so they can be used later to reconstruct sets.
     * 
     * @param table
     * @return a double array containing the min distance, index 1, index 2.
     */
    public static double[] findClosestStats(ArrayList<ArrayList<Double>> table) {
        double[] returnData = new double[3];
        returnData[0] = Double.MAX_VALUE;

        for(int i = 0; i < table.size(); i++) {
            for(int j = 0; j < table.get(i).size(); j++) {
                // Finds minimum distance and the indices where it is found.
                if(table.get(i).get(j) < returnData[0] && table.get(i).get(j) != 0D) {
                    returnData[0] = table.get(i).get(j);
                    returnData[1] = i;
                    returnData[2] = j;
                }
            }
        }
        return returnData;
    }

    /**
     * Method to recalculate table based on the complete link clustering
     * technique. The maximum of the distance between all the points 
     * in the merged clusters are compared and updated in the proximity matrix.
     * One of the merged cluster is then deleted from the matrix so that
     * it can slowly be reduced to one big cluster.
     * 
     * @param table
     * @param clusterReps
     * @param indX
     * @param indY 
     */
    public static void completeLinkage(ArrayList<ArrayList<Double>> table, List<String> clusterReps, int indX, int indY) {
        for(int i = 0; i < table.size(); i++) {
            // Get the largest distance of these two points compared to other points in the graph.
            double max = Double.max(table.get(indX).get(i), table.get(indY).get(i));

            // Replace cell with new values (symmetrically copies too).
            table.get(indX).set(i, max);
            table.get(i).set(indX, max);
        }

        table.get(indX).set(indX, 0D); // Sets its own association weight to 0.

        // Remove the merged cluster's data from the proximity matrix and clusterReps.
        table.remove(indY);
        table.forEach((row) -> row.remove(indY));
        clusterReps.remove(indY);
    }

    /**
     * Method to stringify a 2D-List so that data can be organised and output as
     * structured information to user.
     *
     * @param table : 2D-List containing data (weights of association)
     * @param actors : headers for 2D-list's row and columns
     * @return String containing information about the 2D-list.
     */
    public static String stringifyList2D(ArrayList<ArrayList<Double>> table, List<String> actors) {
        String output = "";
        output = actors.stream().map(actor -> "\t" + actor).reduce(output, String::concat);
        output += "\n";

        for(int i = 0; i < table.size(); i++) {
            output += actors.get(i);
            for(int j = 0; j < table.get(i).size(); j++) {
                if(table.get(i).get(j) != INFINITY) {
                    output += ("\t" + String.format("%.3f", table.get(i).get(j)));
                } else {
                    output += "\tinfin";
                }
            }
            output += "\n";
        }
        return output;
    }
//
//    public static void main(String[] args) {
//        // Test data.
//        double[][] associations = {
//            {0D, 0.5D, 0.4D, 0D, 0D, 0D},
//            {0.5D, 0D, 0D, 0.4D, 0D, 0D},
//            {0.4D, 0D, 0D, 0.3D, 0.5D, 0D},
//            {0D, 0.4D, 0.3D, 0D, 0.8D, 0D},
//            {0D, 0D, 0.5D, 0.8D, 0D, 0.7D},
//            {0D, 0D, 0D, 0D, 0.7D, 0D}};
//        // Social actors involved in the table.
//        String[] actors = {"Anna", "Bill", "Carl", "Dave", "Emma", "Fred"};
//        CommunityClusterFinder clusterFinder = new CommunityClusterFinder(associations, actors);
//        System.out.println(clusterFinder);
//    }
}
