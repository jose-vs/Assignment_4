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

        List<Clique> cliques = new ArrayList<>();
        Set<Actor> R = new HashSet<>();
        Set<Actor> X = new HashSet<>();
        Set<Actor> P = new HashSet<>(network.values());

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
     * @param cliques  list of maximal cliques
     * @param R potential clique nodes
     * @param P remaining nodes
     * @param X skip nodes
     */
    public static void bronKerbosch(List<Clique> cliques, Set<Actor> R, Set<Actor> P, Set<Actor> X) {

        
        if (P.isEmpty() && X.isEmpty()) {
            cliques.add(new Clique(R));
        }

        Set<Actor> pCopy = new HashSet<>(P);

        
        for (Actor actor : pCopy) {
            Set<String> adjacencyList = actor.getAdjacencyList();

            Set<Actor> Rnew = new HashSet<>(R);
            Set<Actor> Pnew = new HashSet<>(P);
            Set<Actor> Xnew = new HashSet<>(X);

            Rnew.add(actor);
            Pnew.removeIf(curr -> (!adjacencyList.contains(curr.getName())));
            Xnew.removeIf(curr -> (!adjacencyList.contains(curr.getName())));

            bronKerbosch(cliques, Rnew, Pnew, Xnew);

            Pnew.remove(actor);
            Xnew.add(actor);
        }
    }
}
