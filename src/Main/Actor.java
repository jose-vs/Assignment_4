/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author jcvsa
 */
public class Actor implements Comparable<Actor> {

    private String name;
    private double weight = Double.MAX_VALUE;
    private int depth;
    private HashMap<String, Double> neighbors; // adjacency list, with HashMap for each edge weight

    public Actor() {
        neighbors = new HashMap();
    }

    public Actor(double weight) {
        this();
        this.weight = weight;
    }

    public Actor(String name) {
        this.name = name;
        this.weight = 0.0;
        neighbors = new HashMap();
    }

    public Actor(String name, double weight) {
        this(name);
        this.weight = weight;
    }
    
    public Actor(String name, double weight, int depth) { 
        this(name, weight); 
        this.depth = depth; 
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setParent(String parent) {
        neighbors = new HashMap<>();
        neighbors.put(parent, 0.0);
    }

    public String getParent() {
        Set<String> neighborLabels = neighbors.keySet();
        if (neighborLabels.size() > 1) {
            return null;
        }
        if (neighborLabels.size() < 1) {
            return null;
        }
        return neighbors.keySet().iterator().next();
    }

    public HashMap<String, Double> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(HashMap<String, Double> neighbors) {
        this.neighbors = neighbors;
    }

    public void addEdge(String toActorName, Double weight) {
        neighbors.put(toActorName, weight);
    }

    public double removeEdge(String toActorName) {
        if (neighbors.containsKey(toActorName)) {
            double currWeight = neighbors.get(toActorName);
            neighbors.remove(toActorName);
            return currWeight;
        }

        return Double.MAX_VALUE;
    }

    public Set<String> getAdjacencyList() {
        return neighbors.keySet();
    }

    public LinkedList<Association> getEdges() {
        LinkedList<Association> edges = new LinkedList<Association>();
        for (String toActorName : neighbors.keySet()) {
            edges.add(new Association(name, toActorName, neighbors.get(toActorName)));
        }

        return edges;
    }

    @Override
    public String toString() {
        StringBuilder actorStringB = new StringBuilder();
        actorStringB.append(name);
        actorStringB.append(": {");
        Set<String> adjacencyList = this.getAdjacencyList();
        Iterator<String> alIt = adjacencyList.iterator();
        HashMap<String, Double> neighbors = this.getNeighbors();
        while (alIt.hasNext()) {
            String neighborName = alIt.next();
            actorStringB.append(neighborName.toString());
            actorStringB.append(": ");
            actorStringB.append(neighbors.get(neighborName));
            if (alIt.hasNext()) {
                actorStringB.append(", ");
            }
        }
        actorStringB.append("}");
        actorStringB.append("\n");

        return actorStringB.toString();
    }

    /**
     * 
     * @param comparedNode
     * @return 
     */
    @Override
    public int compareTo(Actor comparedNode) {
        double distance1 = Math.log(this.weight);
        double distance2 = Math.log(comparedNode.getWeight());
        if (distance1 == distance2) {
            return 0;
        }
        if (distance1 > distance2) {
            return 1;
        }
        return -1;
    }

    public boolean equals(Actor comparedNode) {
        return this.getName().equals(comparedNode.getName());
    }
}
