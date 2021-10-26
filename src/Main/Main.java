/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import AssociationFinder.AssociationFinder;
import CliqueFinder.CliqueFinder;

/**
 *
 * @author jcvsa
 */
public class Main {

    public static void main(String[] args) throws Exception {
        SocialNetwork graph = new SocialNetwork();
        initNetwork(graph);

        System.out.println(graph.toString());
        
        System.out.println("--- BEST ASSOCIATION FINDER ---");
        System.out.println(AssociationFinder.findBestAssociation(graph, "anna", "emma"));
        System.out.println(AssociationFinder.findBestAssociation(graph, "fred", "carl"));
        System.out.println(AssociationFinder.findBestAssociation(graph, "bill", "carl"));
        System.out.println(AssociationFinder.findBestAssociation(graph, "dave", "bill"));
        
        System.out.println("--- MAXIMAL CLIQUE ---");
        System.out.println(CliqueFinder.findLargestClique(graph));
        
        System.out.println("--- COMMUNITY CLUSTER FINDER ---");
        System.out.println("Please see DendrogramGUI.java");

    }

    public static void initNetwork(SocialNetwork graph) {
        graph.addEdge("anna", "carl", 0.4);
        graph.addEdge("anna", "bill", 0.5);

        graph.addEdge("carl", "dave", 0.3);
        graph.addEdge("carl", "emma", 0.5);
        graph.addEdge("carl", "anna", 0.4);

        graph.addEdge("bill", "dave", 0.4);
        graph.addEdge("bill", "anna", 0.5);

        graph.addEdge("emma", "dave", 0.8);
        graph.addEdge("emma", "fred", 0.7);
        graph.addEdge("emma", "carl", 0.5);

        graph.addEdge("dave", "emma", 0.8);
        graph.addEdge("dave", "carl", 0.3);
        graph.addEdge("dave", "bill", 0.4);

        graph.addEdge("fred", "emma", 0.7);
    }
}
