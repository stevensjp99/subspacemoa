/*
 *    GUI.java
 *    Copyright (C) 2010 University of Waikato, Hamilton, New Zealand
 *    @author Albert Bifet (abifet at cs dot waikato dot ac dot nz)
 *    @author FracPete (fracpete at waikato dot ac dot nz)
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program. If not, see <http://www.gnu.org/licenses/>.
 *    
 */
package moa.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import moa.DoTask;
import moa.gui.subspaceclusteringtab.SubspaceClusteringTabPanel;

/**
 * The main class for the MOA gui. Lets the user configure
 * tasks, learners, streams, and perform data stream analysis.
 *
 * @author Albert Bifet (abifet at cs dot waikato dot ac dot nz)
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 7 $
 */
public class SubspaceGui extends JPanel {

    private static final long serialVersionUID = 1L;

    private javax.swing.JTabbedPane panel;

    private SubspaceGui() {
        setLayout(new BorderLayout());

        //Make a Tabbed Pane
        panel = new javax.swing.JTabbedPane();
        add(panel, BorderLayout.CENTER);

        //Add our Tab to it
        AbstractTabPanel tabPanel = new SubspaceClusteringTabPanel();
        panel.addTab(tabPanel.getTabTitle(), null, tabPanel, tabPanel.getDescription());
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            // Create and set up the window.
            JFrame frame = new JFrame("Subspacemoa Graphical User Interface");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            SubspaceGui gui = new SubspaceGui();
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(gui);

            // Display the window.
            frame.pack();
            frame.setVisible(true);
        });
    }
}
