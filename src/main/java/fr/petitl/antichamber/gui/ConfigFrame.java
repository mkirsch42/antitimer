package fr.petitl.antichamber.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import fr.petitl.antichamber.config.Configurable;

public class ConfigFrame extends JFrame {

    private JPanel contentPane;

    /**
     * Create the frame.
     */
    public ConfigFrame() {
	setBounds(100, 100, 450, 300);
	setTitle("Antichamber Configuration");
	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	setContentPane(contentPane);
	GridBagLayout gbl_contentPane = new GridBagLayout();
	gbl_contentPane.columnWidths = new int[] { 0, 75 };
	gbl_contentPane.rowHeights = new int[] {};
	gbl_contentPane.columnWeights = new double[] { 1, 0 };
	gbl_contentPane.rowWeights = new double[] { 0 };
	contentPane.setLayout(gbl_contentPane);
	refresh();
    }

    private void refresh() {
	contentPane.removeAll();
	GridBagConstraints c = new GridBagConstraints();
	c.gridy = 0;
	c.weighty = 1;
	c.fill = GridBagConstraints.BOTH;
	Configurable.allConfigs().forEach(conf -> {
	    c.gridx = 0;
	    contentPane.add(new JLabel(conf.desc()), c);
	    c.gridx = 1;
	    JButton btnToggle;
	    try {
		btnToggle = new JButton(conf.isEnabled() ? "Remove" : "Apply");
	    } catch (IOException e) {
		e.printStackTrace();
		btnToggle = new JButton("Toggle");
	    }
	    btnToggle.addActionListener(event -> {
		try {
		    conf.toggle();
		    refresh();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    });
	    contentPane.add(btnToggle, c);
	    c.gridy = c.gridy + 1;
	});
	contentPane.revalidate();
	contentPane.repaint();
    }

}
