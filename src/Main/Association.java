/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

/**
 *
 * @author jcvsa
 */
public class Association {
    private String fromNode;
    private String toNode;
    private double weight;

    public Association(String fromNode, String toNode, double weight) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        setWeight(weight);
    }

    public String getFromNode() {
        return fromNode;
    }

    public void setFromNode(String fromNode) {
        this.fromNode = fromNode;
    }

    public String getToNode() {
        return toNode;
    }

    public void setToNode(String toNode) {
        this.toNode = toNode;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public Association clone() {
        return new Association(fromNode, toNode, weight);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(fromNode);
        sb.append(",");
        sb.append(toNode);
        sb.append("){");
        sb.append(weight);
        sb.append("}");

        return sb.toString();
    }

    public boolean equals(Association association2) {
        if (hasSameEndpoints(association2) && weight == association2.getWeight())
            return true;

        return false;
    }

    public boolean hasSameEndpoints(Association association2) {
        if (fromNode.equals(association2.getFromNode()) && toNode.equals(association2.getToNode()))
            return true;

        return false;
    }
}
