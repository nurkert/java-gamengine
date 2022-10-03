package eu.nurkert.gamengine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import eu.nurkert.gamengine.logic.GBackground;
import eu.nurkert.gamengine.logic.GContent;
import eu.nurkert.gamengine.visual.GTexture;
@SuppressWarnings("serial")
public class GFrame extends JFrame implements KeyListener, MouseListener {

	private GPanel panel;

	public GFrame(String title, GContent content) {
		super(title);

		panel = new GPanel("default", content);
		panel.setFrame(this);

		setContentPane(panel);
		setResizable(true);

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int dimX = (int) dimension.getWidth();
		int dimY = (int) dimension.getHeight();

		setBounds(dimX / 4, dimY / 4, dimX / 2, dimY / 2);
		setMinimumSize(new Dimension(dimX / 4, dimY / 4));
		setBackground(Color.BLACK);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addMouseListener(this);
		addKeyListener(this);

		init();
	}

	private void init() {
		setVisible(true);
		panel.start();
	}

	public GPanel getPanel() {
		return panel;
	}

	public void setBackground(GTexture texture) {
		panel.setBackground(new GBackground(texture, panel.getViewCenter()));
	}

	public GContent getContent() {
		return panel.getContent();
	}

	public void setContent(GContent content) {
		panel.setContent(content);
	}

	/*
	 * input handling...
	 */

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		panel.getInput().add(new GInput.GMouseClick(e.getX(), e.getY()));
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		panel.getInput().remove(e.getKeyChar());
	}

	@Override
	public void keyPressed(KeyEvent e) {
//		System.out.println(e.getKeyCode());
		panel.getInput().add(e.getKeyChar(), e.getKeyCode());
	}
}
