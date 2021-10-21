/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AssociationFinder;

import Main.Association;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author jcvsa
 */
public class Path {

    private LinkedList<Association> edges;
    private double totalWeight;

    public Path() {
        edges = new LinkedList<>();
        totalWeight = 0;
    }

    public Path(double totalCost) {
        edges = new LinkedList<>();
        this.totalWeight = totalCost;
    }

    public Path(LinkedList<Association> edges) {
        this.edges = edges;
        totalWeight = 0;
        for (Association edge : edges) {
            totalWeight += edge.getWeight();
        }
    }

    public Path(LinkedList<Association> edges, double totalWeight) {
        this.edges = edges;
        this.totalWeight = totalWeight;
    }

    public LinkedList<Association> getEdges() {
        return edges;
    }

    public void setEdges(LinkedList<Association> edges) {
        this.edges = edges;
    }

    public List<String> getActors() {
        LinkedList<String> actors = new LinkedList<String>();

        for (Association edge : edges) {
            actors.add(edge.getFromActor());
        }

        Association lastEdge = edges.getLast();
        if (lastEdge != null) {
            actors.add(lastEdge.getToActor());
        }

        return actors;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public void addFirstActor(String actorName) {
        String firstActor = edges.getFirst().getFromActor();
        edges.addFirst(new Association(actorName, firstActor, 0));
    }

    public void addFirst(Association edge) {
        edges.addFirst(edge);
        totalWeight += edge.getWeight();
    }

    public void add(Association edge) {
        edges.add(edge);
        totalWeight += edge.getWeight();
    }

    public void addLastActor(String actorName) {
        String lastNode = edges.getLast().getToActor();
        edges.addLast(new Association(lastNode, actorName, 0));
    }

    public int size() {
        return edges.size();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int numEdges = edges.size();
        sb.append(totalWeight);
        sb.append(": [");
        if (numEdges > 0) {
            for (int i = 0; i < edges.size(); i++) {
                sb.append(edges.get(i).getFromActor());
                sb.append("-");
            }

            sb.append(edges.getLast().getToActor());
        }
        sb.append("]");
        return sb.toString();
    }

}
