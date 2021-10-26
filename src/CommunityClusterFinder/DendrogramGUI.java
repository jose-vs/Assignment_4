/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CommunityClusterFinder;

/**
 *
 *
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Reference
 * ----------------------------------------------------------------------------------------
 * Marco13. (2014, June 27). Recursive method to draw a dendrogram.
 * StackOverflow.
 * https://stackoverflow.com/questions/24450246/recursive-method-to-draw-a-dendrogram
 *
 * @author Amos Foong <18044418>
 * @author Jose Santos <17993442>
 */
public class DendrogramGUI extends JFrame {

    public static int PANEL_H = 700;
    public static int PANEL_W = 900;

    public DendrogramPaintPanel drawPanel;
    public CommunityClusterFinder clusterFinder;

    public DendrogramGUI() {
        super("Dendrogram GUI");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);

        drawPanel = new DendrogramPaintPanel();
        getContentPane().add(drawPanel);

        initTestData();

        drawPanel.setRoot(clusterFinder.rootNode);
        
        DendroNode<String> test = new DendroNode<>(new DendroNode<>(new DendroNode<>(new DendroNode<>("test"), new DendroNode<>("test2")),new DendroNode<>("test3")),new DendroNode<>("test5"));
        drawPanel.setRoot(test);
    }

    public static void main(String[] args) {
        JFrame frame = new DendrogramGUI();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.repaint();
            }
        });
    }

    public void initTestData() {
        // Test data.
        double[][] associations = {
            {0D, 0.5D, 0.4D, 0D, 0D, 0D},
            {0.5D, 0D, 0D, 0.4D, 0D, 0D},
            {0.4D, 0D, 0D, 0.3D, 0.5D, 0D},
            {0D, 0.4D, 0.3D, 0D, 0.8D, 0D},
            {0D, 0D, 0.5D, 0.8D, 0D, 0.7D},
            {0D, 0D, 0D, 0D, 0.7D, 0D}};
        // Social actors involved in the table.
        String[] actors = {"Anna", "Bill", "Carl", "Dave", "Emma", "Fred"};
        this.clusterFinder = new CommunityClusterFinder(associations, actors);
    }

    private class DendrogramPaintPanel extends JPanel {

        public void setRoot(DendroNode<String> root) {
            this.root = root;
        }
//
//    private <T> DendroNode<T> create(DendroNode<T> child0, DendroNode<T> child1) {
//        return new DendroNode<T>(child0, child1);
//    }

        private DendroNode<String> root;
        private int leaves;
        private int levels;
        private int heightPerLeaf;
        private int widthPerLevel;
        private int height;
        private int width;
        private int currentY;
        private final int margin = 25;

        public DendrogramPaintPanel() {
            super();
            super.setBackground(Color.WHITE);
            super.setPreferredSize(new Dimension(PANEL_W, PANEL_H));
        }
//        root = create(create(create("10"),create(create("9"),create(create("8"),create("7")))),
//                
//                        create(create(create("6"),create("5")),create(create("4"),create(create("3"),
//                                create(create("2"),create("1"))))));
//    }

//        @Override
//        protected void paintComponent(Graphics gr) {
//            super.paintComponent(gr);
//            Graphics2D g = (Graphics2D) gr;
//
//            if(root != null) {
//                leaves = DendrogramBuilder.countLeaves(root);
//                levels = DendrogramBuilder.countLevels(root);
//                heightPerLeaf = (getHeight() - margin - margin) / leaves;
//                widthPerLevel = (getWidth() - margin - margin) / levels;
//                height = getHeight();
//                width = getWidth();
//                currentY = 0;
//
//                g.translate(margin, margin);
////                DendrogramBuilder.drawDendrogram(g, root, 0, leaves, levels, heightPerLeaf, widthPerLevel, height, width, currentY, margin);  
//            }
//
//        }

        private <T> int countLeaves(DendroNode<T> node) {
            List<DendroNode<T>> children = node.getChildren();
            if(children.size() == 0) {
                return 1;
            }
            DendroNode<T> child0 = children.get(0);
            DendroNode<T> child1 = children.get(1);
            return countLeaves(child0) + countLeaves(child1);
        }

        private <T> int countLevels(DendroNode<T> node) {
            List<DendroNode<T>> children = node.getChildren();
            if(children.size() == 0) {
                return 1;
            }
            DendroNode<T> child0 = children.get(0);
            DendroNode<T> child1 = children.get(1);
            return 1 + Math.max(countLevels(child0), countLevels(child1));
        }

        @Override
        protected void paintComponent(Graphics gr) {
            super.paintComponent(gr);
            Graphics2D g = (Graphics2D) gr;

            leaves = countLeaves(root);
            levels = countLevels(root);
            heightPerLeaf = (getHeight() - margin - margin) / leaves;
            widthPerLevel = (getWidth() - margin - margin) / levels;
            currentY = 0;

            g.translate(margin, margin);
            draw(g, root, 0);
        }

        private <T> Point draw(Graphics g, DendroNode<T> node, int y) {
            List<DendroNode<T>> children = node.getChildren();
            if(children.size() == 0) {
                int x = getWidth() - widthPerLevel - 2 * margin;
                g.drawString(String.valueOf(node.getContents()), x + 8, currentY + 8);
                int resultX = x;
                int resultY = currentY;
                currentY += heightPerLeaf;
                return new Point(resultX, resultY);
            }
            if(children.size() >= 2) {
                DendroNode<T> child0 = children.get(0);
                DendroNode<T> child1 = children.get(1);
                Point p0 = draw(g, child0, y);
                Point p1 = draw(g, child1, y + heightPerLeaf);

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
}
