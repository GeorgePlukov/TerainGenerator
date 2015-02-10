package com.GeorgePlukov.Main;

import java.util.Random;

public class Map {
	private static Tile[][] map;
	
	// SETTINGS
	private boolean multiIsland = false;
	private boolean lakes = false;
	
	
	
	
	public Map (int width, int height){
		map = new Tile[width][height];
		initialize();
	}
	public void setMap(Tile[][] m){
		this.map = m;
	}

	public Tile[][] getMap(){
		return map;
	}

	
	// Initialize the array
	private void initialize(){
		// Cycle through all items and make them water
		for (int x = 0; x < map[0].length; x++){
			for (int y = 0; y < map[0].length; y++){
				// make the tile into a panel
				map[x][y] = new Tile(Type.WATER);
			}
		}
	}

	public void addLand() {
		Random r = new Random();
	
		int x = r.nextInt(map[0].length);
		int y = r.nextInt(map[0].length);

		// bring them closer to the center
		if (x < map[0].length/2)
			x += 5;
		else if (x > map[0].length/2)
			x-=5;
		if (y < map[0].length/2)
			y += 5;
		else if (y > map[0].length/2)
			y -= 5;
		
		map[x][y].setType(Type.GRASS);
	}
	public void toggleLakes(){
		if (lakes)
			lakes = false;
		else
			lakes = true;
	}
	public boolean areLakesEnabled(){
		return lakes;
	}
	public boolean isSurroundedBy(int row, int col,Type type) {

		
		return false;
	}
	
	
}
