package eu.nurkert.gamengine.visual;

import java.awt.Image;

public class GSelectiveTexture extends GAnimatedTexture {

	public GSelectiveTexture(String path) {
		super(path);
	}

	public Image getImage(int index) {
		return frames[index];
	}

}
