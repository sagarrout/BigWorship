package main.java.baha;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

//NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), BahaStater.properties.getProperty(Constants.VLCPATH));
public class GoLiveCamera {
	private final JPanel displayPanel;
	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private static GoLiveCamera obj;

	private GoLiveCamera() {

		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),
				BahaStater.properties.getProperty(Constants.VLCPATH));
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		this.displayPanel = new JPanel();
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		displayPanel.setVisible(true);
		BahaStater.liveFrame.add(mediaPlayerComponent);
		BahaStater.liveFrame.setSize(new Dimension(400, 400));
		BahaStater.liveFrame.setResizable(false);
		BahaStater.liveFrame.setUndecorated(true);
		BahaStater.liveFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		BahaStater.liveFrame.setLocationRelativeTo(null); 
//		String[] options = { " :dshow-vdev=Default" + " :dshow-adev=none" + " :dshow-size=1920x1080"
//				+ " :dshow-aspect-ratio=16\\:9" + " :live-caching=200" };

		String[] options = new String[] {
                " :file-caching=300",
                " :network-caching=300",
                " :dshow-vdev=Default",
                " :dshow-adev=none",
                " :dshow-size=1920x1080",
                " :dshow-aspect-ratio=16\\:9",
                " :live-caching=200",
                " :sout = #transcode{vcodec=x264,vb=800,scale=0.25,acodec=none,fps=23}:display :no-sout-rtp-sap :no-sout-standard-sap :ttl=1 :sout-keep"
                };
		JButton previewButton = new JButton("Preview");
		displayPanel.add(previewButton);
		JButton liveButton = new JButton("Go Live");
		displayPanel.add(liveButton);
		JButton stopButton = new JButton("Stop");
		displayPanel.add(stopButton);

		liveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				GraphicsDevice device = BahaStater.initFrame.getGraphicsConfiguration().getDevice();
				for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
					if (!device.equals(gd)) {
						BahaStater.liveFrame.setSize(new Dimension(Double.valueOf(gd.getDisplayMode().getWidth()).intValue(),
								Double.valueOf(gd.getDisplayMode().getHeight()).intValue()));
						BahaStater.liveFrame.setLocation(gd.getDefaultConfiguration().getBounds().getLocation());
						BahaStater.liveFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
						BahaStater.liveFrame.revalidate();
						BahaStater.liveFrame.repaint();
						BahaStater.liveFrame.setVisible(true);
						if (!mediaPlayerComponent.getMediaPlayer().isPlaying())
							mediaPlayerComponent.getMediaPlayer().startMedia("dshow://", options);
						break;
					}
				}

			}
		});

		previewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Rectangle bounds = BahaStater.initFrame.getGraphicsConfiguration().getDevice().getDefaultConfiguration()
						.getBounds();
				BahaStater.liveFrame.setLocation(bounds.getLocation());
				BahaStater.liveFrame.setSize(new Dimension(400, 400));
				BahaStater.liveFrame.setLocationRelativeTo(null); 
				BahaStater.liveFrame.setVisible(true);
				BahaStater.liveFrame.revalidate();
				BahaStater.liveFrame.repaint();
				
				if (!mediaPlayerComponent.getMediaPlayer().isPlaying())
					mediaPlayerComponent.getMediaPlayer().startMedia("dshow://", options);
			}
		});
		
		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BahaStater.displayFrame.setVisible(false);
				BahaStater.showButton.setSelected(false);
				BahaStater.showButton.setText("SHOW SCREEN");
				Rectangle bounds = BahaStater.initFrame.getGraphicsConfiguration().getDevice().getDefaultConfiguration()
						.getBounds();
				BahaStater.liveFrame.setLocation(bounds.getLocation());
				BahaStater.liveFrame.setSize(new Dimension(400, 400));
				BahaStater.liveFrame.setLocationRelativeTo(null); 
				BahaStater.liveFrame.setVisible(false);
				mediaPlayerComponent.getMediaPlayer().stop();
			}
		});

	}

	public static GoLiveCamera getGoLiveCamera() {
		if (obj == null) {
			synchronized (GoLiveCamera.class) {
				if (obj == null) {
					obj = new GoLiveCamera();// instance will be created at request time
				}
			}
		}
		return obj;
	}

	public JPanel getGoLivePanel() {
		return displayPanel;
	}
}
