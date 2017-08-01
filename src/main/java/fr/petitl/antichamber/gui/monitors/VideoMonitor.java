package fr.petitl.antichamber.gui.monitors;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import fr.petitl.antichamber.gui.MonitorFrame;
import fr.petitl.antichamber.triggers.GameStatus;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * Created by mkirsch42 on 7/31/2017.
 */
public class VideoMonitor extends JFrame implements MonitorFrame {

    private EmbeddedMediaPlayerComponent component;
    private EmbeddedMediaPlayer player;
    private boolean setup = false;
    private long startTime = 0;
    
    public VideoMonitor() {
        super("Video");
        this.setSize(640, 360);
        
        new NativeDiscovery().discover();
        
        component = new EmbeddedMediaPlayerComponent();
        player = component.getMediaPlayer();
        getFrame().setContentPane(component);
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
                setupPlayer();
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                super.componentHidden(e);
                stopPlayer();
            }
        });
    }

    private void setupPlayer() {
        JFileChooser fc = new JFileChooser();
        while(fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION);
        String mrl = fc.getSelectedFile().getAbsolutePath();

        startTime = Long.parseLong(JOptionPane.showInputDialog(this, "Start time: "));
        
        player.stop();
        player.prepareMedia(mrl);
        player.parseMedia();
        
        setup = true;
    }

    private void stopPlayer() {
        player.stop();
        setup = false;
    }

    @Override
    public JFrame getFrame() {
        return this;
    }

    @Override
    public void update(GameStatus status) {
    	if(status.isCurrentlyRunning()) {
    		if(!player.isPlaying() && setup) {
    			System.out.println("start video");
    			player.play();
    			player.setTime(startTime);
    		}
    	} else {
    		if(setup) {
    			System.out.println("stop video");
    			player.stop();
    		}
    	}
    }
}
