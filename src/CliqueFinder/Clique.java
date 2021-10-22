/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CliqueFinder;

import Main.Actor;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author jcvsa
 */
public class Clique {
    private Set<Actor> clique; 
    
    public Clique (){ 
        clique = new HashSet<>();
    }
    
    public Clique(Set<Actor> clique) { 
        this.clique = clique; 
    }
    
    public int size() { 
        return clique.size(); 
    }
    
    public Set<Actor> getClique() { 
        return  clique; 
    }
    
    public void setClique(Set<Actor> clique) { 
        this.clique = clique; 
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString(){ 
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        
        for (Iterator<Actor> it = clique.iterator(); it.hasNext();) {
            Actor curr = it.next();
            sb.append(curr.getName());
            if(it.hasNext())
                sb.append("-"); 
        }
        sb.append("]");
        return sb.toString();
    }
}
