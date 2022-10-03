package eu.nurkert.gamengine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import eu.nurkert.gamengine.logic.GBackground;
import eu.nurkert.gamengine.logic.GContent;
import eu.nurkert.gamengine.GInput.GMouseClick;
import eu.nurkert.gamengine.logic.GLocation;
import eu.nurkert.gamengine.logic.events.GEventHandler;
import eu.nurkert.gamengine.logic.events.essential.KeyPressedGEvent;
import eu.nurkert.gamengine.logic.events.essential.MouseClickGEvent;
@SuppressWarnings("serial")
public class GPanel extends JPanel {

	private static final GraphicsConfiguration graphicsConf = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice().getDefaultConfiguration();
	private int fps = 60;

	private String title;
	private String[] infoText;
	private boolean showInfoText = true;

	private BufferedImage imageBuffer;
	private Graphics graphics;

	private GInput input;
	private GContent content;

	private GFrame frame;
	private static GPanel instance;

	public GPanel(String title, GContent content) {
		this.title = title;
		infoText = new String[0];

		input = new GInput();
		this.content = content;

		frame = null;

		imageBuffer = graphicsConf.createCompatibleImage(420, 69);
		graphics = imageBuffer.getGraphics();
		((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		instance = this;
		init();
	}

	public static GPanel getInstance() {
		return instance;
	}

	private void init() {
		setBackground(Color.BLACK);
	}

	public void setFrame(GFrame frame) {
		this.frame = frame;
	}

	public String[] getInfoText() {
		return infoText;
	}

	public void updateInfoText() {
		updateInfoText("FPS: " + getFPS(), "Window: x:" + getWidth() + ", y:" + getHeight());
	}

	public void updateInfoText(String... infoText) {
		this.infoText = infoText;
	}

	Thread panelThread;

	public void start() {
		panelThread = new Thread(title + "Thread") {

			public void run() {
				double last = System.currentTimeMillis();
				double diffAvg = 0.0166666;

//				freeze(800); // startup anderer threads

//				showStartInfos(diffAvg);
				last = System.currentTimeMillis();
				while (true) {

					long current = System.currentTimeMillis();
					double diff = (current - last) / 1000.0;
					diffAvg = 0.98 * diffAvg + 0.02 * diff;
					last = current;
					fps = (int) (1D / diffAvg);

//					if (showInfoText)
//						updateInfoText();

					handleInput(diff);

//					if (paused) {
////						System.out.println("paused");
//						continue;
//					}

					content.deleteDeadObjects();
					content.handle(diff);
//
					clear();

					content.paint(imageBuffer, frame.getPanel());

					if (showInfoText && infoText != null)
						drawInfoText();

					frame.getGraphics().drawImage(imageBuffer, 0, 0, frame);

					// Limit the fps to 60:
					freeze((long) (16 - diff * 1000));
				}
			}

		};
		panelThread.start();
	}

	/*
	 * freeze Frame content
	 */
	public synchronized void freeze(long time) {
		if (time > 0)
			try {
				synchronized (panelThread) {
					wait(time);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	public GLocation getViewCenter() {
		return content.getViewCenter();
	}

	public void setBackground(GBackground gBackground) {
		content.place(gBackground);
	}

	private void handleInput(double diff) {
		try {
			if (input.getPressedKeys().keySet().toArray().length > 0)
				for (int i = 0; i < input.getPressedKeys().size(); i++) {
					if (input != null) {
						char key = (char) input.getPressedKeys().keySet().toArray()[i];
						int code = input.getPressedKeys().get(key);
						GEventHandler.call(new KeyPressedGEvent(diff, key, code, this.content));
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (input.getMouseClicks().size() > 0)
			for (int i = 0; i < input.getMouseClicks().size(); i++) {
				if (input != null) {
					GMouseClick click = input.getMouseClicks().get(i);
					GEventHandler.call(new MouseClickGEvent(diff, click.getMousePressedX(), click.getMousePressedY()));
				}
			}

		input.clearMouseClicks();
	}

	long start = System.currentTimeMillis();

	private void clear() {
		if (frame.getWidth() != imageBuffer.getWidth() || frame.getHeight() != imageBuffer.getHeight()
				|| System.currentTimeMillis() - start < 2000) {
			setSize(frame.getWidth(), frame.getHeight());
			imageBuffer = graphicsConf.createCompatibleImage(getWidth(), getHeight());
			graphics = imageBuffer.getGraphics();
		}

		// clear frame:
		graphics.setColor(Color.DARK_GRAY);
		graphics.fillRect(0, 0, getWidth(), getHeight());
	}

	private void drawInfoText() {
		Graphics graphics = imageBuffer.getGraphics();
		graphics.setFont(new Font("Arial", 0, getHeight() / 50));
		graphics.setColor(Color.WHITE);
		for (int i = 0; i < infoText.length; i++) {
			if (infoText[i] != null) {

				try {
					graphics.drawString(infoText[i], 10, 20 * i + 250);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void pause() {
		try {
			panelThread.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int getFPS() {
		return fps;
	}

	public GContent getContent() {
		return content;
	}

	public void setContent(GContent content) {
		this.content = content;
	}

	public double getSizeFactor() {
		return getHeight() / 100;
	}

	public static GraphicsConfiguration getGraphicsconf() {
		return graphicsConf;
	}

	public String getTitle() {
		return title;
	}

	public BufferedImage getImageBuffer() {
		return imageBuffer;
	}

	public Graphics getGraphics() {
		return graphics;
	}

	public GInput getInput() {
		return input;
	}

	public Thread getPanelThread() {
		return panelThread;
	}
}
