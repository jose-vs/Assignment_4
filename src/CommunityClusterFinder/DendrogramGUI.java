package CommunityClusterFinder;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * GUI class found on StackOverflow. We have made some changes to the code to suit our 
 * application.
 * 
 * Reference
 * ----------------------------------------------------------------------------------------
 * Marco13. (2014, June 27). Recursive method to draw a dendrogram. StackOverflow.
 *      https://stackoverflow.com/questions/24450246/recursive-method-to-draw-a-dendrogram
 *
 * @author Amos Foong <18044418>
 * @author Jose Santos <17993442>
 */
public class DendrogramGUI extends JFrame {

    public static int PANEL_H = 700;
    public static int PANEL_W = 900;

    public DendrogramPaintPanel drawPanel;
    public CommunityClusterFinder clusterFinder;
    public CommunityClusterFinder clusterFinder2;
    public boolean g2dToggler;

    public DendrogramGUI() {
        super("Dendrogram GUI");
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // Make Dendrogram program follow system's theme.
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {}

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        g2dToggler = false;
        
        initTestData();
        initTestData2();

        JPanel panel = new JPanel(new BorderLayout());
        drawPanel = new DendrogramPaintPanel();
        panel.add(drawPanel, BorderLayout.CENTER);
        
        JPanel controlPanel = new JPanel(new GridLayout(1, 6));
        
        JCheckBox checkbox = new JCheckBox(); 
        checkbox.setText("Enable Debug");
        checkbox.setSelected(true);
        checkbox.addActionListener((action) -> {
            if(checkbox.isSelected()) {
                
            }
        });

        JButton fontBtn = new JButton("Switch Fonts");
//        fontBtn.setPreferredSize(new Dimension(50, 40));
        fontBtn.addActionListener((ActionEvent action) -> {
            g2dToggler = !g2dToggler; 
            drawPanel.repaint();
        });
        panel.add(fontBtn, BorderLayout.NORTH);
        
        JButton colourBtn = new JButton("Randomise Link Colours");
//        colourBtn.setPreferredSize(new Dimension(50, 40));
        colourBtn.addActionListener((ActionEvent action) -> drawPanel.repaint());
        panel.add(colourBtn, BorderLayout.SOUTH);
        
        initTestData();
        drawPanel.setRoot(clusterFinder.rootNode);
        
        getContentPane().add(panel);
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
        private DendroNode<String> root;
        private int leaves;
        private int levels;
        private int heightPerLeaf;
        private int widthPerLevel;
        private int currentY;
        private final int MARGIN = 25;

        public DendrogramPaintPanel() {
            super();
            super.setBackground(Color.WHITE);
            super.setPreferredSize(new Dimension(PANEL_W, PANEL_H));
        }
        
        public void setRoot(DendroNode<String> root) {
            this.root = root;
        }
        
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
            heightPerLeaf = (getHeight() - MARGIN - MARGIN) / leaves;
            widthPerLevel = (getWidth() - MARGIN - MARGIN) / levels;
            currentY = 0;

            g.translate(MARGIN, MARGIN);
            draw(g, root, 0);
        }

        private <T> Point draw(Graphics g, DendroNode<T> node, int y) {
            Graphics2D g2d = (Graphics2D) g;
            if(g2dToggler) {
                g2d.setStroke(new BasicStroke(3));                
            } else {
                g2d.setStroke(new BasicStroke(2));                
            }
            
            List<DendroNode<T>> children = node.getChildren();
            if(children.size() == 0) {
                int x = getWidth() - widthPerLevel - 2 * MARGIN;
                g2d.setColor(Color.BLACK);
                if(g2dToggler) {
                    g2d.setFont(new Font("DialogInput", Font.BOLD|Font.ITALIC, 30));
                } else {
                    g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
                }
                g2d.drawString(String.valueOf(node.getContents()), x + 8, currentY + 8);
                int resultX = x;
                int resultY = currentY;
                currentY += heightPerLeaf;
                return new Point(resultX, resultY);
            }
            
            if(children.size() >= 2) {
                DendroNode<T> child0 = children.get(0);
                DendroNode<T> child1 = children.get(1);
                Point p0 = draw(g2d, child0, y);
                Point p1 = draw(g2d, child1, y + heightPerLeaf);

                g2d.fillRect(p0.x - 2, p0.y - 2, 4, 4);
                g2d.fillRect(p1.x - 2, p1.y - 2, 4, 4);
                int dx = widthPerLevel;
                int vx = Math.min(p0.x - dx, p1.x - dx);    
                g2d.setColor(new Color(new Random().nextFloat(),new Random().nextFloat(),new Random().nextFloat()));
                g2d.drawLine(vx, p0.y, p0.x, p0.y);
                g2d.drawLine(vx, p1.y, p1.x, p1.y);
                g2d.drawLine(vx, p0.y, vx, p1.y);
                Point p = new Point(vx, p0.y + (p1.y - p0.y) / 2);
                return p;
            }
            // Should never happen
            return new Point();
        }
    }
}
