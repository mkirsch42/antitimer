package fr.petitl.antichamber.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;

import fr.petitl.antichamber.Configuration;

public class VideoConfigFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8557367377925600923L;
	private Configuration conf;
	private JPanel contentPane;
	
	public VideoConfigFrame(Configuration config) {
		super("Video Configuration");
		setSize(450, 125);
		conf = config;
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 100 };
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
		c.gridx = 0;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		
		contentPane.add(new JLabel("Video path: " + conf.getVideoPath()), c);
		JButton browse = new JButton("Browse...");
		browse.addActionListener(e->{
			setVideoPath();
		});
		c.gridx = 1;
		contentPane.add(browse, c);
		
		c.gridy++;
		c.gridx = 0;
		contentPane.add(new JLabel("Start time:"), c);
		JSpinner startTime = new JSpinner();
		startTime.setValue(conf.getVideoStart());
		c.gridx = 1;
		contentPane.add(startTime, c);
		
		c.gridy++;
		c.gridx = 0;
		JButton save = new JButton("Save");
		save.addActionListener(e->{
			conf.setVideoStart((int)startTime.getValue());
			conf.write();
		});
		contentPane.add(save, c);
		
		contentPane.revalidate();
		contentPane.repaint();
	}
	
	private void setVideoPath() {
		JFileChooser fc = new JFileChooser();
		if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			conf.setVideoPath(fc.getSelectedFile().getAbsolutePath());
			refresh();
		}
	}
	
}
