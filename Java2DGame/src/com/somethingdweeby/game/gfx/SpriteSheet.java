package com.somethingdweeby.game.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	
	public String path;
	public int width;
	public int height;
	public int tileRes = 16;
	
	public int[] pixels;
	
	public SpriteSheet(String path,int tileRes) {
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(SpriteSheet.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(image == null) {
			return;
		}
		
		this.path = path;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.tileRes = tileRes;
		
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
	}
}
