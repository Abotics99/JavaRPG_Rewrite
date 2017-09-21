package com.somethingdweeby.game.gfx;

import com.somethingdweeby.game.Map;

public class Screen {

	public final int MAP_WIDTH = 64;

	public int[] tiles = new int[MAP_WIDTH * MAP_WIDTH];

	public int xOffset = 0;
	public int yOffset = 0;
	public int tileX = 0;
	public int tileY = 0;

	public int width;
	public int height;

	public SpriteSheet sheet;
	Map layer[] = new Map[3];

	public Screen(int width, int height, SpriteSheet sheet) {
		this.width = width;
		this.height = height;
		this.sheet = sheet;
		layer[0] = new Map("res/JavaLand1_1.csv",this);
		layer[1] = new Map("res/JavaLand1_2.csv",this);
		layer[2] = new Map("res/JavaLand1_3.csv",this);
	}
	
	public void renderLayer(int[] pixels, int row, int layerNumber) {
		render(pixels,row,layer[layerNumber-1]);
	}

	public void render(int[] pixels, int row, Map map) {
		for (int yTile = yOffset / sheet.tileRes-1; yTile <= (yOffset + height) / sheet.tileRes; yTile++) {
			int yMin = yTile * sheet.tileRes - yOffset;
			int yMax = yMin + sheet.tileRes;
			for (int xTile = xOffset / sheet.tileRes-1; xTile <= (xOffset + width) / sheet.tileRes; xTile++) {
				int xMin = xTile * sheet.tileRes - xOffset;
				int xMax = xMin + sheet.tileRes;
				for (int y = Math.max(yMin,0); y < Math.min(yMax,height); y++) {
					for (int x = Math.max(xMin,0); x < Math.min(xMax,width); x++) {
						int tileCoord = Math.abs(map.map[(Math.max(xOffset,0)-16+xMax)/sheet.tileRes+(Math.max(yOffset,0)-16+yMax)/sheet.tileRes*MAP_WIDTH]);
						tileX = tileCoord%(sheet.width/sheet.tileRes);
						tileY = tileCoord/(sheet.width/sheet.tileRes);
						int col1 = sheet.pixels[(((y-yMin)%sheet.tileRes+(tileY*sheet.tileRes))*sheet.width)+((x-xMin)%sheet.tileRes+tileX*sheet.tileRes)];
						int col2 = pixels[(y * row)+(x)];
						double ratio = (col1 >> 24) & 0xff;
						pixels[(y * row)+(x)] = blend(col2,col1,ratio/255);
					}
				}
			}
		}
	}
	
	private int blend (int a, int b, double ratio) {
	    double iRatio = 1.0 - ratio;

	    int aA = (a >> 24 & 0xff);
	    int aR = ((a & 0xff0000) >> 16);
	    int aG = ((a & 0xff00) >> 8);
	    int aB = (a & 0xff);

	    int bA = (b >> 24 & 0xff);
	    int bR = ((b & 0xff0000) >> 16);
	    int bG = ((b & 0xff00) >> 8);
	    int bB = (b & 0xff);
	    
	    int a1 = (int)((aA * iRatio) + (bA * ratio));
	    int r1 = (int)((aR * iRatio) + (bR * ratio));
	    int g1 = (int)((aG * iRatio) + (bG * ratio));
	    int b1 = (int)((aB * iRatio) + (bB * ratio));

	    return a1 << 24 | r1 << 16 | g1 << 8 | b1;
	}
}
