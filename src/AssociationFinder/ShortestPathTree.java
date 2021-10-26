/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AssociationFinder;

import Main.Actor;
import java.util.HashMap;

/**
 *
 * @author jcvsa
 */
public class ShortestPathTree {

    private HashMap<String, Actor> actors;
    private final String root;

    public ShortestPathTree() {
        this.actors = new HashMap<>();
        this.root = "";
    }

    public ShortestPathTree(String root) {
        this.actors = new HashMap<>();
        this.root = root;
    }

    public HashMap<String, Actor> getActors() {
        return actors;
    }

    public void setActors(HashMap<String, Actor> actors) {
        this.actors = actors;
    }

    public String getRoot() {
        return root;
    }

    public void add(Actor newActor) {
        actors.put(newActor.getName(), newActor);
    }

    public void setParentOf(String name, String parent) {
        if(!actors.containsKey(name)) {
            actors.put(name, new Actor(name));
        }

        actors.get(name).setParent(parent);

    }

    public String getParentOf(String node) {
        if(actors.containsKey(node)) {
            return actors.get(node).getParent();
        } else {
            return null;
        }
    }
}
