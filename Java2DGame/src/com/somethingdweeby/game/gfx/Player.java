package com.somethingdweeby.game.gfx;

import com.somethingdweeby.game.Game;
import com.somethingdweeby.game.Map;

public class Player {
	
	public int xOffset = 0;
	public int yOffset = 0;
	
	SpriteSheet sheet;
	Screen screen;
	Game game;
	Map collision;
	
	public int tileX = 0;
	public int tileY = 1;
	public int direction = 0;
	public boolean isWalking = false;
	private int playerAnimation[] = {0,1,2,1};
	private int animFrame = 0;
	private int animTimer = 20;
	public int playerX=64;
	public int playerY=64;
	private int width;
	private int height;
	
	
	public Player(int width, int height,SpriteSheet sheet,Screen screen) {
		this.sheet = sheet;
		this.screen = screen;
		this.height = height;
		this.width = width;
		collision = new Map("res/JavaLand1_col.csv",screen);
	}
	
	public boolean isColliding(int xOffset,int yOffset) {
		if(collision.map[(playerX+16+xOffset)/16+(playerY+16+yOffset)/16*screen.MAP_WIDTH]==1) {
			return(true);
		}else {
			return(false);
		}
	}
	
	public void offsetMath() {
		int playerX = this.playerX-width/2+16;
		int playerY = this.playerY-height/2+16;
		if(screen.xOffset<0||xOffset<0) {
			screen.xOffset=0;
			xOffset=playerX;
		}else if(screen.xOffset>screen.MAP_WIDTH*sheet.tileRes||xOffset>0){
			screen.xOffset=0;
			xOffset=playerX-screen.MAP_WIDTH*sheet.tileRes;
		}else {
			screen.xOffset=playerX;
		}
		if(screen.yOffset<0||yOffset<0) {
			screen.yOffset=0;
			yOffset=playerY;
		}else if(screen.yOffset>screen.MAP_WIDTH*sheet.tileRes||yOffset>0){
			screen.yOffset=0;
			yOffset=playerY-screen.MAP_WIDTH*sheet.tileRes;
		}else {
			screen.yOffset=playerY;
		}
	}
	
	public void render(int[] pixels, int row) {
		animationManager();
		int xOffset = this.xOffset+width/2-16;
		int yOffset = this.yOffset+height/2-16;
		for (int y = yOffset; y < yOffset+sheet.tileRes; y++) {
			for (int x = xOffset; x < xOffset+sheet.tileRes; x++) {
				int color = sheet.pixels[(((y-yOffset)%sheet.tileRes+tileY*sheet.tileRes)*sheet.width)+((x-xOffset)%sheet.tileRes+tileX*sheet.tileRes)];
				if(color!=-65281)
				pixels[(y * row)+(x)] = color;
			}
		}
	}
	
	public void animationManager() {
		tileY=direction;
		if(isWalking) {
			animTimer++;
			if(animTimer>20) {
				animTimer=0;
				tileX=playerAnimation[animFrame++];
				if(animFrame==4)animFrame=0;
			}
		}else {
			tileX=1;
			animTimer=20;
		}
	}
}
