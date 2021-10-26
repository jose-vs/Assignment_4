/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AssociationFinder;

import Main.Actor;
import Main.Association;
import Main.SocialNetwork;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * reference:
 * https://www.maxburstein.com/blog/introduction-to-graph-theory-finding-shortest-path/
 *
 * @author jcvsa
 */
public class AssociationFinder {

    /**
     * uses Dijkstra's shortest path algorithm
     *
     * @param socialNetwork
     * @param src
     * @param dest
     * @return
     * @throws Exception
     */
    public static Path findBestAssociation(SocialNetwork socialNetwork, String src, String dest) throws Exception {
        HashMap<String, Actor> network = socialNetwork.getSocialNetwork();

        /**
         * Throw an exception if person not found within the network
         */
        if(!network.containsKey(src) || !network.containsKey(dest)) {
            throw new Exception("Person not found within Social Network");
        }

        /**
         * save the graph in a priority queue and initialize its value with
         * infinity
         */
        ShortestPathTree predTree = new ShortestPathTree(src);
        PriorityQueue<Actor> pq = new PriorityQueue<>();
        for(String actorName : network.keySet()) {
            Actor newActor = new Actor(actorName, Double.MAX_VALUE, Integer.MAX_VALUE);
            predTree.add(newActor);
        }

        /**
         * set the src Actor with a weight of 0
         */
        Actor srcActor = predTree.getActors().get(predTree.getRoot());
        srcActor.setWeight(0);
        srcActor.setDepth(0);
        pq.add(srcActor);

        /**
         *
         */
        int cnt = 0;
        while(!pq.isEmpty()) {

            /**
             *
             */
            Actor curr = pq.poll();
            String currName = curr.getName();

            /**
             *
             */
            if(currName.equals(dest)) {
                /**
                 *
                 */
                Path shortestPath = new Path();
                String currN = dest;
                String parentN = predTree.getParentOf(currN);

                /**
                 *
                 */
                while(parentN != null) {
                    shortestPath.addFirst(new Association(parentN, currN, network.get(parentN).getNeighbors().get(currN)));
                    currN = parentN;
                    parentN = predTree.getParentOf(currN);
                }

                return shortestPath;
            }
            cnt++;
            /**
             *
             */
            HashMap<String, Double> neighbors = network.get(currName).getNeighbors();
            for(String currNeighborName : neighbors.keySet()) {

                /**
                 *
                 */
                Actor neighborActor = predTree.getActors().get(currNeighborName);
                Double currDistance = neighborActor.getWeight();
                Double newDistance = curr.getWeight() + network.get(currName).getNeighbors().get(currNeighborName);

                Double logCurrDistance = convertIfWithinBounds(currDistance);
                Double logNewDistance = convertIfWithinBounds(newDistance);

                /**
                 *
                 */
                if(logNewDistance < logCurrDistance) {
                    Actor neighbor = predTree.getActors().get(currNeighborName);

                    pq.remove(neighbor);
                    neighbor.setWeight(newDistance);
                    neighbor.setDepth(curr.getDepth() + 1);
                    neighbor.setParent(currName);
                    pq.add(neighbor);
                }
            }
        }

        return null;
    }

    private static double convertIfWithinBounds(double weight) {
        return (weight < 1.0 && weight > 0.0) ? Math.log(weight) * -1 : weight;
    }
}
