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
    private String fromActor;
    private String toActor;
    private double weight;

    public Association(String fromActor, String toActor, double weight) {
        this.fromActor = fromActor;
        this.toActor = toActor;
        setWeight(weight);
    }

    public String getFromActor() {
        return fromActor;
    }

    public void setFromActor(String fromActor) {
        this.fromActor = fromActor;
    }

    public String getToActor() {
        return toActor;
    }

    public void setToActor(String toActor) {
        this.toActor = toActor;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public Association clone() {
        return new Association(fromActor, toActor, weight);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(fromActor);
        sb.append(",");
        sb.append(toActor);
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
        if (fromActor.equals(association2.getFromActor()) && toActor.equals(association2.getToActor()))
            return true;

        return false;
    }
}
