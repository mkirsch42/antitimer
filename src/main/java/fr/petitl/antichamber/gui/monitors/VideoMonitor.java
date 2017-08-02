package fr.petitl.antichamber.gui.monitors;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import fr.petitl.antichamber.Configuration;
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

		boolean found = new NativeDiscovery().discover();
		found = false;
		if (!found) {
			JFrame me = this;
			this.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentShown(ComponentEvent e) {
					super.componentShown(e);
					JOptionPane.showMessageDialog(me, "VLC not found! Video capabilities not available!");
				}
			});
			return;
		}

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
		Configuration conf = Configuration.read();

		startTime = conf.getVideoStart();

		player.stop();
		player.prepareMedia(conf.getVideoPath());
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
		if (setup) {
			if (status.isCurrentlyRunning()) {
				if (!player.isPlaying()) {
					System.out.println("start video");
					player.play();
					player.setTime(startTime);
				}
			} else {
				System.out.println("stop video");
				player.stop();
			}
		}

	}
}
