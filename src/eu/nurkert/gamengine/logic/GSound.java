package eu.nurkert.gamengine.logic;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class GSound {

	public static synchronized void playSound(final String url) {
		new Thread(new Runnable() {

			public void run() {
				try {
					InputStream audioSrc = getClass().getResourceAsStream(url);
					//add buffer for mark/reset support
					InputStream bufferedIn = new BufferedInputStream(audioSrc);
					AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufferedIn);
					Clip clip = AudioSystem.getClip();
//					AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(url));
//					inputStream.v
					clip.open(inputStream);
					clip.loop(Clip.LOOP_CONTINUOUSLY);
					

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
