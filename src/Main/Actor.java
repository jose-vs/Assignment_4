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
public class Actor {
    protected String name;
    protected HashMap<String,Double> neighbors; // adjacency list, with HashMap for each edge weight

    public Actor() {
        neighbors = new HashMap();
    }

    public Actor(String name) {
        this.name = name;
        neighbors = new HashMap();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Double> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(HashMap<String, Double> neighbors) {
        this.neighbors = neighbors;
    }

    public void addEdge(String toNodeName,Double weight) {
        neighbors.put(toNodeName, weight);
    }

    public double removeEdge(String toNodeName) {
        if (neighbors.containsKey(toNodeName)) {
            double weight = neighbors.get(toNodeName);
            neighbors.remove(toNodeName);
            return weight;
        }

        return Double.MAX_VALUE;
    }

    public Set<String> getAdjacencyList() {
        return neighbors.keySet();
    }

    public LinkedList<Association> getEdges() {
        LinkedList<Association> edges = new LinkedList<Association>();
        for (String toNodeName : neighbors.keySet()) {
            edges.add(new Association(name,toNodeName,neighbors.get(toNodeName)));
        }

        return edges;
    }
    
    @Override
    public String toString() {
        StringBuilder nodeStringB = new StringBuilder();
        nodeStringB.append(name);
        nodeStringB.append(": {");
        Set<String> adjacencyList = this.getAdjacencyList();
        Iterator<String> alIt = adjacencyList.iterator();
        HashMap<String, Double> neighbors = this.getNeighbors();
        while (alIt.hasNext()) {
            String neighborName = alIt.next();
            nodeStringB.append(neighborName.toString());
            nodeStringB.append(": ");
            nodeStringB.append(neighbors.get(neighborName));
            if (alIt.hasNext())
                nodeStringB.append(", ");
        }
        nodeStringB.append("}");
        nodeStringB.append("\n");

        return nodeStringB.toString();
    }
}
