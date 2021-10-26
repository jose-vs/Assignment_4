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
 * https://www.softwaretestinghelp.com/dijkstras-algorithm-in-java/
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
        if (!network.containsKey(src) || !network.containsKey(dest)) {
            throw new Exception("Person not found within Social Network");
        }

        /**
         * save the graph in a priority queue and initialize its value with
         * infinity
         */
        SocialNetwork predTree = new SocialNetwork(src);
        PriorityQueue<Actor> pq = new PriorityQueue<>((Object t, Object t1) -> {

            /**
             * Ensures that the priority queue is ordered with the strongest
             * weight on top
             */
            Actor a1 = (Actor) t;
            Actor a2 = (Actor) t1;

            if (a1.getWeight() == a2.getWeight()) {
                return 0;
            }
            if (a1.getWeight() < a2.getWeight()) {
                return 1;
            }

            return -1;
        });

        for (String actorName : network.keySet()) {
            Actor newActor = new Actor(actorName, Double.MAX_VALUE);
            predTree.addActor(newActor);
        }

        /**
         * set the src Actor with a weight of 0
         */
        Actor srcActor = predTree.getSocialNetwork().get(predTree.getRoot());
        srcActor.setWeight(0);
        pq.add(srcActor);

        /**
         *
         */
        int cnt = 0;
        while (!pq.isEmpty()) {

            /**
             * retrieves the src actor
             */
            Actor curr = pq.poll();
            String currName = curr.getName();

            /**
             * if the dest actor is found build the path from src to it
             */
            if (currName.equals(dest)) {
                /**
                 * initialize path object the path object is used to store the
                 * path from src to dest
                 */
                Path shortestPath = new Path();
                String currN = dest;

                /**
                 * parentN object is the next actor from current actor to src
                 */
                String parentN = predTree.getParentOf(currN);

                /**
                 * builds the path
                 */
                while (parentN != null) {
                    shortestPath.addFirst(new Association(parentN, currN, network.get(parentN).getNeighbors().get(currN)));
                    currN = parentN;
                    parentN = predTree.getParentOf(currN);
                }

                return shortestPath;
            }

            /**
             * dest not found, search through neighbors for best distance
             * towards src
             */
            cnt++;
            HashMap<String, Double> neighbors = network.get(currName).getNeighbors();
            for (String currNeighborName : neighbors.keySet()) {

                Actor neighborActor = predTree.getSocialNetwork().get(currNeighborName);
                Double currDistance = neighborActor.getWeight();
                Double newDistance = curr.getWeight() + network.get(currName).getNeighbors().get(currNeighborName);

                /**
                 * if new distance from current actor to neighbor actor is less
                 * than curr distance then save it in the priority queue and set
                 * its parent to the current actor node.
                 */
                if (newDistance < currDistance) {
                    Actor neighbor = predTree.getSocialNetwork().get(currNeighborName);

                    pq.remove(neighbor);
                    neighbor.setWeight(newDistance);
                    neighbor.setParent(currName);
                    pq.add(neighbor);
                }
            }
        }

        return null;
    }

}