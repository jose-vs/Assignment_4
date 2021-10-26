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
        System.out.println("Fred to Carl = " + AssociationFinder.findBestAssociation(graph, "fred", "carl"));
        System.out.println("Bill to Carl = " + AssociationFinder.findBestAssociation(graph, "bill", "carl"));
        System.out.println("Dave to Bill = " + AssociationFinder.findBestAssociation(graph, "dave", "bill"));
        System.out.println("Frank to Carl = " + AssociationFinder.findBestAssociation(graph, "frank", "carl"));

        System.out.println("--- MAXIMAL CLIQUE ---");
        System.out.println(CliqueFinder.findLargestClique(graph));

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
        graph.addEdge("emma", "frank", 0.3);

        graph.addEdge("dave", "emma", 0.8);
        graph.addEdge("dave", "carl", 0.3);
        graph.addEdge("dave", "bill", 0.4);
        graph.addEdge("dave", "frank", 0.8);
        graph.addEdge("dave", "fred", 0.3);

        graph.addEdge("fred", "emma", 0.7);
        graph.addEdge("fred", "frank", 0.5);
        graph.addEdge("fred", "dave", 0.3);

        graph.addEdge("frank", "dave", 0.8);
        graph.addEdge("frank", "fred", 0.5);
        graph.addEdge("frank", "emma", 0.3);

    }
}
