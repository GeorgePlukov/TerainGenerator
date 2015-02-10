package com.GeorgePlukov.Main;

public class Tile {
	private Type type;
	public Tile (Type type){
		this.setType(type);
	}
	
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}

}
