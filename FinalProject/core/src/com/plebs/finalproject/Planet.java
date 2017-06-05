package com.plebs.finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

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
		texture = new Texture(string[2]);
		
	}
	public void makeName() throws IOException{
		FileHandle file = Gdx.files.internal("names.txt");
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
	public void drawPlanet(Batch batch){
		batch.draw(texture, x, y);
	}
	public void action(Player player){
		//if (type.equals("type3"))
	}
	
}