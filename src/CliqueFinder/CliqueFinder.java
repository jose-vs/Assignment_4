/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CliqueFinder;

import Main.Actor;
import Main.SocialNetwork;
import java.util.List;

/**
 * References:
 *
 * https://iq.opengenus.org/greedy-approach-to-find-single-maximal-clique/
 * https://iq.opengenus.org/bron-kerbosch-algorithm/
 *
 * @author jcvsa
 */
public class CliqueFinder {

    public static void findMaximalClique(SocialNetwork socialNetwork) {

    }

    public static List<List<Actor>> findCliques(List<Actor> potentialClique, List<Actor> remainingNodes, List<Actor> skipNodes, int depth) {

        if (remainingNodes.isEmpty() && skipNodes.isEmpty()) { 
            System.out.println("Clique found: " + potentialClique.toString()); 
            return potentialClique; 
        }
        
        for (Actor actor : remainingNodes) { 
            
        }
        
        
        return null; 
    }
}
