/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CliqueFinder;

import Main.Actor;
import Main.SocialNetwork;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * References:
 *
 * https://iq.opengenus.org/greedy-approach-to-find-single-maximal-clique/
 * https://iq.opengenus.org/bron-kerbosch-algorithm/
 *
 * @author jcvsa
 */
public class CliqueFinder {

    /**
     * 
     * @param socialNetwork
     * @return 
     */
    public static Clique findLargestClique(SocialNetwork socialNetwork) {

        HashMap<String, Actor> network = socialNetwork.getSocialNetwork();
        /**
         * 
         */
        List<Clique> cliques = new ArrayList<>();
        Set<Actor> R = new HashSet<>();
        Set<Actor> X = new HashSet<>();
        Set<Actor> P = new HashSet<>(network.values());

        /**
         * 
         */
        bronKerbosch(cliques, R, P, X);

        /**
         * find largest clique
         */
        Clique largestClique = null;
        for (Clique currClique : cliques) {
            if (largestClique == null) {
                largestClique = currClique;
            } else if (currClique.size() > largestClique.size()) {
                largestClique = currClique;
            }

        }

        return largestClique;
    }

    /**
     *
     * @param cliques
     * @param potentialClique
     * @param remainingNodes
     * @param skipNodes
     */
    public static void bronKerbosch(List<Clique> cliques, Set<Actor> potentialClique, Set<Actor> remainingNodes, Set<Actor> skipNodes) {

        /**
         * 
         */
        if (remainingNodes.isEmpty() && skipNodes.isEmpty()) {
            cliques.add(new Clique(potentialClique));
        }

        Set<Actor> rnCopy = new HashSet<>(remainingNodes);

        /**
         * 
         */
        for (Actor actor : rnCopy) {
            Set<String> adjacencyList = actor.getAdjacencyList();
            
            /**
             * 
             */
            Set<Actor> pcm = new HashSet<>(potentialClique);
            Set<Actor> rnm = new HashSet<>(remainingNodes);
            Set<Actor> snm = new HashSet<>(skipNodes);

            /**
             * 
             */
            pcm.add(actor);
            rnm.removeIf(curr -> (!adjacencyList.contains(curr.getName())));
            snm.removeIf(curr -> (!adjacencyList.contains(curr.getName())));

            /**
             * 
             */
            bronKerbosch(cliques, pcm, rnm, snm);

            /**
             * 
             */
            remainingNodes.remove(actor);
            skipNodes.add(actor);
        }
    }
}
