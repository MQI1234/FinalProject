package com.plebs.finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;

public class Planet {
	private String name;
	private Texture texture;
	private int x,y;
	private String type;
	
	public Planet(String info) throws IOException{
		String[] string = info.split(" "); // x , y, file name for texture
		x = Integer.parseInt(string[0]);
		y = Integer.parseInt(string[1]);
		makeName();
		type = "get3";
		texture = new Texture("planets/"+string[2]);
		
	}
	
	public void makeName() throws IOException{
		FileHandle file = Gdx.files.internal("planets/names.txt");
		BufferedReader reader = new BufferedReader(file.reader());
		ArrayList<String> lines = new ArrayList<String>();
		String line = reader.readLine();
		while( line != null ) {
			lines.add(line);
			line = reader.readLine();
		}
		Random r = new Random();
		name = lines.get(r.nextInt(lines.size()));
		System.out.println(name);
	}
	
	public Rectangle getRekt(){ //returns bounding rectangle of texture on board
		return new Rectangle(x,y,texture.getWidth(), texture.getHeight());
	}
	
	public Texture getText(){ 
		return texture;
	}
	
	public void drawPlanet(Batch batch){
		batch.draw(texture, x, y);
	}
	
	public void action(Player player){
		//if (type.equals("type3"))
	}
	
}