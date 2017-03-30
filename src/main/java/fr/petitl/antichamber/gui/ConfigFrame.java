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
	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	setContentPane(contentPane);
	GridBagLayout gbl_contentPane = new GridBagLayout();
	gbl_contentPane.columnWidths = new int[]{};
	gbl_contentPane.rowHeights = new int[]{};
	gbl_contentPane.columnWeights = new double[]{1, 0, 0};
	gbl_contentPane.rowWeights = new double[]{0};
	contentPane.setLayout(gbl_contentPane);
	GridBagConstraints c = new GridBagConstraints();
	c.gridy = 0;
	c.weighty = 1;
	Configurable.allConfigs().forEach(conf -> {
	    c.gridx = 0;
	    contentPane.add(new JLabel(conf.desc()), c);
	    c.gridx = 1;
	    JButton btnApply = new JButton("Apply");
	    btnApply.addActionListener(event->{
		try {
		    Configurable t = conf;
		    conf.config();
		} catch(IOException e) {
		    e.printStackTrace();
		}
	    });
	    contentPane.add(btnApply, c);
	    c.gridx = 2;
	    JButton btnUndo = new JButton("Remove");
	    btnUndo.addActionListener(event->{
		try {
		    conf.unconfig();
		} catch(IOException e) {
		    e.printStackTrace();
		}
	    });
	    contentPane.add(btnUndo, c);
	    c.gridy = c.gridy + 1;
	});
    }

}
