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

    private HashMap<String, Actor> nodes;

    public SocialNetwork() {
        nodes = new HashMap<>();
    }

    public SocialNetwork(HashMap<String, Actor> nodes) {
        this.nodes = nodes;
    }

    public void addActor(String name) {
        if (!nodes.containsKey(name)) {
            nodes.put(name, new Actor(name));
        }
    }

    public void addActor(Actor node) {
        String name = node.getName();
        if (!nodes.containsKey(name)) {
            nodes.put(name, node);
        }
    }

    public void addEdge(String name1, String name2, Double weight) {
        if (weight <= 1 && weight >= 0) {
            if (!nodes.containsKey(name1)) {
                addActor(name1);
            }
            if (!nodes.containsKey(name2)) {
                addActor(name2);
            }
            nodes.get(name1).addEdge(name2, weight);
        } else { 
            System.err.println("Edge with [" + name1 + "] and [" + name2 + "] of weight [" + weight + "] not added");
            System.err.println("enter a number within 0 and 1");
        }
    }

    public void addEdge(Association edge) {
        addEdge(edge.getFromNode(), edge.getToNode(), edge.getWeight());
    }

    public Association removeEdge(String name1, String name2) {
        if (nodes.containsKey(name1)) {
            double weight = nodes.get(name1).removeEdge(name2);
            if (weight != Double.MAX_VALUE) {
                return new Association(name1, name2, weight);
            }
        }

        return null;
    }

    public double getEdgeWeight(String name1, String name2) {
        if (nodes.containsKey(name1)) {
            Actor node1 = nodes.get(name1);
            if (node1.getNeighbors().containsKey(name2)) {
                return node1.getNeighbors().get(name2);
            }
        }

        return Double.MAX_VALUE;
    }

    public HashMap<String, Actor> getNodes() {
        return nodes;
    }

    public List<Association> getEdgeList() {
        List<Association> edgeList = new LinkedList<>();

        for (Actor node : nodes.values()) {
            edgeList.addAll(node.getEdges());
        }

        return edgeList;
    }

    public Set<String> getActorNames() {
        return nodes.keySet();
    }

    public Actor getNode(String name) {
        return nodes.get(name);
    }

    public List<Association> removeNode(String name) {
        LinkedList<Association> edges = new LinkedList<>();
        if (nodes.containsKey(name)) {
            Actor node = nodes.remove(name);
            edges.addAll(node.getEdges());
            edges.addAll(removeEdgesToNode(name));
        }

        return edges;
    }

    public List<Association> removeEdgesToNode(String name) {
        List<Association> edges = new LinkedList<>();
        for (Actor node : nodes.values()) {
            if (node.getAdjacencyList().contains(name)) {
                double weight = node.removeEdge(name);
                edges.add(new Association(node.getName(), name, weight));
            }
        }
        return edges;
    }

    public int numActors() {
        return nodes.size();
    }

    public void clear() {
        nodes = new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder graphStringB = new StringBuilder();
        Iterator<String> it = nodes.keySet().iterator();
        while (it.hasNext()) {
            String nodeName = it.next();
            graphStringB.append(nodeName);
            graphStringB.append(": {");
            Actor node = nodes.get(nodeName);
            Set<String> adjacencyList = node.getAdjacencyList();
            Iterator<String> alIt = adjacencyList.iterator();
            HashMap<String, Double> neighbors = node.getNeighbors();
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
