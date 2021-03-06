/**
 * [SubspaceClusterPanel.java] for Subspace MOA
 * 
 * A cluster on the visual panel.
 * 
 * @author Yunsu Kim
 * 		   based on the implementation of Timm Jansen
 * Data Management and Data Exploration Group, RWTH Aachen University
 */

package moa.gui.subspacevisualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JPanel;

import moa.cluster.CFCluster;
import moa.cluster.Cluster;
import moa.cluster.SphereCluster;
import moa.cluster.SubspaceSphereCluster;

public class SubspaceClusterPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Cluster cluster;

    private double[] center;
    private final static int MIN_SIZE = 5;
    protected double decay_rate;

    protected int x_dim = 0;
    protected int y_dim = 1;
    protected Color col;
    protected Color default_color = Color.BLACK;
    protected double[] direction = null;

    protected SubspaceStreamPanel streamPanel;

    protected int panel_size;
    protected int window_size;
    protected boolean highligted = false;
    private double r;
    private boolean[] subspace;
    private boolean isFullSpace;


    /** Creates new form ObjectPanel */

    public SubspaceClusterPanel(Cluster cluster, Color color, SubspaceStreamPanel sp) {
        this.cluster = cluster;
        center = cluster.getCenter();
        
        if (cluster instanceof SubspaceSphereCluster) {
	        r = ((SubspaceSphereCluster) cluster).getRadius();
	        subspace = ((SubspaceSphereCluster) cluster).getSubspace();
	        isFullSpace = false;
        } else if (cluster instanceof SphereCluster) {
        	r = ((SphereCluster) cluster).getRadius();
        	isFullSpace = true;
        } else if (cluster instanceof CFCluster) {
        	r = ((CFCluster) cluster).getRadius();
        	isFullSpace = true;
        } else {
        	System.out.println("[SubspaceClusterPanel] Given cluster is not compatible!");
        }
        
        streamPanel = sp;

        default_color = col = color;

        setVisible(true);
        setOpaque(false);
        setSize(new Dimension(1, 1));
        setLocation(0,0);

        initComponents();
    }

	public void setDirection(double[] direction){
        this.direction = direction;
    }

    public void updateLocation() {
        x_dim = streamPanel.getActiveXDim();
        y_dim = streamPanel.getActiveYDim();

        if (cluster != null && center == null) {
            getParent().remove(this);
        } else {
            // Size of the parent stream panel
            window_size = Math.min(streamPanel.getWidth(), streamPanel.getHeight());

            // Scale down to diameter
            panel_size = (int) (2 * r * window_size);
            if (panel_size < MIN_SIZE)
            	panel_size = MIN_SIZE;
            
            // Location: Top-Left corner of the rectangle(oval)
            int x_location, y_location, x_length, y_length; 
            if (isRelevant(x_dim)) {
            	x_location = (int)(center[x_dim] * window_size - (panel_size / 2));
            	x_length = panel_size + 1;
            } else {
            	x_location = 0;
            	x_length = window_size;
            }
            if (isRelevant(y_dim)) {
            	y_location = (int)(center[y_dim] * window_size - (panel_size / 2));
            	y_length = panel_size + 1;
            } else {
            	y_location = 0;
            	y_length = window_size;
            }
            
            setSize(new Dimension(x_length, y_length));
            setLocation(x_location, y_location);
        }
    }

    public void updateTooltip(){
        setToolTipText(cluster.getInfo());
    }

    @Override
    public boolean contains(int x, int y) {
        //only react on the hull of the cluster
        double dist = Math.sqrt(Math.pow(x-panel_size/2,2)+Math.pow(y-panel_size/2,2));
        if(panel_size/2 - 5 < dist && dist < panel_size/2 + 5)
            return true;
        else
            return false;
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 296, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 266, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        streamPanel.setHighlightedClusterPanel(this);
    }//GEN-LAST:event_formMouseClicked

    @Override
    protected void paintComponent(Graphics g) {
        updateLocation();
        if (highligted) {
            g.setColor(Color.BLUE);
        } else {
            g.setColor(default_color);
        }
        int pos = (int) ((float) panel_size / 2.0f);
        
        if (!isRelevant(x_dim) && !isRelevant(y_dim)) {		// Irrelevant to this screen
        	return;		
        } else {										// Something to draw!
        	// Label
        	if (cluster.getId() >= 0)
                g.drawString("C" + (int)cluster.getId(), pos, pos);
        	
        	if (isRelevant(x_dim) && isRelevant(y_dim)) {
        		g.drawOval(0, 0, panel_size, panel_size);
        	} else {
	        	int fullLength = window_size - 2;
	        	g.drawRect(0, 0, isRelevant(x_dim) ? panel_size : fullLength,
						   		 isRelevant(y_dim) ? panel_size : fullLength);
	        }
        }        

        if (direction != null) {
            double length = Math.sqrt(Math.pow(direction[0], 2) + Math.pow(direction[1], 2));
            g.drawLine(pos, pos, pos+(int)((direction[0] / length) * panel_size), pos + (int)((direction[1] / length) * panel_size));
        }

        updateTooltip();
    }

    public void highlight(boolean enabled) {
        highligted = enabled;
        repaint();
    }

    public boolean isValidCluster() {
        return (center != null);
    }

    public int getClusterID() {
        return (int) cluster.getId();
    }

    public int getClusterLabel() {
        return (int) cluster.getGroundTruth();
    }


    public String getSVGString(int width){
        StringBuffer out = new StringBuffer();

        int x = (int)(center[x_dim]*window_size);
        int y = (int)(center[y_dim]*window_size);
        int radius = panel_size/2;
        
        out.append("<circle ");
        out.append("cx='"+x+"' cy='"+y+"' r='"+radius+"'");
        out.append(" stroke='green' stroke-width='1' fill='white' fill-opacity='0' />");
        out.append("\n");
        return out.toString();
    }

    public void drawOnCanvas(Graphics2D imageGraphics) {
        Point location = getLocation(); 
        int fullLength = window_size - 2;

        if (isRelevant(x_dim) && isRelevant(y_dim)) {
        	imageGraphics.drawOval(location.x, location.y, panel_size, panel_size);
        } else if (!isRelevant(x_dim) && !isRelevant(y_dim)){
        	return;		// Irrelevant to this screen
        } else {
        	imageGraphics.drawRect(location.x, location.y, isRelevant(x_dim) ? panel_size : fullLength,
        												   isRelevant(y_dim) ? panel_size : fullLength);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    
    /** Auxiliaries **/
    private boolean isRelevant(int dim) {
    	if (isFullSpace) {
    		return true;
    	} else {
    		return subspace[dim];
    	}
    }
}