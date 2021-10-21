/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author jcvsa
 */
public class SocialNetwork {

    private HashMap<String, Actor> socialNetwork;
    private String root;

    public SocialNetwork() {
        socialNetwork = new HashMap<>();
    }

    public SocialNetwork(String root) {
        this();
        this.root = root;
    }

    public void addActor(String name) {
        if (!socialNetwork.containsKey(name)) {
            socialNetwork.put(name, new Actor(name));
        }
    }

    public void addActor(Actor actor) {
        String name = actor.getName();
        if (!socialNetwork.containsKey(name)) {
            socialNetwork.put(name, actor);
        }
    }

    public void addEdge(String name1, String name2, Double weight) {
        if (weight <= 1 && weight >= 0) {
            if (!socialNetwork.containsKey(name1)) {
                addActor(name1);
            }
            if (!socialNetwork.containsKey(name2)) {
                addActor(name2);
            }
            socialNetwork.get(name1).addEdge(name2, weight);
        } else {
            System.err.println("Edge with [" + name1 + "] and [" + name2 + "] of weight [" + weight + "] not added");
            System.err.println("enter a number within 0 and 1");
        }
    }

    public Association removeEdge(String name1, String name2) {
        if (socialNetwork.containsKey(name1)) {
            double weight = socialNetwork.get(name1).removeEdge(name2);
            if (weight != Double.MAX_VALUE) {
                return new Association(name1, name2, weight);
            }
        }

        return null;
    }

    public double getEdgeWeight(String name1, String name2) {
        if (socialNetwork.containsKey(name1)) {
            Actor actor1 = socialNetwork.get(name1);
            if (actor1.getNeighbors().containsKey(name2)) {
                return actor1.getNeighbors().get(name2);
            }
        }

        return Double.MAX_VALUE;
    }

    public HashMap<String, Actor> getSocialNetwork() {
        return socialNetwork;
    }

    public List<Association> getEdgeList() {
        List<Association> edgeList = new LinkedList<>();

        for (Actor actor : socialNetwork.values()) {
            edgeList.addAll(actor.getEdges());
        }

        return edgeList;
    }

    public Set<String> getActorNames() {
        return socialNetwork.keySet();
    }

    public Actor getActor(String name) {
        return socialNetwork.get(name);
    }

    public List<Association> removeactor(String name) {
        LinkedList<Association> edges = new LinkedList<>();
        if (socialNetwork.containsKey(name)) {
            Actor actor = socialNetwork.remove(name);
            edges.addAll(actor.getEdges());
            edges.addAll(removeEdgesToactor(name));
        }

        return edges;
    }

    public List<Association> removeEdgesToactor(String name) {
        List<Association> edges = new LinkedList<>();
        for (Actor actor : socialNetwork.values()) {
            if (actor.getAdjacencyList().contains(name)) {
                double weight = actor.removeEdge(name);
                edges.add(new Association(actor.getName(), name, weight));
            }
        }
        return edges;
    }

    public int numSocialNetwork() {
        return socialNetwork.size();
    }

    public void clear() {
        socialNetwork = new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder graphStringB = new StringBuilder();
        Iterator<String> it = socialNetwork.keySet().iterator();
        while (it.hasNext()) {
            String actorName = it.next();
            graphStringB.append(actorName);
            graphStringB.append(": {");
            Actor actor = socialNetwork.get(actorName);
            Set<String> adjacencyList = actor.getAdjacencyList();
            Iterator<String> alIt = adjacencyList.iterator();
            HashMap<String, Double> neighbors = actor.getNeighbors();
            while (alIt.hasNext()) {
                String neighborName = alIt.next();
                graphStringB.append(neighborName);
                graphStringB.append(": ");
                graphStringB.append(neighbors.get(neighborName));
                if (alIt.hasNext()) {
                    graphStringB.append(", ");
                }
            }
            graphStringB.append("}");
            graphStringB.append("\n");
        }

        return graphStringB.toString();
    }

}
