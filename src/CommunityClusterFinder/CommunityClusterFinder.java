package CommunityClusterFinder;

import static CommunityClusterFinder.CommunityFloydWarshall.INFINITY;
import java.util.ArrayList;
import java.util.List;

/**
 * References
 * ---------------------------------------------------------------------------------------
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
        
        // Agglomerate hierarchical clusters using the complete linkage  
        // clustering technique. 
        //produce Agglomerative Hierarchical Clusters using Complete Linkage Clustering.
        agglomerateHClusters(proximityMatrix);        
    }
    
    
    public void agglomerateHClusters(ArrayList<ArrayList<Double>> proximityMatrix) {
        ForestDisjointSets<String> clusters = new ForestDisjointSets<>();
        List<String> clusterReps = new ArrayList<>();
        List<DendroNode<String>> baseNodes = new ArrayList<>();
        List<DendroNode<String>> mergeNodes = new ArrayList<>();
        // Create singleton sets for all social actors.
        for(int i = 0; i < this.n; i++) {
            clusterReps.add(clusters.makeSet(this.actors[i]));
            baseNodes.add(new DendroNode<>(this.actors[i]));
        }

        int nMerge = this.n - 1;
        
        double[][] currentMin = new double[nMerge][3];

        for(int i = 0; i < nMerge; i++) {
            currentMin[i] = findClosestStats(proximityMatrix);
            int indX = (int) currentMin[i][1];
            int indY = (int) currentMin[i][2];
            
            System.out.printf("Current Strongest Association: %.3f with clusters [%d: %s]-[%d: %s]\n", currentMin[i][0], indX, clusterReps.get(indX), indY, clusterReps.get(indY));
            
            
            mergeNodes.add(new DendroNode<>(new DendroNode<>(clusterReps.get(indX)), new DendroNode<>(clusterReps.get(indY))));
            this.rootNode = mergeNodes.get(mergeNodes.size()-1);
//            this.rootNode.contents = mergeNodes.get(mergeNodes.size()-1).getContents();
//            System.out.println(mergeNodes);
//            System.out.println(rootNode);
            
            // Merge the two-closest clusters together and add their weights.
            String rep = clusters.union(clusterReps.get(indX), clusterReps.get(indY));
            clusters.setProximity(rep, currentMin[i][0]);
            
            System.out.println(clusters);
            System.out.printf("Before Merge #%d:\n", i);
            System.out.println(stringifyList2D(proximityMatrix, clusterReps));

            completeLinkage(proximityMatrix, clusterReps, indX, indY);

            System.out.printf("After Merge #%d:\n", i);
            System.out.println(stringifyList2D(proximityMatrix, clusterReps));
        }

    }

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
        for(String actor : actors) {
            output += "\t" + actor;
        }
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
        CommunityClusterFinder clusterFinder = new CommunityClusterFinder(associations, actors);
        System.out.println(clusterFinder);
    }
}
