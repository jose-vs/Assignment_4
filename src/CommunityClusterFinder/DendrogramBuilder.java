/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommunityClusterFinder;

import java.awt.Graphics;
import java.awt.Point;
import java.util.List;

/**
 *
 * @author Amos Foong <18044418>
 */
public class DendrogramBuilder {
    public static <T> int countLeaves(DendroNode<T> node) {
        List<DendroNode<T>> children = node.getChildren();
        if(children.size() == 0) {
            return 1;
        }
        DendroNode<T> child0 = children.get(0);
        DendroNode<T> child1 = children.get(1);
        return countLeaves(child0) + countLeaves(child1);
    }

    public static <T> int countLevels(DendroNode<T> node) {
        List<DendroNode<T>> children = node.getChildren();
        if(children.size() == 0) {
            return 1;
        }
        DendroNode<T> child0 = children.get(0);
        DendroNode<T> child1 = children.get(1);
        return 1 + Math.max(countLevels(child0), countLevels(child1));
    }
    
    public static <T> Point drawDendrogram(Graphics g, DendroNode<T> node, int y, int leaves, int levels, int heightPerLeaf, int widthPerLevel, int height, int width, int currentY, int margin) {
        List<DendroNode<T>> children = node.getChildren();
        
        if(children.size() == 0) {
            int x = width - widthPerLevel - 2 * margin;
            g.drawString(String.valueOf(node.getContents()), x + 8, currentY + 8);
            int resultX = x;
            int resultY = currentY;
            currentY += heightPerLeaf;
            return new Point(resultX, resultY);
        }
        
        if(children.size() >= 2) {
            DendroNode<T> child0 = children.get(0);
            DendroNode<T> child1 = children.get(1);
            Point p0 = drawDendrogram(g, child0, y, leaves, levels, heightPerLeaf, widthPerLevel, height, width, currentY, margin);
            Point p1 = drawDendrogram(g, child1, y + heightPerLeaf, leaves, levels, heightPerLeaf, widthPerLevel, height, width, currentY, margin);

            g.fillRect(p0.x - 2, p0.y - 2, 4, 4);
            g.fillRect(p1.x - 2, p1.y - 2, 4, 4);
            int dx = widthPerLevel;
            int vx = Math.min(p0.x - dx, p1.x - dx);
            g.drawLine(vx, p0.y, p0.x, p0.y);
            g.drawLine(vx, p1.y, p1.x, p1.y);
            g.drawLine(vx, p0.y, vx, p1.y);
            Point p = new Point(vx, p0.y + (p1.y - p0.y) / 2);
            return p;
        }
        // Should never happen
        return new Point();
    }
}
