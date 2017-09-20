package com.somethingdweeby.game;

import java.io.BufferedReader;
import java.io.FileReader;

import com.somethingdweeby.game.gfx.Screen;

public class Map {
	public int[] map;
	String[] mapString;
	
	public Map(String path,Screen screen) {
		map = new int[screen.MAP_WIDTH*screen.MAP_WIDTH];
		try {
			initMap(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initMap(String path) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = null;
		int arrayPos = 0;
		while ((line = br.readLine()) != null) {
			mapString = line.split(",");
			for (String str : mapString) {
				map[arrayPos]=Integer.parseInt(str);
				arrayPos++;
		    }
		}
		br.close();
	}
}
