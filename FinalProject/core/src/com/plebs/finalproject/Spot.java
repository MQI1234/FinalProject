package com.plebs.finalproject;

public class Spot {
	private int x,y;
	private String type;
	
	public Spot(String[]info){
		x = Integer.parseInt(info[0]);
		y = Integer.parseInt(info[1]);
		type = info[3];
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
}
