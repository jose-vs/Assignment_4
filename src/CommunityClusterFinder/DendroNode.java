/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommunityClusterFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class was obtained online from StackOverflow to help with creating a graphical 
 * Dendrogram for the Community Cluster Finder.
 * 
 * Reference 
 * ----------------------------------------------------------------------------------------
 * Marco13. (2014, June 27). Recursive method to draw a dendrogram. StackOverflow.
 *     https://stackoverflow.com/questions/24450246/recursive-method-to-draw-a-dendrogram
 * 
 * @author Amos Foong <18044418>
 * @author Jose Santos <17993442>
 * @param <T>
 */
public class DendroNode<T> {
    protected T contents;
    protected List<DendroNode<T>> children;

    public DendroNode(T contents)
    {
        this.contents = contents;
        this.children = Collections.emptyList();
    }

    public DendroNode(DendroNode<T> child0, DendroNode<T> child1)
    {
        this.contents = null;

        List<DendroNode<T>> list = new ArrayList<>();
        list.add(child0);
        list.add(child1);
        this.children = Collections.unmodifiableList(list);
    }

    public T getContents()
    {
        return contents;
    }

    public List<DendroNode<T>> getChildren()
    {
        return Collections.unmodifiableList(children);
    }
    
    public String toString() {
        return this.getContents().toString();
    }
}
